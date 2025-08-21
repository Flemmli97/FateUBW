package io.github.flemmli97.fateubw.common.entity.servant.lancelot;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Fireworks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LancelotInventory implements Iterable<ItemStack> {

    public static final ResourceLocation SPEAR = Fate.modRes("spear");
    public static final ResourceLocation ARROWS = Fate.modRes("arrows");
    public static final ResourceLocation FIREWORKS = Fate.modRes("fireworks");

    public static final Comparator<ResourceLocation> NAMESPACE_FIRST = Comparator.comparing(ResourceLocation::getNamespace)
            .thenComparing(ResourceLocation::getPath);

    private final LivingEntity entity;

    private final List<ResourceLocation> slotData;
    private final NonNullList<ItemStack> items;

    private SwappedStack swappedStack;

    public LancelotInventory(LivingEntity entity) {
        this.entity = entity;
        List<ResourceLocation> list = new ArrayList<>(LancelotAttackAI.getView()
                .keySet().stream().sorted(NAMESPACE_FIRST).toList());
        list.add(SPEAR);
        list.add(ARROWS);
        list.add(FIREWORKS);
        this.slotData = ImmutableList.copyOf(list);
        this.items = NonNullList.withSize(this.slotData.size(), ItemStack.EMPTY);
    }

    @Nullable
    public ItemStack insert(ItemStack stack) {
        if (stack.getItem() instanceof ArrowItem) {
            int idx = this.slotData.indexOf(ARROWS);
            ItemStack current = this.items.get(idx);
            if (ItemStack.isSameItemSameComponents(current, stack)) {
                int grow = Math.min(stack.getCount(), current.getMaxStackSize() - current.getCount());
                if (grow > 0) {
                    current.grow(grow);
                    stack.shrink(grow);
                }
                return ItemStack.EMPTY;
            }
            if (current.isEmpty() || stack.getCount() > current.getCount()) {
                this.items.set(idx, stack);
                return current;
            }
            return null;
        }
        if (stack.getItem() instanceof FireworkRocketItem) {
            int idx = this.slotData.indexOf(FIREWORKS);
            ItemStack current = this.items.get(idx);
            if (ItemStack.isSameItemSameComponents(current, stack)) {
                int grow = Math.min(stack.getCount(), current.getMaxStackSize() - current.getCount());
                if (grow > 0) {
                    current.grow(grow);
                    stack.shrink(grow);
                }
                return ItemStack.EMPTY;
            }
            Fireworks stackFireworks = stack.get(DataComponents.FIREWORKS);
            Fireworks currentFireworks = current.get(DataComponents.FIREWORKS);
            if (current.isEmpty() || currentFireworks == null || (stackFireworks != null && stackFireworks.explosions().size() > currentFireworks.explosions().size())) {
                this.items.set(idx, stack);
                return current;
            }
            return null;
        }
        Pair<ResourceLocation, LancelotUseHandler> handler = LancelotAttackAI.getFor(stack);
        if (handler != null) {
            int idx = this.slotData.indexOf(handler.getFirst());
            ItemStack current = this.items.get(idx);
            if (current.isEmpty() || handler.getSecond().isBetterThan(this.entity, current, stack)) {
                this.items.set(idx, stack);
                return current;
            }
        }
        if (stack.is(FateTags.Items.SPEARS)) {
            int idx = this.slotData.indexOf(SPEAR);
            ItemStack current = this.items.get(idx);
            if (current.isEmpty() || ItemUtils.isItemBetter(this.entity, null, stack, current)) {
                this.items.set(idx, stack);
                return current;
            }
            return null;
        }
        return null;
    }

    private int idxFor(ResourceLocation id) {
        return this.slotData.indexOf(id);
    }

    public ItemStack get(ResourceLocation id) {
        int idx = this.idxFor(id);
        if (idx == -1)
            return ItemStack.EMPTY;
        return this.items.get(idx);
    }

    public void clearContent() {
        this.items.clear();
    }

    public boolean swapItems(ResourceLocation id) {
        if (id == null) {
            if (this.swappedStack != null) {
                this.entity.setItemInHand(InteractionHand.OFF_HAND, this.swappedStack.stack());
            }
            this.swappedStack = null;
            return true;
        }
        int idx = this.idxFor(id);
        if (idx == -1)
            return false;
        ItemStack current = this.items.get(idx);
        if (current.isEmpty())
            return false;
        if (this.swappedStack != null) {
            this.swapItems(null);
        }
        this.swappedStack = new SwappedStack(this.entity.getOffhandItem(), id);
        this.entity.setItemInHand(InteractionHand.OFF_HAND, this.items.get(idx).copy());
        return true;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag content = new ListTag();
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        for (ItemStack stack : this.items) {
            content.add(ItemStack.OPTIONAL_CODEC.encodeStart(ops, stack).getOrThrow());
        }
        tag.put("Content", content);
        ListTag ids = new ListTag();
        for (ResourceLocation id : this.slotData) {
            ids.add(ResourceLocation.CODEC.encodeStart(ops, id).getOrThrow());
        }
        tag.put("Ids", ids);
        if (this.swappedStack != null) {
            tag.put("SwappedStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.swappedStack.stack()).getOrThrow());
            tag.put("SwappedId", ResourceLocation.CODEC.encodeStart(ops, this.swappedStack.id()).getOrThrow());
        }
        return tag;
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag content = tag.getList("Content", Tag.TAG_COMPOUND);
        ListTag idTag = tag.getList("Ids", Tag.TAG_STRING);
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        int[] idxs = new int[idTag.size()]; // Remape the indices if changes occured
        for (int i = 0; i < idTag.size(); i++) {
            ResourceLocation id = ResourceLocation.CODEC.parse(ops, idTag.get(i)).getOrThrow();
            for (int j = 0; j < this.slotData.size(); j++) {
                if (this.slotData.get(i).equals(id)) {
                    idxs[i] = i;
                    break;
                }
            }
        }
        for (int i = 0; i < content.size(); i++) {
            ItemStack stack = ItemStack.OPTIONAL_CODEC.parse(ops, content.get(i)).getOrThrow();
            this.items.set(idxs[i], stack);
        }
        if (tag.contains("SwappedStack")) {
            this.swappedStack = new SwappedStack(ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("SwappedStack")).getOrThrow(),
                    ResourceLocation.CODEC.parse(ops, tag.get("SwappedId")).getOrThrow());
        }
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return this.items.iterator();
    }

    record SwappedStack(ItemStack stack, ResourceLocation id) {
    }
}
