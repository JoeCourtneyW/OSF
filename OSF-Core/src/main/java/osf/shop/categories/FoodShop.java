package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class FoodShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public FoodShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("food")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Food")
                .build();
    }


    public String getJsonId() {
        return "food";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
