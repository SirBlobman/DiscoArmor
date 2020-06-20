package com.SirBlobman.disco.armor;

import java.util.Collection;
import java.util.logging.Logger;

import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.api.nms.AbstractNMS;
import com.SirBlobman.api.nms.ItemHandler;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.nms.VersionUtil;
import com.SirBlobman.api.plugin.SirBlobmanPlugin;
import com.SirBlobman.api.utility.MessageUtil;
import com.SirBlobman.disco.armor.command.CommandDiscoArmor;
import com.SirBlobman.disco.armor.listener.ListenerDiscoArmor;
import com.SirBlobman.disco.armor.manager.ArmorChoiceManager;
import com.SirBlobman.disco.armor.manager.ArmorTypeManager;
import com.SirBlobman.disco.armor.object.*;
import com.SirBlobman.disco.armor.task.DiscoArmorTask;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DiscoArmorPlugin extends SirBlobmanPlugin<DiscoArmorPlugin> {
    private final ArmorTypeManager armorTypeManager;
    private final ArmorChoiceManager armorChoiceManager;
    private DiscoArmorTask discoArmorTask;
    public DiscoArmorPlugin() {
        this.armorChoiceManager = new ArmorChoiceManager(this);
        this.armorTypeManager = new ArmorTypeManager(this);
        this.discoArmorTask = new DiscoArmorTask(this);
    }
    
    @Override
    public void onLoad() {
        saveDefaultConfig();
    }
    
    @Override
    public void onEnable() {
        int minorVersion = VersionUtil.getMinorVersion();
        if(minorVersion < 13 || minorVersion > 15) {
            String fullVersion = VersionUtil.getMinecraftVersion();
            Logger logger = getLogger();
            logger.warning(fullVersion + " is not supported, please use 1.13.2, 1.14.4, or 1.15.2");
            
            setEnabled(false);
            return;
        }
        
        registerArmorTypes();
        registerCommand(CommandDiscoArmor.class);
        registerListener(new ListenerDiscoArmor(this));
    
        YamlConfiguration config = getConfig();
        long speed = config.getLong("armor-speed", 5L);
    
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        discoArmorTask.runTaskTimerAsynchronously(this, 0L, speed);
        
        String enableBroadcast = config.getString("messages.enable-broadcast");
        if(enableBroadcast != null) {
            String enableBroadcastColor = MessageUtil.color(enableBroadcast);
            Bukkit.broadcastMessage(enableBroadcastColor);
        }
    }
    
    @Override
    public void onDisable() {
        ArmorChoiceManager armorChoiceManager = getArmorChoiceManager();
        Collection<? extends Player> onlinePlayerList = Bukkit.getOnlinePlayers();
        onlinePlayerList.forEach(player -> armorChoiceManager.setArmorType(player, null));
        
        YamlConfiguration config = getConfig();
        String disableBroadcast = config.getString("messages.disable-broadcast");
        if(disableBroadcast != null) {
            String disableBroadcastColor = MessageUtil.color(disableBroadcast);
            Bukkit.broadcastMessage(disableBroadcastColor);
        }
    }
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        
        this.discoArmorTask.cancel();
    
        YamlConfiguration config = getConfig();
        long speed = config.getLong("armor-speed", 5L);
    
        this.discoArmorTask = new DiscoArmorTask(this);
        this.discoArmorTask.runTaskTimerAsynchronously(this, 0L, speed);
    }
    
    public ArmorTypeManager getArmorTypeManager() {
        return this.armorTypeManager;
    }
    
    public ArmorChoiceManager getArmorChoiceManager() {
        return this.armorChoiceManager;
    }
    
    public DiscoArmorTask getDiscoArmorTask() {
        return this.discoArmorTask;
    }
    
    public boolean isDiscoArmor(ItemStack item) {
        if(ItemUtil.isAir(item)) return false;
    
        MultiVersionHandler<?> multiVersionHandler = getMultiVersionHandler();
        AbstractNMS nmsHandler = multiVersionHandler.getInterface();
        ItemHandler itemHandler = nmsHandler.getItemHandler();
        
        String disco = itemHandler.getCustomNBT(item, "disco", "fail");
        return disco.equals("armor");
    }
    
    private void registerArmorTypes() {
        ArmorTypeManager armorTypeManager = getArmorTypeManager();
        armorTypeManager.registerArmorType(GrayscaleArmorType.class);
        armorTypeManager.registerArmorType(OldGloryArmorType.class);
        armorTypeManager.registerArmorType(OneColorArmorType.class);
        armorTypeManager.registerArmorType(RainbowArmorType.class);
        armorTypeManager.registerArmorType(RandomArmorType.class);
        armorTypeManager.registerArmorType(SmoothArmorType.class);
        armorTypeManager.registerArmorType(YellowOrangeArmorType.class);
    }
}