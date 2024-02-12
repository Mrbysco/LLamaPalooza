package com.mrbysco.llamapalooza.data.client;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class LLamaLanguageProvider extends LanguageProvider {
	public LLamaLanguageProvider(PackOutput packOutput) {
		super(packOutput, LlamaPalooza.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addEntityType(LLamaRegistry.LOOT_LLAMA, "Loot Llama");
		addEntityType(LLamaRegistry.ITEM_SPIT, "Item Spit");

		addItem(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG, "Loot Llama Spawn Egg");

		add("itemGroup.llamapalooza", "LlamaPalooza");
	}
}
