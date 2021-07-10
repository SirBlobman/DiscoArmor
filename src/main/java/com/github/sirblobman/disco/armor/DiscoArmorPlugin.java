package com.github.sirblobman.disco.armor;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.UpdateManager;
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

public class DiscoArmorPlugin extends ConfigurablePlugin {
    private final PatternManager patternManager;
    private DiscoArmorTask discoArmorTask;

    public DiscoArmorPlugin() {
        this.patternManager = new PatternManager(this);
        this.discoArmorTask = new DiscoArmorTask(this);
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
        configurationManager.saveDefault("language.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.saveDefaultLanguages();
        languageManager.reloadLanguages();
    }

    @Override
    public void onEnable() {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 13) {
            Logger logger = getLogger();
            logger.warning("This plugin was made for 1.13+");
            logger.warning("You should not be using it on " + VersionUtility.getMinecraftVersion());
        }

        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        UpdateManager updateManager = corePlugin.getUpdateManager();
        updateManager.addResource(this, 60700L);

        registerPatterns();
        registerCommands();
        registerListeners();
        registerTasks();

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        String enableBroadcast = configuration.getString("broadcast.enabled");
        if(enableBroadcast != null) {
            String message = MessageUtility.color(enableBroadcast);
            Bukkit.broadcastMessage(message);
        }
    }

    @Override
    public void onDisable() {
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        if(discoArmorTask != null) {
            try {
                discoArmorTask.cancel();
                discoArmorTask.disableAll();
            } catch(IllegalStateException ignored) {}
        }

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        String disableBroadcast = configuration.getString("broadcast.disabled");

        if(disableBroadcast != null) {
            String message = MessageUtility.color(disableBroadcast);
            Bukkit.broadcastMessage(message);
        }
    }

    public PatternManager getPatternManager() {
        return this.patternManager;
    }

    public DiscoArmorTask getDiscoArmorTask() {
        return this.discoArmorTask;
    }

    public void onReload() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");
        configurationManager.reload("language.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguages();

        registerTasks();
    }

    private void registerPatterns() {
        PatternManager patternManager = getPatternManager();
        patternManager.register(GrayscalePattern.class);
        patternManager.register(OldGloryPattern.class);
        patternManager.register(OneColorPattern.class);
        patternManager.register(RainbowPattern.class);
        patternManager.register(RandomPattern.class);
        patternManager.register(SmoothPattern.class);
        patternManager.register(YellowOrangePattern.class);
    }

    private void registerCommands() {
        new CommandDiscoArmor(this).register();
    }

    private void registerListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ListenerDiscoArmor(this), this);
    }

    private void registerTasks() {
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        if(discoArmorTask != null) {
            try {
                discoArmorTask.cancel();
            } catch(IllegalStateException ignored) {}

            discoArmorTask.disableAll();
        }

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        long taskPeriod = configuration.getLong("armor-speed");

        this.discoArmorTask = new DiscoArmorTask(this);
        this.discoArmorTask.runTaskTimer(this, 5L, taskPeriod);
    }
}
