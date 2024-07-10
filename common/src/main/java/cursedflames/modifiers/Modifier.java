package cursedflames.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Modifier {
	public record ModifierDefinition(HolderSet<Item> supportedItems, int weight, int quality, List<EquipmentSlotGroup> slots) {
		public static final Codec<ModifierDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supportedItems").forGetter(ModifierDefinition::supportedItems),
				ExtraCodecs.intRange(0, 1024).fieldOf("weight").forGetter(ModifierDefinition::weight), // TODO decide actual bounds
				ExtraCodecs.intRange(-512, 512).fieldOf("quality").forGetter(ModifierDefinition::quality), // TODO decide actual bounds
				EquipmentSlotGroup.CODEC.listOf().fieldOf("slots").forGetter(ModifierDefinition::slots)
		).apply(instance, ModifierDefinition::new));
	}

	public static final Codec<Holder<Modifier>> CODEC = RegistryFixedCodec.create(ModifiersMod.MODIFIER_REGISTRY_KEY);

	public static final Codec<Modifier> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ComponentSerialization.CODEC.fieldOf("name").forGetter(Modifier::name),
			ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(Modifier::description),
			ModifierDefinition.CODEC.fieldOf("definition").forGetter(Modifier::definition),
			EnchantmentEffectComponents.CODEC.optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(Modifier::effects)
	).apply(instance, Modifier::new));
	private final Component name;
	private final Optional<Component> description;
	private final ModifierDefinition definition;
	private final DataComponentMap effects;

	private final Enchantment internalEnchantment;

	public Modifier(Component name, Optional<Component> description, ModifierDefinition definition, DataComponentMap effects) {
		this.name = name;
		this.description = description;
		this.definition = definition;
		this.effects = effects;
		var enchantmentDef = new Enchantment.EnchantmentDefinition(definition.supportedItems, Optional.empty(), 1, 1, Enchantment.constantCost(1), Enchantment.constantCost(1), 1, definition.slots);
		this.internalEnchantment = new Enchantment(name, enchantmentDef, HolderSet.empty(), effects);
	}

	public Component name() {
		return name;
	}

	public Optional<Component> description() {
		return description;
	}

	public ModifierDefinition definition() {
		return definition;
	}

	public DataComponentMap effects() {
		return effects;
	}

	/** Used internally for applying Modifier effects; should never be touched otherwise */
	public Enchantment getInternalEnchantment() {
		return internalEnchantment;
	}

	public boolean matchingSlot(EquipmentSlot pSlot) {
		return this.definition.slots().stream().anyMatch((slotGroup) -> slotGroup.test(pSlot));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Modifier) obj;
		return Objects.equals(this.name, that.name) &&
				Objects.equals(this.description, that.description) &&
				Objects.equals(this.definition, that.definition) &&
				Objects.equals(this.effects, that.effects);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, definition, effects);
	}

	@Override
	public String toString() {
		return "Modifier[" +
				"name=" + name + ", " +
				"description=" + description + ", " +
				"definition=" + definition + ", " +
				"effects=" + effects + ']';
	}

}
