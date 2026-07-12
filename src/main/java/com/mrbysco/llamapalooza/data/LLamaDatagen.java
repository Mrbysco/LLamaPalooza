package com.mrbysco.llamapalooza.data;

import com.mrbysco.llamapalooza.data.client.LLamaLanguageProvider;
import com.mrbysco.llamapalooza.data.client.LlamaModelProvider;
import com.mrbysco.llamapalooza.data.server.LLamaLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LLamaDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(true, new LLamaLootProvider(packOutput, lookupProvider));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new LLamaLanguageProvider(packOutput));
			generator.addProvider(true, new LlamaModelProvider(packOutput, helper));
		}
	}
}
