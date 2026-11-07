package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import moe.paring.createlogisticsbackport.BuilderTransformersExtra;
import moe.paring.createlogisticsbackport.content.equipment.armor.CardboardArmorItem;
import moe.paring.createlogisticsbackport.content.equipment.armor.CardboardHelmetItem;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageStyles;
import moe.paring.createlogisticsbackport.content.logistics.filter.FilterItem2;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.ShoppingListItem;
import moe.paring.createlogisticsbackport.content.tool.CardboardSwordItem;
import net.minecraft.core.Registry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;

import static com.simibubi.create.AllTags.AllItemTags.PLATES;
import static com.simibubi.create.AllTags.forgeItemTag;
import static moe.paring.createlogisticsbackport.CreateLogisticsBackport.REGISTRATE;

@SuppressWarnings("unused")
public class ExtraItems {
    static {
        Create.REGISTRATE.creativeModeTab(() -> AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<Item>
            PULP = ingredient("pulp"),
            TRANSMITTER = ingredient("transmitter");

    public static final ItemEntry<CombustibleItem> CARDBOARD = REGISTRATE.item("cardboard", CombustibleItem::new).tag(forgeItemTag("plates/cardboard"), PLATES.tag).onRegister(i -> i.setBurnTime(1000)).register();

    public static final ItemEntry<ShoppingListItem> SHOPPING_LIST = REGISTRATE.item("shopping_list", ShoppingListItem::new).properties(p -> p.stacksTo(1)).register();

    public static final ItemEntry<FilterItem2> PACKAGE_FILTER = REGISTRATE.item("package_filter", FilterItem2::address).register();

    public static final ItemEntry<? extends BaseArmorItem>
            CARDBOARD_HELMET = REGISTRATE.item("cardboard_helmet", p -> new CardboardHelmetItem(EquipmentSlot.HEAD, p)).tag(forgeItemTag("armors/helmet")).onRegisterAfter(Registry.ITEM_REGISTRY, v -> ItemDescription.useKey(v, "item.create.cardboard_armor")).register(),
            CARDBOARD_CHESTPLATE = REGISTRATE.item("cardboard_chestplate", p -> new CardboardArmorItem(EquipmentSlot.CHEST, p)).tag(forgeItemTag("armors/chestplate")).onRegisterAfter(Registry.ITEM_REGISTRY, v -> ItemDescription.useKey(v, "item.create.cardboard_armor")).register(),
            CARDBOARD_LEGGINGS = REGISTRATE.item("cardboard_leggings", p -> new CardboardArmorItem(EquipmentSlot.LEGS, p)).tag(forgeItemTag("armors/leggings")).onRegisterAfter(Registry.ITEM_REGISTRY, v -> ItemDescription.useKey(v, "item.create.cardboard_armor")).register(),
            CARDBOARD_BOOTS = REGISTRATE.item("cardboard_boots", p -> new CardboardArmorItem(EquipmentSlot.FEET, p)).tag(forgeItemTag("armors/boots")).onRegisterAfter(Registry.ITEM_REGISTRY, v -> ItemDescription.useKey(v, "item.create.cardboard_armor")).register();

    public static final ItemEntry<CardboardSwordItem> CARDBOARD_SWORD =
            REGISTRATE.item("cardboard_sword", CardboardSwordItem::new)
                    .properties(p -> p.stacksTo(1))
                    .model(AssetLookup.itemModelWithPartials())
                    .register();

    static {
        boolean rareCreated = false;
        boolean normalCreated = false;
        for (PackageStyles.PackageStyle style : PackageStyles.STYLES) {
            ItemBuilder<PackageItem, CreateRegistrate> packageItem = BuilderTransformersExtra.packageItem(style);

            if (rareCreated && style.rare() || normalCreated && !style.rare())
                packageItem.setData(ProviderType.LANG, NonNullBiConsumer.noop());

            rareCreated |= style.rare();
            normalCreated |= !style.rare();
            packageItem.register();
        }
    }

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new).register();
    }

    public static void register() {
    }

}
