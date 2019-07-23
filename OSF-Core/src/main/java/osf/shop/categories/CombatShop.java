package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class CombatShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public CombatShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("combat")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Combat")
                .build();
    }

    public String getJsonId() {
        return "combat";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
