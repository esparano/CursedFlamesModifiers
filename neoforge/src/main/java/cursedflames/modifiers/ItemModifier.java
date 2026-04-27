package cursedflames.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Optional;
import java.util.function.Consumer;

public record ItemModifier(Optional<Holder<Modifier>> modifier, boolean showInTooltip) implements TooltipProvider {
	public static final Codec<ItemModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Modifier.CODEC.optionalFieldOf("modifier").forGetter(ItemModifier::modifier),
			Codec.BOOL.optionalFieldOf("showInTooltip", true).forGetter(ItemModifier::showInTooltip)
	).apply(instance, ItemModifier::new));

	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
		tooltipAdder.accept(this.modifier.isPresent() ? this.modifier.get().value().name() : Component.translatable("modifier.modifiers.none.info"));
	}
}
