package com.mrbysco.llamapalooza.client.renderer.state;

import net.minecraft.client.renderer.entity.state.LlamaSpitRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import java.util.ArrayList;
import java.util.List;

public class LlamaItemSpitRenderState extends LlamaSpitRenderState {
	public final List<ItemStackRenderState> items = new ArrayList<>();
}
