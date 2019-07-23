package osf.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
    public static void removeInventoryItems(Inventory inv, ItemStack item, int amount) {
        ItemStack[] items = inv.getContents();
        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is != null && is.getType() == item.getType() && is.getDurability() == item.getDurability()) {
                int newamount = is.getAmount() - amount;
                if (newamount > 0) {
                    is.setAmount(newamount);
                    break;
                } else {
                    items[i] = new ItemStack(Material.AIR);
                    amount = -newamount;
                    if (amount == 0)
                        break;
                }
            }
        }
        inv.setContents(items);
    }

    public static void removeInventoryItems(Inventory inv, Material type, int amount) {
        ItemStack[] items = inv.getContents();
        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is != null && is.getType() == type) {
                int newamount = is.getAmount() - amount;
                if (newamount > 0) {
                    is.setAmount(newamount);
                    break;
                } else {
                    items[i] = new ItemStack(Material.AIR);
                    amount = -newamount;
                    if (amount == 0)
                        break;
                }
            }
        }
        inv.setContents(items);
    }

    public static int getTotalItems(Inventory inv, Material type) {
        int amount = 0;
        for (ItemStack is : inv.getContents()) {
            if (is != null) {
                if (is.getType() == type) {
                    amount += is.getAmount();
                }
            }
        }
        return amount;
    }
    public static int getTotalItems(Inventory inv, ItemStack itemStack) {
        int amount = 0;
        for (ItemStack is : inv.getContents()) {
            if (is != null) {
                if (is.getType() == itemStack.getType() && is.getDurability() == itemStack.getDurability()) {
                    amount += is.getAmount();
                }
            }
        }
        return amount;
    }

    public static boolean hasInventorySpace(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isItemSimilar(ItemStack i, String displayName, Material material) {
        return i != null && i.hasItemMeta() && i.getType().equals(material) && i.getItemMeta().hasDisplayName()
                && i.getItemMeta().getDisplayName().contains(displayName);
    }

    public static boolean hasItemMeta(ItemStack i) {
        return i != null && i.hasItemMeta();
    }
}
