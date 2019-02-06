package com.SirBlobman.discoarmor.type;

import com.SirBlobman.discoarmor.configuration.ConfigSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public abstract class DiscoArmorType {
    /**
     * Get the disco armor
     * @return An ItemStack array with the following order: boots, leggings, chestplate, helmet
     */
    public abstract ItemStack[] getDiscoArmor();
    
    protected Color generateRandomColor() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int red = tlr.nextInt(0, 256);
        int green = tlr.nextInt(0, 256);
        int blue = tlr.nextInt(0, 256);
        return Color.fromRGB(red, green, blue);
    }
    
    protected ItemStack getColoredArmor(EquipmentSlot slot, Color color) {
        Material type = null;
        switch(slot) {
        case HEAD:
            type = Material.LEATHER_HELMET;
            break;
        case CHEST:
            type = Material.LEATHER_CHESTPLATE;
            break;
        case LEGS:
            type = Material.LEATHER_LEGGINGS;
            break;
        case FEET:
            type = Material.LEATHER_BOOTS;
            break;
        default: return null;
        }
        
        ItemStack item = new ItemStack(type, 1);
        ItemMeta meta = item.getItemMeta();
        LeatherArmorMeta leatherArmor = (LeatherArmorMeta) meta;
        
        String armorName = ConfigSettings.getMessage("armor display name");
        leatherArmor.setDisplayName(armorName);
        leatherArmor.setColor(color);
        leatherArmor.addEnchant(Enchantment.LUCK, 0, true);
        leatherArmor.addItemFlags(ItemFlag.values());
        
        item.setItemMeta(leatherArmor);
        return item;
    }
    
    private static final Map<String, DiscoArmorType> ARMOR_TYPES = new HashMap<>();
    public static void registerDiscoArmorType(String typeName, DiscoArmorType handler) {
        final String actualName = typeName.toUpperCase().replaceAll("[^A-Za-z_]", "_");
        if(ARMOR_TYPES.containsKey(actualName)) throw new IllegalArgumentException("The armor type " + actualName + " is already registered!");
        ARMOR_TYPES.put(actualName, handler);
    }
    
    public static DiscoArmorType getArmorType(String typeName) {
        final String actualName = typeName.toUpperCase().replaceAll("[^A-Za-z_]", "_");
        return ARMOR_TYPES.getOrDefault(actualName, null);
    }
}