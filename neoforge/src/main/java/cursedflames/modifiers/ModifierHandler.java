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

  public static void removeModifier(ItemStack itemStack) {
    itemStack.remove(ModifiersMod.ITEM_MODIFIER_COMPONENT);
  }

  public static void setModifier(ItemStack itemStack, ItemModifier itemModifier) {
    // first, always remove existing modifiers
    removeModifier(itemStack);

    // If there is no modifier to set, don't.
    if (itemModifier.modifier().isEmpty()) return;

    // set custom modifier component
    itemStack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, itemModifier);
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
