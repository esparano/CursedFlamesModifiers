package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ModifiersMod;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	// These mixins apply the enchantment effects of any modifiers on items
	@Inject(
			method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;)V",
			at = @At("HEAD")
	)
	private static void onRunIterationOnItem(ItemStack pStack, EnchantmentHelper.EnchantmentVisitor pVisitor, CallbackInfo ci) {
		var itemModifier = pStack.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
		if (itemModifier == null || itemModifier.modifier().isEmpty()) return;
		var enchantment = itemModifier.modifier().get().value().getInternalEnchantment();
		pVisitor.accept(Holder.direct(enchantment), 1);
	}

	@Inject(
			method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentInSlotVisitor;)V",
			at = @At("HEAD")
	)
	private static void onRunIterationOnItemInSlot(ItemStack pStack, EquipmentSlot pSlot, LivingEntity pEntity, EnchantmentHelper.EnchantmentInSlotVisitor pVisitor, CallbackInfo ci) {
		var enchantediteminuse = new EnchantedItemInUse(pStack, pSlot, pEntity);
		var itemModifier = pStack.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
		if (itemModifier == null || itemModifier.modifier().isEmpty()) return;
		var modifier = itemModifier.modifier().get().value();
		if (modifier.matchingSlot(pSlot)) {
			var enchantment = modifier.getInternalEnchantment();
			pVisitor.accept(Holder.direct(enchantment), 1, enchantediteminuse);
		}
	}
}
