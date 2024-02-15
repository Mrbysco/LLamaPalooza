package com.mrbysco.llamapalooza.compat.top;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.entity.LootLlama;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.fml.InterModComms;

import java.util.function.Function;

public class TOPCompat {
	public static void register() {
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	public static final class GetTheOneProbe implements Function<ITheOneProbe, Void> {
		@Override
		public Void apply(ITheOneProbe input) {
			input.registerEntityProvider(new LLamaStatInfo());
			input.registerEntityProvider(new LLamaTimerInfo());
			return null;
		}
	}

	public static final class LLamaStatInfo implements IProbeInfoEntityProvider {
		@Override
		public String getID() {
			return new ResourceLocation(LlamaPalooza.MOD_ID, "stats").toString();
		}

		@Override
		public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
			if (entity instanceof LootLlama lootLlama) {
				probeInfo.horizontal().text(Component.translatable("llamapalooza.stats", lootLlama.getLootSpeed(), lootLlama.getLootGain(), lootLlama.getLootStrength()));
			}
		}
	}


	public static final class LLamaTimerInfo implements IProbeInfoEntityProvider {
		@Override
		public String getID() {
			return new ResourceLocation(LlamaPalooza.MOD_ID, "timer").toString();
		}

		@Override
		public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
			if (entity instanceof LootLlama lootLlama) {
				//Get timer converted to seconds
				int seconds = Mth.ceil(lootLlama.getTimer() / 20f);
				probeInfo.horizontal().text(Component.translatable("llamapalooza.cooldown", seconds));
			}
		}
	}
}
