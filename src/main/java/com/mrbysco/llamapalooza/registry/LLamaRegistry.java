package com.mrbysco.llamapalooza.registry;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.config.LLamaConfig;
import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.entity.projectile.LlamaItemSpit;
import com.mrbysco.llamapalooza.item.LootLlamaSpawnEgg;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LLamaRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LlamaPalooza.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LlamaPalooza.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LlamaPalooza.MOD_ID);

    public static final RegistryObject<EntityType<LootLlama>> LOOT_LLAMA = ENTITY_TYPES.register("loot_llama",
            () -> EntityType.Builder.<LootLlama>of(LootLlama::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.87F)
                    .clientTrackingRange(10).build("loot_llama"));
    public static final RegistryObject<EntityType<LlamaItemSpit>> ITEM_SPIT = ENTITY_TYPES.register("item_spit",
            () -> EntityType.Builder.<LlamaItemSpit>of(LlamaItemSpit::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("item_spit"));

    public static final RegistryObject<LootLlamaSpawnEgg> LOOT_LLAMA_SPAWN_EGG = ITEMS.register("loot_llama_spawn_egg", () ->
            new LootLlamaSpawnEgg(new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup.llamapalooza"))
            .displayItems((displayParameters, output) -> {
                for (String table : LLamaConfig.COMMON.lootTables.get()) {
                    if (table.isEmpty()) continue;

                    ItemStack spawnEgg = new ItemStack(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.get());
                    CompoundTag tag = spawnEgg.getOrCreateTag();
                    tag.putString("LootTable", table);
                    spawnEgg.setTag(tag);

                    output.accept(spawnEgg);
                }
            }).build());
}
