package com.mrbysco.llamapalooza.data;

import com.mrbysco.llamapalooza.data.client.LLamaLanguageProvider;
import com.mrbysco.llamapalooza.data.client.LlamaModelProvider;
import com.mrbysco.llamapalooza.data.server.LLamaLootProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LLamaDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new LLamaLootProvider(packOutput));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new LLamaLanguageProvider(packOutput));
			generator.addProvider(true, new LlamaModelProvider(packOutput, helper));
		}
	}
}
