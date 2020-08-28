package com.SirBlobman.disco.armor.pattern;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SirBlobman.api.item.ArmorType;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;

public class RandomPattern extends Pattern {
    public RandomPattern(DiscoArmorPlugin plugin) {
        super(plugin, "random");
    }

    @Override
    public String getDisplayName() {
        ChatColor[] chatColorArray = ChatColor.values();
        ChatColor[] colorArray = Arrays.stream(chatColorArray).filter(ChatColor::isColor).toArray(ChatColor[]::new);
        int colorArrayLength = colorArray.length;

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int colorIndex = rng.nextInt(colorArrayLength);
        ChatColor randomColor = colorArray[colorIndex];
        return (randomColor + "Random Color");
    }

    @Override
    public Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int red = rng.nextInt(256);
        int green = rng.nextInt(256);
        int blue = rng.nextInt(256);
        return Color.fromRGB(red, green, blue);
    }

    @Override
    public Map<ArmorType, ItemStack> getNextArmor(Player player) {
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        ArmorType[] armorTypeArray = ArmorType.values();
        for(ArmorType armorType : armorTypeArray) {
            Color nextColor = getNextColor(player);
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
