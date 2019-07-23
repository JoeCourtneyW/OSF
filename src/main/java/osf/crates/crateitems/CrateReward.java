package osf.crates.crateitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CrateReward {

    //String is the name of the reward given to the player, has to be edited here in case of random amounts
    String giveToPlayer(Player player);

    double getChance();

    ItemStack getDisplayItem();
}
