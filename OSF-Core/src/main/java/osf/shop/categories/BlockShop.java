package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class BlockShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public BlockShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("blocks")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Blocks")
                .build();
    }

    public String getJsonId() {
        return "blocks";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
