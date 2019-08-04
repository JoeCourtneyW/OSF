package osf.potions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InfinitePotionEffect {
    PotionEffectType potionEffectType;
    int potionEffectAmplifer;

    public InfinitePotionEffect(PotionEffectType potionEffectType, int potionEffectAmplifer) {
        this.potionEffectType = potionEffectType;
        this.potionEffectAmplifer = potionEffectAmplifer;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    public int getPotionEffectAmplifer() {
        return potionEffectAmplifer;
    }

    public void giveToPlayer(Player player) {
        player.addPotionEffect(new PotionEffect(potionEffectType, potionEffectAmplifer, Integer.MAX_VALUE));
    }

    public String getDisplayName() {
        return this.potionEffectType.getName() + " " + this.getPotionEffectAmplifer();
    }

    public ItemStack generateItemStack() {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setColor(potionEffectType.getColor());
        potionMeta.setDisplayName(ChatColor.AQUA + getDisplayName() + ChatColor.GOLD + "" + ChatColor.BOLD + " (INFINITE)");
        potion.setItemMeta(potionMeta);
        return potion;
    }

    @Override
    public boolean equals(Object otherObject) {
        if(otherObject instanceof InfinitePotionEffect) {
            InfinitePotionEffect otherInfinitePotionEffect = (InfinitePotionEffect) otherObject;
            return otherInfinitePotionEffect.getPotionEffectType() == this.getPotionEffectType()
                    && otherInfinitePotionEffect.getPotionEffectAmplifer() == this.getPotionEffectAmplifer();
        } else {
            return false;
        }
    }
    
}
