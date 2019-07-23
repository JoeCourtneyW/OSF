package osf.traits;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketTypeEnum;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.TextLine;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import net.minecraft.server.v1_12_R1.PacketPlayOutMount;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import osf.OSFCitizens;

public class NameTagFixTrait extends Trait {

    public NameTagFixTrait() {
        super("nametagfix");
        plugin = JavaPlugin.getPlugin(OSFCitizens.class);
    }

    OSFCitizens plugin;


    @Persist
    public String nametag = "";

    // Called every tick
    @Override
    public void run() {

    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        if(event.getNPC() == this.getNPC()) {
            if(event.getClicker().getInventory().getItemInMainHand() != null
                    && event.getClicker().getInventory().getItemInMainHand().getType() == Material.NAME_TAG
                    && event.getClicker().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
                    && event.getClicker().hasPermission("osf.citizens.nametag")){
                this.nametag = event.getClicker().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                event.getNPC().despawn();
                event.getNPC().spawn(event.getNPC().getStoredLocation());
            }
        }
    }

    //Run code when your trait is attached to a NPC.
    //This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + " has been assigned NameTagFix Trait!");
    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
    @Override
    public void onDespawn() {
        if (!this.getNPC().getEntity().getPassengers().isEmpty())
            this.getNPC().getEntity().getPassengers().get(0).remove();
        if (OSFCitizens.hologramManager.getHologram(this.getNPC().getUniqueId().toString()) != null)
            OSFCitizens.hologramManager.deleteHologram(OSFCitizens.hologramManager.getHologram(this.getNPC().getUniqueId().toString()));
    }

    //Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {
        addArmorStand();
        Hologram hologram = new Hologram(this.getNPC().getUniqueId().toString(), this.getNPC().getStoredLocation().clone().add(0, 1.8, 0));
        hologram.addLine(new TextLine(hologram, nametag));
        OSFCitizens.hologramManager.addActiveHologram(hologram);

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
        if (!this.getNPC().getEntity().getPassengers().isEmpty())
            this.getNPC().getEntity().getPassengers().get(0).remove();
        if (OSFCitizens.hologramManager.getHologram(this.getNPC().getUniqueId().toString()) != null)
            OSFCitizens.hologramManager.deleteHologram(OSFCitizens.hologramManager.getHologram(this.getNPC().getUniqueId().toString()));
    }

    public void addArmorStand() {
        ArmorStand armorStand = (ArmorStand) this.getNPC().getEntity().getWorld().spawnEntity(this.getNPC().getStoredLocation(), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setArms(false);
        armorStand.setMarker(true);
        this.getNPC().getEntity().addPassenger(armorStand);
    }
}
