package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class MobDropShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public MobDropShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("mobdrops")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Mob Drops")
                .build();
    }
    public String getJsonId() {
        return "mobdrops";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
