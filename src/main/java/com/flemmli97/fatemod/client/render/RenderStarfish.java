package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.client.model.ModelStarfishDemon;
import com.flemmli97.fatemod.common.entity.EntityLesserMonster;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderStarfish extends RenderLiving<EntityLesserMonster>{

	public static ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/starfish.png");
	private static ModelStarfishDemon model = new ModelStarfishDemon();
	//20
	public static Animation idle = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/starfish_standing_idle.json"));
	//31
	public static Animation walk = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/starfish_standing_walk.json"));
	//length 20, attack at 15
	public static Animation attack = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/starfish_standing_attack.json"));

	public RenderStarfish(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLesserMonster entity) {
		return tex;
	}

}
