package com.github.sirblobman.disco.armor;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.SpigotUpdateManager;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.disco.armor.command.CommandDiscoArmor;
import com.github.sirblobman.disco.armor.configuration.DiscoArmorConfiguration;
import com.github.sirblobman.disco.armor.listener.ListenerDiscoArmor;
import com.github.sirblobman.disco.armor.pattern.PatternManager;
import com.github.sirblobman.disco.armor.pattern.GrayscalePattern;
import com.github.sirblobman.disco.armor.pattern.OldGloryPattern;
import com.github.sirblobman.disco.armor.pattern.OneColorPattern;
import com.github.sirblobman.disco.armor.pattern.RainbowPattern;
import com.github.sirblobman.disco.armor.pattern.RandomPattern;
import com.github.sirblobman.disco.armor.pattern.SmoothPattern;
import com.github.sirblobman.disco.armor.pattern.YellowOrangePattern;
import com.github.sirblobman.disco.armor.task.DiscoArmorTaskManager;
import com.github.sirblobman.api.shaded.bstats.bukkit.Metrics;
import com.github.sirblobman.api.shaded.bstats.charts.SimplePie;

public final class DiscoArmorPlugin extends ConfigurablePlugin {
    private final DiscoArmorConfiguration configuration;

    private final PatternManager patternManager;
    private final DiscoArmorTaskManager taskManager;

    public DiscoArmorPlugin() {
        this.configuration = new DiscoArmorConfiguration();

        this.patternManager = new PatternManager(this);
        this.taskManager = new DiscoArmorTaskManager(this);
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
        if (minorVersion < 18) {
            Logger logger = getLogger();
            logger.warning("This plugin was made for 1.18+");
            logger.warning("You should not be using it on " + VersionUtility.getMinecraftVersion());
        }

        reloadConfiguration();

        LanguageManager languageManager = getLanguageManager();
        languageManager.onPluginEnable();

        registerPatterns();

        registerCommands();
        registerListeners();
        registerUpdateChecker();

        broadcastEnabledMessage();
        register_bStats();
    }

    @Override
    public void onDisable() {
        broadcastDisabledMessage();
    }

    @Override
    protected void reloadConfiguration() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");

        YamlConfiguration configurationFile = configurationManager.get("config.yml");
        DiscoArmorConfiguration configuration = getConfiguration();
        configuration.load(configurationFile);

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguages();
    }

    public @NotNull DiscoArmorConfiguration getConfiguration() {
        return this.configuration;
    }

    public @NotNull PatternManager getPatternManager() {
        return this.patternManager;
    }

    public @NotNull DiscoArmorTaskManager getTaskManager() {
        return this.taskManager;
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

    private void registerUpdateChecker() {
        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        SpigotUpdateManager updateManager = corePlugin.getSpigotUpdateManager();
        updateManager.addResource(this, 60700L);
    }

    private void register_bStats() {
        Metrics metrics = new Metrics(this, 16221);
        metrics.addCustomChart(new SimplePie("selected_language", this::getDefaultLanguageCode));
    }

    private String getDefaultLanguageCode() {
        LanguageManager languageManager = getLanguageManager();
        Language defaultLanguage = languageManager.getDefaultLanguage();
        return (defaultLanguage == null ? "none" : defaultLanguage.getLanguageName());
    }

    private void broadcastEnabledMessage() {
        LanguageManager languageManager = getLanguageManager();
        languageManager.broadcastMessage("broadcast.enabled", null);
    }

    private void broadcastDisabledMessage() {
        LanguageManager languageManager = getLanguageManager();
        languageManager.broadcastMessage("broadcast.disabled", null);
    }
}
