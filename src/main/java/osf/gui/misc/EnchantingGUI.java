package osf.gui.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import osf.OSF;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryContents;
import osf.gui.content.InventoryProvider;

public class EnchantingGUI implements InventoryProvider, Listener {
    public static SmartInventory INVENTORY = SmartInventory.builder()
            .id("max_enchanting")
            .provider(new EnchantingGUI())
            .type(InventoryType.ENCHANTING)
            .manager(OSF.inventoryManager)
            .build();

    public EnchantingGUI() {
        Bukkit.getPluginManager().registerEvents(this, OSF.getInstance());
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
