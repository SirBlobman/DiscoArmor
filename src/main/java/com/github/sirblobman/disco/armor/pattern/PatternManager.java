package com.github.sirblobman.disco.armor.pattern;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.pattern.PatternConfiguration;

import org.slf4j.LoggerFactory;

public final class PatternManager {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PatternManager.class);
    private final DiscoArmorPlugin plugin;
    private final Map<String, PatternConfiguration> patternMap;

    public PatternManager(@NotNull DiscoArmorPlugin plugin) {
        this.plugin = plugin;
        this.patternMap = new LinkedHashMap<>();
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull Logger getLogger() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    public void clearPatterns() {
        this.patternMap.clear();
    }

    public void loadConfigurationFiles() {
        DiscoArmorPlugin plugin = getPlugin();
        Logger logger = getLogger();

        File dataFolder = plugin.getDataFolder();
        File patternFolder = new File(dataFolder, "patterns");
        if (!patternFolder.exists()) {
            logger.warning("The folder at path '" + patternFolder + "' does not exist.");
            return;
        }

        FilenameFilter filenameFilter = (folder, fileName) -> fileName.endsWith(".yml");
        File[] configurationFileArray = patternFolder.listFiles(filenameFilter);
        if (configurationFileArray == null) {
            logger.warning("No patterns found in folder '" + patternFolder + "'.");
            return;
        }

        for (File configurationFile : configurationFileArray) {
            loadConfigurationFile(configurationFile);
        }

        int loadedPatternCount = this.patternMap.size();
        logger.info("Loaded " + loadedPatternCount + " pattern(s).");
    }

    private void loadConfigurationFile(@NotNull File file) {
        DiscoArmorPlugin plugin = getPlugin();
        Logger logger = getLogger();

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        String patternId = yamlConfiguration.getString("id");
        if (patternId == null || patternId.isBlank()) {
            logger.warning("Found file '" + file + "' in patterns folder but file doesn't have YML field 'id'");
            return;
        }

        if (doesPatternExist(patternId)) {
            logger.warning("Found duplicate pattern in '" + file + "' with id '" + patternId + "'");
            return;
        }

        logger.info("Loading pattern with id " + patternId + "' from file '" + file + "'.");
        PatternConfiguration patternConfiguration = new PatternConfiguration(plugin, patternId);
        patternConfiguration.load(yamlConfiguration);
        registerPattern(patternConfiguration);
    }

    public void registerPattern(@NotNull PatternConfiguration configuration) {
        String id = configuration.getId();
        PatternConfiguration existing = this.patternMap.get(id);
        if (existing != null) {
            throw new IllegalArgumentException("A custom pattern with id '" + id + "' is already registered.");
        }

        this.patternMap.put(id, configuration);
    }

    public boolean doesPatternExist(@NotNull String id) {
        return this.patternMap.containsKey(id);
    }

    public void unregister(@NotNull String id) {
        this.patternMap.remove(id);
    }

    public @NotNull List<String> getPatternIds() {
        return List.copyOf(this.patternMap.keySet());
    }

    public @NotNull List<PatternConfiguration> getPatterns() {
        return List.copyOf(this.patternMap.values());
    }

    public @Nullable PatternConfiguration getPattern(@NotNull String id) {
        return this.patternMap.get(id);
    }
}
