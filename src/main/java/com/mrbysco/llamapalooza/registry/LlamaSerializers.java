package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LlamaSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, LlamaPalooza.MOD_ID);

    public static final Supplier<EntityDataSerializer<Optional<ResourceLocation>>> RESOURCE_LOCATION = ENTITY_DATA_SERIALIZER.register("optional_resource_location", () -> EntityDataSerializer.optional(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation));
    public static final Supplier<EntityDataSerializer<List<ItemStack>>> ITEM_STACKS = ENTITY_DATA_SERIALIZER.register("item_stacks", () -> new EntityDataSerializer<>() {

        @Override
        public void write(FriendlyByteBuf byteBuf, List<ItemStack> stacks) {
            byteBuf.writeInt(stacks.size());
            for (ItemStack stack : stacks) {
                byteBuf.writeItem(stack);
            }
        }

        @Override
        public List<ItemStack> read(FriendlyByteBuf byteBuf) {
            int count = byteBuf.readInt();
            if (count > 0) {
                List<ItemStack> stacks = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    stacks.add(byteBuf.readItem());
                }
                return stacks;
            }

            return new ArrayList<>();
        }

        @Override
        public List<ItemStack> copy(List<ItemStack> stacks) {
            return stacks;
        }
    });
}
