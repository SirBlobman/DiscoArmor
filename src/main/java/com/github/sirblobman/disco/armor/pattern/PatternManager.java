package com.github.sirblobman.disco.armor.pattern;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class PatternManager {
    private final DiscoArmorPlugin plugin;
    private final Map<String, DiscoArmorPattern> patternMap;

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

    public void register(@NotNull Class<? extends DiscoArmorPattern> patternClass) {
        try {
            DiscoArmorPlugin plugin = getPlugin();
            Class<? extends DiscoArmorPlugin> pluginClass = plugin.getClass();
            Constructor<? extends DiscoArmorPattern> constructor = patternClass.getDeclaredConstructor(pluginClass);
            DiscoArmorPattern pattern = constructor.newInstance(plugin);

            String patternId = pattern.getId();
            if (this.patternMap.containsKey(patternId)) {
                String message = ("An armor pattern with id '" + patternId + "' is already registered.");
                throw new IllegalArgumentException(message);
            }

            this.patternMap.put(patternId, pattern);
        } catch (ReflectiveOperationException | IllegalArgumentException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to register an armor pattern:", ex);
        }
    }

    public void unregister(@NotNull DiscoArmorPattern pattern) {
        String patternId = pattern.getId();
        this.patternMap.remove(patternId);
    }

    public @NotNull List<String> getPatternIds() {
        Set<String> keySet = this.patternMap.keySet();
        return List.copyOf(keySet);
    }

    public @NotNull List<DiscoArmorPattern> getPatterns() {
        Collection<DiscoArmorPattern> patternCollection = this.patternMap.values();
        return List.copyOf(patternCollection);
    }

    public @Nullable DiscoArmorPattern getPattern(@NotNull String id) {
        return this.patternMap.get(id);
    }
}
