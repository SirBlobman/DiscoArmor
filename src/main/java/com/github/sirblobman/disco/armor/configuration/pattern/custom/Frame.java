package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

import com.github.sirblobman.api.configuration.IConfigurable;

public final class Frame implements IConfigurable {
    private static final Pattern RANDOM_COLOR_PATTERN;

    static {
        RANDOM_COLOR_PATTERN = Pattern.compile("random\\((\\d{1,3}),(\\d{1,3})\\)");
    }

    private ColorValues color;
    private ArmorTrim trim;
    private int duration;

    public Frame() {
        this.color = null;
        this.trim = null;
        this.duration = 5;
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        int duration = section.getInt("duration", 5);
        setDuration(duration);

        String colorString = section.getString("color");
        ColorValues colorValues = parseColorValues(colorString);
        setColor(colorValues);

        String trimString = section.getString("trim");
        ArmorTrim armorTrim = parseArmorTrim(trimString);
        setTrim(armorTrim);
    }

    private @Nullable ArmorTrim parseArmorTrim(@Nullable String trim) {
        if (trim == null || trim.isBlank()) {
            // Trim not set.
            return null;
        }

        String[] split = trim.split(Pattern.quote(";"));
        if (split.length != 2) {
            // Trim must be in the format 'material;pattern'.
            return null;
        }

        String materialKeyString = split[0];
        TrimMaterial material = parseRegistryValue(RegistryKey.TRIM_MATERIAL, materialKeyString);
        if (material == null) {
            // Trim material must be registered.
            return null;
        }

        String patternKeyString = split[1];
        TrimPattern pattern = parseRegistryValue(RegistryKey.TRIM_PATTERN, patternKeyString);
        if (pattern == null) {
            // Trim pattern must be registered.
            return null;
        }

        return new ArmorTrim(material, pattern);
    }

    private <T extends Keyed> @Nullable T parseRegistryValue(@NotNull RegistryKey<@NotNull T> registryType, @Nullable String key) {
        if (key == null || key.isBlank()) {
            return null;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull T> registry = registryAccess.getRegistry(registryType);
        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null) {
            return null;
        }

        return registry.get(namespacedKey);
    }

    private @Nullable ColorValues parseColorValues(@Nullable String string) {
        if (string == null || string.isBlank()) {
            // Color not set.
            return null;
        }

        String[] split = string.split(Pattern.quote(";"));
        if (split.length != 3) {
            // All colors must be in format 'r;g;b'
            return null;
        }

        String redValues = split[0];
        String greenValues = split[1];
        String blueValues = split[2];

        MinMax red = parseMinMax(redValues);
        MinMax green = parseMinMax(greenValues);
        MinMax blue = parseMinMax(blueValues);
        return new ColorValues(red, green, blue);
    }

    private @NotNull MinMax parseMinMax(String values) {
        Matcher matcher = RANDOM_COLOR_PATTERN.matcher(values);
        if (matcher.matches()) {
            String minString = matcher.group(1);
            String maxString = matcher.group(2);
            int min = Integer.parseInt(minString);
            int max = Integer.parseInt(maxString);

            if (min < 0 || min > 255 || max < 0 || max > 255) {
                return new MinMax(0);
            }

            return new MinMax(min, max);
        }

        try {
            int single = Integer.parseInt(values);
            return new MinMax(single);
        } catch (IllegalArgumentException ex) {
            return new MinMax(0, 0);
        }
    }

    public @Nullable ColorValues getColor() {
        return this.color;
    }

    public void setColor(@Nullable ColorValues color) {
        this.color = color;
    }

    public @Nullable ArmorTrim getTrim() {
        return this.trim;
    }

    public void setTrim(@Nullable ArmorTrim trim) {
        this.trim = trim;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
