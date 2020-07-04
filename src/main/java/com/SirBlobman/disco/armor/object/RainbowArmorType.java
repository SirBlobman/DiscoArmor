package com.SirBlobman.disco.armor.object;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RainbowArmorType extends ArmorType {
    private static final List<Color> VALID_COLORS = Arrays.asList(
            Color.fromRGB(255, 0, 0), // Red
            Color.fromRGB(255, 165, 0), // Orange
            Color.fromRGB(255, 255, 0), // Yellow
            Color.fromRGB(0, 128, 0), // Green
            Color.fromRGB(0, 0, 255), // Blue
            Color.fromRGB(75, 0, 130), // Indigo
            Color.fromRGB(238, 130, 238) // Violet
    );
    public RainbowArmorType() {
        super("rainbow");
    }
    
    @Override
    public String getDisplayName() {
        return "&aR&ba&ci&dn&eb&fo&9w";
    }
    
    @Override
    public Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int validColorsSize = VALID_COLORS.size();
        
        int index = rng.nextInt(validColorsSize);
        return VALID_COLORS.get(index);
    }
    
    @Override
    public Map<EquipmentSlot, ItemStack> getNextArmor(Player player) {
        Map<EquipmentSlot, ItemStack> nextArmor = new EnumMap<>(EquipmentSlot.class);
        EquipmentSlot[] slotArray = EquipmentSlot.values();
        Color color = getNextColor(player);
    
        for(EquipmentSlot slot : slotArray) {
            ItemStack item = createLeatherArmor(player, slot, color);
            if(item == null) continue;
        
            nextArmor.put(slot, item);
        }
    
        return nextArmor;
    }
    
    @Override
    public ItemStack getMenuIcon() {
        Material[] validBannerArray = {
                Material.BLACK_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.CYAN_BANNER,
                Material.GRAY_BANNER, Material.GREEN_BANNER, Material.LIGHT_BLUE_BANNER, Material.LIGHT_GRAY_BANNER,
                Material.LIME_BANNER, Material.MAGENTA_BANNER, Material.ORANGE_BANNER, Material.PINK_BANNER,
                Material.PURPLE_BANNER, Material.RED_BANNER, Material.WHITE_BANNER, Material.YELLOW_BANNER
        };
        int validBannerArrayLength = validBannerArray.length;
        
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Material bannerType = validBannerArray[random.nextInt(validBannerArrayLength)];
        return new ItemStack(bannerType);
    }
}