package com.mrbysco.llamapalooza.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.llamapalooza.client.renderer.state.LlamaItemSpitRenderState;
import com.mrbysco.llamapalooza.entity.projectile.LlamaItemSpit;
import net.minecraft.client.model.animal.llama.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

public class LlamaItemSpitRenderer extends EntityRenderer<LlamaItemSpit, LlamaItemSpitRenderState> {
	private static final Identifier LLAMA_SPIT_LOCATION = Identifier.withDefaultNamespace("textures/entity/llama/spit.png");
	private final LlamaSpitModel model;
	private final ItemModelResolver itemModelResolver;

	public LlamaItemSpitRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemModelResolver = context.getItemModelResolver();
		this.model = new LlamaSpitModel(context.bakeLayer(ModelLayers.LLAMA_SPIT));
	}

	@Override
	public void submit(LlamaItemSpitRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.15F, 0.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
		submitNodeCollector.submitModel(
				this.model, state, poseStack, LLAMA_SPIT_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null
		);

		for (ItemStackRenderState itemState : state.items) {
			poseStack.pushPose();
			itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
			poseStack.popPose();
		}
		poseStack.popPose();
		super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	public LlamaItemSpitRenderState createRenderState() {
		return new LlamaItemSpitRenderState();
	}

	@Override
	public void extractRenderState(LlamaItemSpit entity, LlamaItemSpitRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
		for (int i = 0; i < entity.getItems().size(); i++) {
			if (state.items.size() <= i) {
				state.items.add(new ItemStackRenderState());
			}
			this.itemModelResolver.updateForNonLiving(state.items.get(i), entity.getItems().get(i), ItemDisplayContext.GROUND, entity);
		}
	}
}
