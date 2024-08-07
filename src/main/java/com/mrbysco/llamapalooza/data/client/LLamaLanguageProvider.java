package com.mrbysco.llamapalooza.data.client;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

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

		add("config.jade.plugin_llamapalooza.stats", "Stats: %s/%s/%s");
		add("config.jade.plugin_llamapalooza.cooldown", "Spit Cooldown: %s");
	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add("llamapalooza.configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add("llamapalooza.configuration." + path + ".tooltip", description);
	}
}
