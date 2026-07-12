package com.mrbysco.llamapalooza.data.client;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class LlamaModelProvider extends ModelProvider {
	public LlamaModelProvider(PackOutput packOutput) {
		super(packOutput, LlamaPalooza.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
	}
}
