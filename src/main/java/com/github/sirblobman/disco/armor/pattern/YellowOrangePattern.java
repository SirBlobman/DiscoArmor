package com.github.sirblobman.disco.armor.pattern;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public class YellowOrangePattern extends Pattern {
    private static final Color DEFAULT_COLOR = Color.fromRGB(255, 255, 0);
    private final Map<UUID, Color> colorMap;
    public YellowOrangePattern(DiscoArmorPlugin plugin) {
        super(plugin, "yellow_orange");
        this.colorMap = new HashMap<>();
    }

    @Override
    public String getDisplayName() {
        return "&eYellow &6Orange";
    }

    @Override
    public Color getNextColor(Player player) {
        UUID uuid = player.getUniqueId();
        Color currentColor = this.colorMap.getOrDefault(uuid, DEFAULT_COLOR);
        int red = currentColor.getRed();
        int green = currentColor.getGreen();
        int blue = currentColor.getBlue();

        green += 5;
        if(green > 255) green = 128;

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
        ItemStack item = new ItemStack(Material.YELLOW_BANNER);
        BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
        if(bannerMeta == null) throw new IllegalStateException("null banner meta!");

        org.bukkit.block.banner.Pattern orangeGradient = new org.bukkit.block.banner.Pattern(DyeColor.ORANGE, PatternType.GRADIENT);
        bannerMeta.addPattern(orangeGradient);

        item.setItemMeta(bannerMeta);
        return item;
    }
}