package com.github.sirblobman.disco.armor.task;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.pattern.BuiltInType;
import com.github.sirblobman.disco.armor.configuration.pattern.PatternConfiguration;
import com.github.sirblobman.disco.armor.configuration.pattern.PatternType;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.ArmorSlot;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.ColorValues;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPattern;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.Frame;
import com.github.sirblobman.disco.armor.configuration.playerdata.MemoryData;
import com.github.sirblobman.disco.armor.configuration.playerdata.MemoryDataManager;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;
import com.github.sirblobman.disco.armor.pattern.PatternManager;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class DiscoArmorTask extends EntityTaskDetails<Player> {
    private final DiscoArmorPlugin plugin;
    private final Map<ArmorSlot, Long> customFrameTicksLeft;

    private long nextBuiltInFrameTicksLeft;
    private BuiltInType currentBuiltInType;
    private DiscoArmorPattern currentBuiltInPattern;

    public DiscoArmorTask(@NotNull DiscoArmorPlugin plugin, @NotNull Player entity) {
        super(plugin, entity);
        this.plugin = plugin;
        this.customFrameTicksLeft = new EnumMap<>(ArmorSlot.class);

        this.nextBuiltInFrameTicksLeft = 0L;
        setDelay(1L);
        setPeriod(1L);
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        MemoryDataManager memoryDataManager = plugin.getMemoryDataManager();
        MemoryData data = memoryDataManager.getData(player);

        PatternType patternType = data.getSelectedPatternType();
        if (patternType == PatternType.BUILT_IN) {
            if (this.nextBuiltInFrameTicksLeft > 0) {
                --this.nextBuiltInFrameTicksLeft;
                return;
            }

            nextBuiltInFrame(player);
        } else {
            nextCustomFrame(player);
        }
    }

    private @NotNull DiscoArmorPlugin getDiscoArmorPlugin() {
        return this.plugin;
    }

    private @NotNull MemoryDataManager getMemoryDataManager() {
        return getDiscoArmorPlugin().getMemoryDataManager();
    }

    private @NotNull MemoryData getMemoryData(@NotNull Player player) {
        return getMemoryDataManager().getData(player);
    }

    private @Nullable DiscoArmorPattern getCurrentBuiltInPattern(@NotNull Player player) {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        MemoryData memoryData = getMemoryData(player);
        BuiltInType builtInType = memoryData.getSelectedBuiltInType();
        if (builtInType == null) {
            return null;
        }

        if (this.currentBuiltInType == null || this.currentBuiltInType != builtInType) {
            this.currentBuiltInType = builtInType;
            this.currentBuiltInPattern = null;
        }

        if (this.currentBuiltInPattern != null) {
            return this.currentBuiltInPattern;
        }

        try {
            Class<? extends DiscoArmorPattern> patternClass = builtInType.getPatternClass();
            Constructor<? extends DiscoArmorPattern> constructor = patternClass.getConstructor(DiscoArmorPlugin.class, String.class);
            this.currentBuiltInPattern = constructor.newInstance(plugin, builtInType.name());
            return this.currentBuiltInPattern;
        } catch (ReflectiveOperationException ex) {
            Logger logger = plugin.getLogger();
            logger.log(Level.SEVERE, "Pattern task error:", ex);
            return null;
        }
    }

    private void nextBuiltInFrame(@NotNull Player player) {
        DiscoArmorPattern currentPattern = getCurrentBuiltInPattern(player);
        if (currentPattern == null) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        Map<ArmorType, ItemStack> nextArmor = currentPattern.getNextArmor(player);
        nextArmor.forEach((armorType, itemStack) -> {
            EquipmentSlot slot = armorType.getEquipmentSlot();
            playerInventory.setItem(slot, itemStack);
        });

        long armorSpeed = getDiscoArmorPlugin().getConfiguration().getArmorSpeed();
        this.nextBuiltInFrameTicksLeft = (armorSpeed < 1 ? 0 : armorSpeed);
    }

    private void nextCustomFrame(@NotNull Player player) {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        MemoryData memoryData = getMemoryData(player);
        String customPatternId = memoryData.getSelectCustomPattern();
        if (customPatternId == null) {
            return;
        }

        PatternManager patternManager = plugin.getPatternManager();
        PatternConfiguration configuration = patternManager.getPattern(customPatternId);
        if (configuration == null) {
            return;
        }

        ArmorSlot[] armorSlots = ArmorSlot.values();
        PlayerInventory playerInventory = player.getInventory();

        for (ArmorSlot armorSlot : armorSlots) {
            CustomPattern customPattern = configuration.getCustomPattern(armorSlot);
            if (customPattern == null) {
                continue;
            }

            long nextFrameTimeLeft = getNextCustomFrameTimeLeft(armorSlot);
            if (nextFrameTimeLeft > 0) {
                setNextCustomFrameTimeLeft(armorSlot, --nextFrameTimeLeft);
                continue;
            }

            EquipmentSlot slot = armorSlot.getEquipmentSlot();
            Frame frame = getNextFrame(customPattern);
            ItemStack armor = createArmor(armorSlot, frame);
            playerInventory.setItem(slot, armor);
        }
    }

    private long getNextCustomFrameTimeLeft(@NotNull ArmorSlot slot) {
        return this.customFrameTicksLeft.getOrDefault(slot, 0L);
    }

    private void setNextCustomFrameTimeLeft(@NotNull ArmorSlot slot, long timeLeft) {
        this.customFrameTicksLeft.put(slot, timeLeft);
    }

    private @NotNull ItemStack createArmor(@NotNull ArmorSlot armorSlot, @NotNull Frame frame) {
        XMaterial material = armorSlot.getMaterial();
        ItemStack item = material.parseItem();
        if (item == null) {
            throw new IllegalStateException("Missing material '" + material.name() + "'.");
        }

        ItemMeta itemMeta = item.getItemMeta();


        if (itemMeta instanceof LeatherArmorMeta leatherArmorMeta) {
            ColorValues colorValues = frame.getColor();
            if (colorValues != null) {
                int red = colorValues.getRed();
                int green = colorValues.getGreen();
                int blue = colorValues.getBlue();
                Color color = Color.fromRGB(red, green, blue);
                leatherArmorMeta.setColor(color);
                item.setItemMeta(leatherArmorMeta);
            }
        }

        if (itemMeta instanceof ArmorMeta armorMeta) {
            ArmorTrim armorTrim = frame.getTrim();
            armorMeta.setTrim(armorTrim);
            item.setItemMeta(armorMeta);
        }

        return item;
    }

    private @NotNull Frame getNextFrame(@NotNull CustomPattern pattern) {
        // TODO
        return pattern.nextFrame();
    }
}
