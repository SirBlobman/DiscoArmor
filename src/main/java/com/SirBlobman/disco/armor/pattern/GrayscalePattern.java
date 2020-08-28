package com.SirBlobman.disco.armor.pattern;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SirBlobman.api.item.ArmorType;
import com.SirBlobman.api.xseries.XMaterial;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;

public class GrayscalePattern extends Pattern {
    public GrayscalePattern(DiscoArmorPlugin plugin) {
        super(plugin, "grayscale");
    }

    @Override
    public String getDisplayName() {
        return "&7Grayscale";
    }
    @Override
    public Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int red = rng.nextInt(256);
        int green = rng.nextInt(256);
        int blue = rng.nextInt(256);

        int colorSum = (red + green + blue);
        int colorAverage = (colorSum / 3);
        return Color.fromRGB(colorAverage, colorAverage, colorAverage);
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
        return XMaterial.GRAY_BANNER.parseItem();
    }

}