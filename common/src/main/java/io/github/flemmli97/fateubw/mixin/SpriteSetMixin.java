package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.mixinhelper.SpriteList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(targets = "net/minecraft/client/particle/ParticleEngine$MutableSpriteSet")
public abstract class SpriteSetMixin implements SpriteList {

    @Shadow
    private List<TextureAtlasSprite> sprites;

    @Override
    public List<TextureAtlasSprite> fateUBW$getSprites() {
        return this.sprites;
    }
}
