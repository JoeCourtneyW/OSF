package osf.crates.crateitems;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import osf.utils.ItemBuilder;
import osf.utils.MathUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrateRewardCommand implements CrateReward {

    private static final Pattern NUMBER_RANGE_PATTERN = Pattern.compile("%([0-9]+)-([0-9]+)%");

    private double chance;
    private ItemStack displayItem;
    private String command;
    private String message;

    public CrateRewardCommand(JsonNode crateItemJson) {
        this.chance = crateItemJson.get("CHANCE").asDouble();
        this.command = crateItemJson.get("COMMAND").asText();
        this.message = crateItemJson.get("MESSAGE").asText();
        displayItem = new ItemBuilder(Material.getMaterial(crateItemJson.get("DISPLAY_MATERIAL").asText()))
                .displayName(ChatColor.translateAlternateColorCodes('&', crateItemJson.get("NAME").asText()))
                .grab();
        if(crateItemJson.has("DISPLAY_DATA"))
            displayItem.setDurability((short) crateItemJson.get("DISPLAY_DATA").asInt());
    }

    @Override
    public String giveToPlayer(Player player) {
        String playerCommand = command;
        playerCommand = playerCommand.replaceAll("%player%", player.getName());

        Matcher numberRange = NUMBER_RANGE_PATTERN.matcher(playerCommand);
        int randomAmount = 0;
        if(numberRange.find()){
            randomAmount = MathUtil.rand(Integer.parseInt(numberRange.group(1)), Integer.parseInt(numberRange.group(2)));
            playerCommand = playerCommand.replaceAll(NUMBER_RANGE_PATTERN.pattern(), "" + randomAmount);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand);
        return message.replaceAll("%amount%", "" + randomAmount);
    }

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public ItemStack getDisplayItem() {
        return displayItem;
    }
}
