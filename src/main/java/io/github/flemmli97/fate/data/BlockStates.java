package io.github.flemmli97.fate.data;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.blocks.BlockChalkLine;
import io.github.flemmli97.fate.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Fate.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.chalkBase();
        for (RegistryObject<BlockChalkLine> e : ModBlocks.chalks.values()) {
            this.horizontalBlock(e.get(), this.chalk(e.get()));
        }
        this.simpleBlock(ModBlocks.charmOre.get(), this.models().singleTexture(ModBlocks.charmOre.getId().getPath(), new ResourceLocation(Fate.MODID, "block/ore"), "ore", new ResourceLocation(Fate.MODID, "blocks/charm_ore")));
        this.simpleBlock(ModBlocks.crystalOre.get(), this.models().singleTexture(ModBlocks.crystalOre.getId().getPath(), new ResourceLocation(Fate.MODID, "block/ore"), "ore", new ResourceLocation(Fate.MODID, "blocks/crystal_ore")));
        this.horizontalBlock(ModBlocks.altar.get(), this.models().getExistingFile(new ResourceLocation(Fate.MODID, "block/altar")));
    }

    @Override
    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "blocks" + "/" + name.getPath());
    }

    private ModelFile chalk(Block block) {
        return this.models().singleTexture(block.getRegistryName().getPath(), new ResourceLocation(Fate.MODID, "block/chalk_base"), "tex", this.blockTexture(block));
    }

    private ModelFile chalkBase() {
        return this.models().singleTexture("fateubw:block/chalk_base", this.mcLoc("block/thin_block"), "particle", new ResourceLocation(Fate.MODID, "blocks/chalk_line_none"))
                .element().from(0, 0, 0).to(16, 0.5f, 16)
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#tex").end()
                .face(Direction.DOWN).uvs(0, 16, 16, 0).cullface(Direction.DOWN).texture("#tex").end()
                .end();
    }
}
