package com.github.sirblobman.disco.armor.configuration.pattern.custom;

public enum AnimationType {
    /**
     * Display the frames in the order they are configured.
     * Example: 1 2 3 4 5 6 7 8
     */
    LOOP,

    /**
     * Display the frames in the opposite of their configuration.
     * Example: 8 7 6 5 4 3 2 1
     */
    REVERSE,

    /**
     * A random frame be selected from all configured values.
     * Example: |7 6 4 3 5 2 7 8 | 5 3 2...
     */
    RANDOM,

    /**
     * Forwards first, then backwards, then forwards again
     * Example: 1 2 3 4 5 6 7 8 |  7 6 5 4 3 2 1 | 2 3 4 5 6 7 8
     */
    REFLECT,

    /**
     * Shuffle the list of possible frames once, and then repeat forever
     * Example: 7 3 4 2 1 5 6 8 | 7 3 4 2 1 5 6 8
     */
    SHUFFLE_LOOP
}
