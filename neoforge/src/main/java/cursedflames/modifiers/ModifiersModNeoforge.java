package cursedflames.modifiers;


import cursedflames.modifiers.item.ItemModifierBook;
import cursedflames.modifiers.item.ItemReforgeTemplate;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Set;

@Mod(Constants.MOD_ID)
public class ModifiersModNeoforge {
  static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
    event.dataPackRegistry(
      // The registry key.
      ModifiersMod.MODIFIER_REGISTRY_KEY,
      // The codec of the registry contents.
      Modifier.DIRECT_CODEC,
      // The network codec of the registry contents. Often identical to the normal codec.
      // May be a reduced variant of the normal codec that omits data that is not needed on the client.
      // May be null. If null, registry entries will not be synced to the client at all.
      // May be omitted, which is functionally identical to passing null (a method overload
      // with two parameters is called that passes null to the normal three parameter method).
      Modifier.DIRECT_CODEC
    );
  }

  static void onGatherData(GatherDataEvent event) {
    event.getGenerator().addProvider(
      // Only run datapack generation when server data is being generated
      event.includeServer(),
      // Create the provider
      (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
        output,
        event.getLookupProvider(),
        // Our registry set builder to generate the data from.
        new RegistrySetBuilder().add(ModifiersMod.MODIFIER_REGISTRY_KEY, ModifiersDatagen::bootstrap),
        // A set of mod ids we are generating. Usually only your own mod id.
        Set.of(Constants.MOD_ID)
      )
    );
  }

  static void onRegisterDataComponents(RegisterEvent.RegisterHelper<DataComponentType<?>> registry) {
    // java generics are gonna be the death of me
    DataComponentType.Builder<ItemModifier> builder = DataComponentType.builder();
    DataComponentType<ItemModifier> componentType = builder.persistent(ItemModifier.CODEC).cacheEncoding().build();
    ModifiersMod.ITEM_MODIFIER_COMPONENT = componentType;
    registry.register(ModifiersMod.resourceLocation("modifier"), componentType);
  }

  static void onRegisterItems(RegisterEvent.RegisterHelper<Item> registry) {
    registry.register(ModifiersMod.resourceLocation("modifier_book"), ModifiersMod.MODIFIER_BOOK = new ItemModifierBook());
    registry.register(ModifiersMod.resourceLocation("reforge_template"), ModifiersMod.REFORGE_TEMPLATE = new ItemReforgeTemplate());
  }

  static void onRegisterCreativeTabs(RegisterEvent.RegisterHelper<CreativeModeTab> registry) {
    registry.register(ModifiersMod.resourceLocation("modifier_books"),
      CreativeModeTab.builder()
        .icon(() -> new ItemStack(ModifiersMod.MODIFIER_BOOK))
        .title(Component.translatable("itemGroup.modifiers_books"))
        .displayItems((CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) -> {
          output.accept(ItemReforgeTemplate.create());

          // TODO: Empty books don't work yet
          output.accept(ItemModifierBook.createForModifier(null));


          params.holders().lookup(ModifiersMod.MODIFIER_REGISTRY_KEY).ifPresent(modifiers -> {
            modifiers.listElements()
              // Debug the eligible items for each modifier
//              .peek(a -> System.out.println("Modifier: " + a))
              .map(modifierReference -> ItemModifierBook.createForModifier(modifierReference.getDelegate()))
              .forEach(output::accept);
          });
        })
        .build()
    );
  }

  static void onRegister(RegisterEvent event) {
    event.register(Registries.DATA_COMPONENT_TYPE, ModifiersModNeoforge::onRegisterDataComponents);
    event.register(Registries.ITEM, ModifiersModNeoforge::onRegisterItems);
    event.register(Registries.CREATIVE_MODE_TAB, ModifiersModNeoforge::onRegisterCreativeTabs);
  }

  public ModifiersModNeoforge(IEventBus eventBus) {
    eventBus.addListener(ModifiersModNeoforge::registerDatapackRegistries);
    eventBus.addListener(ModifiersModNeoforge::onGatherData);
    eventBus.addListener(ModifiersModNeoforge::onRegister);
  }
}