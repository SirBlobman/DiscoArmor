package com.SirBlobman.disco.armor.menu;

import java.util.Objects;

import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.menu.button.MenuButton;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.manager.ArmorChoiceManager;
import com.SirBlobman.disco.armor.object.ArmorType;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import org.bukkit.event.inventory.InventoryClickEvent;

public class ArmorTypeButton extends MenuButton {
    private final ArmorType armorType;
    private final DiscoArmorPlugin plugin;
    public ArmorTypeButton(DiscoArmorPlugin plugin, ArmorType armorType) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.armorType = armorType;
    }
    
    @Override
    public void onClick(InventoryClickEvent e) {
        HumanEntity human = e.getWhoClicked();
        if(!(human instanceof Player)) return;
        Player player = (Player) human;
    
        player.closeInventory();
        ArmorChoiceManager armorChoiceManager = this.plugin.getArmorChoiceManager();
        armorChoiceManager.setArmorType(player, this.armorType);
    
        ConfigManager<?> configManager = this.plugin.getConfigManager();
        String messageFormat = configManager.getConfigMessage("config.yml", "messages.change-type", true);
        String armorTypeString = (this.armorType == null ? "none" : armorType.getId());
        
        String message = messageFormat.replace("{type}", armorTypeString);
        player.sendMessage(message);
    }
}