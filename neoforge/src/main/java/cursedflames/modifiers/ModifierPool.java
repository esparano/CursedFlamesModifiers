package cursedflames.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModifierPool {
  //  public Predicate<ItemStack> isApplicable;
  public int totalWeight = 0;
  //  // Pool weight, for choosing which pool to use when multiple are applicable.
//  public int poolWeight = 0;
  public List<Holder<Modifier>> modifiers = new ArrayList<>();

  public ModifierPool() {
//    this.isApplicable = isApplicable;
  }

//  public ModifierPool(Predicate<ItemStack> isApplicable) {
//    this.isApplicable = isApplicable;
//  }

  public void add(Holder.Reference<Modifier> mod) {
    modifiers.add(mod);
    totalWeight += mod.value().definition().weight();
  }

  public Holder<Modifier> roll(RandomSource random) {
    if (totalWeight == 0 || modifiers.isEmpty()) return null;
    int i = random.nextInt(totalWeight);
    int j = 0;
    for (Holder<Modifier> modifier : modifiers) {
      j += modifier.value().definition().weight();
      if (i < j) {
        return modifier;
      }
    }
    // This shouldn't happen
    return null;
  }
}
