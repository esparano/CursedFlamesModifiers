//package cursedflames.modifiers;
//
//import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.item.ArmorItem;
//import net.minecraft.world.item.DiggerItem;
//import net.minecraft.world.item.SwordItem;
//import net.minecraft.resources.ResourceLocation;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Modifiers {
//  public static Map<ResourceLocation, Modifier> modifiers = new HashMap<>();
//
//  public static Modifier NONE = new Modifier.ModifierBuilder(ModifiersMod.resourceLocation(Constants.MOD_ID), "modifier_none", Modifier.ModifierType.BOTH).setWeight(0).build();
//
//  static {
//    modifiers.put(NONE.name, NONE);
//  }
//
//  public static ModifierPool curio_pool = new ModifierPool(stack -> {
//    // TODO find a better way of determining if an item is armor
//    if (stack.getItem() instanceof ArmorItem) return true;
//    return ModifiersMod.curioProxy.isModifiableCurio(stack);
//  });
//
//  // TODO sub-pools for different weapon types? melee/ranged
//  public static ModifierPool tool_pool = new ModifierPool(stack -> {
//    // TODO find a better way of determining if an item is a tool
//    // TODO probably want modifiers on tridents and ranged weapons too
//    if (stack.getItem() instanceof SwordItem) return true;
//    return stack.getItem() instanceof DiggerItem;
//  });
//
//  private static Modifier.ModifierBuilder curio(String name) {
//    return new Modifier.ModifierBuilder(ModifiersMod.resourceLocation(Constants.MOD_ID), "modifier_" + name, Modifier.ModifierType.EQUIPPED);
//  }
//
//  private static Modifier.ModifierBuilder both(String name) {
//    return new Modifier.ModifierBuilder(ModifiersMod.resourceLocation(Constants.MOD_ID), "modifier_" + name, Modifier.ModifierType.BOTH);
//  }
//
//  private static Modifier.ModifierBuilder tool(String name) {
//    return new Modifier.ModifierBuilder(ModifiersMod.resourceLocation(Constants.MOD_ID), "modifier_" + name, Modifier.ModifierType.HELD);
//  }
//
//  private static void addCurio(Modifier modifier) {
//    modifiers.put(modifier.name, modifier);
//    curio_pool.add(modifier);
//  }
//
//  private static void addTool(Modifier modifier) {
//    modifiers.put(modifier.name, modifier);
//    tool_pool.add(modifier);
//  }
//
//  private static Modifier.AttributeModifierSupplier mod(double amount, Operation op) {
//    return new Modifier.AttributeModifierSupplier(amount, op);
//  }
//
//  public static void init() {
//    addCurio(curio("half_hearted").setWeight(300).addModifier(Attributes.MAX_HEALTH, mod(1, ADDITION)).build());
//    addCurio(curio("hearty").setWeight(100).addModifier(Attributes.MAX_HEALTH, mod(2, ADDITION)).build());
//
//  }
//
//  static {
//    init();
//  }
//}
