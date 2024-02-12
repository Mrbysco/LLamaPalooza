package com.mrbysco.llamapalooza.data;

import com.mrbysco.llamapalooza.data.client.LLamaLanguageProvider;
import com.mrbysco.llamapalooza.data.client.LlamaModelProvider;
import com.mrbysco.llamapalooza.data.server.LLamaLootProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
