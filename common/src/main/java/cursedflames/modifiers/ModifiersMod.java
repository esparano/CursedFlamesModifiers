package cursedflames.modifiers;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModifiersMod {
	public static final ResourceKey<Registry<Modifier>> MODIFIER_REGISTRY_KEY = ResourceKey.createRegistryKey(ModifiersMod.resourceLocation("modifiers"));
	public static DataComponentType<ItemModifier> ITEM_MODIFIER_COMPONENT;
    public static Item MODIFIER_BOOK;

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}