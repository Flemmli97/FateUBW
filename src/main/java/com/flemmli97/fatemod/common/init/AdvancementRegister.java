package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.common.advancements.GrailWarTrigger;

import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementRegister {

	public static final GrailWarTrigger grailWarTrigger = new GrailWarTrigger();
	
	public static final void init()
	{
		CriteriaTriggers.register(grailWarTrigger);
	}
}
