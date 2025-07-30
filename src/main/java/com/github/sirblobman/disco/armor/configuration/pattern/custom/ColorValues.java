package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

public final class ColorValues {
    private final Random random;
    private int minRed;
    private int maxRed;
    private int minGreen;
    private int maxGreen;
    private int minBlue;
    private int maxBlue;

    public ColorValues() {
        this.random = new Random();
        this.minRed = 0;
        this.maxRed = 0;
        this.minGreen = 0;
        this.maxGreen = 0;
        this.minBlue = 0;
        this.maxBlue = 0;
    }

    public ColorValues(@NotNull MinMax red, @NotNull MinMax green, @NotNull MinMax blue) {
        this();
        setMinRed(red.min());
        setMaxRed(red.max());
        setMinGreen(green.min());
        setMaxGreen(green.max());
        setMinBlue(blue.min());
        setMaxBlue(blue.max());
    }

    public int getMinRed() {
        return minRed;
    }

    public void setMinRed(int minRed) {
        this.minRed = minRed;
    }

    public int getMaxRed() {
        return maxRed;
    }

    public void setMaxRed(int maxRed) {
        this.maxRed = maxRed;
    }

    public int getMinGreen() {
        return minGreen;
    }

    public void setMinGreen(int minGreen) {
        this.minGreen = minGreen;
    }

    public int getMaxGreen() {
        return maxGreen;
    }

    public void setMaxGreen(int maxGreen) {
        this.maxGreen = maxGreen;
    }

    public int getMinBlue() {
        return minBlue;
    }

    public void setMinBlue(int minBlue) {
        this.minBlue = minBlue;
    }

    public int getMaxBlue() {
        return maxBlue;
    }

    public void setMaxBlue(int maxBlue) {
        this.maxBlue = maxBlue;
    }

    private @NotNull Random getRandom() {
        return this.random;
    }

    private int getRandomInt(int min, int max) {
        if (min >= max) {
            return min;
        }

        Random random = getRandom();
        return random.nextInt(min, max + 1);
    }

    public int getRed() {
        int minRed = getMinRed();
        int maxRed = getMaxRed();
        return getRandomInt(minRed, maxRed);
    }

    public int getGreen() {
        int minGreen = getMinGreen();
        int maxGreen = getMaxGreen();
        return getRandomInt(minGreen, maxGreen);
    }

    public int getBlue() {
        int minBlue = getMinBlue();
        int maxBlue = getMaxBlue();
        return getRandomInt(minBlue, maxBlue);
    }
}
