package com.SirBlobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import com.SirBlobman.api.command.PlayerCommand;
import com.SirBlobman.api.command.SubCommand;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.manager.ArmorChoiceManager;
import com.SirBlobman.disco.armor.menu.DiscoArmorMenu;

import org.bukkit.entity.Player;

public class CommandDiscoArmor extends PlayerCommand<DiscoArmorPlugin> {
    public CommandDiscoArmor(DiscoArmorPlugin plugin) {
        super(plugin, "disco-armor");
    }
    
    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean onCommand(Player player, String[] args) {
        return false;
    }
    
    @SubCommand(name="reload")
    public boolean reload(Player player, String[] args) {
        if(!player.hasPermission("disco-armor.command.disco-armor.reload")) return true;
        
        this.plugin.reloadConfig();
        return true;
    }
    
    @SubCommand(name="menu")
    public boolean menu(Player player, String[] args) {
        DiscoArmorMenu menu = new DiscoArmorMenu(this.plugin, player);
        menu.open();
        return true;
    }
    
    @SubCommand(name="on")
    public boolean on(Player player, String[] args) {
        return menu(player, args);
    }
    
    @SubCommand(name="off")
    public boolean off(Player player, String[] args) {
        ArmorChoiceManager armorChoiceManager = this.plugin.getArmorChoiceManager();
        armorChoiceManager.setArmorType(player, null);
        return true;
    }
}