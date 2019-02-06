package com.SirBlobman.discoarmor.type;

import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class OneColorType extends DiscoArmorType {
    @Override
    public ItemStack[] getDiscoArmor() {
        final Color randomColor = generateRandomColor();
        ItemStack boots = getColoredArmor(EquipmentSlot.FEET, randomColor);
        ItemStack leggings = getColoredArmor(EquipmentSlot.LEGS, randomColor);
        ItemStack chestplate = getColoredArmor(EquipmentSlot.CHEST, randomColor);
        ItemStack helmet = getColoredArmor(EquipmentSlot.HEAD, randomColor);
        return new ItemStack[] {boots, leggings, chestplate, helmet};
    }
}