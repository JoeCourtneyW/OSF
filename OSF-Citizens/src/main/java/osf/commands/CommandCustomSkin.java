package osf.commands;

import com.fasterxml.jackson.databind.JsonNode;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import osf.OSFCitizens;
import osf.utils.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class CommandCustomSkin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("osf.citizens.customskin")) {
            p.sendMessage(ChatColor.RED + "You don't have permission to run this command");
            return false;
        }
        if (args.length < 1) {
            p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "You must enter a valid MineSkin url id");
            return false;
        }
        new BukkitRunnable() {
            public void run() {
                String apiUrl = "https://api.mineskin.org/generate/url";
                HttpURLConnection connection = HttpUtil.connect(apiUrl);
                String jsonPayload = "{\"url\": \"http://textures.minecraft.net/texture/" + args[0] + "\"}";
                InputStream response;
                try {
                    response = HttpUtil.post(connection, jsonPayload);
                } catch (IOException e) {
                    e.printStackTrace();
                    p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "An unexpected error has occured, please check the console");
                    return;
                }
                JsonNode jsonResponse = HttpUtil.parse(response);

                String skinTexture = jsonResponse.get("data").get("texture").get("value").asText();
                String skinSignature = jsonResponse.get("data").get("texture").get("signature").asText();

                NPC selected = CitizensAPI.getDefaultNPCSelector().getSelected(p);
                if (selected == null) {
                    p.sendMessage(OSFCitizens.PREFIX + "You have not selected an NPC");
                        return;
                }
                selected.data().setPersistent("player-skin-textures", skinTexture);
                selected.data().setPersistent("player-skin-signature", skinSignature);
                selected.data().setPersistent("player-skin-use-latest", false);
                p.sendMessage(OSFCitizens.PREFIX + "You have changed the skin on " + selected.getFullName());
                new BukkitRunnable() {
                    public void run() {
                        selected.despawn();
                        selected.spawn(selected.getStoredLocation());
                    }
                }.runTask(OSFCitizens.getInstance());
            }
        }.runTaskAsynchronously(OSFCitizens.getInstance());

        return false;
    }
}
