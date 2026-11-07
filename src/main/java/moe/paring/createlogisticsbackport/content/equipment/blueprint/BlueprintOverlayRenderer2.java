package moe.paring.createlogisticsbackport.content.equipment.blueprint;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.blueprint.BlueprintEntity;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.trains.track.TrackPlacement;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;
import moe.paring.createlogisticsbackport.content.logistics.packager.InventorySummary;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.BlueprintOverlayShopContext;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.ShoppingListItem;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlockEntity;
import moe.paring.createlogisticsbackport.polyfill.ItemHelperExtra;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;
import moe.paring.createlogisticsbackport.registry.ExtraItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class BlueprintOverlayRenderer2 {

    static BlueprintOverlayShopContext shopContext;

    static boolean active;
    static boolean empty;
    static boolean noOutput;

    static List<Pair<ItemStack, Boolean>> ingredients = new ArrayList<>();

    static Map<ItemStack, ItemStack[]> cachedRenderedFilters = new IdentityHashMap<>();
    static List<ItemStack> results = new ArrayList<>();
    static boolean resultCraftable = false;

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();

        active = false;
        noOutput = false;
        shopContext = null;

        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        HitResult mouseOver = mc.hitResult;
        if (mouseOver == null)
            return;
        if (mouseOver.getType() != HitResult.Type.ENTITY)
            return;

        EntityHitResult entityRay = (EntityHitResult) mouseOver;
        if (!(entityRay.getEntity() instanceof BlueprintEntity blueprintEntity))
            return;

        active = true;
    }

    public static void displayTrackRequirements(TrackPlacement.PlacementInfo info, ItemStack pavementItem) {
        if (active)
            return;
        prepareCustomOverlay();

        int tracks = info.requiredTracks;
        while (tracks > 0) {
            ingredients.add(
                    Pair.of(new ItemStack(info.trackMaterial.getBlock(), Math.min(64, tracks)), info.hasRequiredTracks));
            tracks -= 64;
        }

        int pavement = info.requiredPavement;
        while (pavement > 0) {
            ingredients.add(Pair.of(ItemHandlerHelper.copyStackWithSize(pavementItem, Math.min(64, pavement)),
                    info.hasRequiredPavement));
            pavement -= 64;
        }
    }

    public static void displayChainRequirements(Item chainItem, int count, boolean fulfilled) {
        if (active)
            return;
        prepareCustomOverlay();

        int chains = count;
        while (chains > 0) {
            ingredients.add(Pair.of(new ItemStack(chainItem, Math.min(64, chains)), fulfilled));
            chains -= 64;
        }
    }

    public static void displayClothShop(TableClothBlockEntity dce, int alreadyPurchased, ShoppingListItem.ShoppingList list) {
        if (active)
            return;
        prepareCustomOverlay();
        noOutput = false;

        shopContext = new BlueprintOverlayShopContext(false, dce.getStockLevelForTrade(list), alreadyPurchased);

            ingredients.add(Pair.of(moe.paring.createlogisticsbackport.foundation.util.ItemStackHelper.copyWithCount(dce.getPaymentItem(), dce.getPaymentAmount()),
                !dce.getPaymentItem()
                        .isEmpty() && shopContext.stockLevel() > shopContext.purchases()));
        for (BigItemStack entry : dce.requestData.encodedRequest.stacks())
            results.add(moe.paring.createlogisticsbackport.foundation.util.ItemStackHelper.copyWithCount(entry.stack, entry.count));
    }

    public static void displayShoppingList(Couple<InventorySummary> bakedList) {
        if (active || bakedList == null)
            return;
        Minecraft mc = Minecraft.getInstance();
        prepareCustomOverlay();
        noOutput = false;

        shopContext = new BlueprintOverlayShopContext(true, 1, 0);

        for (BigItemStack entry : bakedList.getSecond()
                .getStacksByCount()) {
            ingredients.add(Pair.of(moe.paring.createlogisticsbackport.foundation.util.ItemStackHelper.copyWithCount(entry.stack, entry.count), canAfford(mc.player, entry)));
        }

        for (BigItemStack entry : bakedList.getFirst()
                .getStacksByCount())
            results.add(moe.paring.createlogisticsbackport.foundation.util.ItemStackHelper.copyWithCount(entry.stack, entry.count));
    }

    private static boolean canAfford(Player player, BigItemStack entry) {
        int itemsPresent = 0;
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack item = player.getInventory()
                    .getItem(i);
            if (item.isEmpty() || !ItemHandlerHelper.canItemStacksStack(item, entry.stack))
                continue;
            itemsPresent += item.getCount();
        }
        return itemsPresent >= entry.count;
    }

    private static void prepareCustomOverlay() {
        active = true;
        empty = false;
        noOutput = true;
        ingredients.clear();
        results.clear();
        shopContext = null;
    }

    private static ItemStack[] getItemsMatchingFilter(ItemStack filter) {
        return cachedRenderedFilters.computeIfAbsent(filter, itemStack -> {
            CompoundTag tag = itemStack.getOrCreateTag();

            if (AllItems.FILTER.isIn(itemStack) && !tag.getBoolean("Blacklist")) {
                ItemStackHandler filterItems = FilterItem.getFilterItems(itemStack);
                return ItemHelperExtra.getNonEmptyStacks(filterItems).toArray(ItemStack[]::new);
            }

//            if (AllItems.ATTRIBUTE_FILTER.isIn(itemStack)) {
//                AttributeFilterMenu.WhitelistMode whitelistMode = AttributeFilterMenu.WhitelistMode.values()[tag.getInt("WhitelistMode")];
//                ListTag attributes = tag.getList("MatchedAttributes", net.minecraft.nbt.Tag.TAG_COMPOUND);
//                if (whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ && attributes.size() == 1) {
//                    ItemAttribute fromNBT = ItemAttribute.fromNBT((CompoundTag) attributes.get(0));
//                    if (fromNBT instanceof InTagAttribute inTag) {
//                        ITagManager<Item> tagManager = ForgeRegistry.ITEM_REGISTRY.tags();
//                        if (tagManager.isKnownTagName(inTag.tag)) {
//                            ITag<Item> taggedItems = tagManager.getTag(inTag.tag);
//                            if (!taggedItems.isEmpty()) {
//                                ItemStack[] stacks = new ItemStack[taggedItems.size()];
//                                int i = 0;
//                                for (Item item : taggedItems) {
//                                    stacks[i] = new ItemStack(item);
//                                    i++;
//                                }
//                                return stacks;
//                            }
//                        }
//                    }
//                }
//            }

            return new ItemStack[0];
        });
    }

}

