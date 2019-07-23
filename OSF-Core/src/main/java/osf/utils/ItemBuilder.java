package osf.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class ItemBuilder {
    private ItemStack item;

    private String displayname = "";
    private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private String[] lore = new String[0];
    private short data = 0;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder displayName(String displayName) {
        this.displayname = displayName;
        return this;
    }

    public ItemBuilder data(short data) {
        this.data = data;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchant(HashMap<Enchantment, Integer> enchants) {
        enchantments.putAll(enchants);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        String[] lore = new String[this.lore.length + 1];

        for (int i = 0; i < this.lore.length; i++)
            lore[i] = this.lore[i];

        lore[lore.length - 1] = line;

        this.lore = lore;
        return this;
    }

    public ItemBuilder addLoreSpacer() {
        String[] lore = new String[this.lore.length + 1];

        for (int i = 0; i < this.lore.length; i++)
            lore[i] = this.lore[i];

        lore[lore.length - 1] = ChatColor.ITALIC + " ";

        this.lore = lore;
        return this;
    }

    public ItemStack grab() {
        ItemMeta itemMeta = item.getItemMeta();

        if (this.lore.length > 0) {
            itemMeta.setLore(Arrays.asList(this.lore));
        }

        if (displayname.length() > 0) {
            itemMeta.setDisplayName(displayname);
        }
        if (data != 0) {
            item.setDurability(data);
        }
        for (Enchantment e : enchantments.keySet())
            if (e.getMaxLevel() < enchantments.get(e))
                item.addUnsafeEnchantment(e, enchantments.get(e));
            else
                item.addEnchantment(e, enchantments.get(e));

        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemBuilder clone() {
        return new ItemBuilder(item).data(data).displayName(displayname).lore(this.lore)
                .enchant(enchantments);
    }
}
