package com.SirBlobman.disco.armor.object;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class OldGloryArmorType extends ArmorType {
    private static final Color OLD_GLORY_RED = Color.fromRGB(0xBF0A30);
    private static final Color OLD_GLORY_WHITE = Color.fromRGB(0xFFFFFF);
    private static final Color OLD_GLORY_BLUE = Color.fromRGB(0x002868);
    public OldGloryArmorType() {
        super("old_glory");
    }
    
    @Override
    public String getDisplayName() {
        return "&9Old &cGlory";
    }
    
    @Override
    public Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int choice = rng.nextInt(3);
        return (choice == 0 ? OLD_GLORY_RED : (choice == 1 ? OLD_GLORY_BLUE : OLD_GLORY_WHITE));
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
        ItemStack item = new ItemStack(Material.RED_BANNER);
        BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
        if(bannerMeta == null) throw new IllegalStateException("null banner meta!");
        
        Pattern whitePaly = new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL);
        bannerMeta.addPattern(whitePaly);
        
        Pattern blueChiefDexterCanton = new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_LEFT);
        bannerMeta.addPattern(blueChiefDexterCanton);
        
        item.setItemMeta(bannerMeta);
        return item;
    }
}