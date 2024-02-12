package com.mrbysco.llamapalooza.item;

import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class LootLlamaSpawnEgg extends ForgeSpawnEggItem {
	public LootLlamaSpawnEgg(final Properties properties) {
		super(LLamaRegistry.LOOT_LLAMA, 12623485, 10051392, properties);
	}

	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack handStack = context.getItemInHand();
			BlockPos clickedPos = context.getClickedPos();
			Direction clickedFace = context.getClickedFace();
			BlockState blockstate = level.getBlockState(clickedPos);
			CompoundTag tag = handStack.getTag() == null ? new CompoundTag() : handStack.getTag();
			if (blockstate.is(Blocks.SPAWNER)) {
				BlockEntity blockentity = level.getBlockEntity(clickedPos);
				if (blockentity instanceof SpawnerBlockEntity spawnerblockentity) {
					EntityType<LootLlama> type = LLamaRegistry.LOOT_LLAMA.get();
					spawnerblockentity.setEntityId(type, level.getRandom());
					blockentity.setChanged();
					level.sendBlockUpdated(clickedPos, blockstate, blockstate, 3);
					handStack.shrink(1);
					return InteractionResult.CONSUME;
				}
			} else {
				BlockPos pos;
				if (blockstate.getCollisionShape(level, clickedPos).isEmpty()) {
					pos = clickedPos;
				} else {
					pos = clickedPos.relative(clickedFace);
				}

				EntityType<LootLlama> type = LLamaRegistry.LOOT_LLAMA.get();
				LootLlama llama = type.spawn((ServerLevel) level, handStack, context.getPlayer(), pos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(clickedPos, pos) && clickedFace == Direction.UP);
				if (llama != null) {
					if (tag.contains("LootTable")) {
						llama.setLootTable(ResourceLocation.tryParse(tag.getString("LootTable")));
					}
					handStack.shrink(1);
				}
			}

			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		CompoundTag tag = stack.hasTag() ? stack.getTag() : new CompoundTag();
		if (tag != null && !tag.getString("LootTable").isEmpty()) {
			ResourceLocation location = ResourceLocation.tryParse(tag.getString("LootTable"));
			if (location != null) {
				tooltip.add(Component.literal("Table: ").withStyle(ChatFormatting.YELLOW)
						.append(Component.literal(location.toString()).withStyle(ChatFormatting.GOLD)));
			}
		}
	}
}