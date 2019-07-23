package osf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEnchanter extends OSFCommand implements CommandExecutor {


    public CommandEnchanter() {
        super("enchanter", false, "osf.enchanter");
    }


    public void run(CommandSender sender, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player)
                getPlayer().openEnchanting(null, true);
            else
                respond(ChatColor.RED + "Only players can open the gui for themselves");
        } else {
            Player target = getTargetPlayerFromArg(0);
            if (target == null) {
                respond(ChatColor.RED + "The target player is not online");
                return;
            }
            target.openEnchanting(null, true);
        }
    }
}
