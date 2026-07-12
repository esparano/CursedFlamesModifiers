package cursedflames.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Optional;

public class ModifierHandler {
  public static final String tagName = "itemModifier";
  public static final String rerollTagName = "rollModifier";
  public static final String bookTagName = "bookModifier";

  // remove the "mod" modifier / attribute, but ALSO remove the base Minecraft attribute that matches the mod attribute
  public static void removeModifier(ItemStack itemStack) {
    itemStack.remove(ModifiersMod.ITEM_MODIFIER_COMPONENT);

    // Also remove any matching "base minecraft" item attributes which were added
    // Copy all other modifiers ATTRIBUTE_MODIFIERS to make sure we don't delete them by accident.
    ItemAttributeModifiers currentModifiers = itemStack.getAttributeModifiers();
    ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
    // Add existing modifiers to the builder by iterating their entries
    for (ItemAttributeModifiers.Entry entry : currentModifiers.modifiers()) {

      // Only copy these custom modifiers if they are NOT from this mod
      if (!entry.modifier().id().getNamespace().equals(Constants.MOD_ID)) {
        builder.add(entry.attribute(), entry.modifier(), entry.slot());
      }
    }

    // 5. Apply the final set of attributes stripped of "modifier mod" attributes
    itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
  }

  public static void setModifier(ItemStack itemStack, ItemModifier itemModifier) {
    // first, always remove existing modifiers
    removeModifier(itemStack);

    // If there is no modifier to set, don't.
    if (itemModifier.modifier().isEmpty()) return;

    // set custom modifier component
    itemStack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, itemModifier);

    // Copy all other modifiers ATTRIBUTE_MODIFIERS to make sure we don't overwrite them by accident.
    ItemAttributeModifiers currentModifiers = itemStack.getAttributeModifiers();
    ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
    // Add existing modifiers to the builder by iterating their entries
    for (ItemAttributeModifiers.Entry entry : currentModifiers.modifiers()) {
      builder.add(entry.attribute(), entry.modifier(), entry.slot());
    }

    EquipmentSlot realEquipmentSlot = null;
    if (itemStack.getItem() instanceof ArmorItem) {
      realEquipmentSlot = ((ArmorItem) itemStack.getItem()).getType().getSlot();
    }

    for (TypedDataComponent<?> component : itemModifier.modifier().get().value().effects()) {
      if (component.type() == DataComponents.ATTRIBUTE_MODIFIERS) {
        ItemAttributeModifiers newMods = (ItemAttributeModifiers) component.value();
        for (ItemAttributeModifiers.Entry entry : newMods.modifiers()) {
          String uniqueId = entry.modifier().id().getPath();
          // Generate a unique ID per slot to ensure that modifiers from multiple armor pieces stack
          if (realEquipmentSlot != null) {
            uniqueId += "." + realEquipmentSlot.toString().toLowerCase();
          }
          // Use the builder to add the modifier with the new, unique ID
          builder.add(
            entry.attribute(),
            new AttributeModifier(
              ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, uniqueId),
              entry.modifier().amount(),
              entry.modifier().operation()
            ),
            entry.slot()
          );
        }
      }
      // This probably isn't needed... The modifier's "effects" should always be attribute modifiers
//      else {
//        modifiers$applyComponent(itemStack, (TypedDataComponent) component);
//      }
    }

    // 5. Apply the final, merged set
    itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
  }

  @SuppressWarnings("resource")
  public static Optional<Holder<Modifier>> rollModifier(ItemStack itemStack, Level level) {
    RegistryAccess registryAccess = level.registryAccess();
    var registry = registryAccess.registry(ModifiersMod.MODIFIER_REGISTRY_KEY);
    if (registry.isEmpty()) return Optional.empty();

    ModifierPool pool = new ModifierPool();
    // Iterate over registry holders
    registry.get().holders().forEach(holder -> {
      if (holder.value().definition().supportedItems().contains(itemStack.getItemHolder())) {
        pool.add(holder);
      }
    });

    return Optional.ofNullable(pool.roll(level.getRandom()));
  }


}
