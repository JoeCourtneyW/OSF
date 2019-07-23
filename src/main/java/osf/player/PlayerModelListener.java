package osf.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class PlayerModelListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST) // Make this lowest so that it gets called FIRST
    public void createConfigOnJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String ip = event.getAddress().getHostAddress();
        String name = player.getName();
        PlayerModel playerModel;
        if (!PlayerModel.playerExists(uuid)) { // First time the player has joined
            PlayerModel.newPlayer(uuid, name, ip);

            //TODO: First join welcome message
        } else { // They exist, check their IP and Username
            playerModel = PlayerModel.getPlayerModel(uuid);
            if (!(playerModel.getDatabaseCell("IP").getValue().asString().equalsIgnoreCase(ip)
                    && playerModel.getDatabaseCell("NAME").getValue().asString().equalsIgnoreCase(name))) { //If their IP or name has changed since their last time joining
                playerModel.getDatabaseCell("IP").setValue(ip);
                playerModel.getDatabaseCell("NAME").setValue(name);
                playerModel.pushRowToDatabase();
            }
        }

    }
}
