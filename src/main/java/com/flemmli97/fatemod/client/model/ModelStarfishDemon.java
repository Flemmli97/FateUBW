package com.flemmli97.fatemod.client.model;

import com.flemmli97.fatemod.common.entity.EntityLesserMonster;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Starfish Demons - Black_Saturn
 * Created using Tabula 7.0.0
 */
public class ModelStarfishDemon extends ModelBase implements IResetModel{
	
    public ModelRendererPlus MouthBottom;
    public ModelRendererPlus MouthSide1;
    public ModelRendererPlus MouthSide2;
    public ModelRendererPlus MouthSide3;
    public ModelRendererPlus MouthSide4;
    public ModelRendererPlus MouthSide5;
    public ModelRendererPlus MouthSide6;
    public ModelRendererPlus TentacleP1S1;
    public ModelRendererPlus Tentaclepapulae;
    public ModelRendererPlus Tentaclepapulae_1;
    public ModelRendererPlus TentacleP2S1;
    public ModelRendererPlus Tentaclepapulae_2;
    public ModelRendererPlus Tentaclepapulae_3;
    public ModelRendererPlus TentacleP3S1;
    public ModelRendererPlus Tentaclepapulae_4;
    public ModelRendererPlus Tentaclepapulae_5;
    public ModelRendererPlus TentacleP1S2;
    public ModelRendererPlus Tentaclepapulae_6;
    public ModelRendererPlus Tentaclepapulae_7;
    public ModelRendererPlus TentacleP2S2;
    public ModelRendererPlus Tentaclepapulae_8;
    public ModelRendererPlus Tentaclepapulae_9;
    public ModelRendererPlus TentacleP3S2;
    public ModelRendererPlus Tentaclepapulae_10;
    public ModelRendererPlus Tentaclepapulae_11;
    public ModelRendererPlus TentacleP1S3;
    public ModelRendererPlus Tentaclepapulae_12;
    public ModelRendererPlus Tentaclepapulae_13;
    public ModelRendererPlus TentacleP2S3;
    public ModelRendererPlus Tentaclepapulae_14;
    public ModelRendererPlus Tentaclepapulae_15;
    public ModelRendererPlus TentacleP3S3;
    public ModelRendererPlus Tentaclepapulae_16;
    public ModelRendererPlus Tentaclepapulae_17;
    public ModelRendererPlus TentacleP1S4;
    public ModelRendererPlus Tentaclepapulae_18;
    public ModelRendererPlus Tentaclepapulae_19;
    public ModelRendererPlus TentacleP2S4;
    public ModelRendererPlus Tentaclepapulae_20;
    public ModelRendererPlus Tentaclepapulae_21;
    public ModelRendererPlus TentacleP3S4;
    public ModelRendererPlus Tentaclepapulae_22;
    public ModelRendererPlus Tentaclepapulae_23;
    public ModelRendererPlus TentacleP1S5;
    public ModelRendererPlus Tentaclepapulae_24;
    public ModelRendererPlus Tentaclepapulae_25;
    public ModelRendererPlus TentacleP2S5;
    public ModelRendererPlus Tentaclepapulae_26;
    public ModelRendererPlus Tentaclepapulae_27;
    public ModelRendererPlus TentacleP3S5;
    public ModelRendererPlus Tentaclepapulae_28;
    public ModelRendererPlus Tentaclepapulae_29;
    public ModelRendererPlus TentacleP1S6;
    public ModelRendererPlus Tentaclepapulae_30;
    public ModelRendererPlus Tentaclepapulae_31;
    public ModelRendererPlus TentacleP2S6;
    public ModelRendererPlus Tentaclepapulae_32;
    public ModelRendererPlus Tentaclepapulae_33;
    public ModelRendererPlus TentacleP3S6;
    public ModelRendererPlus Tentaclepapulae_34;
    public ModelRendererPlus Tentaclepapulae_35;

    //20
    public Animation idle;
    //31
    public Animation walk;
    //length 20, attack at 15
    public Animation attack;

