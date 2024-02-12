package com.mrbysco.llamapalooza.config;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaTables;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class LLamaConfig {
	public static class Common {
		public final ModConfigSpec.ConfigValue<List<? extends String>> lootTables;
		public final ModConfigSpec.IntValue spitInterval;
		public final ModConfigSpec.IntValue speedReduction;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("general");

			String[] tables = new String[]
					{
							LLamaTables.GENERAL.toString(),
							LLamaTables.ORES.toString()
					};


			lootTables = builder
					.comment("The list of loot tables known to the mod (Syntax: namespace:loot_table_name, e.g. minecraft:chests/simple_dungeon)")
					.defineListAllowEmpty(List.of("tables"), () -> List.of(tables), o -> (o instanceof String));

			spitInterval = builder
					.comment("The interval in ticks between Loot Llamas spitting items out (20 ticks = 1 second) [default = 200 ticks])")
					.defineInRange("spitInterval", 200, 1, Integer.MAX_VALUE);

			speedReduction = builder
					.comment("The amount of ticks reduced per Speed upgrade level (20 ticks = 1 second) [default = 20 ticks]")
					.defineInRange("speedReduction", 20, 1, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		LlamaPalooza.LOGGER.debug("Loaded LlamaPalooza's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		LlamaPalooza.LOGGER.warn("LlamaPalooza's config just got changed on the file system!");
	}
}
