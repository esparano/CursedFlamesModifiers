package cursedflames.modifiers.item;

import cursedflames.modifiers.ItemModifier;
import cursedflames.modifiers.Modifier;
import cursedflames.modifiers.ModifiersMod;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemReforgeTemplate extends Item {
	public ItemReforgeTemplate() {
		super(new Properties().rarity(Rarity.RARE));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		// Should be false for this item.
		return super.isFoil(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context,
								List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable(this.getDescriptionId()));
	}

	public static ItemStack create() {
		return new ItemStack(ModifiersMod.REFORGE_TEMPLATE);
	}
}