    public ModelStarfishDemon() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.MouthSide4 = new ModelRendererPlus(this, 0, 0);
        this.MouthSide4.setDefaultValues(0, 0, 3.0F, 0, 3.141592653589793F, 0);
        this.MouthSide4.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0);
        this.TentacleP1S3 = new ModelRendererPlus(this, 16, 10);
        this.TentacleP1S3.setDefaultValues(0, 0, -0.1F, -0.17453292519943295F, 0, 0);
        this.TentacleP1S3.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0);
        this.Tentaclepapulae_22 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_22.setDefaultRotPoint(0, 0, -1.7F);
        this.Tentaclepapulae_22.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.TentacleP3S4 = new ModelRendererPlus(this, 0, 17);
        this.TentacleP3S4.setDefaultValues(0, 0, -4.5F, -0.5235987755982988F, 0, 0);
        this.TentacleP3S4.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0);
        this.Tentaclepapulae_16 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_16.setDefaultRotPoint(0, 0, -1.7F);
        this.Tentaclepapulae_16.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_6 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_6.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_6.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_11 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_11.setDefaultRotPoint(0, 0, -3.3F);
        this.Tentaclepapulae_11.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.TentacleP2S5 = new ModelRendererPlus(this, 0, 10);
        this.TentacleP2S5.setDefaultValues(0, 0, -4.6F, -0.3490658503988659F, 0, 0);
        this.TentacleP2S5.addBox(-1.5F, -1.0F, -5.0F, 3, 2, 5, 0);
        this.Tentaclepapulae_18 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_18.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_18.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_21 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_21.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_21.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_1 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_1.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_1.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP3S2 = new ModelRendererPlus(this, 0, 17);
        this.TentacleP3S2.setDefaultValues(0, 0, -4.5F, -0.5235987755982988F, 0, 0);
        this.TentacleP3S2.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0);
        this.Tentaclepapulae_26 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_26.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_26.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_12 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_12.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_12.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP3S5 = new ModelRendererPlus(this, 0, 17);
        this.TentacleP3S5.setDefaultValues(0, 0, -4.5F, -0.5235987755982988F, 0, 0);
        this.TentacleP3S5.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0);
        this.Tentaclepapulae_31 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_31.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_31.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP2S1 = new ModelRendererPlus(this, 0, 10);
        this.TentacleP2S1.setDefaultValues(0, 0, -4.6F, 0.8726646259971648F, 0, 0);
        this.TentacleP2S1.addBox(-1.5F, -1.0F, -5.0F, 3, 2, 5, 0);
        this.Tentaclepapulae_33 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_33.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_33.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_29 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_29.setDefaultRotPoint(0, 0, -3.3F);
        this.Tentaclepapulae_29.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_34 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_34.setDefaultRotPoint(0, 0, -1.7F);
        this.Tentaclepapulae_34.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_19 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_19.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_19.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP1S5 = new ModelRendererPlus(this, 16, 10);
        this.TentacleP1S5.setDefaultValues(0, 0, -0.1F, -0.17453292519943295F, 0, 0);
        this.TentacleP1S5.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0);
        this.MouthSide6 = new ModelRendererPlus(this, 0, 0);
        this.MouthSide6.setDefaultValues(2.6F, 0, -1.5F, 0, 5.235987755982989F, 0);
        this.MouthSide6.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0);
        this.Tentaclepapulae_2 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_2.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_2.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP1S1 = new ModelRendererPlus(this, 16, 10);
        this.TentacleP1S1.setDefaultValues(0, 0, -0.1F, -0.5235987755982988F, 0, 0);
        this.TentacleP1S1.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0);
        this.Tentaclepapulae_3 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_3.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_3.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP2S4 = new ModelRendererPlus(this, 0, 10);
        this.TentacleP2S4.setDefaultValues(0, 0, -4.6F, -0.3490658503988659F, 0, 0);
        this.TentacleP2S4.addBox(-1.5F, -1.0F, -5.0F, 3, 2, 5, 0);
        this.Tentaclepapulae_9 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_9.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_9.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP3S3 = new ModelRendererPlus(this, 0, 17);
        this.TentacleP3S3.setDefaultValues(0, 0, -4.5F, -0.5235987755982988F, 0, 0);
        this.TentacleP3S3.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0);
        this.Tentaclepapulae_24 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_24.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_24.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP2S6 = new ModelRendererPlus(this, 0, 10);
        this.TentacleP2S6.setDefaultValues(0, 0, -4.6F, 0.8726646259971648F, 0, 0);
        this.TentacleP2S6.addBox(-1.5F, -1.0F, -5.0F, 3, 2, 5, 0);
        this.TentacleP1S2 = new ModelRendererPlus(this, 16, 10);
        this.TentacleP1S2.setDefaultValues(0, 0, -0.1F, -0.17453292519943295F, 0, 0);
        this.TentacleP1S2.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0);
        this.MouthSide3 = new ModelRendererPlus(this, 0, 0);
        this.MouthSide3.setDefaultValues(-2.6F, 0, 1.5F, 0, 2.0943951023931953F, 0);
        this.MouthSide3.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0);
        this.Tentaclepapulae_13 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_13.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_13.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP2S2 = new ModelRendererPlus(this, 0, 10);
        this.TentacleP2S2.setDefaultValues(0, 0, -4.6F, -0.3490658503988659F, 0, 0);
        this.TentacleP2S2.addBox(-1.5F, -1.0F, -5.0F, 3, 2, 5, 0);
        this.Tentaclepapulae_28 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_28.setDefaultRotPoint(0, 0, -1.7F);
        this.Tentaclepapulae_28.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_5 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_5.setDefaultRotPoint(0, 0, -3.3F);
        this.Tentaclepapulae_5.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_32 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_32.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_32.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.MouthSide5 = new ModelRendererPlus(this, 0, 0);
        this.MouthSide5.setDefaultValues(2.6F, 0, 1.5F, 0, 4.1887902047863905F, 0);
        this.MouthSide5.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0);
        this.MouthSide1 = new ModelRendererPlus(this, 0, 0);
        this.MouthSide1.setDefaultRotPoint(0, 0, -3.0F);
        this.MouthSide1.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0);
        this.Tentaclepapulae_7 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_7.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_7.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_27 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_27.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_27.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_14 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_14.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_14.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP3S1 = new ModelRendererPlus(this, 0, 17);
        this.TentacleP3S1.setDefaultValues(0, 0, -4.5F, -1.5707963267948966F, 0, 0);
        this.TentacleP3S1.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0);
        this.Tentaclepapulae_8 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_8.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_8.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_4 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_4.setDefaultRotPoint(0, 0, -1.7F);
        this.Tentaclepapulae_4.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.TentacleP2S3 = new ModelRendererPlus(this, 0, 10);
        this.TentacleP2S3.setDefaultValues(0, 0, -4.6F, -0.3490658503988659F, 0, 0);
        this.TentacleP2S3.addBox(-1.5F, -1.0F, -5.0F, 3, 2, 5, 0);
        this.Tentaclepapulae_35 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_35.setDefaultRotPoint(0, 0, -3.3F);
        this.Tentaclepapulae_35.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_25 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_25.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_25.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.MouthBottom = new ModelRendererPlus(this, 0, 3);
        this.MouthBottom.setDefaultValues(0, 13.0F, 0, 1.5707963267948966F, 0, 0.5235987755982988F);
        this.MouthBottom.addBox(-3.0F, 0, -3.0F, 6, 1, 6, 0);
        this.Tentaclepapulae_23 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_23.setDefaultRotPoint(0, 0, -3.3F);
        this.Tentaclepapulae_23.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.TentacleP1S4 = new ModelRendererPlus(this, 16, 10);
        this.TentacleP1S4.setDefaultValues(0, 0, -0.1F, -0.17453292519943295F, 0, 0);
        this.TentacleP1S4.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0);
        this.Tentaclepapulae_15 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_15.setDefaultRotPoint(0, 0, -3.5F);
        this.Tentaclepapulae_15.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.TentacleP1S6 = new ModelRendererPlus(this, 16, 10);
        this.TentacleP1S6.setDefaultValues(0, 0, -0.1F, -0.5235987755982988F, 0, 0);
        this.TentacleP1S6.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0);
        this.Tentaclepapulae_30 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_30.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_30.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.Tentaclepapulae_10 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_10.setDefaultRotPoint(0, 0, -1.7F);
        this.Tentaclepapulae_10.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.Tentaclepapulae_20 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_20.setDefaultRotPoint(0, 0, -1.5F);
        this.Tentaclepapulae_20.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0);
        this.MouthSide2 = new ModelRendererPlus(this, 0, 0);
        this.MouthSide2.setDefaultValues(-2.6F, 0, -1.5F, 0, 1.0471975511965976F, 0);
        this.MouthSide2.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0);
        this.Tentaclepapulae_17 = new ModelRendererPlus(this, 0, 10);
        this.Tentaclepapulae_17.setDefaultRotPoint(0, 0, -3.3F);
        this.Tentaclepapulae_17.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0);
        this.TentacleP3S6 = new ModelRendererPlus(this, 0, 17);
        this.TentacleP3S6.setDefaultValues(0, 0, -4.5F, -1.5707963267948966F, 0, 0);
        this.TentacleP3S6.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0);
        this.MouthBottom.addChild(this.MouthSide4);
        this.MouthSide3.addChild(this.TentacleP1S3);
        this.TentacleP3S4.addChild(this.Tentaclepapulae_22);
        this.TentacleP2S4.addChild(this.TentacleP3S4);
        this.TentacleP3S3.addChild(this.Tentaclepapulae_16);
        this.TentacleP1S2.addChild(this.Tentaclepapulae_6);
        this.TentacleP3S2.addChild(this.Tentaclepapulae_11);
        this.TentacleP1S5.addChild(this.TentacleP2S5);
        this.TentacleP1S4.addChild(this.Tentaclepapulae_18);
        this.TentacleP2S4.addChild(this.Tentaclepapulae_21);
        this.TentacleP1S1.addChild(this.Tentaclepapulae_1);
        this.TentacleP2S2.addChild(this.TentacleP3S2);
        this.TentacleP2S5.addChild(this.Tentaclepapulae_26);
        this.TentacleP1S3.addChild(this.Tentaclepapulae_12);
        this.TentacleP2S5.addChild(this.TentacleP3S5);
        this.TentacleP1S6.addChild(this.Tentaclepapulae_31);
        this.TentacleP1S1.addChild(this.TentacleP2S1);
        this.TentacleP2S6.addChild(this.Tentaclepapulae_33);
        this.TentacleP3S5.addChild(this.Tentaclepapulae_29);
        this.TentacleP3S6.addChild(this.Tentaclepapulae_34);
        this.TentacleP1S4.addChild(this.Tentaclepapulae_19);
        this.MouthSide5.addChild(this.TentacleP1S5);
        this.MouthBottom.addChild(this.MouthSide6);
        this.TentacleP2S1.addChild(this.Tentaclepapulae_2);
        this.MouthSide1.addChild(this.TentacleP1S1);
        this.TentacleP2S1.addChild(this.Tentaclepapulae_3);
        this.TentacleP1S4.addChild(this.TentacleP2S4);
        this.TentacleP2S2.addChild(this.Tentaclepapulae_9);
        this.TentacleP2S3.addChild(this.TentacleP3S3);
        this.TentacleP1S5.addChild(this.Tentaclepapulae_24);
        this.TentacleP1S6.addChild(this.TentacleP2S6);
        this.MouthSide2.addChild(this.TentacleP1S2);
        this.MouthBottom.addChild(this.MouthSide3);
        this.TentacleP1S3.addChild(this.Tentaclepapulae_13);
        this.TentacleP1S2.addChild(this.TentacleP2S2);
        this.TentacleP3S5.addChild(this.Tentaclepapulae_28);
        this.TentacleP3S1.addChild(this.Tentaclepapulae_5);
        this.TentacleP2S6.addChild(this.Tentaclepapulae_32);
        this.MouthBottom.addChild(this.MouthSide5);
        this.MouthBottom.addChild(this.MouthSide1);
        this.TentacleP1S2.addChild(this.Tentaclepapulae_7);
        this.TentacleP2S5.addChild(this.Tentaclepapulae_27);
        this.TentacleP2S3.addChild(this.Tentaclepapulae_14);
        this.TentacleP2S1.addChild(this.TentacleP3S1);
        this.TentacleP2S2.addChild(this.Tentaclepapulae_8);
        this.TentacleP3S1.addChild(this.Tentaclepapulae_4);
        this.TentacleP1S3.addChild(this.TentacleP2S3);
        this.TentacleP3S6.addChild(this.Tentaclepapulae_35);
        this.TentacleP1S5.addChild(this.Tentaclepapulae_25);
        this.TentacleP3S4.addChild(this.Tentaclepapulae_23);
        this.MouthSide4.addChild(this.TentacleP1S4);
        this.TentacleP2S3.addChild(this.Tentaclepapulae_15);
        this.TentacleP1S1.addChild(this.Tentaclepapulae);
        this.MouthSide6.addChild(this.TentacleP1S6);
        this.TentacleP1S6.addChild(this.Tentaclepapulae_30);
        this.TentacleP3S2.addChild(this.Tentaclepapulae_10);
        this.TentacleP2S4.addChild(this.Tentaclepapulae_20);
        this.MouthBottom.addChild(this.MouthSide2);
        this.TentacleP3S3.addChild(this.Tentaclepapulae_17);
        this.TentacleP2S6.addChild(this.TentacleP3S6);
        
        this.idle = new Animation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/starfish_standing_idle.json"));
        this.walk = new Animation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/starfish_standing_walk.json"));
        this.attack = new Animation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/starfish_standing_attack.json"));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.MouthBottom.render(f5);
    }

    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
    	this.resetModel();
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		AnimatedAction anim = ((EntityLesserMonster)entity).getAnimation();
    	if(anim!=null)
    	{
    		if(anim.getID().equals("attack"))
    			this.attack.animate(anim.getTick(), partialTicks);
    		if(anim.getID().equals("walk"))
        		this.walk.animate(anim.getTick(), partialTicks);
    	}
    	else
    		this.idle.animate(((int)ageInTicks), partialTicks);
    }

	@Override
	public void resetModel() {
		this.MouthBottom.reset();
		this.resetChild(this.MouthBottom);
	}
}
