package com.mrbysco.llamapalooza.data.client;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class LlamaModelProvider extends ItemModelProvider {
    public LlamaModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, LlamaPalooza.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent(LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.getId().getPath(), new ResourceLocation("item/template_spawn_egg"));
    }
}
