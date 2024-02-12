package com.mrbysco.llamapalooza.client.renderer;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.entity.LootLlama;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LootLLamaRenderer extends MobRenderer<LootLlama, LlamaModel<LootLlama>> {
    private static final ResourceLocation CREAMY = new ResourceLocation("textures/entity/llama/creamy.png");
    private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/llama/white.png");
    private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/llama/brown.png");
    private static final ResourceLocation GRAY = new ResourceLocation("textures/entity/llama/gray.png");
    private static final ResourceLocation NEUTRON = new ResourceLocation(LlamaPalooza.MOD_ID, "textures/entity/llama/wheezer.png");

    public LootLLamaRenderer(EntityRendererProvider.Context context) {
        super(context, new LlamaModel<>(context.bakeLayer(ModelLayers.LLAMA)), 0.7F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(LootLlama llama) {
        if (llama.getName().getString().equals("Carl")) {
            //Croissant
            return NEUTRON;
        }
        return switch (llama.getVariant()) {
            case CREAMY -> CREAMY;
            case WHITE -> WHITE;
            case BROWN -> BROWN;
            case GRAY -> GRAY;
        };
    }
}