package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Utility;
import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.utility.ConfigurationHelper;

public abstract class CustomPattern implements IConfigurable {
    private final ArmorSlot slot;
    private CustomPatternType type;

    public CustomPattern(@NotNull ArmorSlot slot) {
        this.slot = slot;
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        String typeString = section.getString("type", "FRAMES");
        setType(ConfigurationHelper.parseEnum(CustomPatternType.class, typeString, CustomPatternType.FRAMES));
    }

    public @NotNull ArmorSlot getSlot() {
        return this.slot;
    }

    public @NotNull CustomPatternType getType() {
        return type;
    }

    public void setType(@NotNull CustomPatternType type) {
        this.type = type;
    }
}
