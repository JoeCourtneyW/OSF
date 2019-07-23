package osf.shop;


import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import osf.commands.OSFCommand;

public class CommandShop extends OSFCommand implements CommandExecutor {

    public CommandShop() {
        super("shop", true);
    }
    public void run(CommandSender sender, String[] args) {
        ShopGUI.INVENTORY.open(getPlayer());
    }

}
