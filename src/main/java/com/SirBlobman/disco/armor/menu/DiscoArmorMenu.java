package com.SirBlobman.disco.armor.menu;

import java.util.List;

import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.api.menu.AbstractMenu;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.manager.ArmorTypeManager;
import com.SirBlobman.disco.armor.object.ArmorType;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class DiscoArmorMenu extends AbstractMenu<DiscoArmorPlugin> {
    public DiscoArmorMenu(DiscoArmorPlugin plugin, Player player) {
        super(plugin, player);
    }
    
    @Override
    public void onValidClick(InventoryClickEvent e) {
        // Do Nothing
    }
    
    @Override
    public void onValidDrag(InventoryDragEvent e) {
        // Do Nothing
    }
    
    @Override
    public void onValidClose(InventoryCloseEvent e) {
        // Do Nothing
    }
    
    @Override
    public Inventory getInventory() {
        ArmorTypeManager armorTypeManager = this.plugin.getArmorTypeManager();
        List<ArmorType> armorTypeList = armorTypeManager.getArmorTypes();
        int armorTypeListSize = armorTypeList.size();
        
        YamlConfiguration config = this.plugin.getConfig();
        String title = config.getString("menu-title");
        int size = getInventorySize(armorTypeListSize + 1);
        
        Inventory inventory = getInventory(size, title);
        ItemStack[] contents = inventory.getContents();
        
        for(int slot = 0; slot < size && slot < armorTypeListSize; slot++) {
            ArmorType armorType = armorTypeList.get(slot);
            contents[slot] = armorType.getNamedMenuIcon();
            
            ArmorTypeButton button = new ArmorTypeButton(this.plugin, armorType);
            addButton(slot, button);
        }
        
        contents[size - 1] = ItemUtil.newItem(Material.BARRIER, 1, 0, "&fDisable Disco Armor");
        addButton(size - 1, new ArmorTypeButton(this.plugin, null));
        
        inventory.setContents(contents);
        return inventory;
    }
    
    private int getInventorySize(int number) {
        if(number <= 9) return 9;
        if(number <= 18) return 18;
        if(number <= 27) return 27;
        if(number <= 36) return 36;
        if(number <= 45) return 45;
        return 54;
    }
}