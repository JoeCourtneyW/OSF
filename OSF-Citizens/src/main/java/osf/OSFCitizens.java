package osf;

import com.sainttx.holograms.api.HologramManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import osf.commands.CommandCustomSkin;
import osf.commands.CommandNPCAction;
import osf.traits.NameTagFixTrait;
import osf.traits.CustomActionTrait;

public class OSFCitizens extends JavaPlugin {
    public static final String PREFIX = "§7[§a§lOSF-Citizens§7] §f";

    private static OSFCitizens instance;
    public static HologramManager hologramManager;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§7[§aOSF-Citizens§7] §rhas been enabled");
        instance = this;

        registerCitizensTraits();

        hologramManager = OSF.hologramManager;
        registerCommands();

    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§7[§aOSF-Citizens§7] §rhas been disabled");
        deregisterCitizensTraits();
        instance = null;
    }

    public static OSFCitizens getInstance() {
        return instance;
    }

    private static TraitInfo nameTagFixTraitInfo;
    private static TraitInfo customActionTraitInfo;

    private void registerCommands() {
        getCommand("customskin").setExecutor(new CommandCustomSkin());
        getCommand("npcaction").setExecutor(new CommandNPCAction());
    }

    private void registerEventListeners() {
    }

    private void registerCitizensTraits() {
        nameTagFixTraitInfo = TraitInfo.create(NameTagFixTrait.class).withName("nametagfix");
        CitizensAPI.getTraitFactory().registerTrait(nameTagFixTraitInfo);
        customActionTraitInfo = TraitInfo.create(CustomActionTrait.class).withName("customaction");
        CitizensAPI.getTraitFactory().registerTrait(customActionTraitInfo);

    }

    private void deregisterCitizensTraits() {
        CitizensAPI.getTraitFactory().deregisterTrait(nameTagFixTraitInfo);
        CitizensAPI.getTraitFactory().deregisterTrait(customActionTraitInfo);
    }
}
