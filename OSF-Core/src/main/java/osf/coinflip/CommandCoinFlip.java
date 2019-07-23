package osf.coinflip;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import osf.OSF;
import osf.commands.OSFCommand;
import osf.utils.MathUtil;

public class CommandCoinFlip extends OSFCommand {

    public CommandCoinFlip() {
        super("coinflips", true, "osf.coinflip", "/cf <bet>");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        Player p = getPlayer();
        if (args.length == 0) {
            //Show CoinFlipGUI
            CoinFlipGUI.INVENTORY.open(p);
            return;
        }else if (args.length > 1) {
            sendUsage();
            return;
        }


        //Args must be equal to 1 below

        if(args[0].equalsIgnoreCase("cancel")){
            if(CoinFlip.getBets().containsKey(p.getUniqueId())){
                CoinFlip.removeBet(p.getUniqueId());
                respond(ChatColor.GREEN + "Your bet has been removed");
                return;
            } else {
                respond(ChatColor.RED + "You do not have a currently active bet");
                return;
            }
        }

        double bet;
        if(!MathUtil.isNumeric(args[0])){
            respond(ChatColor.RED + args[0] + " is not a valid number");
            return;
        }
        bet = Integer.parseInt(args[0]);

        if (!OSF.getInstance().getEconomy().has(p, bet)) {
            respond(ChatColor.RED + "You don't have enough money to make that bet");
            return;
        }

        if(CoinFlip.getBets().containsKey(p.getUniqueId())) {
            respond(ChatColor.RED + "You already have a bet placed");
            return;
        }

        CoinFlip.addBet(new Bet(p, (short) 0, bet));
        respond(ChatColor.GREEN + "You have placed a bet of" + bet + ".");
    }
}
