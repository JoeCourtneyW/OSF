package osf.crates.crateitems;

import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import osf.OSF;
import osf.potions.InfinitePotionEffect;
import osf.utils.InventoryUtil;
import osf.utils.ItemBuilder;
import osf.utils.ItemUtil;
import osf.utils.MathUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrateRewardItem implements CrateReward {

    private static final Pattern NUMBER_RANGE_PATTERN = Pattern.compile("%([0-9]+)-([0-9]+)%");

    private double chance;
    private ItemStack displayItem;
    private JsonNode reward;
    private String message;


    public CrateRewardItem(JsonNode crateItemJson) {
        this.chance = crateItemJson.get("CHANCE").asDouble();
        this.reward = crateItemJson.get("REWARD");
        this.message = crateItemJson.get("MESSAGE").asText();
        displayItem = new ItemBuilder(Material.getMaterial(crateItemJson.get("DISPLAY_MATERIAL").asText()))
                .displayName(ChatColor.translateAlternateColorCodes('&', crateItemJson.get("NAME").asText()))
                .grab();
        if(crateItemJson.has("DISPLAY_DATA"))
            displayItem.setDurability((short) crateItemJson.get("DISPLAY_DATA").asInt());
    }

    @Override
    public String giveToPlayer(Player player) {
        ItemStack rewardItem = getItemStackRewardFromJson(reward);
        if (InventoryUtil.hasInventorySpace(player)) {
            player.getInventory().addItem(rewardItem);
        } else {
            player.getWorld().dropItem(player.getLocation(), rewardItem);
            player.sendMessage(OSF.PREFIX + ChatColor.RED + "" + ChatColor.UNDERLINE + "You did not have any inventory space, so your crate reward was dropped on the ground!");
        }
        return message.replaceAll("%amount%", "" + rewardItem.getAmount()).replaceAll("%name%", ItemUtil.getItemStackName(rewardItem));
    }

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public ItemStack getDisplayItem() {
        return displayItem;
    }

    private ItemStack getItemStackRewardFromJson(JsonNode rewardNode) {
        Material type = Material.getMaterial(rewardNode.get("TYPE").asText());
        ItemBuilder reward = new ItemBuilder(type);
        if(type == Material.MOB_SPAWNER) {
            reward = new ItemBuilder(OSF.silkUtil.setSpawnerType(reward.grab(), (short) rewardNode.get("ENTITY_ID").asInt(), "&e%creature% &fSpawner"));
        }else if(type == Material.POTION) {
            if(rewardNode.has("INFINITE") && rewardNode.get("INFINITE").asBoolean()) {
                PotionEffectType potionEffectType = PotionEffectType.getByName(rewardNode.get("EFFECT_TYPE").asText());
                int potionEffectAmplifier = rewardNode.get("EFFECT_AMPLIFIER").asInt();
                reward = new ItemBuilder(new InfinitePotionEffect(potionEffectType, potionEffectAmplifier).generateItemStack());
            }
        }

        if (rewardNode.has("DATA")) {
            reward.data((short) rewardNode.get("DATA").asInt());
        }

        if (rewardNode.has("NAME")) {
            reward.displayName(rewardNode.get("NAME").asText());
        }

        String amount = rewardNode.get("AMOUNT").asText();
        if (!MathUtil.isNumeric(amount)) {
            Matcher numberRange = NUMBER_RANGE_PATTERN.matcher(amount);

            if (numberRange.find()) {
                amount = amount.replaceAll(NUMBER_RANGE_PATTERN.pattern(), "" + MathUtil.rand(Integer.parseInt(numberRange.group(1)), Integer.parseInt(numberRange.group(2))));
            }
            reward.amount(Integer.parseInt(amount));
        } else {
            reward.amount(Integer.parseInt(amount));
        }
        ItemStack builtReward = reward.grab();
        if(rewardNode.has("NBT")) {
            NBTItem nbtItem = new NBTItem(builtReward);
            for(JsonNode nbtNode : rewardNode.get("NBT")) {
                nbtItem.setString(nbtNode.get("key").asText(), nbtNode.get("value").asText());
            }
            builtReward = nbtItem.getItem();
        }
        return builtReward;
    }
}
