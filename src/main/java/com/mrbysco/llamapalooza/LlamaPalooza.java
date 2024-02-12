package com.mrbysco.llamapalooza;

import com.mojang.logging.LogUtils;
import com.mrbysco.llamapalooza.client.ClientHandler;
import com.mrbysco.llamapalooza.config.LLamaConfig;
import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaSerializers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(LlamaPalooza.MOD_ID)
public class LlamaPalooza {
	public static final String MOD_ID = "llamapalooza";
	public static final Logger LOGGER = LogUtils.getLogger();

	public LlamaPalooza() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LLamaConfig.commonSpec);
		eventBus.register(LLamaConfig.class);

		LLamaRegistry.ITEMS.register(eventBus);
		LLamaRegistry.CREATIVE_MODE_TABS.register(eventBus);
		LLamaRegistry.ENTITY_TYPES.register(eventBus);
		LlamaSerializers.ENTITY_DATA_SERIALIZER.register(eventBus);

		eventBus.addListener(this::registerEntityAttributes);

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
		}
	}

	public void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(LLamaRegistry.LOOT_LLAMA.get(), LootLlama.createAttributes().build());
	}
}
