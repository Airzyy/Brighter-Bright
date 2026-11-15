package com.airzyy.brighterbright;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.resources.ResourceLocation;

/**
 * Extends Sodium's existing brightness slider without replacing its option,
 * value binding, formatter, or persistence behavior.
 */
public final class BrightnessConfig implements ConfigEntryPoint {
    private static final ResourceLocation SODIUM_GAMMA =
            ResourceLocation.fromNamespaceAndPath("sodium", "general.gamma");

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .setName("Brighter Bright")
                .setVersion("1.0.0")
                .registerOptionOverlay(
                        SODIUM_GAMMA,
                        builder.createIntegerOption(SODIUM_GAMMA)
                                .setRange(0, 200, 1)
                );
    }
}
