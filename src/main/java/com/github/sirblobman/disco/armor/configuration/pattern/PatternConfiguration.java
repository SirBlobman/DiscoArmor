package com.github.sirblobman.disco.armor.configuration.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.CustomPattern;

public final class PatternConfiguration implements IConfigurable {
    private final String id;
    private MenuIconConfiguration menuIcon;
    private PatternType patternType;
    private BuiltInType builtInType;
    private List<CustomPattern> patternList;

    public PatternConfiguration(@NotNull String id) {
        this.id = id;
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        ConfigurationSection menuIconSection = getOrCreateSection(section, "menu-icon");
        MenuIconConfiguration menuIconConfiguration = new MenuIconConfiguration();
        menuIconConfiguration.load(menuIconSection);
        setMenuIcon(menuIconConfiguration);

        String patternTypeName = section.getString("type", "BUILD_IN");
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull MenuIconConfiguration getMenuIcon() {
        return this.menuIcon;
    }

    public void setMenuIcon(@NotNull MenuIconConfiguration menuIcon) {
        this.menuIcon = menuIcon;
    }

    public @NotNull PatternType getPatternType() {
        return this.patternType;
    }

    public void setPatternType(@NotNull PatternType patternType) {
        this.patternType = patternType;
    }

    public @Nullable BuiltInType getBuiltInType() {
        return this.builtInType;
    }

    public void setBuiltInType(@Nullable BuiltInType builtInType) {
        this.builtInType = builtInType;
    }

    public @Nullable List<CustomPattern> getCustomPatternList() {
        return Collections.unmodifiableList(this.patternList);
    }

    public void setCustomPatternList(@Nullable List<CustomPattern> customPatternList) {
        if (customPatternList == null) {
            this.patternList = null;
        } else {
            this.patternList = new ArrayList<>(customPatternList);
        }
    }
}
