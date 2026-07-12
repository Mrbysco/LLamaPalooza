package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LlamaDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, LlamaPalooza.MOD_ID);

	public static final Supplier<DataComponentType<Identifier>> LOOT_TABLE = DATA_COMPONENT_TYPES.registerComponentType("loot_table", builder ->
			builder
					.persistent(Identifier.CODEC)
					.networkSynchronized(Identifier.STREAM_CODEC)
					.cacheEncoding()
	);
}
