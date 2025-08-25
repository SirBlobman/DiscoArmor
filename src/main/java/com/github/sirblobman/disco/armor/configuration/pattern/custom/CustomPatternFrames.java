package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.random.RandomGenerator;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.utility.ConfigurationHelper;

public final class CustomPatternFrames extends CustomPattern {
    private AnimationType animationType;
    private final List<Frame> frameList;

    public CustomPatternFrames(@NotNull ArmorSlot slot) {
        super(slot);
        this.animationType = AnimationType.LOOP;
        this.frameList = new ArrayList<>();
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        super.load(section);

        String typeString = section.getString("animation-type", "LOOP");
        setAnimationType(ConfigurationHelper.parseEnum(AnimationType.class, typeString, AnimationType.LOOP));

        ConfigurationSection framesSection = getOrCreateSection(section, "frames");
        Set<String> frameIdSet = framesSection.getKeys(false);

        List<Frame> frameList = new ArrayList<>();
        for (String frameId : frameIdSet) {
            Frame frame = new Frame();
            frame.load(getOrCreateSection(section, frameId));
            frameList.add(frame);
        }

        setFrameList(frameList);
    }

    public @NotNull AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(@NotNull AnimationType animationType) {
        this.animationType = animationType;
    }

    public @NotNull List<Frame> getFrameList() {
        return Collections.unmodifiableList(this.frameList);
    }

    public void setFrameList(List<Frame> frameList) {
        this.frameList.clear();
        this.frameList.addAll(frameList);
    }

    public void shuffleFrames(@NotNull RandomGenerator random) {
        Collections.shuffle(this.frameList, random);
    }

    public @NotNull Frame getRandomFrame(@NotNull RandomGenerator random) {
        List<Frame> frameList = getFrameList();
        int frameListSize = frameList.size();
        int randomValue = random.nextInt(frameListSize);
        return frameList.get(randomValue);
    }
}
