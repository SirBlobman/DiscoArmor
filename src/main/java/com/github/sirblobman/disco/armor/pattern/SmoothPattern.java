package com.github.sirblobman.disco.armor.pattern;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class SmoothPattern extends DiscoArmorPattern {
    private final Color defaultColor;
    private final Map<UUID, Color> colorMap;

    public SmoothPattern(DiscoArmorPlugin plugin) {
        super(plugin, "smooth");
        this.defaultColor = Color.fromRGB(0x000000);
        this.colorMap = new HashMap<>();
    }

    @Override
    public String getDisplayName() {
        return "<purple>Smooth</purple>";
    }

    @Override
    protected Color getNextColor(Player player) {UUID uuid = player.getUniqueId();
        Color currentColor = this.colorMap.getOrDefault(uuid, this.defaultColor);
        int red = currentColor.getRed();
        int green = currentColor.getGreen();
        int blue = currentColor.getBlue();

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int choice = rng.nextInt(3);

        if(choice == 0) {
            red += 16;
            if(red > 255) red -= 255;
        } else if(choice == 1) {
            green += 16;
            if(green > 255) green -= 255;
        } else {
            blue += 16;
            if(blue > 255) blue -= 255;
        }

        Color newColor = Color.fromRGB(red, green, blue);
        this.colorMap.put(uuid, newColor);
        return newColor;
    }

    @Override
    public Map<ArmorType, ItemStack> getNextArmor(Player player) {
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        Color nextColor = getNextColor(player);

        ArmorType[] armorTypeArray = ArmorType.values();
        for(ArmorType armorType : armorTypeArray) {
            ItemStack armor = createArmor(player, armorType, nextColor);
            armorMap.put(armorType, armor);
        }

        return armorMap;
    }

    @Override
    protected ItemStack getMenuItem() {
        ItemStack item = new ItemStack(Material.RED_BANNER);
        BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
        if(bannerMeta == null) {
            throw new IllegalStateException("null banner meta!");
        }

        Pattern blueGradient = new Pattern(DyeColor.BLUE, PatternType.GRADIENT);
        bannerMeta.addPattern(blueGradient);

        item.setItemMeta(bannerMeta);
        return item;
    }
}
