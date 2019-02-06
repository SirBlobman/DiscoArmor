package com.SirBlobman.discoarmor.type;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class AmericanFlagType extends DiscoArmorType {
    private static final Color OLD_GLORY_RED = Color.fromRGB(0xBF0A30);
    private static final Color OLD_GLORY_WHITE = Color.fromRGB(0xFFFFFF);
    private static final Color OLD_GLORY_BLUE = Color.fromRGB(0x002868);
    
    @Override
    public ItemStack[] getDiscoArmor() {
        int random = ThreadLocalRandom.current().nextInt(0, 3);
        final Color randomColor = (random == 2 ? OLD_GLORY_RED : (random == 1 ? OLD_GLORY_WHITE : OLD_GLORY_BLUE));
        
        ItemStack boots = getColoredArmor(EquipmentSlot.FEET, randomColor);
        ItemStack leggings = getColoredArmor(EquipmentSlot.LEGS, randomColor);
        ItemStack chestplate = getColoredArmor(EquipmentSlot.CHEST, randomColor);
        ItemStack helmet = getColoredArmor(EquipmentSlot.HEAD, randomColor);
        return new ItemStack[] {boots, leggings, chestplate, helmet};
    }
}