package osf.crates;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import osf.commands.OSFCommand;

public class CommandCrates extends OSFCommand {

    public CommandCrates() {
        super("crates", false, "osf.crates");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if(sender instanceof Player)
                CrateGUI.INVENTORY.open(getPlayer());
            else
                respond(ChatColor.RED + "Only players can open the gui for themselves");
        } else {
            Player target = getTargetPlayerFromArg(0);
            if (target == null) {
                respond(ChatColor.RED + "The target player is not online");
                return;
            }
            CrateGUI.INVENTORY.open(target);
        }
    }
}
