package com.flemmli97.fate.data;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.blocks.BlockChalkLine;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Fate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event){
        DataGenerator data = event.getGenerator();
        if(event.includeClient()) {
            data.addProvider(new BlockStates(data, event.getExistingFileHelper()));
        }
    }

    public static class BlockStates extends BlockStateProvider {

        public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
            super(gen, Fate.MODID, helper);
        }

        @Override
        protected void registerStatesAndModels() {
            this.chalkBase();
            for(RegistryObject<BlockChalkLine> e : ModBlocks.chalks.values()) {
                this.horizontalBlock(e.get(), this.chalk(e.get()));
            }
        }

        @Override
        public ResourceLocation blockTexture(Block block) {
            ResourceLocation name = block.getRegistryName();
            return new ResourceLocation(name.getNamespace(), "blocks" + "/" + name.getPath());
        }

        private ModelFile chalk(Block block){
            return this.models().singleTexture(block.getRegistryName().getPath(), new ResourceLocation(Fate.MODID, "block/chalk_base"), "tex", this.blockTexture(block));
        }

        private ModelFile chalkBase(){
            return this.models().singleTexture("fatemod:block/chalk_base", mcLoc("block/thin_block"), "particle", new ResourceLocation(Fate.MODID,"blocks/chalk_line_none"))
                    .element().from(0,0,0).to(16,0.5f,16)
                        .face(Direction.UP).uvs(0,  0, 16, 16).texture("#tex").end()
                        .face(Direction.DOWN).uvs(0,  16, 16, 0).cullface(Direction.DOWN).texture("#tex").end()
                    .end();
        }
    }
}
