package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LlamaSerializers {
	public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, LlamaPalooza.MOD_ID);

	public static final Supplier<EntityDataSerializer<Optional<ResourceLocation>>> RESOURCE_LOCATION = ENTITY_DATA_SERIALIZER.register("optional_resource_location", () ->
			EntityDataSerializer.forValueType(
					ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional)
			)
	);

	public static final Supplier<EntityDataSerializer<List<ItemStack>>> ITEM_STACKS = ENTITY_DATA_SERIALIZER.register("item_stacks", () ->
			EntityDataSerializer.forValueType(
					ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list())
			)
	);
}
