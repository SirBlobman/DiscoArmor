package com.github.sirblobman.disco.armor.pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextComponent.Builder;
import com.github.sirblobman.api.shaded.adventure.text.format.TextColor;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class YellowOrangePattern extends DiscoArmorPattern {
    private static final Color DEFAULT_COLOR;

    static {
        DEFAULT_COLOR = Color.fromRGB(0xFFFF00);
    }

    private final Map<UUID, Color> colorMap;

    public YellowOrangePattern(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "yellow_orange");
        this.colorMap = new HashMap<>();
    }

    @Override
    public @NotNull Component getDisplayName() {
        TextColor yellow = TextColor.color(0xFFFF00);
        TextColor orange = TextColor.color(0xFFA500);

        Builder builder = Component.text();
        builder.append(Component.text("Yellow", yellow));
        builder.append(Component.space());
        builder.append(Component.text("Orange", orange));
        return builder.build();
    }

    @Override
    protected @NotNull Color getNextColor(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        Color currentColor = this.colorMap.getOrDefault(playerId, DEFAULT_COLOR);

        int red = currentColor.getRed();
        int green = currentColor.getGreen();
        int blue = currentColor.getBlue();

        green += 5;
        if (green > 255) {
            green = 128;
        }

        Color newColor = Color.fromRGB(red, green, blue);
        this.colorMap.put(playerId, newColor);
        return newColor;
    }

    @Override
    protected @NotNull ItemStack getMenuItem() {
        ItemStack item = XMaterial.YELLOW_BANNER.parseItem();
        if (item == null) {
            return new ItemStack(Material.BARRIER);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof BannerMeta bannerMeta)) {
            return new ItemStack(Material.BARRIER);
        }

        Pattern orangeGradient = new Pattern(DyeColor.ORANGE, PatternType.GRADIENT);
        bannerMeta.addPattern(orangeGradient);

        item.setItemMeta(bannerMeta);
        return item;
    }
}
