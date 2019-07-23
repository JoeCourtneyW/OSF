package osf.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSafeRestart extends OSFCommand implements CommandExecutor {

    public CommandSafeRestart() {
        super("saferestart", false, "osf.saferestart");
    }

    public void run(CommandSender sender, String[] args) {

    }
}
