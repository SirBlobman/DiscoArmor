package com.SirBlobman.discoarmor.type;

import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RandomColorType extends DiscoArmorType {
    @Override
    public ItemStack[] getDiscoArmor() {
        Color bootsColor = generateRandomColor();
        ItemStack boots = getColoredArmor(EquipmentSlot.FEET, bootsColor);
        
        Color leggingsColor = generateRandomColor();
        ItemStack leggings = getColoredArmor(EquipmentSlot.LEGS, leggingsColor);

        Color chestplateColor = generateRandomColor();
        ItemStack chestplate = getColoredArmor(EquipmentSlot.CHEST, chestplateColor);

        Color helmetColor = generateRandomColor();
        ItemStack helmet = getColoredArmor(EquipmentSlot.HEAD, helmetColor);
        
        return new ItemStack[] {boots, leggings, chestplate, helmet};
    }
}