package com.SirBlobman.discoarmor;

import com.SirBlobman.discoarmor.configuration.ConfigSettings;
import com.SirBlobman.discoarmor.type.AmericanFlagType;
import com.SirBlobman.discoarmor.type.DiscoArmorType;
import com.SirBlobman.discoarmor.type.OneColorType;
import com.SirBlobman.discoarmor.type.RandomColorType;
import com.SirBlobman.discoarmor.type.SmoothArmorType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

public class DiscoArmor extends JavaPlugin implements Listener {
    public static final Logger LOG = Logger.getLogger("Disco Armor");
    
    private static DiscoArmorRunnable DAR = null;
    public static DiscoArmor INSTANCE;
    public static File FOLDER;
    
    @Override
    public void onEnable() {
        INSTANCE = this;
        FOLDER = getDataFolder();
        ConfigSettings.load();
        
        getCommand("discoarmor").setExecutor(this);
        
        Bukkit.getPluginManager().registerEvents(this, this);
        DiscoArmorType.registerDiscoArmorType("RANDOM", new RandomColorType());
        DiscoArmorType.registerDiscoArmorType("ONE_COLOR", new OneColorType());
        DiscoArmorType.registerDiscoArmorType("SMOOTH", new SmoothArmorType());
        DiscoArmorType.registerDiscoArmorType("OLD_GLORY", new AmericanFlagType());
    }
    
    @Override
    public void onDisable() {
        if(DAR != null) DAR.cancel();
        Bukkit.getOnlinePlayers().stream().filter(DiscoArmor::hasDiscoArmorEnabled).forEach(player -> {
            UUID uuid = player.getUniqueId();
            DISCO_ARMOR.put(uuid, false);
            
            ItemStack air = new ItemStack(Material.AIR, 1);
            ItemStack[] oldArmor = OLD_ARMOR.getOrDefault(uuid, new ItemStack[] {air, air, air, air});
            PlayerInventory pi = player.getInventory();
            pi.setArmorContents(oldArmor);
            OLD_ARMOR.remove(uuid);
        });
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command c, String label, String[] args) {
        String cmd = c.getName().toLowerCase();
        if(cmd.equals("discoarmor")) {
            if(cs instanceof Player) {
                Player player = (Player) cs;
                UUID uuid = player.getUniqueId();
                if(args.length > 0) {
                    String sub = args[0].toLowerCase();
                    if(sub.equals("on")) {
                        if(DAR == null) reloadDiscoArmorRunnable();
                        
                        if(!hasDiscoArmorEnabled(player)) {
                            PlayerInventory pi = player.getInventory();
                            ItemStack[] armor = pi.getArmorContents();
                            OLD_ARMOR.put(uuid, armor);
                            
                            DISCO_ARMOR.put(uuid, true);
                            ConfigSettings.sendMessage(player, "enabled");
                            return true;
                        } else {
                            ConfigSettings.sendMessage(player, "already enabled");
                            return true;
                        }
                    } else if(sub.equals("off")) {
                        if(hasDiscoArmorEnabled(player)) {
                            DISCO_ARMOR.put(uuid, false);
                            
                            ItemStack air = new ItemStack(Material.AIR, 1);
                            ItemStack[] oldArmor = OLD_ARMOR.getOrDefault(uuid, new ItemStack[] {air, air, air, air});
                            PlayerInventory pi = player.getInventory();
                            pi.setArmorContents(oldArmor);
                            OLD_ARMOR.remove(uuid);
                            
                            ConfigSettings.sendMessage(player, "disabled");
                            return true;
                        } else {
                            ConfigSettings.sendMessage(player, "already disabled");
                            return true;
                        }
                    } else if(sub.equals("reload")) {
                        ConfigSettings.load();
                        reloadDiscoArmorRunnable();                        
                        ConfigSettings.sendMessage(player, "reloaded");
                        return true;
                    } else {
                        ConfigSettings.sendMessage(player, "invalid usage");
                        return false;
                    }
                } else {
                    ConfigSettings.sendMessage(player, "invalid usage");
                    return false;
                }
            } else {
                ConfigSettings.sendMessage(cs, "only player");
                return true;
            }
        } else return false;
    }
    
    private void reloadDiscoArmorRunnable() {
        int timer = ConfigSettings.getOption("options.armor speed", 1);
        if(timer < 1) timer = 1;
        
        String armorTypeString = ConfigSettings.getOption("options.armor type", "RANDOM");
        DiscoArmorType armorType = DiscoArmorType.getArmorType(armorTypeString);
        if(armorType == null) {
            getLogger().warning("Invalid armor type '" + armorType + "' in config. Defaulting to RANDOM");
            armorType = DiscoArmorType.getArmorType("RANDOM");
        }
        
        if(DAR != null) DAR.cancel();
        DAR = new DiscoArmorRunnable(armorType);
        DAR.runTaskTimerAsynchronously(this, 0L, timer);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String label, String[] args) {
        if(args.length == 1) {
            String sub = args[0];
            List<String> valid = Lists.newArrayList("on", "off");
            if(cs.hasPermission("discoarmor.reload")) valid.add("reload");
            List<String> matching = Lists.newArrayList();
            valid.forEach(str -> {
                String lower = str.toLowerCase();
                String arg = sub.toLowerCase();
                if(lower.startsWith(arg)) matching.add(lower);
            });
            return matching;
        } else return Lists.newArrayList();
    }
    
    private static Map<UUID, Boolean> DISCO_ARMOR = new HashMap<>();
    private static Map<UUID, ItemStack[]> OLD_ARMOR = new HashMap<>(); 
    public static boolean hasDiscoArmorEnabled(Player player) {
        UUID uuid = player.getUniqueId();
        return DISCO_ARMOR.getOrDefault(uuid, false);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if(item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if(meta.hasDisplayName()) {
                String displayName = meta.getDisplayName();
                String armorName = ConfigSettings.getMessage("armor display name");
                if(displayName.equals(armorName)) {
                    e.setCancelled(true);
                    ConfigSettings.sendMessage(e.getWhoClicked(), "no move");
                }
            }
        }
    }
}