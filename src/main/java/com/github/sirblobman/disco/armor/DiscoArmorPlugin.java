package com.github.sirblobman.disco.armor;

import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.bstats.bukkit.Metrics;
import com.github.sirblobman.api.bstats.charts.SimplePie;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.UpdateManager;
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

public final class DiscoArmorPlugin extends ConfigurablePlugin {
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
        languageManager.saveDefaultLanguageFiles();
    }

    @Override
    public void onEnable() {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            Logger logger = getLogger();
            logger.warning("This plugin was made for 1.13+");
            logger.warning("You should not be using it on " + VersionUtility.getMinecraftVersion());
        }

        reloadConfiguration();
        registerPatterns();

        registerCommands();
        registerListeners();
        registerUpdateChecker();

        broadcastEnabledMessage();
        registerbStats();
    }

    @Override
    public void onDisable() {
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        if (discoArmorTask != null) {
            try {
                discoArmorTask.cancel();
                discoArmorTask.disableAll();
            } catch (IllegalStateException ignored) {
                // Do Nothing
            }
        }

        broadcastDisabledMessage();
    }

    @Override
    protected void reloadConfiguration() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguageFiles();

        registerTasks();
    }

    public PatternManager getPatternManager() {
        return this.patternManager;
    }

    public DiscoArmorTask getDiscoArmorTask() {
        return this.discoArmorTask;
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
        new ListenerDiscoArmor(this).register();
    }

    private void registerTasks() {
        DiscoArmorTask discoArmorTask = getDiscoArmorTask();
        if (discoArmorTask != null) {
            try {
                discoArmorTask.cancel();
            } catch (IllegalStateException ignored) {
                // Do Nothing
            }

            discoArmorTask.disableAll();
        }

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        long taskPeriod = configuration.getLong("armor-speed");

        this.discoArmorTask = new DiscoArmorTask(this);
        this.discoArmorTask.runTaskTimer(this, 5L, taskPeriod);
    }

    private void registerUpdateChecker() {
        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        UpdateManager updateManager = corePlugin.getUpdateManager();
        updateManager.addResource(this, 60700L);
    }

    private void registerbStats() {
        Metrics metrics = new Metrics(this, 16221);
        metrics.addCustomChart(new SimplePie("selected_language", this::getDefaultLanguageCode));
    }

    private String getDefaultLanguageCode() {
        LanguageManager languageManager = getLanguageManager();
        Language defaultLanguage = languageManager.getDefaultLanguage();
        return (defaultLanguage == null ? "none" : defaultLanguage.getLanguageCode());
    }

    private void broadcastEnabledMessage() {
        LanguageManager languageManager = getLanguageManager();
        languageManager.broadcastMessage("broadcast.enabled", null, null);
    }

    private void broadcastDisabledMessage() {
        LanguageManager languageManager = getLanguageManager();
        languageManager.broadcastMessage("broadcast.disabled", null, null);
    }
}
