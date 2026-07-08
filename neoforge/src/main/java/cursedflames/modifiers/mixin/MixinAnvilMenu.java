package cursedflames.modifiers.mixin;

import cursedflames.modifiers.*;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {
  public MixinAnvilMenu() {
    super(null, 0, null, null);
  }

  @Shadow
  @Final
  private DataSlot cost;
  @Shadow
  private int repairItemCountCost;

  @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
  private void onCreateResult(CallbackInfo ci) {
    var stack1 = this.inputSlots.getItem(0);
    var stack2 = this.inputSlots.getItem(1);
    var outputStack = stack1.copy();
    if (stack2.getItem() == ModifiersMod.MODIFIER_BOOK) {
      var itemModifier = stack2.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
      if (itemModifier != null && itemModifier.modifier().isPresent()) {
        var modifier = itemModifier.modifier().get().value();
        if (modifier.definition().supportedItems().contains(stack1.getItemHolder())) {
          outputStack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, itemModifier);
          this.resultSlots.setItem(0, outputStack);
          this.repairItemCountCost = 1;
          this.cost.set(1);
          this.broadcastChanges();
          ci.cancel();
        }
      }
      // This is the "no modifier" modifier book
      else {
        outputStack.remove(ModifiersMod.ITEM_MODIFIER_COMPONENT);
        this.resultSlots.setItem(0, outputStack);
        this.repairItemCountCost = 1;
        this.cost.set(1);
        this.broadcastChanges();
        ci.cancel();
      }
    } else if (stack2.getItem() == ModifiersMod.REFORGE_TEMPLATE) {
      // OLD CODE rolls a new modifier, but the problem is that the user can see the rolled modifier before accepting!
//      var modifier = rollModifierForItemStack(stack1);
//      if (modifier.isPresent()) {
//        outputStack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, new ItemModifier(modifier, true));
//        this.resultSlots.setItem(0, outputStack);
//        this.repairItemCountCost = 1;
//        this.cost.set(1);
//        this.broadcastChanges();
//        ci.cancel();
      // Instead, we simply remove any modifier if a reforging template is applied.
      outputStack.remove(ModifiersMod.ITEM_MODIFIER_COMPONENT);
      this.resultSlots.setItem(0, outputStack);
      this.repairItemCountCost = 1;
      this.cost.set(1);
      this.broadcastChanges();
      ci.cancel();
    }
  }

  @Unique
  @SuppressWarnings("resource")
  public Optional<Holder<Modifier>> rollModifierForItemStack(ItemStack itemStack) {
    RegistryAccess registryAccess = player.level().registryAccess();
    var registry = registryAccess.registry(ModifiersMod.MODIFIER_REGISTRY_KEY);
    if (registry.isEmpty()) return Optional.empty();

    ModifierPool pool = new ModifierPool();

    // Iterate over registry holders
    registry.get().holders().forEach(holder -> {
      if (holder.value().definition().supportedItems().contains(itemStack.getItemHolder())) {
        pool.add(holder);
      }
    });

    return Optional.ofNullable(pool.roll(player.getRandom()));
  }
}
