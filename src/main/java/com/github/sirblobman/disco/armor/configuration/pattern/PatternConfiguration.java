package com.github.sirblobman.disco.armor.configuration.pattern;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.plugin.IMultiVersionPlugin;
import com.github.sirblobman.api.utility.ConfigurationHelper;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.item.ItemLoader;
import com.github.sirblobman.disco.armor.configuration.item.ItemType;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.ArmorSlot;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPattern;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPatternCopy;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPatternFixed;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPatternFrames;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPatternType;

public final class PatternConfiguration implements IConfigurable {
    private final String id;
    private final DiscoArmorPlugin plugin;
    private final Map<ArmorSlot, CustomPattern> patternMap;

    private ItemStack menuIcon;
    private PatternType patternType;
    private BuiltInType builtInType;

    public PatternConfiguration(@NotNull DiscoArmorPlugin plugin, @NotNull String id) {
        this.id = id;
        this.plugin = plugin;
        this.patternMap = new EnumMap<>(ArmorSlot.class);
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        DiscoArmorPlugin plugin = getPlugin();
        Logger logger = plugin.getLogger();

        try {
            ConfigurationSection menuIconSection = getOrCreateSection(section, "menu-icon");
            String itemTypeName = menuIconSection.getString("item-type", "ITEM");
            ItemType itemType = ConfigurationHelper.parseEnum(ItemType.class, itemTypeName, ItemType.ITEM);

            Class<? extends ItemLoader> loaderClass = itemType.getLoaderClass();
            Constructor<? extends ItemLoader> constructor = loaderClass.getConstructor(IMultiVersionPlugin.class);
            ItemLoader itemLoader = constructor.newInstance(plugin);
            this.menuIcon = itemLoader.loadItemStack(menuIconSection);
            if (this.menuIcon == null) {
                logger.warning("Failed to load menu icon for pattern '" + getId() + "'.");
                return;
            }
        } catch (ReflectiveOperationException ex) {
            logger.log(Level.WARNING, "Failed to load menu icon:", ex);
            return;
        }

        String patternTypeName = section.getString("type", "BUILT_IN");
        PatternType patternType = ConfigurationHelper.parseEnum(PatternType.class, patternTypeName, PatternType.BUILT_IN);
        setPatternType(patternType);

        if (patternType == PatternType.CUSTOM && section.isSet("custom-patterns")) {
            ConfigurationSection customPatternsSection = getOrCreateSection(section, "custom-patterns");
            ArmorSlot[] armorSlotValues = ArmorSlot.values();
            for (ArmorSlot armorSlot : armorSlotValues) {
                String key = armorSlot.name();
                ConfigurationSection customPatternSection = getOrCreateSection(customPatternsSection, key);
                CustomPattern customPattern = parseCustomPattern(armorSlot, customPatternSection);
                if (customPattern != null) {
                    setCustomPattern(armorSlot, customPattern);
                }
            }
        }
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    private @Nullable CustomPattern parseCustomPattern(@NotNull ArmorSlot slot, @NotNull ConfigurationSection section) {
        String typeName = section.getString("type");
        if (typeName == null || typeName.isBlank()) {
            return null;
        }

        CustomPatternType customPatternType = ConfigurationHelper.parseEnum(CustomPatternType.class, typeName, null);
        if (customPatternType == null) {
            return null;
        }

        CustomPattern pattern = switch (customPatternType) {
            case FRAMES -> new CustomPatternFrames(slot);
            case COPY -> new CustomPatternCopy(slot);
            case FIXED -> new CustomPatternFixed(slot);
        };

        pattern.load(section);
        return pattern;
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull ItemStack getMenuIcon() {
        return this.menuIcon;
    }

    public @NotNull PatternType getPatternType() {
        return this.patternType;
    }

    public void setPatternType(@NotNull PatternType patternType) {
        this.patternType = patternType;
    }

    public @Nullable BuiltInType getBuiltInType() {
        return this.builtInType;
    }

    public void setBuiltInType(@Nullable BuiltInType builtInType) {
        this.builtInType = builtInType;
    }

    public @Nullable CustomPattern getCustomPattern(@NotNull ArmorSlot slot) {
        return this.patternMap.get(slot);
    }

    public void setCustomPattern(@NotNull ArmorSlot slot, @Nullable CustomPattern pattern) {
        if(pattern == null) {
            this.patternMap.remove(slot);
        } else {
            this.patternMap.put(slot, pattern);
        }
    }
}
