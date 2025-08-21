package io.github.flemmli97.fateubw.common.entity.servant.lancelot;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.lancelot.impl.BowHandler;
import io.github.flemmli97.fateubw.common.entity.servant.lancelot.impl.CrossbowHandler;
import io.github.flemmli97.fateubw.common.entity.servant.lancelot.impl.TridentHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LancelotAttackAI {

    private static final List<OrderedResource> ORDERED_RESOURCES = new ArrayList<>();
    private static final Map<ResourceLocation, LancelotUseHandler> REGISTRY = new HashMap<>();

    public static ResourceLocation BOW = register(Fate.modRes("bow"), new BowHandler());
    public static ResourceLocation CROSSBOW = register(Fate.modRes("crossbow"), new CrossbowHandler());
    public static ResourceLocation TRIDENT = register(Fate.modRes("trident"), new TridentHandler());

    public static void init() {
    }

    public static synchronized ResourceLocation register(ResourceLocation id, LancelotUseHandler handler) {
        return register(id, ORDERED_RESOURCES.size(), handler);
    }

    public static synchronized ResourceLocation register(ResourceLocation id, int order, LancelotUseHandler handler) {
        if (REGISTRY.put(id, handler) != null) {
            throw new IllegalStateException("Handler with id " + id + " already registered!");
        }
        ORDERED_RESOURCES.add(new OrderedResource(order, id));
        ORDERED_RESOURCES.sort(Collections.reverseOrder());
        return id;
    }

    public static Map<ResourceLocation, LancelotUseHandler> getView() {
        return ImmutableMap.copyOf(REGISTRY);
    }

    public static LancelotUseHandler get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    @Nullable
    public static Pair<ResourceLocation, LancelotUseHandler> getFor(ItemStack stack) {
        for (OrderedResource r : ORDERED_RESOURCES) {
            LancelotUseHandler v = REGISTRY.get(r.res);
            if (v.matches(stack))
                return Pair.of(r.res, v);
        }
        return null;
    }

    record OrderedResource(int order, ResourceLocation res) implements Comparable<OrderedResource> {

        @Override
        public int compareTo(@NotNull OrderedResource o) {
            return this.order == o.order ? this.res.compareTo(o.res) : Integer.compare(this.order, o.order);
        }
    }
}
