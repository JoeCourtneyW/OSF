package osf.traits;

import me.clip.placeholderapi.PlaceholderAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import osf.OSFCitizens;
import osf.commands.NPCActionType;

public class CustomActionTrait extends Trait {
    public CustomActionTrait() {
        super("customaction");
        plugin = JavaPlugin.getPlugin(OSFCitizens.class);
    }

    OSFCitizens plugin;


    @Persist
    public String command = "";

    @Persist
    public String npc_action_type = "";

    @Persist
    public String action_permission = "";

    public boolean hasPermission(Player player) {
        return player.hasPermission(action_permission);
    }

    public NPCActionType getActionType() {
        return NPCActionType.valueOf(npc_action_type);
    }

    // Called every tick
    @Override
    public void run() {

    }

    @EventHandler
    public void performAction(NPCRightClickEvent event) {
        if (event.getNPC() == this.getNPC()) {
            if (!this.action_permission.isEmpty() || event.getClicker().hasPermission(this.action_permission)) {
                if (!this.command.isEmpty()) {
                    if (NPCActionType.valueOf(npc_action_type) == NPCActionType.PLAYER_COMMAND)
                        event.getClicker().performCommand(PlaceholderAPI.setPlaceholders(event.getClicker(), command));
                    else if (NPCActionType.valueOf(npc_action_type) == NPCActionType.SERVER_COMMAND)
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(event.getClicker(), command));
                }
            }
        }
    }

    //Run code when your trait is attached to a NPC.
    //This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + " has been assigned CustomAction Trait!");
    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
    @Override
    public void onDespawn() {
    }

    //Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
    }
}
