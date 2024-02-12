package com.mrbysco.llamapalooza.entity.projectile;

import com.mrbysco.llamapalooza.entity.LootLlama;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaSerializers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LlamaItemSpit extends Projectile implements ItemSupplier {
	private static final EntityDataAccessor<List<ItemStack>> DATA_ITEM_STACK = SynchedEntityData.defineId(
			LlamaItemSpit.class, LlamaSerializers.ITEM_STACKS.get()
	);

	public LlamaItemSpit(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	public LlamaItemSpit(Level level, LootLlama llama) {
		this(LLamaRegistry.ITEM_SPIT.get(), level);
		this.setOwner(llama);
		this.setPos(
				llama.getX() - (double) (llama.getBbWidth() + 1.0F) * 0.5 * (double) Mth.sin(llama.yBodyRot * (float) (Math.PI / 180.0)),
				llama.getEyeY() - 0.1F,
				llama.getZ() + (double) (llama.getBbWidth() + 1.0F) * 0.5 * (double) Mth.cos(llama.yBodyRot * (float) (Math.PI / 180.0))
		);
	}

	public void setItems(List<ItemStack> stacks) {
		if (!stacks.isEmpty()) {
			this.getEntityData().set(DATA_ITEM_STACK, stacks);
		}
	}

	public List<ItemStack> getItems() {
		return this.getEntityData().get(DATA_ITEM_STACK);
	}

	@Override
	public ItemStack getItem() {
		return getItems().isEmpty() ? new ItemStack(Items.AIR) : getItems().get(0);
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(DATA_ITEM_STACK, new ArrayList<>());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ItemCount", this.getItems().size());
		if (!this.getItems().isEmpty()) {
			for (int i = 0; i < this.getItems().size(); i++) {
				tag.put("Item" + i, this.getItems().get(i).save(new CompoundTag()));
			}
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		int count = tag.getInt("ItemCount");
		if (count > 0) {
			List<ItemStack> stacks = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				stacks.add(ItemStack.of(tag.getCompound("Item" + i)));
			}
			this.setItems(stacks);
		}
	}


	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		super.tick();
		Vec3 vec3 = this.getDeltaMovement();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult))
			this.onHit(hitresult);
		double d0 = this.getX() + vec3.x;
		double d1 = this.getY() + vec3.y;
		double d2 = this.getZ() + vec3.z;
		this.updateRotation();
		float f = 0.99F;
		float f1 = 0.06F;
		if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
			this.discard();
		} else if (this.isInWaterOrBubble()) {
			this.discard();
		} else {
			this.setDeltaMovement(vec3.scale(0.99F));
			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.06F, 0.0));
			}

			this.setPos(d0, d1, d2);
		}
	}

	/**
	 * Called when the arrow hits an entity
	 */
	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity entity = this.getOwner();
		if (entity instanceof LivingEntity livingentity) {
			this.spawnItems();
			if (!(livingentity instanceof LootLlama)) {
				result.getEntity().hurt(this.damageSources().mobProjectile(this, livingentity), 1.0F);
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level().isClientSide) {
			this.spawnItems();
			this.discard();
		}
	}

	public void spawnItems() {
		List<ItemStack> stacks = this.getItems();
		if (!stacks.isEmpty()) {
			for (ItemStack stack : stacks) {
				this.spawnAtLocation(stack, 0.5F);
			}
		}
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
		super.recreateFromPacket(pPacket);
		double d0 = pPacket.getXa();
		double d1 = pPacket.getYa();
		double d2 = pPacket.getZa();

		for (int i = 0; i < 7; ++i) {
			double d3 = 0.4 + 0.1 * (double) i;
			this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
		}

		this.setDeltaMovement(d0, d1, d2);
	}
}
