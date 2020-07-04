package com.SirBlobman.disco.armor.object;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RandomArmorType extends ArmorType {
    public RandomArmorType() {
        super("random");
    }
    
    @Override
    public String getDisplayName() {
        ChatColor[] colorFormatArray = ChatColor.values();
        ChatColor[] colorArray = Arrays.stream(colorFormatArray).filter(ChatColor::isColor).toArray(ChatColor[]::new);
        int colorArrayLength = colorArray.length;
        
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int randomIndex = rng.nextInt(colorArrayLength);
        
        ChatColor chatColor = colorArray[randomIndex];
        return (chatColor + "Random Color");
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
    public Map<EquipmentSlot, ItemStack> getNextArmor(Player player) {
        Map<EquipmentSlot, ItemStack> nextArmor = new EnumMap<>(EquipmentSlot.class);
        EquipmentSlot[] slotArray = EquipmentSlot.values();
        
        for(EquipmentSlot slot : slotArray) {
            Color color = getNextColor(player);
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
