package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ModifiersMod;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {
	public MixinAnvilMenu() {
		super(null, 0, null, null);
	}

	@Shadow @Final private DataSlot cost;
	@Shadow private int repairItemCountCost;

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	private void onCreateResult(CallbackInfo ci) {
		var stack1 = this.inputSlots.getItem(0);
		var stack2 = this.inputSlots.getItem(1);
		var outputStack = stack1.copy();
		if (stack2.getItem() == ModifiersMod.MODIFIER_BOOK) {
			var itemModifier = stack2.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
			if (itemModifier.modifier().isPresent()) {
				var modifier = itemModifier.modifier().get().value();
				if (modifier.definition().supportedItems().contains(stack1.getItemHolder())) {
					outputStack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, itemModifier);
					this.resultSlots.setItem(0, outputStack);
					this.repairItemCountCost = 1;
					this.cost.set(1);
					this.broadcastChanges();
					ci.cancel();
				}
			} // TODO allow for removing modifier using "no modifier" book
		}
	}
}
