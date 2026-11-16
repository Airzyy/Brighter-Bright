package com.airzyy.brighterbright.mixin;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
    @Redirect(
            method = "updateLightTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(FF)F",
                    ordinal = 2
            )
    )
    private float brighterbright$removeGammaClamp(float first, float second) {
        return second;
    }
}
