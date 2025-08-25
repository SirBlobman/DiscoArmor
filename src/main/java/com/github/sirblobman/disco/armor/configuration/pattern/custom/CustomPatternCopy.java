package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.utility.ConfigurationHelper;

public final class CustomPatternCopy extends CustomPattern {
    private ArmorSlot copiedSlot;

    public CustomPatternCopy(@NotNull ArmorSlot slot) {
        super(slot);
        this.copiedSlot = ArmorSlot.HELMET;
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        super.load(section);

        String copyFromString = section.getString("copy-from", "HELMET");
        setCopiedSlot(ConfigurationHelper.parseEnum(ArmorSlot.class, copyFromString, ArmorSlot.HELMET));
    }

    public @NotNull ArmorSlot getCopiedSlot() {
        return this.copiedSlot;
    }

    public void setCopiedSlot(@NotNull ArmorSlot copiedSlot) {
        this.copiedSlot = copiedSlot;
    }
}
