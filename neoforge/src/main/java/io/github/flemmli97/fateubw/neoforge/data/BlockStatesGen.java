package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider {

    public BlockStatesGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Fate.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(FateBlocks.CHALK.get(), this.models().singleTexture("fateubw:block/" + FateBlocks.CHALK.getID().getPath(), this.mcLoc("block/thin_block"), "texture", Fate.modRes("block/" + FateBlocks.CHALK.getID().getPath()))
                .texture("particle", Fate.modRes("block/" + FateBlocks.CHALK.getID().getPath())).element().from(0, 0, 0).to(16, 0.5f, 16)
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#texture").end()
                .face(Direction.DOWN).uvs(0, 16, 16, 0).cullface(Direction.DOWN).texture("#texture").end()
                .end());
        this.simpleBlock(FateBlocks.ARTIFACT_ORE.get(), this.models().singleTexture(FateBlocks.ARTIFACT_ORE.getID().getPath(), Fate.modRes("block/ore"), "ore", Fate.modRes("block/artifact_ore")));
        this.simpleBlock(FateBlocks.GEM_ORE.get(), this.models().singleTexture(FateBlocks.GEM_ORE.getID().getPath(), Fate.modRes("block/ore"), "ore", Fate.modRes("block/crystal_ore")));
        this.simpleBlock(FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), this.models().getBuilder(FateBlocks.DEEP_SLATE_ARTIFACT_ORE.getID().getPath())
                .parent(this.models().getExistingFile(Fate.modRes("block/ore")))
                .texture("ore", Fate.modRes("block/artifact_ore"))
                .texture("stone", ResourceLocation.withDefaultNamespace("block/deepslate")));
        this.simpleBlock(FateBlocks.DEEP_SLATE_GEM_ORE.get(), this.models().getBuilder(FateBlocks.DEEP_SLATE_GEM_ORE.getID().getPath())
                .parent(this.models().getExistingFile(Fate.modRes("block/ore")))
                .texture("ore", Fate.modRes("block/crystal_ore"))
                .texture("stone", ResourceLocation.withDefaultNamespace("block/deepslate")));
        this.horizontalBlock(FateBlocks.ALTAR.get(), this.models().getExistingFile(Fate.modRes("block/summoning_altar")));
    }
}
