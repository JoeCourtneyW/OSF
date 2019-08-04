package osf.shop;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import osf.OSF;
import osf.gui.ClickableItem;
import osf.utils.InventoryUtil;
import osf.utils.ItemBuilder;
import osf.utils.ItemUtil;
import osf.utils.MathUtil;

public class ShopItem {

    private ItemStack shopItemStack;
    private JsonNode jsonItem;

    public ShopItem(ItemStack shopItemStack, JsonNode jsonItem) {

        this.shopItemStack = shopItemStack;
        this.jsonItem = jsonItem;
        if (shopItemStack.getType() == Material.MOB_SPAWNER) {
            this.shopItemStack = OSF.silkUtil.setSpawnerType(shopItemStack, (short) getJsonItem().get("ENTITY_ID").asInt(), "&e%creature% &fSpawner");
        }
        if(shopItemStack.getType() == Material.POTION || shopItemStack.getType() == Material.SPLASH_POTION) {
            PotionMeta potionMeta = (PotionMeta) shopItemStack.getItemMeta();
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(getJsonItem().get("EFFECT").asText()), getJsonItem().get("AMPLIFIER").asInt(), getJsonItem().get("DURATION").asInt()), true);
            shopItemStack.setItemMeta(potionMeta);
        }
    }

    private JsonNode getJsonItem() {
        return jsonItem;
    }


    public ClickableItem getClickableItem() {
        double buyPrice = getJsonItem().get("BUY_PRICE").asDouble();
        double sellPrice = getJsonItem().get("SELL_PRICE").asDouble();
        ItemBuilder display = new ItemBuilder(shopItemStack.getType());
        if(shopItemStack.getItemMeta().hasDisplayName())
            display.displayName(shopItemStack.getItemMeta().getDisplayName());
        if (buyPrice > 0)
            display.addLoreLine(ChatColor.GRAY + "Buy: " + ChatColor.RED + "$" + MathUtil.formatDouble(buyPrice));
        if (sellPrice > 0)
            display.addLoreLine(ChatColor.GRAY + "Sell: " + ChatColor.GREEN + "$" + MathUtil.formatDouble(sellPrice));
        if (getJsonItem().has("NAME"))
            display.displayName(ChatColor.translateAlternateColorCodes('&', getJsonItem().get("NAME").asText()));
        if (getJsonItem().has("DATA")) {
            display.data((short) getJsonItem().get("DATA").asInt());
            shopItemStack.setDurability((short) getJsonItem().get("DATA").asInt());
        }


        return ClickableItem.of(display.grab(), e -> {
            int amount = 1;
            if (e.isShiftClick())
                amount = 64;
            if (e.isLeftClick() && buyPrice > 0) {
                double totalBuyPrice = buyPrice * amount;
                if (OSF.getInstance().getEconomy().has(Bukkit.getOfflinePlayer(e.getWhoClicked().getUniqueId()), totalBuyPrice)) {
                    if (InventoryUtil.hasInventorySpace((Player) e.getWhoClicked())) {
                        ItemStack purchasedItem = shopItemStack.clone();
                        purchasedItem.setAmount(amount);
                        e.getWhoClicked().getInventory().addItem(purchasedItem);
                        OSF.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(e.getWhoClicked().getUniqueId()), totalBuyPrice);
                        e.getWhoClicked().sendMessage(OSF.PREFIX + ChatColor.GREEN + "You purchased " + ChatColor.WHITE + amount + "x " + ItemUtil.getItemStackName(shopItemStack)
                                + ChatColor.GREEN + " for $" + MathUtil.formatDouble(totalBuyPrice));
                    } else {
                        e.getWhoClicked().sendMessage(OSF.PREFIX + ChatColor.RED + "You do not have enough inventory space to purchase this item!");
                    }
                } else {
                    e.getWhoClicked().sendMessage(OSF.PREFIX + ChatColor.RED + "You can't afford this item!");
                }
                //Purchase
            } else if (e.isRightClick() && sellPrice > 0) {
                double totalSellPrice = sellPrice * amount;
                if (InventoryUtil.getTotalItems(e.getWhoClicked().getInventory(), shopItemStack) >= amount) {
                    InventoryUtil.removeInventoryItems(e.getWhoClicked().getInventory(), shopItemStack, amount);
                    OSF.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(e.getWhoClicked().getUniqueId()), totalSellPrice);
                    e.getWhoClicked().sendMessage(OSF.PREFIX + ChatColor.GREEN + "You sold " + ChatColor.WHITE + amount + "x " + ItemUtil.getItemStackName(shopItemStack)
                            + ChatColor.GREEN + " for $" + MathUtil.formatDouble(totalSellPrice));
                } else {
                    e.getWhoClicked().sendMessage(OSF.PREFIX + ChatColor.RED + "You don't have enough items to sell!");
                }
                //Sell
            }
        });
    }
}
