package com.mrbysco.llamapalooza.entity;

import com.mrbysco.llamapalooza.config.LLamaConfig;
import com.mrbysco.llamapalooza.entity.projectile.LlamaItemSpit;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaSerializers;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
	private static final EntityDataAccessor<Optional<Identifier>> LOOT_ID = SynchedEntityData.defineId(LootLlama.class, LlamaSerializers.RESOURCE_LOCATION.get());
	private static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> SPEED_ID = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> GAIN_ID = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> STRENGTH_ID = SynchedEntityData.defineId(LootLlama.class, EntityDataSerializers.INT);

	public LootLlama(EntityType<? extends Llama> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(LOOT_ID, Optional.empty());
		builder.define(SPEED_ID, 0);
		builder.define(GAIN_ID, 0);
		builder.define(STRENGTH_ID, 0);
		builder.define(TIMER, 0);
	}

	public void setLootTable(@Nullable Identifier lootTable) {
		if (lootTable != null)
			this.entityData.set(LOOT_ID, Optional.of(lootTable));
		else
			this.entityData.set(LOOT_ID, Optional.empty());
	}

	public Identifier getLootID() {
		return this.entityData.get(LOOT_ID).orElse(null);
	}

	public ResourceKey<LootTable> getLootKey() {
		Identifier id = getLootID();
		if (id == null) return null;
		return ResourceKey.create(Registries.LOOT_TABLE, id);
	}

	public void setLootSpeed(int growth) {
		this.entityData.set(SPEED_ID, Mth.clamp(growth, 0, getMaxLootSpeed()));
	}

	public int getMaxLootSpeed() {
		return LLamaConfig.COMMON.maxSpeed.get();
	}

	public int getLootSpeed() {
		return this.entityData.get(SPEED_ID);
	}

	public void setLootGain(int gain) {
		this.entityData.set(GAIN_ID, Mth.clamp(gain, 0, getMaxLootGain()));
	}

	public int getMaxLootGain() {
		return LLamaConfig.COMMON.maxGain.get();
	}

	public int getLootGain() {
		return this.entityData.get(GAIN_ID);
	}

	public void setLootStrength(int strength) {
		this.entityData.set(STRENGTH_ID, Mth.clamp(strength, 0, getMaxLootStrength()));
	}

	public int getMaxLootStrength() {
		return LLamaConfig.COMMON.maxStrength.get();
	}

	public void setTimer(int count) {
		this.entityData.set(TIMER, count);
	}

	public int getTimer() {
		return this.entityData.get(TIMER);
	}

	public int getLootStrength() {
		return this.entityData.get(STRENGTH_ID);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);

		if (this.getLootID() != null) {
			output.store("LootTable", Identifier.CODEC, this.getLootID());
		}
		output.putInt("LootSpeed", this.getLootSpeed());
		output.putInt("LootGain", this.getLootGain());
		output.putInt("LootStrength", this.getLootStrength());

		output.putInt("SpitTimer", getTimer());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);

		Optional<Identifier> optionalLootTable = input.read("LootTable", Identifier.CODEC);
		optionalLootTable.ifPresent(this::setLootTable);

		this.setLootSpeed(input.getIntOr("LootSpeed", 0));
		this.setLootGain(input.getIntOr("LootGain", 0));
		this.setLootStrength(input.getIntOr("LootStrength", 0));

		this.setTimer(input.getIntOr("SpitTimer", 0));
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.isAlive()) {

		}
	}

	@Override
	protected void customServerAiStep(ServerLevel serverLevel) {
		super.customServerAiStep(serverLevel);
		if (this.getTimer() == -1) {
			this.setTimer(this.getSpitCooldown());
		} else if (this.getTimer() > 0) {
			this.setTimer(this.getTimer() - 1);
		} else {
			Player player = this.level().getNearestPlayer(this, 16.0D);
			if (player != null && this.hasLineOfSight(player)) {
				double d0 = player.getX() - this.getX();
				double d1 = player.getY(0.3333333333333333) - (this.getEyeY() - 0.1F);
				double d2 = player.getZ() - this.getZ();
				double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2F;
				Vec3 targetPos = new Vec3(d0, d1 + d3, d2);
				getLookControl().setLookAt(player);
				lookAt(Anchor.EYES, targetPos);
				this.spitItem(targetPos, 1.0F);
			} else {
				if (LLamaConfig.COMMON.alwaysSpit.get()) {
					this.spitItem(new Vec3(0, 1, 0), 0.5F);
				}
			}
			this.setTimer(this.getSpitCooldown());
		}
	}

	@Override
	protected void createInventory() {
		SimpleContainer old = this.inventory;
		this.inventory = new SimpleContainer(2);
		if (old != null) {
			int i = Math.min(old.getContainerSize(), this.inventory.getContainerSize());

			for (int j = 0; j < i; j++) {
				ItemStack itemstack = old.getItem(j);
				if (!itemstack.isEmpty()) {
					this.inventory.setItem(j, itemstack.copy());
				}
			}
		}
	}

	@Nullable
	@Override
	public Llama getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		LootLlama llama = LLamaRegistry.LOOT_LLAMA.get().create(level, EntitySpawnReason.BREEDING);
		if (llama != null && otherParent instanceof LootLlama otherLlama) {
			this.setOffspringAttributes(otherLlama, llama);
			llama.setVariant(this.random.nextBoolean() ? this.getVariant() : otherLlama.getVariant());

			//Average the stats of the parents
			int currentSpeed = Mth.ceil((this.getLootSpeed() + otherLlama.getLootSpeed()) / 2.0);
			int currentGain = Mth.ceil((this.getLootGain() + otherLlama.getLootGain()) / 2.0);
			int currentStrength = Mth.ceil((this.getLootStrength() + otherLlama.getLootStrength()) / 2.0);
			//Have a chance of increasing a stat by 1
			if (currentSpeed < getMaxLootSpeed() && this.random.nextInt(getMaxLootSpeed()) == 0) {
				currentSpeed = Math.min(currentSpeed + 1, getMaxLootSpeed());
			}
			if (currentSpeed < getMaxLootGain() && this.random.nextInt(getMaxLootGain()) == 0) {
				currentGain = Math.min(currentGain + 1, getMaxLootGain());
			}
			if (currentSpeed < getMaxLootStrength() && this.random.nextInt(getMaxLootStrength()) == 0) {
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
		if (this.getLootID() != null && !this.level().isClientSide()) {
			ServerLevel serverLevel = (ServerLevel) this.level();
			List<ItemStack> stacks = new ArrayList<>();
			LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(this.getLootKey());
			LootParams.Builder builder = (new LootParams.Builder(serverLevel))
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.withParameter(LootContextParams.ORIGIN, this.position())
					.withLuck(getStrength());
			for (int i = 0; i < (getLootGain() + 1); i++) {
				List<ItemStack> generatedLoot = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
				if (!generatedLoot.isEmpty()) {
					Collections.shuffle(generatedLoot);
					stacks.add(generatedLoot.getFirst());
				}
			}
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

		getLookControl().setLookAt(targetPos);
		lookAt(Anchor.EYES, targetPos);
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

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @org.jspecify.annotations.Nullable SpawnGroupData groupData) {
		groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);

		setTimer(this.getSpitCooldown());

		return groupData;
	}
}
