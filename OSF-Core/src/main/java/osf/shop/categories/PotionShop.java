package osf.shop.categories;

import org.bukkit.ChatColor;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryProvider;

public class PotionShop extends Category implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public PotionShop(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("potions")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop | Potions")
                .build();
    }
    public String getJsonId() {
        return "potions";
    }

    public SmartInventory getInventory() {
        return INVENTORY;
    }
}
