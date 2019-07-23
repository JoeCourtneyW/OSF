package osf.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import osf.utils.ItemBuilder;

import java.util.HashMap;
import java.util.List;

public abstract class InventoryGUI {

    public Inventory inventory;

    public String inventoryId;
    public String inventoryTitle;
    public int size;
    public boolean border;
    public ItemStack borderItem;
    public boolean paged;

    HashMap<Integer, ItemStack[]> pages;

    List<ItemStack> contents;


    public InventoryGUI(String inventoryId, String inventoryTitle, int size, boolean border, boolean paged) {
        this.inventoryId = inventoryId;
        this.inventoryTitle = inventoryTitle;
        if (this.size % 9 == 0)
            this.size = size;
        else
            this.size = 54;
        this.border = border;
        this.paged = paged;

        this.pages = new HashMap<>();
        this.pages.put(0, new ItemStack[size]);
    }

    public void setBorderItem(ItemStack item) {
        this.borderItem = item;
    }

    public void addItem(ClickableItem item) {

    }

    private void buildInventory() {
        Inventory inventory = Bukkit.createInventory(null, size, inventoryTitle);
        if (border) {
            for (int i = 0; i < 9; i++)
                inventory.setItem(i, borderItem); //Top border
            for (int i = 45; i < 54; i++)
                inventory.setItem(i, borderItem); //Bottom border
            for (int i = 0; i < 6; i++)
                inventory.setItem(i * 9, borderItem); //Left border
            for (int i = 0; i < 6; i++)
                inventory.setItem(i * 9 + 8, borderItem); //Right border
        }
        if (paged) {
            inventory.setItem(48, new ItemBuilder(Material.ARROW).displayName(ChatColor.AQUA + "Previous Page").grab());
            inventory.setItem(50, new ItemBuilder(Material.ARROW).displayName(ChatColor.AQUA + "Next Page").grab());
        }


    }


    public void open(Player player) {
        if (inventory == null)
            buildInventory();

        player.openInventory(inventory);
    }


}
