package com.github.sirblobman.disco.armor.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public class RainbowPattern extends Pattern {
    private static final List<Color> RAINBOW_COLOR_LIST = Arrays.asList(
            Color.fromRGB(255, 0, 0), // Red
            Color.fromRGB(255, 165, 0), // Orange
            Color.fromRGB(255, 255, 0), // Yellow
            Color.fromRGB(0, 128, 0), // Green
            Color.fromRGB(0, 0, 255), // Blue
            Color.fromRGB(75, 0, 130), // Indigo
            Color.fromRGB(238, 130, 238) // Violet
    );
    public RainbowPattern(DiscoArmorPlugin plugin) {
        super(plugin, "rainbow");
    }

    @Override
    public String getDisplayName() {
        return "&aR&ba&ci&dn&eb&fo&9w";
    }

    @Override
    public Color getNextColor(Player player) {
        int colorListSize = RAINBOW_COLOR_LIST.size();
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        return RAINBOW_COLOR_LIST.get(rng.nextInt(colorListSize));
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
        Tag<Material> bannerTag = Tag.ITEMS_BANNERS;
        Set<Material> bannerSet = bannerTag.getValues();
        List<Material> bannerList = new ArrayList<>(bannerSet);
        int bannerListSize = bannerList.size();

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Material bannerType = bannerList.get(rng.nextInt(bannerListSize));
        return new ItemStack(bannerType, 1);
    }
}