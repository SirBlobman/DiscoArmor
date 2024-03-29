package com.github.sirblobman.disco.armor.listener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.listener.PluginListener;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.DiscoArmorConfiguration;
import com.github.sirblobman.disco.armor.task.DiscoArmorTask;
import com.github.sirblobman.disco.armor.task.DiscoArmorTaskManager;

public final class ListenerDiscoArmor extends PluginListener<DiscoArmorPlugin> {
    public ListenerDiscoArmor(@NotNull DiscoArmorPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DiscoArmorTaskManager taskManager = getTaskManager();
        taskManager.createTask(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        DiscoArmorTaskManager taskManager = getTaskManager();
        taskManager.removeTask(player);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        DiscoArmorPlugin plugin = getPlugin();
        DiscoArmorConfiguration configuration = plugin.getConfiguration();
        if (!configuration.isDisableOnDamage()) {
            return;
        }

        DiscoArmorTaskManager taskManager = getTaskManager();
        DiscoArmorTask task = taskManager.getTask(player);
        if (task == null) {
            return;
        }

        if (task.isEnabled()) {
            task.disable();
            if (configuration.isPreventFirstHit()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (isNotDiscoArmor(item)) {
            return;
        }

        e.setCancelled(true);
        HumanEntity human = e.getWhoClicked();
        DiscoArmorPlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        languageManager.sendMessage(human, "error.prevent-removal");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        Item entity = e.getItemDrop();
        ItemStack item = entity.getItemStack();
        if (isNotDiscoArmor(item)) {
            return;
        }

        e.setCancelled(true);
        Player player = e.getPlayer();
        DiscoArmorPlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        languageManager.sendMessage(player, "error.prevent-removal");
    }

    private @NotNull DiscoArmorTaskManager getTaskManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getTaskManager();
    }

    private boolean isNotDiscoArmor(@Nullable ItemStack item) {
        if (ItemUtility.isAir(item)) {
            return true;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return true;
        }

        DiscoArmorPlugin plugin = getPlugin();
        NamespacedKey discoArmorKey = new NamespacedKey(plugin, "disco");
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        byte discoArmor = persistentDataContainer.getOrDefault(discoArmorKey, PersistentDataType.BYTE, (byte) 0);
        return (discoArmor != 1);
    }
}
