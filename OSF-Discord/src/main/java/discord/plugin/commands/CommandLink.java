package discord.plugin.commands;

import discord.plugin.OSFDiscord;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import osf.commands.OSFCommand;
import osf.player.PlayerModel;
import osf.utils.MathUtil;

import java.util.HashMap;

public class CommandLink extends OSFCommand {
    public static HashMap<String, PlayerModel> verification = new HashMap<>();

    public CommandLink() {
        super("link", true);
    }

    @Override
    public void run(CommandSender commandSender, String[] args) {
        String verificationNumber = MathUtil.rand(10000, 99999) + "";
        verification.put(verificationNumber, getPlayerModel());
        respond(ChatColor.GRAY + "To link your minecraft account to discord, use the #link-your-account channel and type " + ChatColor.AQUA + "\"-link " + verificationNumber + "\"");
        respond(ChatColor.RED + "This code will expire in 5 minutes");
        new BukkitRunnable() {
            public void run() {
                if(verification.containsKey(verificationNumber)){
                    respond(ChatColor.DARK_RED + "Your verification code has expired, please type /link again if you wish to link your accounts");
                    verification.remove(verificationNumber);
                }
            }
        }.runTaskLater(OSFDiscord.getInstance(), 1000*60*5);
    }
}
