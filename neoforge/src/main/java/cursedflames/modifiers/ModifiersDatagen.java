package cursedflames.modifiers;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Optional;

public class ModifiersDatagen {
  // Some of these aren't on the NeoForge Tags class? idk
//  private static final TagKey<Item> MELEE_WEAPONS = tag("melee_weapons");
//  private static final TagKey<Item> RANGED_WEAPONS = tag("ranged_weapons");


  // Example tag:       c:tools/melee_weapons
  private static final TagKey<Item> MELEE_WEAPONS = tag("tools/melee_weapons");
  private static final TagKey<Item> RANGED_WEAPONS = tag("tools/ranged_weapons");
  private static final TagKey<Item> BOWS = tag("tools/bows");
  // Includes ALL tools, melee weapons, and ranged weapons. (c:tools)
  private static final TagKey<Item> TOOLS = tag("tools");

  private static TagKey<Item> tag(String name) {
    return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
  }

  public static void bootstrap(BootstrapContext<Modifier> bootstrap) {
    var items = bootstrap.lookup(Registries.ITEM);

    var armorItems = items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE);
    registerArmorModifiers(bootstrap, armorItems);

    //    var meleeItems = items.getOrThrow(MELEE_WEAPONS);

    // We could consider adding enchantments to ranged weapons, but not going to do it right now for balancing reasons.
    // TODO: This is not actually working. Ranged items are still in the JSON "supported tags" for each individual
    // modifier. Instead, we should add each Melee weapon manually. TODO: Check whether bows have extra attack damage
    //  and attack speed (e.g. from "legendary").
    var toolItems = items.getOrThrow(TOOLS);
//    var nonRangeItemsList = toolItems.stream().filter(item ->
//                                                        !item.is(ItemTags.BOW_ENCHANTABLE)
//                                                          && !item.is(ItemTags.CROSSBOW_ENCHANTABLE)
//                                                          && !item.is(BOWS)
//                                                          && !item.is(RANGED_WEAPONS)
//    ).toList();
//    var nonRangeItemsHolderSet = HolderSet.direct(nonRangeItemsList);
//    registerToolWeaponModifiers(bootstrap, nonRangeItemsHolderSet);
    registerToolWeaponModifiers(bootstrap, toolItems);
  }

  private static void registerArmorModifiers(BootstrapContext<Modifier> bootstrap, HolderSet<Item> items) {
    register(bootstrap, "half_hearted", new Modifier.ModifierDefinition(items, 30, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.MAX_HEALTH, new AttributeModifier(id("half_hearted"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "hearty", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.MAX_HEALTH, new AttributeModifier(id("hearty"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "hard", new Modifier.ModifierDefinition(items, 30, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ARMOR, new AttributeModifier(id("hard"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "guarding", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ARMOR, new AttributeModifier(id("guarding"), 1.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "armored", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ARMOR, new AttributeModifier(id("armored"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "warding", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(id("warding"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "jagged", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("jagged"), 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "spiked", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("spiked"), 0.02, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "angry", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("angry"), 0.03, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "menacing", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("menacing"), 0.04, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "brisk", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("brisk"), 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "fleeting", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("fleeting"), 0.02, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "hasty", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("hasty"), 0.03, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "quick", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("quick"), 0.04, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "wild", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("wild"), 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "rash", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("rash"), 0.02, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "intrepid", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("intrepid"), 0.03, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());

    register(bootstrap, "violent", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("violent"), 0.04, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ARMOR).build()).build());
  }

  private static void registerToolWeaponModifiers(BootstrapContext<Modifier> bootstrap, HolderSet<Item> items) {
    register(bootstrap, "legendary", new Modifier.ModifierDefinition(items, 3, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("legendary"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("legendary"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("legendary"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "deadly", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("deadly"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "vicious", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("vicious"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "sharp", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("sharp"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "broken", new Modifier.ModifierDefinition(items, 7, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("broken"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "damaged", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("damaged"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "agile", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("agile"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("agile"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "swift", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("swift"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "sluggish", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("sluggish"), -0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("sluggish"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "slow", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("slow"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "light", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("light"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("light"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());

    register(bootstrap, "heavy", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                                                                           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id("heavy"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.ATTACK_SPEED, new AttributeModifier(id("heavy"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND)
                                                                           .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(id("heavy"), -0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND).build()).build());
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