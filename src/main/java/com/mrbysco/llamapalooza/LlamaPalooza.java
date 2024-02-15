package com.mrbysco.llamapalooza;

import com.mojang.logging.LogUtils;
import com.mrbysco.llamapalooza.client.ClientHandler;
import com.mrbysco.llamapalooza.compat.top.TOPCompat;
import com.mrbysco.llamapalooza.config.LLamaConfig;
import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaSerializers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;

@Mod(LlamaPalooza.MOD_ID)
public class LlamaPalooza {
	public static final String MOD_ID = "llamapalooza";
	public static final Logger LOGGER = LogUtils.getLogger();

	public LlamaPalooza(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LLamaConfig.commonSpec);
		eventBus.register(LLamaConfig.class);

		eventBus.addListener(this::sendImc);

		LLamaRegistry.ITEMS.register(eventBus);
		LLamaRegistry.CREATIVE_MODE_TABS.register(eventBus);
		LLamaRegistry.ENTITY_TYPES.register(eventBus);
		LlamaSerializers.ENTITY_DATA_SERIALIZER.register(eventBus);

		eventBus.addListener(this::registerEntityAttributes);

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
		}
	}

	public void sendImc(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded("theoneprobe")) {
			TOPCompat.register();
		}
	}

	public void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(LLamaRegistry.LOOT_LLAMA.get(), LootLlama.createAttributes().build());
	}
}
