package com.github.sirblobman.disco.armor.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class RainbowPattern extends DiscoArmorPattern {
    private static final List<Color> RAINBOW_COLOR_LIST;

    static {
        RAINBOW_COLOR_LIST = new ArrayList<>();
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0xFF0000)); // Red
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0xFFA500)); // Orange
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0xFFFF00)); // Yellow
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0x008000)); // Green
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0x0000FF)); // Blue
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0x4B0082)); // Indigo
        RAINBOW_COLOR_LIST.add(Color.fromRGB(0xEE82EE)); // Violet
    }

    public RainbowPattern(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "rainbow");
    }

    @Override
    protected @NotNull Color getNextColor(@NotNull Player player) {
        int colorListSize = RAINBOW_COLOR_LIST.size();
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int randomIndex = rng.nextInt(colorListSize);
        return RAINBOW_COLOR_LIST.get(randomIndex);
    }

    @Override
    protected @NotNull ItemStack getMenuItem() {
        Set<Material> bannerSet = Tag.ITEMS_BANNERS.getValues();
        List<Material> bannerList = new ArrayList<>(bannerSet);
        int bannerListSize = bannerList.size();

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Material bannerType = bannerList.get(rng.nextInt(bannerListSize));
        return new ItemStack(bannerType, 1);
    }
}
