package com.mrbysco.llamapalooza.client.renderer;

import com.google.common.collect.Maps;
import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.client.renderer.layer.LootLlamaDecorLayer;
import com.mrbysco.llamapalooza.client.renderer.state.LootLlamaRenderState;
import com.mrbysco.llamapalooza.entity.LootLlama;
import net.minecraft.client.model.animal.llama.BabyLlamaModel;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.equine.Llama;

import java.util.Map;

public class LootLLamaRenderer extends AgeableMobRenderer<LootLlama, LootLlamaRenderState, LlamaModel> {
	private static final Map<Llama.Variant, Identifier> TEXTURES = Maps.newEnumMap(
			Map.of(
					Llama.Variant.CREAMY,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_creamy.png"),
					Llama.Variant.WHITE,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_white.png"),
					Llama.Variant.BROWN,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_brown.png"),
					Llama.Variant.GRAY,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_gray.png")
			)
	);
	private static final Map<Llama.Variant, Identifier> BABY_TEXTURES = Maps.newEnumMap(
			Map.of(
					Llama.Variant.CREAMY,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_creamy_baby.png"),
					Llama.Variant.WHITE,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_white_baby.png"),
					Llama.Variant.BROWN,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_brown_baby.png"),
					Llama.Variant.GRAY,
					Identifier.withDefaultNamespace("textures/entity/llama/llama_gray_baby.png")
			)
	);
	private static final Identifier NEUTRON = LlamaPalooza.modLoc("textures/entity/llama/wheezer.png");

	public LootLLamaRenderer(EntityRendererProvider.Context context) {
		super(context, new LlamaModel(context.bakeLayer(ModelLayers.LLAMA)), new BabyLlamaModel(context.bakeLayer(ModelLayers.LLAMA_BABY)), 0.7F);
		this.addLayer(new LootLlamaDecorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
	}

	@Override
	public Identifier getTextureLocation(LootLlamaRenderState state) {
		Map<Llama.Variant, Identifier> textures = state.isBaby ? BABY_TEXTURES : TEXTURES;
		if (!state.isBaby && state.nameTag != null && state.nameTag.getString().equals("Carl")) {
			return NEUTRON;
		}
		return textures.get(state.variant);
	}

	@Override
	public LootLlamaRenderState createRenderState() {
		return new LootLlamaRenderState();
	}

	@Override
	public void extractRenderState(LootLlama entity, LootLlamaRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.variant = entity.getVariant();
		state.hasChest = !entity.isBaby() && entity.hasChest();
		state.bodyItem = entity.getBodyArmorItem();
		state.isTraderLlama = entity.isTraderLlama();
	}
}