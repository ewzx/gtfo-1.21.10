package ewzx.gtfo;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItemGroup {
    // 1. Cleaner, single-line registration using your new factory pattern
    public static final RegistryKey<ItemGroup> GTFO_TAB = register("gtfo_tab", builder -> builder
            .icon(() -> new ItemStack(ModItems.BIO_TRACKER))
            .displayName(Text.translatable("itemGroup.gtfo.gtfo_tab"))
            .entries((displayContext, entries) -> {
                entries.add(ModItems.BIO_TRACKER);
                entries.add(ModBlocks.PING_BLOCK.asItem());
                // You can cleanly append more items here later
            })
            .build()
    );

    // 2. The Factory Method (Mirrors your ModItem registry setup)
    private static RegistryKey<ItemGroup> register(String name, Function<ItemGroup.Builder, ItemGroup> groupFactory) {
        // Automatically build the Registry Key using your Mod ID
        RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(GTFO.MOD_ID, name));

        // Pass a fresh builder into your factory function to build the final ItemGroup
        ItemGroup itemGroup = groupFactory.apply(FabricItemGroup.builder());

        // Register it into Minecraft's system
        Registry.register(Registries.ITEM_GROUP, key, itemGroup);

        // Return the key so your code can reference it easily
        return key;
    }

    public static void registerItemGroups() {
        // Keeps the initialization hook consistent with your other systems
    }
}
