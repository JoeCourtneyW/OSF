package osf.crates;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import osf.OSF;
import osf.commands.OSFCommand;
import osf.player.PlayerModel;
import osf.utils.MathUtil;

public class CommandCrateKey extends OSFCommand {

    public CommandCrateKey() {
        super("cratekey", false, "osf.cratekey", "/crateKey [Player] [CrateID] [Amount]");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if(args.length < 3){
            sendUsage();
            return;
        }

        Player target = getTargetPlayerFromArg(0);
        Crate crate = OSF.crateManager.getCrateById(args[1]);

        if(crate == null) {
            respond(ChatColor.RED + "That crate does not exist");
            return;
        }

        int amount;
        if(MathUtil.isNumeric(args[2])){
            amount = Integer.parseInt(args[2]);
        } else {
            respond(ChatColor.RED + "Key amount must be an integer!");
            return;
        }

        PlayerModel.getPlayerModel(target).giveCrateKeys(crate.getId(), amount);
        respond(ChatColor.GREEN + "" + amount + " " + ChatColor.AQUA + crate.getName() + ChatColor.GREEN + " keys have been added to " + target.getName() + "'s balance");
    }
}
