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

public final class RandomPattern extends DiscoArmorPattern {
    public RandomPattern(DiscoArmorPlugin plugin) {
        super(plugin, "random");
    }

    @Override
    protected @NotNull Color getNextColor(@NotNull Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int red = rng.nextInt(256);
        int green = rng.nextInt(256);
        int blue = rng.nextInt(256);
        return Color.fromRGB(red, green, blue);
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
