package com.github.sirblobman.disco.armor.configuration.pattern.custom;

public record MinMax(int min, int max) {
    public MinMax(int single) {
        this(single, single);
    }
}
