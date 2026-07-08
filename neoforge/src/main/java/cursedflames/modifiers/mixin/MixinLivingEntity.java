package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ItemModifier;
import cursedflames.modifiers.ModifierHandler;
import cursedflames.modifiers.ModifiersMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
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

    // 1. Remove old modifier using the Data Component access
    ItemModifier fromData = from.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
    if (fromData != null && fromData.modifier().isPresent()) {
      ModifierHandler.removeModifier(from);
    }

    // 2. Apply/Roll new modifier
    ItemModifier toData = to.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);

    if (toData == null || toData.modifier().isEmpty()) {
      var rolled = ModifierHandler.rollModifier(to, this.level());
      if (rolled.isPresent()) {
        ModifierHandler.setModifier(to, new ItemModifier(rolled, true));
        toData = to.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
      }
    }

    if (toData != null && toData.modifier().isPresent()) {
      ModifierHandler.setModifier(to, toData);
    }
  }
}