package cursedflames.modifiers;

import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModEventHandler {

  @SubscribeEvent
  public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
    ItemStack stack = event.getItemStack();


    if (stack.has(ModifiersMod.ITEM_MODIFIER_COMPONENT)) {
      // --- ONE-TIME LEGACY MIGRATION ---
      // If the item has the old hardcoded "minecraft:attribute_modifiers" containing our mod's identifiers,
      // strip out all minecraft:attribute_modifiers so that the new dynamic event system can take over permanently.
      if (stack.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
        var attrComponent = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (attrComponent != null) {
          boolean hasOurOldModifiers = attrComponent.modifiers().stream()
                                         .anyMatch(entry -> entry.modifier().id().getNamespace().equals(Constants.MOD_ID));
          if (hasOurOldModifiers) {
            // "remove" left a weird modifier like "!minecraft:attribute_modifiers:{}"
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
          }
        }
      }
      // ---------------------------------

      var itemModifier = stack.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
      if (itemModifier != null && itemModifier.modifier().isPresent()) {

        // --- DYNAMIC MODIFIER INJECTION ---
        EquipmentSlot realEquipmentSlot = null;
        // If we don't do this, wearing multiple pieces of armor with the same modifier will not stack.
        if (stack.getItem() instanceof ArmorItem) {
          realEquipmentSlot = ((ArmorItem) stack.getItem()).getType().getSlot();
        }

        for (var component : itemModifier.modifier().get().value().effects()) {
          if (component.type() == DataComponents.ATTRIBUTE_MODIFIERS) {
            ItemAttributeModifiers modifierEffects = (ItemAttributeModifiers) component.value();

            for (ItemAttributeModifiers.Entry entry : modifierEffects.modifiers()) {
              String uniqueId = entry.modifier().id().getPath();
              // Generate a unique ID per slot to ensure that modifiers from multiple armor pieces stack
              if (realEquipmentSlot != null) {
                uniqueId += "." + realEquipmentSlot.toString().toLowerCase();
              }

              AttributeModifier uniqueModifier = new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, uniqueId),
                entry.modifier().amount(),
                entry.modifier().operation()
              );

              event.addModifier(entry.attribute(), uniqueModifier, entry.slot());
            }
          }
        }
      }
    }
  }
}