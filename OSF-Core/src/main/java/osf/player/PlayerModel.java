package osf.player;


import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import osf.OSF;
import osf.database.Cell;
import osf.database.Row;
import osf.database.tables.PlayerDataTable;
import osf.potions.InfinitePotionEffect;

import java.util.*;
import java.util.function.Predicate;

public class PlayerModel {

    private static PlayerDataTable playerDataTable;
    private static ArrayList<PlayerModel> playerModels = new ArrayList<>();

    private UUID uuid;

    private HashMap<String, Integer> crateKeys = new HashMap<>();
    private ArrayList<InfinitePotionEffect> infinitePotionEffects = new ArrayList<>();

    public static void registerPlayerTable(PlayerDataTable playerDataTable) {
        PlayerModel.playerDataTable = playerDataTable;
        playerDataTable.forEach(playerRow -> playerModels
                .add(PlayerModel.loadPlayerModel(playerRow.getPrimaryKey().getValue().asUUID())));

    }

    private PlayerModel(UUID uuid) {
        this.uuid = uuid;
        registerCrateKeys();
        registerInfinitePotionEffects();
    }

    private void registerInfinitePotionEffects() {
        this.infinitePotionEffects = new ArrayList<>();
        String potionList = getDatabaseRow().getCell("INIFINITE_POTION_EFFECTS").getValue().toString();
        String[] potionListSplit = potionList.split(";");
        for(String potionEffect : potionListSplit) {
            if(!potionEffect.isEmpty()) {
                String[] data = potionEffect.split(",");
                String effectType = data[0];
                int amplifier = Integer.parseInt(data[1]);
                this.infinitePotionEffects.add(new InfinitePotionEffect(PotionEffectType.getByName(effectType), amplifier));
            }
        }
    }

    public Collection<InfinitePotionEffect> getInfinitePotionEffects() {
        return Collections.unmodifiableCollection(infinitePotionEffects);
    }

    public void addInfinitePotionEffect(InfinitePotionEffect effect) {
        infinitePotionEffects.add(effect);
        effect.giveToPlayer(getPlayer());
        updateInfinitePotionEffectsCell();
    }

    private void updateInfinitePotionEffectsCell() {
        StringJoiner serialized = new StringJoiner(";");
        for(InfinitePotionEffect infinitePotionEffect : infinitePotionEffects) {
            serialized.add(infinitePotionEffect.getPotionEffectType().toString() + "," + infinitePotionEffect.getPotionEffectAmplifer());
        }
        getDatabaseRow().getCell("INFINITE_POTION_EFFECTS").setValue(serialized.toString());
        pushRowToDatabase();
    }

    private void registerCrateKeys() {
        this.crateKeys = new HashMap<>();
        String keyList = getDatabaseRow().getCell("CRATE_KEYS").getValue().toString();
        String[] crateKeysSplit = keyList.split(";");
        for(String crateKey : crateKeysSplit) {
            if(!crateKey.isEmpty()) {
                String[] data = crateKey.split("=");
                String id = data[0];
                int amount = Integer.parseInt(data[1]);
                crateKeys.put(id, amount);
            }
        }
    }

    public int getCrateKeys(String crateId) {
        if(crateKeys.containsKey(crateId))
            return crateKeys.get(crateId);
        return 0;
    }

    public void useCrateKey(String crateId) {
        if(crateKeys.containsKey(crateId) && crateKeys.get(crateId) > 0)
            crateKeys.put(crateId, crateKeys.get(crateId)-1);
        updateCrateKeysCell();

    }

    public void giveCrateKeys(String crateId, int amount) {
        if(crateKeys.containsKey(crateId))
            crateKeys.put(crateId, crateKeys.get(crateId)+amount);
        else
            crateKeys.put(crateId, amount);
        updateCrateKeysCell();
    }

