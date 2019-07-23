package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class FarmingShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public FarmingShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("farming")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Farming")
                .build();
    }

    public String getJsonId() {
        return "farming";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
