package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class LLamaTables {
	public static final ResourceKey<LootTable> GENERAL = registerTable("resources/general");
	public static final ResourceKey<LootTable> ORES = registerTable("resources/ore");

	private static ResourceKey<LootTable> registerTable(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, LlamaPalooza.modLoc(path));
	}
}
