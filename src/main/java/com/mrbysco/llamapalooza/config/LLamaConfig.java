package com.mrbysco.llamapalooza.config;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaTables;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class LLamaConfig {
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> lootTables;
        public final ForgeConfigSpec.IntValue spitInterval;
        public final ForgeConfigSpec.IntValue speedReduction;

        Common(ForgeConfigSpec.Builder builder) {
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

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
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
