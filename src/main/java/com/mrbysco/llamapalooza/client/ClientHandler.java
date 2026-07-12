package com.mrbysco.llamapalooza.client;

import com.mrbysco.llamapalooza.client.renderer.LlamaItemSpitRenderer;
import com.mrbysco.llamapalooza.client.renderer.LootLLamaRenderer;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(LLamaRegistry.LOOT_LLAMA.get(), LootLLamaRenderer::new);
		event.registerEntityRenderer(LLamaRegistry.ITEM_SPIT.get(), LlamaItemSpitRenderer::new);
	}
}
