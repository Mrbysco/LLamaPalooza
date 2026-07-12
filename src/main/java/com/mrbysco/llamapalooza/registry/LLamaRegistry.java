package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.config.LLamaConfig;
import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.entity.projectile.LlamaItemSpit;
import com.mrbysco.llamapalooza.item.LootLlamaSpawnEgg;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LLamaRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LlamaPalooza.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LlamaPalooza.MOD_ID);
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(LlamaPalooza.MOD_ID);

	public static final Supplier<EntityType<LootLlama>> LOOT_LLAMA = ENTITY_TYPES.registerEntityType("loot_llama",
			LootLlama::new,
			MobCategory.CREATURE,
			builder -> builder
					.sized(0.9F, 1.87F)
					.eyeHeight(1.7765F)
					.passengerAttachments(new Vec3(0.0, 1.37, -0.3))
					.clientTrackingRange(10)
	);

	public static final Supplier<EntityType<LlamaItemSpit>> ITEM_SPIT = ENTITY_TYPES.registerEntityType("item_spit",
			LlamaItemSpit::new,
			MobCategory.MISC,
			builder -> builder
					.noLootTable()
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10)
	);

	public static final DeferredItem<LootLlamaSpawnEgg> LOOT_LLAMA_SPAWN_EGG = ITEMS.registerItem("loot_llama_spawn_egg", LootLlamaSpawnEgg::new, () ->
			new Item.Properties().spawnEgg(LLamaRegistry.LOOT_LLAMA.get()));

	public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.llamapalooza"))
			.displayItems((displayParameters, output) -> {
				for (String table : LLamaConfig.COMMON.lootTables.get()) {
					if (table.isEmpty()) continue;

					ItemStack spawnEgg = new ItemStack(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.get());
					Identifier tableLocation = Identifier.tryParse(table);
					if (tableLocation == null) {
						LlamaPalooza.LOGGER.error("Invalid loot table: {}", table);
						continue;
					}
					spawnEgg.set(LlamaDataComponents.LOOT_TABLE, tableLocation);
					output.accept(spawnEgg);
				}
			}).build());
}
