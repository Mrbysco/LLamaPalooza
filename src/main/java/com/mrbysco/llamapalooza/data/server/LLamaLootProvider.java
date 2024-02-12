package com.mrbysco.llamapalooza.data.server;

import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LLamaTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class LLamaLootProvider extends LootTableProvider {
	public LLamaLootProvider(PackOutput packOutput) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(LLamaGiftLoot::new, LootContextParamSets.GIFT),
				new SubProviderEntry(LLamaEntityLoot::new, LootContextParamSets.ENTITY)
		));
	}

	private static class LLamaEntityLoot extends EntityLootSubProvider {
		protected LLamaEntityLoot() {
			super(FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		public void generate() {
			this.add(
					LLamaRegistry.LOOT_LLAMA.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(Items.LEATHER)
															.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
															.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
											)
							)
			);
		}

		@Override
		protected Stream<EntityType<?>> getKnownEntityTypes() {
			return LLamaRegistry.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
		}
	}

	private static class LLamaGiftLoot implements LootTableSubProvider {
		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
			consumer.accept(LLamaTables.GENERAL, LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.name("main")
							.add(TagEntry.tagContents(ItemTags.DIRT).setWeight(10)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F)))
							)
							.add(TagEntry.tagContents(ItemTags.LOGS).setWeight(8)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
							)
							.add(LootItem.lootTableItem(Items.GRAVEL).setWeight(8)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.COBBLESTONE).setWeight(8)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F)))
							)
							.add(TagEntry.tagContents(ItemTags.SAND).setWeight(6)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F)))
							)
							.add(LootItem.lootTableItem(Items.CLAY).setWeight(5)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
							)
					)
			);
			consumer.accept(LLamaTables.ORES, LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.name("main")
							.add(TagEntry.tagContents(Tags.Items.ORES_COAL).setWeight(20)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_IRON).setWeight(18)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_COPPER).setWeight(15)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_GOLD).setWeight(10)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_LAPIS).setWeight(8)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_REDSTONE).setWeight(8)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_QUARTZ).setWeight(6)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_EMERALD).setWeight(5)
									.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
							)
							.add(TagEntry.tagContents(Tags.Items.ORES_DIAMOND).setWeight(4)
									.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
							)
					)
			);
		}
	}
}
