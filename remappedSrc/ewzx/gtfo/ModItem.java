package ewzx.gtfo;


import java.util.function.Function;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItem {
    public class ModItems {
        public static <T extends Item> T register(String name, Function<Item.Settings, T> itemFactory, Item.Settings settings) {
            // Create the item key.
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GTFO.MOD_ID, name));

            // Create the item instance.
            T item = itemFactory.apply(settings.registryKey(itemKey));

            // Register the item.
            Registry.register(Registries.ITEM, itemKey, item);

            return item;
        }
    }
}
