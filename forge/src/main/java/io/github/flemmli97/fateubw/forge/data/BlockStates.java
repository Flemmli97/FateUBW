package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.blocks.ChalkBlock;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Fate.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.chalkBase();

        this.chalk(ModBlocks.chalk.get());
        this.simpleBlock(ModBlocks.artifactOre.get(), this.models().singleTexture(ModBlocks.artifactOre.getID().getPath(), new ResourceLocation(Fate.MODID, "block/ore"), "ore", new ResourceLocation(Fate.MODID, "blocks/charm_ore")));
        this.simpleBlock(ModBlocks.gemOre.get(), this.models().singleTexture(ModBlocks.gemOre.getID().getPath(), new ResourceLocation(Fate.MODID, "block/ore"), "ore", new ResourceLocation(Fate.MODID, "blocks/crystal_ore")));
        this.simpleBlock(ModBlocks.deepSlateArtifactOre.get(), this.models().getBuilder(ModBlocks.deepSlateArtifactOre.getID().getPath())
                .parent(this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/ore")))
                .texture("ore", new ResourceLocation(Fate.MODID, "blocks/charm_ore"))
                .texture("stone", new ResourceLocation("block/deepslate")));
        this.simpleBlock(ModBlocks.deepSlateGemOre.get(), this.models().getBuilder(ModBlocks.deepSlateGemOre.getID().getPath())
                .parent(this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/ore")))
                .texture("ore", new ResourceLocation(Fate.MODID, "blocks/crystal_ore"))
                .texture("stone", new ResourceLocation("block/deepslate")));
        this.horizontalBlock(ModBlocks.altar.get(), this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/altar")));
    }

    @Override
    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "blocks" + "/" + name.getPath());
    }

    private void chalk(ChalkBlock block) {
        this.getVariantBuilder(block)
                .forAllStates(state -> {
                    String model = block.getRegistryName().getPath() + "_" + state.getValue(ChalkBlock.POSITION).getID();
                    return ConfiguredModel.builder()
                            .modelFile(this.models().singleTexture(model, new ResourceLocation(Fate.MODID, "block/chalk_base"), new ResourceLocation(block.getRegistryName().getNamespace(), "blocks/" + model)))
                            .rotationY((int) (state.getValue(ChalkBlock.FACING).toYRot() + 180) % 360)
                            .build();
                });
    }

    private ModelFile chalkBase() {
        return this.models().singleTexture("fateubw:block/chalk_base", this.mcLoc("block/thin_block"), "particle", new ResourceLocation(Fate.MODID, "blocks/chalk_line_none"))
                .element().from(0, 0, 0).to(16, 0.5f, 16)
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#texture").end()
                .face(Direction.DOWN).uvs(0, 16, 16, 0).cullface(Direction.DOWN).texture("#texture").end()
                .end();
    }
}
