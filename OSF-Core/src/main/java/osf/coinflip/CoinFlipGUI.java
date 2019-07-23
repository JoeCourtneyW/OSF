package osf.coinflip;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import osf.OSF;
import osf.gui.ClickableItem;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryContents;
import osf.gui.content.InventoryProvider;
import osf.utils.ItemBuilder;

public class CoinFlipGUI implements InventoryProvider {

    public static SmartInventory INVENTORY;

    public CoinFlipGUI() {
        INVENTORY = SmartInventory.builder()
                .id("coinflip")
                .provider(this)
                .manager(OSF.inventoryManager)
                .size(3, 9)
                .title(ChatColor.BLUE + "CoinFlip")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemBuilder borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).data((short)15).displayName(" ");
        contents.fillBorders(ClickableItem.empty(borderItem.grab()));

        ItemBuilder carrot = new ItemBuilder(Material.CARROT).displayName("§b§lCarrot");
        contents.set(1, 1, ClickableItem.of(carrot.grab(),
                e -> player.sendMessage(ChatColor.GOLD + "You clicked on a carrot.")));
        ItemBuilder exitItem = new ItemBuilder(Material.BARRIER).displayName("§c§nEXIT");
        contents.set(1, 7, ClickableItem.of(exitItem.grab(),
                e -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
