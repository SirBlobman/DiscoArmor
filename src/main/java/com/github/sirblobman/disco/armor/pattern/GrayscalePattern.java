package com.github.sirblobman.disco.armor.pattern;

import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.format.NamedTextColor;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class GrayscalePattern extends DiscoArmorPattern {
    public GrayscalePattern(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "grayscale");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Grayscale", NamedTextColor.GRAY);
    }

    @Override
    protected @NotNull Color getNextColor(@NotNull Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int red = rng.nextInt(256);
        int green = rng.nextInt(256);
        int blue = rng.nextInt(256);

        int colorSum = (red + green + blue);
        int colorAverage = (colorSum / 3);
        return Color.fromRGB(colorAverage, colorAverage, colorAverage);
    }

    @Override
    protected @NotNull ItemStack getMenuItem() {
        ItemStack item = XMaterial.GRAY_BANNER.parseItem();
        return (item == null ? new ItemStack(Material.BARRIER) : item);
    }
}
