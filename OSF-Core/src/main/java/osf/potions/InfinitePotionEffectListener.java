package osf.potions;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import osf.OSF;
import osf.player.PlayerModel;

public class InfinitePotionEffectListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        for(InfinitePotionEffect infinitePotionEffect : PlayerModel.getPlayerModel(event.getPlayer()).getInfinitePotionEffects()) {
            infinitePotionEffect.giveToPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for(InfinitePotionEffect infinitePotionEffect : PlayerModel.getPlayerModel(event.getPlayer()).getInfinitePotionEffects()) {
            infinitePotionEffect.giveToPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if(event.getItem().getType() == Material.MILK_BUCKET) {
            for(InfinitePotionEffect infinitePotionEffect : PlayerModel.getPlayerModel(event.getPlayer()).getInfinitePotionEffects()) {
                infinitePotionEffect.giveToPlayer(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onConsumeInfinitePotion(PlayerItemConsumeEvent event) {
        if(event.getItem().getType() == Material.POTION) {
            NBTItem nbtPotion = new NBTItem(event.getItem());
            if(nbtPotion.hasNBTData() && nbtPotion.hasKey("infinitePotionEffect")) {
                //Apply infinite effect

                InfinitePotionEffect infinitePotionEffect = nbtPotion.getObject("infinitePotionEffect", InfinitePotionEffect.class);
                PlayerModel.getPlayerModel(event.getPlayer()).addInfinitePotionEffect(infinitePotionEffect);
                infinitePotionEffect.giveToPlayer(event.getPlayer());
                event.getPlayer().sendMessage(OSF.PREFIX + ChatColor.GREEN + "You have consumed an infinite potion!");
                event.getPlayer().sendMessage(OSF.PREFIX + ChatColor.GRAY + "You will now have "+ ChatColor.AQUA + infinitePotionEffect.getDisplayName() + ChatColor.GRAY + " for the rest of the season!");
            }
        }
    }
}
