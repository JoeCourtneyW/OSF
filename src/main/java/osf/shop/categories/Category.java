package osf.shop.categories;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import osf.OSF;
import osf.gui.ClickableItem;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryContents;
import osf.gui.content.SlotIterator;
import osf.gui.content.SlotPos;
import osf.shop.ShopGUI;
import osf.shop.ShopItem;
import osf.utils.ItemBuilder;

public abstract class Category {


    public void init(Player player, InventoryContents contents) {
        ItemBuilder borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 15).displayName(" ");
        contents.fillBorders(ClickableItem.empty(borderItem.grab()));
        ClickableItem[] items = new ClickableItem[OSF.shopJson.get(getJsonId()).size()];
        contents.pagination().setItemsPerPage(28);

        int counter = 0;
        for (JsonNode item : OSF.shopJson.get(getJsonId())) {
            ShopItem shopItem = new ShopItem(new ItemStack(Material.valueOf(item.get("TYPE").asText())), item);
            items[counter] = shopItem.getClickableItem();
            counter++;
        }

        contents.pagination().setItems(items);
        SlotIterator shopIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 1));
        shopIterator.allowOverride(false);
        contents.pagination().addToIterator(shopIterator);


        ItemBuilder categories = new ItemBuilder(Material.BARRIER).displayName(ChatColor.RED + "Back to Categories");
        contents.set(5, 4, ClickableItem.of(categories.grab(),
                e2 -> ShopGUI.INVENTORY.open(player)));

        if (counter > 28) {
            ItemBuilder previous = new ItemBuilder(Material.ARROW).displayName(ChatColor.AQUA + "Previous Page");
            contents.set(5, 3, ClickableItem.of(previous.grab(),
                    e2 -> getInventory().open(player, contents.pagination().previous().getPage())));
            ItemBuilder next = new ItemBuilder(Material.ARROW).displayName(ChatColor.AQUA + "Next Page");
            contents.set(5, 5, ClickableItem.of(next.grab(),
                    e2 -> getInventory().open(player, contents.pagination().next().getPage())));
        }
    }

    public void update(Player player, InventoryContents inventoryContents) {

    }

    public abstract SmartInventory getInventory();

    public abstract String getJsonId();
}
