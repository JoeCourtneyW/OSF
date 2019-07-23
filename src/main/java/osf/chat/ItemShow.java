package osf.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import osf.OSF;
import osf.utils.ItemUtil;



public class ItemShow implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.isCancelled()) {
            String keyword = OSF.getConfiguration().getItemShowKeyword();
            String message = event.getMessage();
            if (message.toLowerCase().contains(keyword)) {
                Player sender = event.getPlayer();
                if (sender.getInventory().getItemInMainHand() != null && sender.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    ItemStack item = sender.getInventory().getItemInMainHand();
                    String itemJson = ItemUtil.itemStackToJsonReflection(item);
                    String itemName = ItemUtil.getItemStackName(item);

                    int messageIndex = message.toLowerCase().indexOf(keyword);
                    String afterItemText = message.substring(messageIndex + keyword.length()-1, message.length());

                    //Json to configure keyword
                    TextComponent jsonMessage = new TextComponent(itemName);
                    BaseComponent[] hoverEventComponents = new BaseComponent[]{
                            new TextComponent(itemJson)};
                    jsonMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents));

                    //All text after keyword
                    ChatColor color = ChatColor.GRAY;
                    if(!OSF.getInstance().getChat().getPlayerSuffix(sender).isEmpty())
                        color = ChatColor.getByChar(OSF.getInstance().getChat().getPlayerSuffix(sender).charAt(1));
                    TextComponent afterText = new TextComponent(afterItemText);
                    afterText.setColor(net.md_5.bungee.api.ChatColor.getByChar(color.getChar()));

                    event.setCancelled(true);
                    for (Player recipient : event.getRecipients()) {
                        //Get default chat format for sender to each recipient (need faction relation color)
                        String perRecipientformat = ChatFormatter.formatChatMessage(sender, recipient, "");
                        //Text and formatting before keyword
                        String beforeItemText = message.substring(0, messageIndex + 1);
                        //Converts legacy chatcolor codes to avoid displacement via bold text
                        BaseComponent[] beforeText = TextComponent.fromLegacyText(perRecipientformat + beforeItemText);

                        //Copy all the textcomponents into a single array to send to the player
                        BaseComponent[] allText = new BaseComponent[beforeText.length+2];
                        System.arraycopy(beforeText, 0, allText, 0, beforeText.length);
                        allText[allText.length-2] = jsonMessage;
                        allText[allText.length-1] = afterText;
                        //Send to all recipients
                        recipient.spigot().sendMessage(allText);

                    }
                }

            }
        }
    }

    //TODO: gui lib
}