    private void updateCrateKeysCell() {
        StringJoiner serialized = new StringJoiner(";");
        for(Map.Entry crate : crateKeys.entrySet()) {
            serialized.add(crate.getKey() + "=" + crate.getValue());
        }
        getDatabaseRow().getCell("CRATE_KEYS").setValue(serialized.toString());
        pushRowToDatabase();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public CraftPlayer getCraftPlayer() {
        return (CraftPlayer) Bukkit.getPlayer(uuid);
    }

    public String getName() {
        return getPlayer().getName();
    }

    public Player getNearestPlayer() {
        Player p = getPlayer();
        Player nearestPlayer = p;
        double distanceSquared = Double.MAX_VALUE;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getWorld().getName().equalsIgnoreCase(p.getWorld().getName())) {
                if (p.getLocation().distanceSquared(onlinePlayer.getLocation()) != 0
                        && p.getLocation().distanceSquared(onlinePlayer.getLocation()) < distanceSquared) {
                    distanceSquared = p.getLocation().distanceSquared(onlinePlayer.getLocation());
                    nearestPlayer = onlinePlayer;
                }
            }
        }
        return nearestPlayer;
    }

    public void addNoReductionEffect(PotionEffect effect) {
        Player p = getPlayer();
        for (PotionEffect activeEffect : p.getActivePotionEffects()) {
            if (activeEffect.getDuration() > 100000)
                continue;
            if (activeEffect.getType() == effect.getType()) {
                if (activeEffect.getAmplifier() > effect.getAmplifier()
                        || (activeEffect.getAmplifier() == effect.getAmplifier()
                        && activeEffect.getDuration() > effect.getDuration()))
                    continue;
                p.removePotionEffect(effect.getType());
                p.addPotionEffect(effect);
            }
        }
        p.addPotionEffect(effect);
    }


    public Row getDatabaseRow() {
        return playerDataTable.getRow(getUUID());
    }

    public Cell.CellValue getDatabaseValue(String column) {
        return getDatabaseRow().getCell(column).getValue();
    }

    public Cell getDatabaseCell(String column) {
        return getDatabaseRow().getCell(column);
    }

    public void pushRowToDatabase() {
        playerDataTable.updateEntry(getDatabaseRow());
    }

    public void sendMessage(String message) {
        getPlayer().sendMessage(message);
    }

    public void sendPrefixedMessage(String message) {
        getPlayer().sendMessage(OSF.PREFIX + message);
    }

    public static Collection<PlayerModel> getAllPlayers() {
        return Collections.unmodifiableCollection(playerModels);
    }

    public static Collection<PlayerModel> getPlayers(Predicate<PlayerModel> predicate) {
        Collection<PlayerModel> players = new ArrayList<>();
        for (PlayerModel player : getAllPlayers()) { // Loops through every player
            if (predicate.test(player)) { // Tests each player with the given predicate
                players.add(player); // Those who pass match the filter
            }
        }
        return Collections.unmodifiableCollection(players);
    }

    public static Collection<PlayerModel> getOnlinePlayers() {
        return getPlayers(PlayerModel::isOnline);
    }

    public static boolean playerExists(UUID uuid) {
        return getPlayerModel(uuid) != null;
    }

    public static PlayerModel loadPlayerModel(UUID uuid) {
        return new PlayerModel(uuid);
    }

    public static PlayerModel newPlayer(Player p) {
        return newPlayer(p.getUniqueId(), p.getName(), p.getAddress().getHostName());
    }

    public static PlayerModel newPlayer(UUID uuid, String name, String ip) {
        Row playerRow = playerDataTable.getDefaultRow();

        playerRow.getPrimaryKey().setValue(uuid); // Set the dynamic default values and primary keys
        playerRow.getCell("NAME").setValue(name);
        playerRow.getCell("IP").setValue(ip);

        PlayerModel playerModel = loadPlayerModel(uuid);// Adds this player to the list of models

        playerDataTable.newEntry(playerRow); // Creates new row in contents and actual database

        return playerModel;
    }

    public static PlayerModel getPlayerModel(Player p) {
        return getPlayerModel(p.getUniqueId());
    }

    public static PlayerModel getPlayerModel(UUID uuid) {
        for (PlayerModel pm : getAllPlayers()) {
            if (pm.getUUID().toString().equals(uuid.toString()))
                return pm;
        }
        return null;
    }

    public static PlayerModel getPlayerModelByDiscordSnowflake(String snowflake) {
        for(PlayerModel pm : getAllPlayers()) {
            if(pm.getDatabaseRow().getCell("DISCORD_SNOWFLAKE").getValue().asString().equalsIgnoreCase(snowflake))
                return pm;
        }
        return null;
    }

    public boolean equals(PlayerModel p) {
        return (p.getUUID().equals(getUUID()));
    }
}
