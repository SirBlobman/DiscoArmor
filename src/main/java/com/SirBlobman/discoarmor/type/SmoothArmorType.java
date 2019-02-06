package com.SirBlobman.discoarmor.type;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/*
 * Extra Information:
 * Max Color Integer: 16777215
 * Min Color Integer: 0
 */
public class SmoothArmorType extends DiscoArmorType {
    @Override
    public ItemStack[] getDiscoArmor() {
        final Color randomColor = getNextColor();
        ItemStack boots = getColoredArmor(EquipmentSlot.FEET, randomColor);
        ItemStack leggings = getColoredArmor(EquipmentSlot.LEGS, randomColor);
        ItemStack chestplate = getColoredArmor(EquipmentSlot.CHEST, randomColor);
        ItemStack helmet = getColoredArmor(EquipmentSlot.HEAD, randomColor);
        return new ItemStack[] {boots, leggings, chestplate, helmet};
    }
    
    private Color PREVIOUS_COLOR = null;
    private boolean reachedMaxRed = false;
    private boolean reachedMinRed = true;
    private boolean reachedMaxGreen = false;
    private boolean reachedMinGreen = true;
    private boolean reachedMaxBlue = false;
    private boolean reachedMinBlue = true;
    private Color getNextColor() {
        if(PREVIOUS_COLOR == null) PREVIOUS_COLOR = generateRandomColor();
        
        int red = PREVIOUS_COLOR.getRed();
        int green = PREVIOUS_COLOR.getGreen();
        int blue = PREVIOUS_COLOR.getBlue();
        
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int chooseColor = tlr.nextInt(0, 3);
        
        final int variance = 16;
        if(chooseColor == 0) {
            if(reachedMaxRed) {
                red = Math.max(0, red - variance);
                if(red == 0) {
                    reachedMinRed = true;
                    reachedMaxRed = false;
                }
            } else if(reachedMinRed) {
                red = Math.min(255, red + variance);
                if(red == 255) {
                    reachedMinRed = false;
                    reachedMaxRed = true;
                }
            }
        } else if(chooseColor == 1) {
            if(reachedMaxGreen) {
                green = Math.max(0, green - variance);
                if(green == 0) {
                    reachedMinGreen = true;
                    reachedMaxGreen = false;
                }
            } else if(reachedMinGreen) {
                green = Math.min(255, green + variance);
                if(green == 255) {
                    reachedMinGreen = false;
                    reachedMaxGreen = true;
                }
            }
        } else if(chooseColor == 2) {
            if(reachedMaxBlue) {
                blue = Math.max(0, blue - variance);
                if(blue == 0) {
                    reachedMinBlue = true;
                    reachedMaxBlue = false;
                }
            } else if(reachedMinBlue) {
                blue = Math.min(255, blue + variance);
                if(blue == 255) {
                    reachedMinBlue = false;
                    reachedMaxBlue = true;
                }
            }
        }
        
       PREVIOUS_COLOR = Color.fromRGB(red, green, blue);
       return PREVIOUS_COLOR;
    }
}