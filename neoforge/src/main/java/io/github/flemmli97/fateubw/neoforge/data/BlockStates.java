package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Fate.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ModBlocks.CHALK.get(), this.models().singleTexture("fateubw:block/" + ModBlocks.CHALK.getID().getPath(), this.mcLoc("block/thin_block"), "texture", new ResourceLocation(Fate.MODID, "blocks/" + ModBlocks.CHALK.getID().getPath()))
                .texture("particle", new ResourceLocation(Fate.MODID, "blocks/" + ModBlocks.CHALK.getID().getPath())).element().from(0, 0, 0).to(16, 0.5f, 16)
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#texture").end()
                .face(Direction.DOWN).uvs(0, 16, 16, 0).cullface(Direction.DOWN).texture("#texture").end()
                .end());
        this.simpleBlock(ModBlocks.ARTIFACT_ORE.get(), this.models().singleTexture(ModBlocks.ARTIFACT_ORE.getID().getPath(), new ResourceLocation(Fate.MODID, "block/ore"), "ore", new ResourceLocation(Fate.MODID, "blocks/charm_ore")));
        this.simpleBlock(ModBlocks.GEM_ORE.get(), this.models().singleTexture(ModBlocks.GEM_ORE.getID().getPath(), new ResourceLocation(Fate.MODID, "block/ore"), "ore", new ResourceLocation(Fate.MODID, "blocks/crystal_ore")));
        this.simpleBlock(ModBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), this.models().getBuilder(ModBlocks.DEEP_SLATE_ARTIFACT_ORE.getID().getPath())
                .parent(this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/ore")))
                .texture("ore", new ResourceLocation(Fate.MODID, "blocks/charm_ore"))
                .texture("stone", new ResourceLocation("block/deepslate")));
        this.simpleBlock(ModBlocks.DEEP_SLATE_GEM_ORE.get(), this.models().getBuilder(ModBlocks.DEEP_SLATE_GEM_ORE.getID().getPath())
                .parent(this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/ore")))
                .texture("ore", new ResourceLocation(Fate.MODID, "blocks/crystal_ore"))
                .texture("stone", new ResourceLocation("block/deepslate")));
        this.horizontalBlock(ModBlocks.ALTAR.get(), this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/summoning_altar")));
    }

    @Override
    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "blocks" + "/" + name.getPath());
    }
}
