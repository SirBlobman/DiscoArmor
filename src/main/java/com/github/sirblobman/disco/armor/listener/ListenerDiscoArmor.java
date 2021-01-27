package com.github.sirblobman.disco.armor.listener;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.task.DiscoArmorTask;

public class ListenerDiscoArmor implements Listener {
    private final DiscoArmorPlugin plugin;
    public ListenerDiscoArmor(DiscoArmorPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        DiscoArmorTask discoArmorTask = this.plugin.getDiscoArmorTask();
        discoArmorTask.disable(player);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if(!(entity instanceof Player)) return;

        ConfigurationManager configurationManager = this.plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        if(!configuration.getBoolean("disable-on-damage")) return;

        Player player = (Player) entity;
        DiscoArmorTask discoArmorTask = this.plugin.getDiscoArmorTask();
        discoArmorTask.disable(player);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if(isNotDiscoArmor(item)) return;
        e.setCancelled(true);

        HumanEntity human = e.getWhoClicked();
        LanguageManager languageManager = this.plugin.getLanguageManager();
        languageManager.sendMessage(human, "error.prevent-removal", null, true);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onDrop(PlayerDropItemEvent e) {
        Item entity = e.getItemDrop();
        ItemStack item = entity.getItemStack();
        if(isNotDiscoArmor(item)) return;
        e.setCancelled(true);

        Player player = e.getPlayer();
        LanguageManager languageManager = this.plugin.getLanguageManager();
        languageManager.sendMessage(player, "error.prevent-removal", null, true);
    }

    private boolean isNotDiscoArmor(ItemStack item) {
        if(ItemUtility.isAir(item)) return true;
        MultiVersionHandler multiVersionHandler = this.plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();

        String customNBT = itemHandler.getCustomNBT(item, "disco", "not");
        return !customNBT.equals("armor");
    }
}