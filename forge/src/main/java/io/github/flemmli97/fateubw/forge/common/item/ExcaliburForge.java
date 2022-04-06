package io.github.flemmli97.fateubw.forge.common.item;

import io.github.flemmli97.fateubw.common.items.weapons.ItemExcalibur;
import io.github.flemmli97.fateubw.forge.client.ClientItemRenderer;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class ExcaliburForge extends ItemExcalibur {
    public ExcaliburForge(Properties props) {
        super(props);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ClientItemRenderer.excalibur());
    }
}
