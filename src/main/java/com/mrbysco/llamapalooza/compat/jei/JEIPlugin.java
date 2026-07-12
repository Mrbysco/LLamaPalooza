package com.mrbysco.llamapalooza.compat.jei;

import com.mrbysco.llamapalooza.LlamaPalooza;
import com.mrbysco.llamapalooza.registry.LLamaRegistry;
import com.mrbysco.llamapalooza.registry.LlamaDataComponents;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	private static final ResourceLocation UID = LlamaPalooza.modLoc("jei_plugin");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LLamaRegistry.LOOT_LLAMA_SPAWN_EGG.get(), new EggSubTypes());
	}

	private static class EggSubTypes implements ISubtypeInterpreter<ItemStack> {
		@Override
		@Nullable
		public Object getSubtypeData(ItemStack ingredient, UidContext context) {
			return ingredient.get(LlamaDataComponents.LOOT_TABLE);
		}

		@Override
		public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
			ResourceLocation lootTable = ingredient.get(LlamaDataComponents.LOOT_TABLE);
			if (lootTable == null) return "";
			return BuiltInRegistries.ITEM.getKey(ingredient.getItem()) + "@" + lootTable;
		}

	}
}
