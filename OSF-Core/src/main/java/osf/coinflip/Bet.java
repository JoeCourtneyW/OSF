package osf.coinflip;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import osf.utils.ItemBuilder;

import java.util.UUID;


public class Bet {
    private UUID player;
    private short woolColor;
    private double betAmount;
    private long timeCreated;

    public Bet(Player player, short woolColor, double betAmount) {
        this.player = player.getUniqueId();
        this.woolColor = woolColor;
        this.betAmount = betAmount;
        this.timeCreated = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public short getWoolColor() {
        return woolColor;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public ItemStack getItemStack() {
        ItemBuilder item = new ItemBuilder(Material.WOOL).data(woolColor);
        item.displayName("§e§lChallenge " + Bukkit.getPlayer(player).getName());
        item.addLoreLine("§fBet: §a§u$" + betAmount)
                .addLoreSpacer()
                .addLoreLine("§b§iClick to accept challenge");

        return item.grab();
    }
}
