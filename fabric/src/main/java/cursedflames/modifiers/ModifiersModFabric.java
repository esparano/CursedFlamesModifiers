package cursedflames.modifiers;

import cursedflames.modifiers.item.ItemModifierBook;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModifiersModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DynamicRegistries.registerSynced(ModifiersMod.MODIFIER_REGISTRY_KEY, Modifier.DIRECT_CODEC);

        DataComponentType.Builder<ItemModifier> builder = DataComponentType.builder();
        DataComponentType<ItemModifier> componentType = builder.persistent(ItemModifier.CODEC).cacheEncoding().build();
        ModifiersMod.ITEM_MODIFIER_COMPONENT = componentType;
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ModifiersMod.resourceLocation("modifier"), componentType);
        Registry.register(BuiltInRegistries.ITEM, ModifiersMod.resourceLocation("modifier_book"), ModifiersMod.MODIFIER_BOOK = new ItemModifierBook());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModifiersMod.resourceLocation("modifier_books"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModifiersMod.MODIFIER_BOOK))
                        .title(Component.translatable("itemGroup.modifiers_books"))
                        .displayItems((CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) -> {
                            output.accept(ItemModifierBook.createForModifier(null));
                            params.holders().lookup(ModifiersMod.MODIFIER_REGISTRY_KEY).ifPresent(modifiers -> {
                                modifiers.listElements()
                                        .map(ItemModifierBook::createForModifier)
                                        .forEach(output::accept);
                            });
                        })
                        .build());

    }
}
