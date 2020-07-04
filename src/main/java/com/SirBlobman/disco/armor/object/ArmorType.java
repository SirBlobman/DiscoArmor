package com.SirBlobman.disco.armor.object;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.api.nms.AbstractNMS;
import com.SirBlobman.api.nms.ItemHandler;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.utility.MessageUtil;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.manager.ArmorChoiceManager;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ArmorType {
    private final String id;
    ArmorType(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null!");
        if(this.id.isEmpty()) throw new IllegalArgumentException("id must not be empty!");
    }
    
    public final String getId() {
        return this.id;
    }
    
    public final ItemStack createLeatherArmor(Player player, EquipmentSlot slot, Color color) {
        DiscoArmorPlugin discoArmorPlugin = JavaPlugin.getPlugin(DiscoArmorPlugin.class);
        YamlConfiguration config = discoArmorPlugin.getConfig();
        
        MultiVersionHandler<?> multiVersionHandler = discoArmorPlugin.getMultiVersionHandler();
        AbstractNMS nmsHandler = multiVersionHandler.getInterface();
        ItemHandler itemHandler = nmsHandler.getItemHandler();
    
        Material material = getMaterial(slot);
        if(material == null) return null;
        
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if(!(meta instanceof LeatherArmorMeta)) return item;
    
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        armorMeta.setColor(color);
        
        String displayName = config.getString("display-name");
        if(displayName != null) {
            String displayNameColored = MessageUtil.color(displayName);
            armorMeta.setDisplayName(displayNameColored);
        }
        
        List<String> loreList = config.getStringList("lore");
        if(!loreList.isEmpty()) {
            List<String> loreListColored = MessageUtil.colorList(loreList);
            armorMeta.setLore(loreListColored);
        }
    
        ArmorChoiceManager armorChoiceManager = discoArmorPlugin.getArmorChoiceManager();
        if(armorChoiceManager.shouldGlow(player)) {
            armorMeta.addEnchant(Enchantment.LUCK, 1, true);
            armorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        
        item.setItemMeta(armorMeta);
        return itemHandler.setCustomNBT(item, "disco", "armor");
    }
    
    public final Material getMaterial(EquipmentSlot slot) {
        switch(slot) {
            case HEAD: return Material.LEATHER_HELMET;
            case LEGS: return Material.LEATHER_LEGGINGS;
            case FEET: return Material.LEATHER_BOOTS;
            default: break;
        }
        
        return Material.LEATHER_CHESTPLATE;
    }
    
    public final ItemStack getNamedMenuIcon() {
        ItemStack item = getMenuIcon();
        if(item == null) return ItemUtil.newItem(Material.BARRIER);
        
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        
        String displayName = getDisplayName();
        String displayNameColored = MessageUtil.color(displayName);
        meta.setDisplayName(displayNameColored);
    
        ItemFlag[] itemFlagArray = ItemFlag.values();
        meta.addItemFlags(itemFlagArray);
        
        item.setItemMeta(meta);
        return item;
    }
    
    public abstract String getDisplayName();
    public abstract Color getNextColor(Player player);
    public abstract Map<EquipmentSlot, ItemStack> getNextArmor(Player player);
    public abstract ItemStack getMenuIcon();
}
