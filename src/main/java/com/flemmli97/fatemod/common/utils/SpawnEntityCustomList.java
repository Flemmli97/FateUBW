package com.flemmli97.fatemod.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.util.ResourceLocation;

public class SpawnEntityCustomList
{
	public static HashMap<ResourceLocation, EntityEggInfo> entityEggs = new LinkedHashMap<ResourceLocation, EntityEggInfo>();
    
    private static void addMapping(Class<? extends Entity> entity, int eggfirstcolour, int eggsecondcolour)
    {
        entityEggs.put(EntityList.getKey(entity), new EntityList.EntityEggInfo(EntityList.getKey(entity), eggfirstcolour, eggsecondcolour));
    }

    static
    {
        addMapping(EntityArthur.class, 0x048dd0, 0xecee37);
        //saber class
        //""
        //""
        addMapping(EntityGilgamesh.class, 0xfff400, 0xffdb00);
        addMapping(EntityEmiya.class, 0x9f0707, 0x000000);
        //archer class
        //""
        addMapping(EntityDiarmuid.class, 0x000000, 0x2a079a);
        addMapping(EntityCuchulainn.class, 0x0038ff, 0xb6c0c1);
        //lancer class
        //""
        addMapping(EntityIskander.class, 0xd40000, 0x8d0101);
        addMapping(EntityMedusa.class, 0x000000, 0xf234ea);
        //rider class
        //""
        addMapping(EntityGilles.class, 0x100460, 0x600453);
        addMapping(EntityMedea.class, 0x6f086b, 0x4a8be5);
        //caster class
        //""
        addMapping(EntityHassan.class, 0x000, 0x3a393a);
        addMapping(EntitySasaki.class, 0x4e04c3, 0xa77cec);
        //assassin class
        //""       
        addMapping(EntityLancelot.class, 0x071a33, 0x1d4f94);
        addMapping(EntityHeracles.class, 0x3c1d06, 0x5e3c22);
        //berserker class
        //""
    }
}