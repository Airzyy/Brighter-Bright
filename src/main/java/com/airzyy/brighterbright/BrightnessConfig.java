package com.airzyy.brighterbright;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

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
        save();
    }

    private static void applyBrightness() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options == null) return;

        try {
            OptionInstance<Double> gamma = minecraft.options.gamma();
            Field valueField = OptionInstance.class.getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(gamma, brightness / 100.0);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not apply brightness above 100%", exception);
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
