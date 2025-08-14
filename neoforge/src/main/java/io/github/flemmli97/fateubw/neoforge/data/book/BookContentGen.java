package io.github.flemmli97.fateubw.neoforge.data.book;

import io.github.flemmli97.fateubw.Fate;
import net.favouriteless.modopedia.api.datagen.BookContentOutput;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BookContentGen extends ContentSetProvider {

    private final Map<String, String> translations = new HashMap<>();

    public BookContentGen(CompletableFuture<HolderLookup.Provider> registries, PackOutput output) {
        super(Fate.MODID, "fateubw_book", "en_us", registries, output);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        this.createTranslations();
        return super.run(output);
    }

    @Override
    public void buildEntries(HolderLookup.Provider registries, BookContentOutput output) {
    }

    @Override
    public void buildCategories(HolderLookup.Provider registries, BookContentOutput output) {
        int sort = 0;
    }

    protected void createTranslations() {

    }
}
