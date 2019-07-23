package osf.crates;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import osf.OSF;
import osf.crates.crateitems.CrateReward;
import osf.crates.crateitems.CrateRewardCommand;
import osf.crates.crateitems.CrateRewardItem;
import osf.player.PlayerModel;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Crate {
    public NavigableMap<Double, CrateReward> rewards;
    private double totalWeight = 0.0;
    private Random random;

    private String id;
    private String name;
    private ItemStack displayItem;
    private String[] description;
    private ItemStack icon;


    public Crate(String id, JsonNode crateJson) {
        this.random = new Random();
        this.rewards = new TreeMap<>();

        this.id = id;
        this.name = ChatColor.translateAlternateColorCodes('&', crateJson.get("name").asText());
        this.displayItem = new ItemStack(Material.getMaterial(crateJson.get("display_material").asText()));
        JsonNode descriptionJson = crateJson.get("description");
        this.description = new String[descriptionJson.size()];
        for(int i = 0; i < descriptionJson.size(); i++) {
            description[i] = ChatColor.translateAlternateColorCodes('&', descriptionJson.get(i).asText());
        }

        for(JsonNode crateRewardJson : crateJson.get("rewards")) {
            CrateReward crateReward;

            if(crateRewardJson.get("TYPE").asText().equalsIgnoreCase("command"))
                crateReward = new CrateRewardCommand(crateRewardJson);
            else
                crateReward = new CrateRewardItem(crateRewardJson);

            add(crateReward);
        }
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    public String[] getDescription() {
        return this.description;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public Collection<CrateReward> getCrateRewards() {
        return rewards.values();
    }

    private void add(CrateReward crateReward) {
        if (crateReward.getChance() <= 0) return;
        totalWeight += crateReward.getChance();
        rewards.put(totalWeight, crateReward);
    }

    public CrateReward getRandomReward() {
        double value = random.nextDouble() * totalWeight;
        return rewards.higherEntry(value).getValue();
    }

    public boolean open(Player player) {
        if(getKeysForPlayer(PlayerModel.getPlayerModel(player)) > 0){
            String message = getRandomReward().giveToPlayer(player);
            PlayerModel.getPlayerModel(player).useCrateKey(id);
            player.sendMessage(OSF.PREFIX + "You received " + ChatColor.translateAlternateColorCodes('&', message) + " from the " + getName());
            return true;
        } else {
            return false;
        }
    }

    public int getKeysForPlayer(PlayerModel playerModel) {
        return playerModel.getCrateKeys(id);
    }
}
