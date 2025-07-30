package com.github.sirblobman.disco.armor.configuration.pattern;

import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;
import com.github.sirblobman.disco.armor.pattern.GrayscalePattern;
import com.github.sirblobman.disco.armor.pattern.OldGloryPattern;
import com.github.sirblobman.disco.armor.pattern.OneColorPattern;
import com.github.sirblobman.disco.armor.pattern.RainbowPattern;
import com.github.sirblobman.disco.armor.pattern.RandomPattern;
import com.github.sirblobman.disco.armor.pattern.SmoothPattern;
import com.github.sirblobman.disco.armor.pattern.YellowOrangePattern;

public enum BuiltInType {
    GRAYSCALE(GrayscalePattern.class),
    OLD_GLORY(OldGloryPattern.class),
    ONE_COLOR(OneColorPattern.class),
    RAINBOW(RainbowPattern.class),
    RANDOM(RandomPattern.class),
    SMOOTH(SmoothPattern.class),
    YELLOW_ORANGE(YellowOrangePattern.class);

    private final Class<? extends DiscoArmorPattern> patternClass;

    BuiltInType(Class<? extends DiscoArmorPattern> patternClass) {
        this.patternClass = patternClass;
    }

    public Class<? extends DiscoArmorPattern> getPatternClass() {
        return this.patternClass;
    }
}
