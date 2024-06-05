package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LlamaDataComponents {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, LlamaPalooza.MOD_ID);

	public static final Supplier<DataComponentType<ResourceLocation>> LOOT_TABLE = DATA_COMPONENT_TYPES.register("loot_table", () ->
			DataComponentType.<ResourceLocation>builder()
					.persistent(ResourceLocation.CODEC)
					.networkSynchronized(ResourceLocation.STREAM_CODEC)
					.cacheEncoding()
					.build()
	);
}
