package com.mrbysco.llamapalooza.item;

import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;
import java.util.Objects;

public class LootLlamaSpawnEgg extends DeferredSpawnEggItem {
	public LootLlamaSpawnEgg(final Properties properties) {
		super(LLamaRegistry.LOOT_LLAMA, 12623485, 10051392, properties);
	}

	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = context.getItemInHand();
			BlockPos blockpos = context.getClickedPos();
			Direction direction = context.getClickedFace();
			BlockState blockstate = level.getBlockState(blockpos);
			if (level.getBlockEntity(blockpos) instanceof Spawner spawner) {
				EntityType<LootLlama> type = LLamaRegistry.LOOT_LLAMA.get();
				spawner.setEntityId(type, level.getRandom());
				level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
				level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
				itemstack.shrink(1);
				return InteractionResult.CONSUME;
			} else {
				BlockPos pos;
				if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
					pos = blockpos;
				} else {
					pos = blockpos.relative(direction);
				}

				EntityType<LootLlama> type = LLamaRegistry.LOOT_LLAMA.get();
				LootLlama llama = type.spawn((ServerLevel) level, itemstack, context.getPlayer(), pos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, pos) && direction == Direction.UP);
				if (llama != null) {
					if (itemstack.has(LlamaDataComponents.LOOT_TABLE)) {
						llama.setLootTable(itemstack.get(LlamaDataComponents.LOOT_TABLE));
					}
					itemstack.shrink(1);
					level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
				}
			}

			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		if (stack.has(LlamaDataComponents.LOOT_TABLE)) {
			ResourceLocation location = stack.get(LlamaDataComponents.LOOT_TABLE);
			if (location != null) {
				tooltip.add(Component.literal("Table: ").withStyle(ChatFormatting.YELLOW)
						.append(Component.literal(location.toString()).withStyle(ChatFormatting.GOLD)));
			}
		}
	}
}