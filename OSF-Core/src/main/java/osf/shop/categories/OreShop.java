package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class OreShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public OreShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("ores")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Ores")
                .build();
    }

    public String getJsonId() {
        return "ores";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
