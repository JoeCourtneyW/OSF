package osf.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import osf.OSFCitizens;
import osf.traits.CustomActionTrait;

import java.util.StringJoiner;

public class CommandNPCAction implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("osf.citizens.npcaction")) {
            p.sendMessage(ChatColor.RED + "You don't have permission to run this command");
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "/npcAction [ActionType, 'permission'] [Action, Permission]");
            return false;
        }

        NPC selected = CitizensAPI.getDefaultNPCSelector().getSelected(p);
        if (selected == null) {
            p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "You have not selected an NPC");
            return false;
        }
        if(!selected.hasTrait(CustomActionTrait.class)) {
            p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "The selected NPC does not have the CustomAction trait");
            return false;
        }
        if(args[0].equalsIgnoreCase("permission")){
            if(args.length < 2){
                p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "/npcAction permission [Permission]");
                return false;
            }
            selected.getTrait(CustomActionTrait.class).action_permission = args[1].toLowerCase();
            p.sendMessage(OSFCitizens.PREFIX + ChatColor.YELLOW + "Set the selected NPC's custom action permission to: " + ChatColor.AQUA + args[1].toLowerCase());
        } else {
            NPCActionType actionType = NPCActionType.valueOf(args[0].toUpperCase());
            if (actionType == null) {
                p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "Valid Action Types: [SERVER_COMMAND, PLAYER_COMMAND]");
                return false;
            }
            if (args.length < 2) {
                p.sendMessage(OSFCitizens.PREFIX + ChatColor.RED + "/npcAction " + args[0].toUpperCase() + "[Command]");
                return false;
            }
            String npcCommand = getStringFromArgs(args, 1);
            selected.getTrait(CustomActionTrait.class).npc_action_type = actionType.toString();
            selected.getTrait(CustomActionTrait.class).command = npcCommand;
            p.sendMessage(OSFCitizens.PREFIX + ChatColor.YELLOW + "Set the selected NPC's custom action to: " + ChatColor.AQUA + "[" + actionType.toString() + "] " + npcCommand);
        }
        return false;
    }
    private String getStringFromArgs(String[] args, int startIndex) {
        StringJoiner string = new StringJoiner(" ");
        for (int i = startIndex; i < args.length; i++) {
            string.add(args[i]);
        }
        return string.toString();
    }
}
