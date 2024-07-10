package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ModifiersMod;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements DataComponentHolder {
	@Shadow
	public abstract <T extends TooltipProvider> void addToTooltip(
			DataComponentType<T> pComponent, Item.TooltipContext pContext, Consumer<Component> pTooltipAdder, TooltipFlag pTooltipFlag
	);

	// Add modifier to item name
	// slice from ITEM_NAME being accessed (which happens after the first return) so we don't add modifier to custom item names
	@ModifyReturnValue(method = "getHoverName", at = @At(value = "RETURN"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;ITEM_NAME:Lnet/minecraft/core/component/DataComponentType;")))
	private Component onGetHoverName(Component original) {
		var thingy = this.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
		if (thingy == null || thingy.modifier().isEmpty()) return original;
		return Component.translatable("modifiers.item_name_pattern", thingy.modifier().get().value().name(), original).withStyle(original.getStyle());
	}

	// Add modifier to tooltip directly after enchantments
	@Inject(method = "getTooltipLines", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 0, target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V"),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;ENCHANTMENTS:Lnet/minecraft/core/component/DataComponentType;"))
			)
	private void onGetTooltipLines(Item.TooltipContext pTooltipContext, Player pPlayer, TooltipFlag pTooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local Consumer<Component> consumer) {
		this.addToTooltip(ModifiersMod.ITEM_MODIFIER_COMPONENT, pTooltipContext, consumer, pTooltipFlag);
	}
}
