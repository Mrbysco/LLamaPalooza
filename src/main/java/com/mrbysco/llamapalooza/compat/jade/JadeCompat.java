package com.mrbysco.llamapalooza.compat.jade;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.entity.LootLlama;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
		registration.registerEntityComponent(LootLlamaStatProvider.INSTANCE, LootLlama.class);
		registration.registerEntityComponent(LootLlamaCooldownProvider.INSTANCE, LootLlama.class);
	}

	public static class LootLlamaStatProvider implements IEntityComponentProvider {
		private static final ResourceLocation STATS = new ResourceLocation(LlamaPalooza.MOD_ID, "stats");

		public static final LootLlamaStatProvider INSTANCE = new LootLlamaStatProvider();

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

	public static class LootLlamaCooldownProvider implements IEntityComponentProvider {
		private static final ResourceLocation COOLDOWN = new ResourceLocation(LlamaPalooza.MOD_ID, "cooldown");

		public static final LootLlamaCooldownProvider INSTANCE = new LootLlamaCooldownProvider();

		@Override
		public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
			if (entityAccessor.getEntity() instanceof LootLlama lootLlama && lootLlama.getTimer() >= 0) {
				//Get timer converted to seconds
				int seconds = Mth.ceil(lootLlama.getTimer() / 20f);
				iTooltip.add(Component.translatable("llamapalooza.cooldown", seconds));
			}
		}

		@Override
		public ResourceLocation getUid() {
			return COOLDOWN;
		}
	}
}
