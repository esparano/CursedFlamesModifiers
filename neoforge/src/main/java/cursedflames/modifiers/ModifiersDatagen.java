package cursedflames.modifiers;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

import java.util.List;
import java.util.Optional;

public class ModifiersDatagen {
	// Some of these aren't on the NeoForge Tags class? idk
	private static final TagKey<Item> MELEE_WEAPONS = tag("melee_weapons");
	private static final TagKey<Item> RANGED_WEAPONS = tag("ranged_weapons");

	private static TagKey<Item> tag(String name) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
	}

	public static void bootstrap(BootstrapContext<Modifier> bootstrap) {
		var items = bootstrap.lookup(Registries.ITEM);
		var armor = items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE);
		var melee = items.getOrThrow(MELEE_WEAPONS);
		var ranged = items.getOrThrow(RANGED_WEAPONS);

		register(bootstrap, "half_hearted", new Modifier.ModifierDefinition(armor, 30, 0, List.of(EquipmentSlotGroup.ARMOR)),
				DataComponentMap.builder().set(EnchantmentEffectComponents.ATTRIBUTES,
						List.of(new EnchantmentAttributeEffect(id("half_hearted"), Attributes.MAX_HEALTH, LevelBasedValue.constant(1), AttributeModifier.Operation.ADD_VALUE)))
						.build());
	}

	private static void register(BootstrapContext<Modifier> bootstrap, String name, Modifier.ModifierDefinition def, DataComponentMap effects) {
		bootstrap.register(key(name), modifier(name, def, effects));
	}

	private static Modifier modifier(String name, Modifier.ModifierDefinition def, DataComponentMap effects) {
		return new Modifier(component("modifier.modifiers." + name), Optional.empty(), def, effects);
	}

	private static Component component(String path) {
		return Component.translatable(path);
	}

	private static ResourceKey<Modifier> key(String name) {
		return ResourceKey.create(ModifiersMod.MODIFIER_REGISTRY_KEY, id(name));
	}


	private static ResourceLocation id(String name) {
		return ModifiersMod.resourceLocation(name);
	}
}
