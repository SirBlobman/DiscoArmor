package com.SirBlobman.disco.armor.object;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class GrayscaleArmorType extends ArmorType {
    public GrayscaleArmorType() {
        super("grayscale");
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
    
        int colorSum = (red + blue + green);
        int colorAverage = (colorSum / 3);
        return Color.fromRGB(colorAverage, colorAverage, colorAverage);
    }
    
    @Override
    public Map<EquipmentSlot, ItemStack> getNextArmor(Player player) {
        Map<EquipmentSlot, ItemStack> nextArmor = new EnumMap<>(EquipmentSlot.class);
        EquipmentSlot[] slotArray = EquipmentSlot.values();
        Color color = getNextColor(player);
    
        for(EquipmentSlot slot : slotArray) {
            ItemStack item = createLeatherArmor(slot, color);
            if(item == null) continue;
        
            nextArmor.put(slot, item);
        }
    
        return nextArmor;
    }
    
    @Override
    public ItemStack getMenuIcon() {
        return new ItemStack(Material.GRAY_BANNER);
    }
}