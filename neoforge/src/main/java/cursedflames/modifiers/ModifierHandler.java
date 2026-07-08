package cursedflames.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ModifierHandler {
  public static final String tagName = "itemModifier";
  public static final String rerollTagName = "rollModifier";
  public static final String bookTagName = "bookModifier";

  public static void removeModifier(ItemStack outputStack) {
    outputStack.remove(ModifiersMod.ITEM_MODIFIER_COMPONENT);
  }

  public static void setModifier(ItemStack outputStack, ItemModifier itemModifier) {
    outputStack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, itemModifier);
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

