package com.github.sirblobman.disco.armor;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.update.UpdateChecker;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.disco.armor.command.CommandDiscoArmor;
import com.github.sirblobman.disco.armor.listener.ListenerDiscoArmor;
import com.github.sirblobman.disco.armor.manager.PatternManager;
import com.github.sirblobman.disco.armor.pattern.GrayscalePattern;
import com.github.sirblobman.disco.armor.pattern.OldGloryPattern;
import com.github.sirblobman.disco.armor.pattern.OneColorPattern;
import com.github.sirblobman.disco.armor.pattern.RainbowPattern;
import com.github.sirblobman.disco.armor.pattern.RandomPattern;
import com.github.sirblobman.disco.armor.pattern.SmoothPattern;
import com.github.sirblobman.disco.armor.pattern.YellowOrangePattern;
import com.github.sirblobman.disco.armor.task.DiscoArmorTask;

public class DiscoArmorPlugin extends JavaPlugin {
    private final ConfigurationManager configurationManager;
    private final LanguageManager languageManager;
    private final PlayerDataManager playerDataManager;
    private final PatternManager patternManager;
    private DiscoArmorTask discoArmorTask;
    public DiscoArmorPlugin() {
        this.configurationManager = new ConfigurationManager(this);
        this.languageManager = new LanguageManager(this, this.configurationManager);
        this.playerDataManager = new PlayerDataManager(this);
        this.patternManager = new PatternManager(this);
        this.discoArmorTask = new DiscoArmorTask(this);
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
        configurationManager.saveDefault("language.yml");
        configurationManager.saveDefault("language/en_us.lang.yml");
    }

    @Override
    public void onEnable() {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 13) {
            Logger logger = getLogger();
            logger.warning("This plugin was made for 1.13+");
            logger.warning("You should not be using it on " + VersionUtility.getMinecraftVersion());
        }

        PatternManager patternManager = getPatternManager();
        patternManager.register(GrayscalePattern.class);
        patternManager.register(OldGloryPattern.class);
        patternManager.register(OneColorPattern.class);
        patternManager.register(RainbowPattern.class);
        patternManager.register(RandomPattern.class);
        patternManager.register(SmoothPattern.class);
        patternManager.register(YellowOrangePattern.class);

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        long taskPeriod = configuration.getLong("armor-speed");

        CommandDiscoArmor command = new CommandDiscoArmor(this);
        command.register();

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ListenerDiscoArmor(this), this);

        DiscoArmorTask task = getDiscoArmorTask();
        task.runTaskTimer(this, 5L, taskPeriod);

        String enableBroadcast = configuration.getString("broadcast.enabled");
        if(enableBroadcast != null) {
            String message = MessageUtility.color(enableBroadcast);
            Bukkit.broadcastMessage(message);
        }

        UpdateChecker updateChecker = new UpdateChecker(this, 60700L);
        updateChecker.runCheck();
    }

    @Override
    public void onDisable() {
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        discoArmorTask.cancel();
        discoArmorTask.disableAll();

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        String disableBroadcast = configuration.getString("broadcast.disabled");
        if(disableBroadcast != null) {
            String message = MessageUtility.color(disableBroadcast);
            Bukkit.broadcastMessage(message);
        }
    }

    @Override
    public YamlConfiguration getConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        return configurationManager.get("config.yml");
    }

    @Override
    public void reloadConfig() {
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        discoArmorTask.cancel();
        discoArmorTask.disableAll();

        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");
        configurationManager.reload("language.yml");
        configurationManager.reload("language/en_us.lang.yml");

        YamlConfiguration configuration = configurationManager.get("config.yml");
        long taskPeriod = configuration.getLong("armor-speed");

        this.discoArmorTask = new DiscoArmorTask(this);
        this.discoArmorTask.runTaskTimer(this, 5L, taskPeriod);
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    public PatternManager getPatternManager() {
        return this.patternManager;
    }

    public DiscoArmorTask getDiscoArmorTask() {
        return this.discoArmorTask;
    }

    public MultiVersionHandler getMultiVersionHandler() {
        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        return corePlugin.getMultiVersionHandler();
    }
}