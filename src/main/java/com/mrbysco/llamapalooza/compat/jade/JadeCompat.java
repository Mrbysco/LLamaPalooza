package com.mrbysco.llamapalooza.compat.jade;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.entity.LootLlama;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {

	@Override
	public void register(IWailaCommonRegistration registration) {
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(LlootLlamaProvider.INSTANCE, LootLlama.class);
	}

	public static class LlootLlamaProvider implements IEntityComponentProvider {
		private static final ResourceLocation STATS = new ResourceLocation(LlamaPalooza.MOD_ID, "stats");

		public static final LlootLlamaProvider INSTANCE = new LlootLlamaProvider();

		@Override
		public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
			if (entityAccessor.getEntity() instanceof LootLlama lootLlama) {
				iTooltip.add(Component.translatable("llamapalooza.stats",
						lootLlama.getLootSpeed(), lootLlama.getLootGain(), lootLlama.getLootStrength()));
			}
		}

		@Override
		public ResourceLocation getUid() {
			return STATS;
		}
	}
}
