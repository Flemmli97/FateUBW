package io.github.flemmli97.fateubw.fabric.client;

import io.github.flemmli97.fateubw.mixinhelper.SpriteList;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

import java.util.List;

public class ParticleSpriteWrap implements SpriteSet, SpriteList {

    private final FabricSpriteProvider wrapped;

    public ParticleSpriteWrap(FabricSpriteProvider wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public List<TextureAtlasSprite> fateUBW$getSprites() {
        return this.wrapped.getSprites();
    }

    @Override
    public TextureAtlasSprite get(int age, int lifetime) {
        return this.wrapped.get(age, lifetime);
    }

    @Override
    public TextureAtlasSprite get(RandomSource random) {
        return this.wrapped.get(random);
    }
}
