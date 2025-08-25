package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

public final class CustomPatternFixed extends CustomPattern {
    private Frame frame;

    public CustomPatternFixed(@NotNull ArmorSlot slot) {
        super(slot);
        this.frame = new Frame();
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        super.load(section);

        Frame frame = new Frame();
        frame.load(section);
        setFrame(frame);
    }

    public @NotNull Frame getFrame() {
        return this.frame;
    }

    public void setFrame(@NotNull Frame frame) {
        this.frame = frame;
    }
}
