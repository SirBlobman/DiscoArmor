package com.github.sirblobman.disco.armor.configuration;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.configuration.IConfigurable;

public final class DiscoArmorConfiguration implements IConfigurable {
    private long armorSpeed;
    private boolean disableOnDamage;
    private boolean preventFirstHit;
    private boolean glowing;

    public DiscoArmorConfiguration() {
        this.armorSpeed = 5;
        this.disableOnDamage = true;
        this.preventFirstHit = true;
        this.glowing = false;
    }

    @Override
    public void load(@NotNull ConfigurationSection config) {
        setArmorSpeed(config.getLong("armor-speed", 5L));
        setDisableOnDamage(config.getBoolean("disable-on-damage", true));
        setPreventFirstHit(config.getBoolean("prevent-first-hit", true));
        setGlowing(config.getBoolean("glowing", false));
    }

    public long getArmorSpeed() {
        return this.armorSpeed;
    }

    public void setArmorSpeed(long armorSpeed) {
        this.armorSpeed = armorSpeed;
    }

    public boolean isDisableOnDamage() {
        return this.disableOnDamage;
    }

    public void setDisableOnDamage(boolean disableOnDamage) {
        this.disableOnDamage = disableOnDamage;
    }

    public boolean isPreventFirstHit() {
        return this.preventFirstHit;
    }

    public void setPreventFirstHit(boolean preventFirstHit) {
        this.preventFirstHit = preventFirstHit;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }
}
