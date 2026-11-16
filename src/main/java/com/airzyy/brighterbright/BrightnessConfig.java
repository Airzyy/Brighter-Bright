package com.airzyy.brighterbright;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Extends Sodium's existing brightness slider and stores the extended value
 * separately so values above Minecraft's normal gamma range survive reloads.
 */
public final class BrightnessConfig implements ConfigEntryPoint {
    private static final ResourceLocation SODIUM_GAMMA =
            ResourceLocation.fromNamespaceAndPath("sodium", "general.gamma");
    private static final Path CONFIG_FILE =
            FMLPaths.CONFIGDIR.get().resolve("brighterbright.properties");

    private static int brightness = 100;

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        load();
        applyBrightness();

        builder.registerOwnModOptions()
                .setName("Brighter Bright")
                .setVersion("1.0.0")
                .registerOptionOverlay(
                        SODIUM_GAMMA,
                        builder.createIntegerOption(SODIUM_GAMMA)
                                .setRange(0, 200, 1)
                                .setBinding(BrightnessConfig::setBrightness, BrightnessConfig::getBrightness)
                                .setStorageHandler(BrightnessConfig::save)
                );
    }

    private static int getBrightness() {
        return brightness;
    }

    private static void setBrightness(int value) {
        brightness = Math.max(0, Math.min(200, value));
        applyBrightness();
    }

    private static void applyBrightness() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options != null) {
            minecraft.options.gamma().set(brightness / 100.0);
        }
    }

    private static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            brightness = 100;
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_FILE)) {
            properties.load(input);
            brightness = Math.max(0, Math.min(200,
                    Integer.parseInt(properties.getProperty("brightness", "100").trim())));
        } catch (IOException | NumberFormatException exception) {
            brightness = 100;
        }
    }

    private static void save() {
        Properties properties = new Properties();
        properties.setProperty("brightness", Integer.toString(brightness));

        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            try (OutputStream output = Files.newOutputStream(CONFIG_FILE)) {
                properties.store(output, "Brighter Bright settings");
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not save Brighter Bright settings", exception);
        }
    }
}
