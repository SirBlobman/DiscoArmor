package com.github.sirblobman.disco.armor.manager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

public final class PatternManager {
    private final DiscoArmorPlugin plugin;
    private final Map<String, DiscoArmorPattern> patternMap;
    public PatternManager(DiscoArmorPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.patternMap = new LinkedHashMap<>();
    }

    public void register(Class<? extends DiscoArmorPattern> patternClass) {
        Validate.notNull(patternClass, "patternClass must not be null!");
        try {
            Constructor<? extends DiscoArmorPattern> constructor = patternClass.getDeclaredConstructor(
                    DiscoArmorPlugin.class);
            DiscoArmorPattern pattern = constructor.newInstance(this.plugin);
            String patternId = pattern.getId();

            if(this.patternMap.containsKey(patternId)) {
                String message = ("An armor pattern with id '" + patternId + "' is already registered.");
                throw new IllegalArgumentException(message);
            }

            this.patternMap.put(patternId, pattern);
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering an armor pattern:", ex);
        }
    }

    public void unregister(DiscoArmorPattern pattern) {
        String patternId = pattern.getId();
        this.patternMap.remove(patternId);
    }

    public List<String> getPatternIds() {
        Set<String> keySet = this.patternMap.keySet();
        return new ArrayList<>(keySet);
    }

    public List<DiscoArmorPattern> getPatterns() {
        Collection<DiscoArmorPattern> patternCollection = this.patternMap.values();
        return new ArrayList<>(patternCollection);
    }

    public DiscoArmorPattern getPattern(String id) {
        if(id == null) {
            return null;
        }

        return this.patternMap.getOrDefault(id, null);
    }
}
