package cursedflames.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Optional;
import java.util.function.Consumer;

public record ItemModifier(Optional<Holder<Modifier>> modifier, boolean showInTooltip) implements TooltipProvider {
//  public static final Codec<ItemModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//    Modifier.CODEC.optionalFieldOf("modifier").forGetter(ItemModifier::modifier),
//    Codec.BOOL.optionalFieldOf("showInTooltip", true).forGetter(ItemModifier::showInTooltip)
//  ).apply(instance, ItemModifier::new));

//  public static final Codec<ItemModifier> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
//    // 1. Lazy init makes this safe to define at startup
//    // 2. optionalFieldOf makes this safe to save/load in JSON
//    Modifier.CODEC.optionalFieldOf("modifier").forGetter(ItemModifier::modifier),
//    Codec.BOOL.optionalFieldOf("showInTooltip", true).forGetter(ItemModifier::showInTooltip)
//  ).apply(instance, ItemModifier::new)));

//  public static final Codec<ItemModifier> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
//    // Pass the registry key directly to create a more resilient codec
//    ExtraCodecs.holderByName(ModifiersMod.MODIFIER_REGISTRY_KEY, Modifier.DIRECT_CODEC)
//      .optionalFieldOf("modifier")
//      .forGetter(ItemModifier::modifier),
//    Codec.BOOL.optionalFieldOf("showInTooltip", true)
//      .forGetter(ItemModifier::showInTooltip)
//  ).apply(instance, ItemModifier::new)));

  // Keep this for JSON/NBT logic, but make it lazy
  public static final Codec<ItemModifier> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
    Modifier.CODEC.optionalFieldOf("modifier").forGetter(ItemModifier::modifier),
    Codec.BOOL.optionalFieldOf("showInTooltip", true).forGetter(ItemModifier::showInTooltip)
  ).apply(instance, ItemModifier::new)));

  public static final StreamCodec<RegistryFriendlyByteBuf, ItemModifier> STREAM_CODEC = StreamCodec.composite(
    // Use the registry's holder codec to sync the reference properly
    ByteBufCodecs.holderRegistry(ModifiersMod.MODIFIER_REGISTRY_KEY).apply(ByteBufCodecs::optional),
    ItemModifier::modifier,
    ByteBufCodecs.BOOL,
    ItemModifier::showInTooltip,
    ItemModifier::new
  );

  @Override
  public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
    tooltipAdder.accept(this.modifier.isPresent() ? this.modifier.get().value().name() : Component.translatable("modifier.modifiers.none.info"));
  }
}
