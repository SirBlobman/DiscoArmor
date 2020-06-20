package com.SirBlobman.disco.armor.listener;

import java.util.Objects;

import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.manager.ArmorChoiceManager;
import com.SirBlobman.disco.armor.task.DiscoArmorTask;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerDiscoArmor implements Listener {
    private final DiscoArmorPlugin plugin;
    public ListenerDiscoArmor(DiscoArmorPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ArmorChoiceManager armorChoiceManager = this.plugin.getArmorChoiceManager();
        armorChoiceManager.setArmorType(player, null);
    
        DiscoArmorTask discoArmorTask = this.plugin.getDiscoArmorTask();
        discoArmorTask.loadOldArmor(player);
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if(!(entity instanceof Player)) return;
        Player player = (Player) entity;
    
        YamlConfiguration config = this.plugin.getConfig();
        if(!config.getBoolean("disable-on-damage", true)) return;
    
        ArmorChoiceManager armorChoiceManager = this.plugin.getArmorChoiceManager();
        armorChoiceManager.setArmorType(player, null);
        
        DiscoArmorTask discoArmorTask = this.plugin.getDiscoArmorTask();
        discoArmorTask.loadOldArmor(player);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if(ItemUtil.isAir(item)) return;
        if(!this.plugin.isDiscoArmor(item)) return;
    
        ConfigManager<?> configManager = this.plugin.getConfigManager();
        String message = configManager.getConfigMessage("config.yml", "messages.prevent-click", true);
    
        HumanEntity human = e.getWhoClicked();
        human.sendMessage(message);
        e.setCancelled(true);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onDrop(PlayerDropItemEvent e) {
        Item entity = e.getItemDrop();
        ItemStack item = entity.getItemStack();
        if(ItemUtil.isAir(item)) return;
        if(!this.plugin.isDiscoArmor(item)) return;
    
        ConfigManager<?> configManager = this.plugin.getConfigManager();
        String message = configManager.getConfigMessage("config.yml", "messages.prevent-click", true);
    
        Player player = e.getPlayer();
        player.sendMessage(message);
        e.setCancelled(true);
    }
}