package com.mrbysco.llamapalooza.entity;

import com.mrbysco.llamapalooza.config.LLamaConfig;
import com.mrbysco.llamapalooza.entity.projectile.LlamaItemSpit;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LootLlama extends Llama {
	private static final EntityDataAccessor<Optional<ResourceLocation>> LOOT_ID = SynchedEntityData.defineId(LootLlama.class, LlamaSerializers.RESOURCE_LOCATION.get());
	private static final EntityDataAccessor<Integer> SPEED_ID = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> GAIN_ID = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> STRENGTH_ID = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);
	private int spitTimer = -1;

	public LootLlama(EntityType<? extends Llama> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(LOOT_ID, Optional.empty());
		this.entityData.define(SPEED_ID, 0);
		this.entityData.define(GAIN_ID, 0);
		this.entityData.define(STRENGTH_ID, 0);
	}

	public void setLootTable(@Nullable ResourceLocation lootTable) {
		if (lootTable != null)
			this.entityData.set(LOOT_ID, Optional.of(lootTable));
		else
			this.entityData.set(LOOT_ID, Optional.empty());
	}

	public ResourceLocation getLootID() {
		return this.entityData.get(LOOT_ID).orElse(null);
	}

	public void setLootSpeed(int growth) {
		this.entityData.set(SPEED_ID, Mth.clamp(growth, 0, getMaxLootSpeed()));
	}

	public int getMaxLootSpeed() {
		return 9;
	}

	public int getLootSpeed() {
		return this.entityData.get(SPEED_ID);
	}

	public void setLootGain(int gain) {
		this.entityData.set(GAIN_ID, Mth.clamp(gain, 0, getMaxLootGain()));
	}

	public int getMaxLootGain() {
		return 9;
	}

	public int getLootGain() {
		return this.entityData.get(GAIN_ID);
	}

	public void setLootStrength(int strength) {
		this.entityData.set(STRENGTH_ID, Mth.clamp(strength, 0, getMaxLootStrength()));
	}

	public int getMaxLootStrength() {
		return 9;
	}

	public int getLootStrength() {
		return this.entityData.get(STRENGTH_ID);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getLootID() != null) {
			tag.putString("LootTable", this.getLootID().toString());
		}
		tag.putInt("LootSpeed", this.getLootSpeed());
		tag.putInt("LootGain", this.getLootGain());
		tag.putInt("LootStrength", this.getLootStrength());

		tag.putInt("SpitTimer", this.spitTimer);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("LootTable", 8)) {
			this.setLootTable(new ResourceLocation(tag.getString("LootTable")));
		}
		this.setLootSpeed(tag.getInt("LootSpeed"));
		this.setLootGain(tag.getInt("LootGain"));
		this.setLootStrength(tag.getInt("LootStrength"));

		this.spitTimer = tag.getInt("SpitTimer");
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (this.spitTimer == -1) {
			this.spitTimer = this.getSpitCooldown();
		}
		if (this.spitTimer > 0) {
			--this.spitTimer;
		} else {
			Player player = this.level().getNearestPlayer(this, 16.0D);
			if (player != null && this.hasLineOfSight(player)) {
				double d0 = player.getX() - this.getX();
				double d1 = player.getY(0.3333333333333333) - (this.getEyeY() - 0.1F);
				double d2 = player.getZ() - this.getZ();
				double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2F;
				this.spitItem(new Vec3(d0, d1 + d3, d2), 1.0F);
			} else {
				this.spitItem(new Vec3(0, 1, 0), 0.5F);
			}
			this.spitTimer = this.getSpitCooldown();
		}
	}

	@Override
	protected int getInventorySize() {
		return 2;
	}

	@Nullable
	@Override
	public Llama getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		LootLlama llama = LLamaRegistry.LOOT_LLAMA.get().create(level);
		if (llama != null && otherParent instanceof LootLlama otherLlama) {
			this.setOffspringAttributes(otherLlama, llama);
			llama.setVariant(this.random.nextBoolean() ? this.getVariant() : otherLlama.getVariant());

			//Average the stats of the parents
			int currentSpeed = Mth.ceil((this.getLootSpeed() + otherLlama.getLootSpeed()) / 2.0);
			int currentGain = Mth.ceil((this.getLootGain() + otherLlama.getLootGain()) / 2.0);
			int currentStrength = Mth.ceil((this.getLootStrength() + otherLlama.getLootStrength()) / 2.0);
			//Have a chance of increasing a stat by 1
			if (currentSpeed < getMaxLootSpeed() && this.random.nextInt(10) == 0) {
				currentSpeed = Math.min(currentSpeed + 1, getMaxLootSpeed());
			}
			if (currentSpeed < getMaxLootGain() && this.random.nextInt(10) == 0) {
				currentGain = Math.min(currentGain + 1, getMaxLootGain());
			}
			if (currentSpeed < getMaxLootStrength() && this.random.nextInt(10) == 0) {
				currentStrength = Math.min(currentStrength + 1, getMaxLootStrength());
			}
			//Set the stats to the offspring
			llama.setLootSpeed(currentSpeed);
			llama.setLootGain(currentGain);
			llama.setLootStrength(currentStrength);
		}

		return llama;
	}

	@Override
	protected void setOffspringAttributes(AgeableMob parent, AbstractHorse child) {
		super.setOffspringAttributes(parent, child);
		if (child instanceof LootLlama llama && parent instanceof LootLlama parentLlama) {
			llama.setLootTable(parentLlama.getLootID());
		}
	}

	public int getSpitCooldown() {
		return Math.max(1, LLamaConfig.COMMON.spitInterval.get() - (this.getLootSpeed() * LLamaConfig.COMMON.speedReduction.get()));
	}

	private List<ItemStack> generateLoot() {
		if (this.getLootID() != null && !this.level().isClientSide) {
			ServerLevel serverLevel = (ServerLevel) this.level();
			List<ItemStack> stacks = new ArrayList<>();
			LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(this.getLootID());
			LootParams.Builder builder = (new LootParams.Builder(serverLevel))
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.withParameter(LootContextParams.ORIGIN, this.position());
			for (int i = 0; i < (getLootGain() + 1); i++) {
				List<ItemStack> generatedLoot = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
				if (!generatedLoot.isEmpty()) {
					Collections.shuffle(generatedLoot);
					stacks.add(generatedLoot.get(0));
				}
			}
			System.out.println(stacks);
			return stacks;
		}
		return new ArrayList<>();
	}

	private void spitItem(Vec3 targetPos, float velocity) {
		List<ItemStack> loot = generateLoot();
		if (loot.isEmpty()) {
			return;
		}
		LlamaItemSpit itemSpit = new LlamaItemSpit(this.level(), this);
		itemSpit.setItems(loot);

		itemSpit.shoot(targetPos.x, targetPos.y, targetPos.z, velocity, 2.0F);
		if (!this.isSilent()) {
			this.level()
					.playSound(
							null,
							this.getX(),
							this.getY(),
							this.getZ(),
							SoundEvents.LLAMA_SPIT,
							this.getSoundSource(),
							1.0F,
							1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
					);
		}

		this.level().addFreshEntity(itemSpit);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
	                                    MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
		return super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, groupData, tag);
	}
}
