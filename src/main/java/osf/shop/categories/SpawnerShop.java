package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class SpawnerShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public SpawnerShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("spawners")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Spawners")
                .build();
    }
    public String getJsonId() {
        return "spawners";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
