package cursedflames.modifiers.item;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.ItemModifier;
import cursedflames.modifiers.Modifier;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemModifierBook extends Item {
	public ItemModifierBook() {
		super(new Properties().rarity(Rarity.EPIC));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		var component = stack.get(ModifiersMod.ITEM_MODIFIER_COMPONENT);
		return super.isFoil(stack) || component != null && component.modifier().isPresent();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context,
								List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable(this.getDescriptionId()+".tooltip.0"));
			tooltip.add(Component.translatable(this.getDescriptionId()+".tooltip.1"));
	}

	public static ItemStack createForModifier(@Nullable Holder<Modifier> modifier) {
		ItemStack itemstack = new ItemStack(ModifiersMod.MODIFIER_BOOK);
		itemstack.set(ModifiersMod.ITEM_MODIFIER_COMPONENT, new ItemModifier(Optional.ofNullable(modifier), true));
		return itemstack;
	}
}
