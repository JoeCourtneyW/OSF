package osf.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandVote extends OSFCommand implements CommandExecutor {

    private final String[] VOTING_LINKS = {"Needs", "to", "be", "configured"};

    public CommandVote() {
        super("vote", true);
    }

    public void run(CommandSender sender, String[] args) {
        respond(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "Voting Links:");

        for (String link : VOTING_LINKS) {
            respond(ChatColor.YELLOW + "- " + link);
        }
    }
}
