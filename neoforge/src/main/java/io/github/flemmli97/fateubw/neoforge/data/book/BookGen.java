package io.github.flemmli97.fateubw.neoforge.data.book;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateCreativeTab;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.datagen.builders.BookBuilder;
import net.favouriteless.modopedia.api.datagen.providers.BookProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BookGen extends BookProvider {

    public BookGen(CompletableFuture<HolderLookup.Provider> registries, PackOutput output) {
        super(Fate.MODID, registries, output);
    }

    @Override
    protected void build(HolderLookup.Provider provider, BiConsumer<String, Book> biConsumer) {
        BookBuilder.of("fateubw_book.book.title")
                .landingText("fateubw_book.book.landing")
                .tab(FateCreativeTab.TAB.getID())
                .build("fateubw_book", biConsumer);
    }
}
