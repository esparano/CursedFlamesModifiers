package cursedflames.modifiers;

import net.minecraft.core.HolderSet;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

  private static void registerArmorModifiers(BootstrapContext<Modifier> bootstrap, net.minecraft.core.HolderSet<Item> items) {
    // Old method:
    //		addCurio(curio("half_hearted").setWeight(300).addModifier(Attributes.MAX_HEALTH, mod(1, ADDITION)).build());

    register(bootstrap, "half_hearted", new Modifier.ModifierDefinition(items, 30, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("half_hearted"),
          Attributes.MAX_HEALTH, LevelBasedValue.constant(1), AttributeModifier.Operation.ADD_VALUE)))
        .build());

    register(bootstrap, "hearty", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("hearty"),
          Attributes.MAX_HEALTH, LevelBasedValue.constant(2), AttributeModifier.Operation.ADD_VALUE)))
        .build());

    register(bootstrap, "hard", new Modifier.ModifierDefinition(items, 30, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("hard"),
          Attributes.ARMOR, LevelBasedValue.constant(1), AttributeModifier.Operation.ADD_VALUE)))
        .build());

    register(bootstrap, "guarding", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("guarding"),
          Attributes.ARMOR, LevelBasedValue.constant(1.5f), AttributeModifier.Operation.ADD_VALUE)))
        .build());

    register(bootstrap, "armored", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("armored"),
          Attributes.ARMOR, LevelBasedValue.constant(2), AttributeModifier.Operation.ADD_VALUE)))
        .build());

    register(bootstrap, "warding", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("warding"),
          Attributes.ARMOR_TOUGHNESS, LevelBasedValue.constant(1), AttributeModifier.Operation.ADD_VALUE)))
        .build());

    register(bootstrap, "jagged", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("jagged"),
          Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.01f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "spiked", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("spiked"),
          Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.02f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "angry", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("angry"),
          Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.03f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "menacing", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("menacing"),
          Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.04f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "brisk", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("brisk"),
          Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(0.01f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "fleeting", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("fleeting"),
          Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(0.02f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "hasty", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("hasty"),
          Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(0.03f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "quick", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("quick"),
          Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(0.04f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "wild", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("wild"),
          Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.01f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "rash", new Modifier.ModifierDefinition(items, 20, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("rash"),
          Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.02f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "intrepid", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("intrepid"),
          Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.03f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());

    register(bootstrap, "violent", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.ARMOR)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(new EnchantmentAttributeEffect(id("violent"),
          Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.04f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))
        .build());
  }

  private static void registerToolWeaponModifiers(BootstrapContext<Modifier> bootstrap, HolderSet<Item> items) {
    register(bootstrap, "legendary", new Modifier.ModifierDefinition(items, 3, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("legendary"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.15f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("legendary"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("legendary"),
            Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(0.05f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "deadly", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("deadly"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.15f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "vicious", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("vicious"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "sharp", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("sharp"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.05f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "broken", new Modifier.ModifierDefinition(items, 7, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("broken"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(-0.2f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "damaged", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("damaged"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(-0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "agile", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("agile"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.05f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("agile"),
            Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "swift", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("swift"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "sluggish", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("sluggish"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(-0.05f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("sluggish"),
            Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(-0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "slow", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("slow"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(-0.15f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "light", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("light"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(-0.1f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("light"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(0.15f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
        .build());

    register(bootstrap, "heavy", new Modifier.ModifierDefinition(items, 10, 0, List.of(EquipmentSlotGroup.MAINHAND)),
      DataComponentMap.builder()
        .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
          new EnchantmentAttributeEffect(id("heavy"),
            Attributes.ATTACK_DAMAGE, LevelBasedValue.constant(0.2f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("heavy"),
            Attributes.ATTACK_SPEED, LevelBasedValue.constant(-0.15f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
          new EnchantmentAttributeEffect(id("heavy"),
            Attributes.MOVEMENT_SPEED, LevelBasedValue.constant(-0.05f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ))
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
