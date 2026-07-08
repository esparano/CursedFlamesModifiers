package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ItemModifier;
import cursedflames.modifiers.Modifier;
import cursedflames.modifiers.ModifierHandler;
import cursedflames.modifiers.ModifiersMod;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(value = LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
  private MixinLivingEntity(EntityType<?> entityTypeIn, Level worldIn) {
    super(entityTypeIn, worldIn);
  }

  @Inject(method = "collectEquipmentChanges",
    at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
    locals = LocalCapture.CAPTURE_FAILHARD)
  private void onCollectEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir,
                                         Map<EquipmentSlot, ItemStack> map,
                                         EquipmentSlot[] slots,
                                         int i,
                                         int j,
                                         EquipmentSlot equipmentSlot,
                                         ItemStack from,
                                         ItemStack to) { // Locals updated to match 1.21.1 method signature

    // Only roll if the item has no modifier and is a valid target
    if (!to.isEmpty() && !to.has(ModifiersMod.ITEM_MODIFIER_COMPONENT)) {
      var rolled = ModifierHandler.rollModifier(to, this.level());
      if (rolled.isPresent()) {
        Modifier modifier = rolled.get().value();

        // 1. Set the custom modifier component for your UI/Logic
        to.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, new ItemModifier(rolled, true));

        // 2. Set built in attributes to actually affect the item's stats
        for (TypedDataComponent<?> component : modifier.effects()) {
          modifiers$applyComponent(to, (TypedDataComponent) component);
        }
      }
    }
  }

  @Unique
  private <T> void modifiers$applyComponent(ItemStack stack, TypedDataComponent<T> component) {
    stack.set(component.type(), component.value());
  }
}
