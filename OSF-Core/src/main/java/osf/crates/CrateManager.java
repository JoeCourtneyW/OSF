package osf.crates;

import com.fasterxml.jackson.databind.JsonNode;
import osf.gui.InventoryManager;

import java.util.Collection;
import java.util.HashMap;

public class CrateManager {

    private JsonNode cratesFile;
    private InventoryManager inventoryManager;
    private CrateGUI crateGUI;

    private HashMap<String, Crate> crates;


    public CrateManager(JsonNode cratesFile, InventoryManager inventoryManager) {
        crates = new HashMap<>();

        cratesFile.get("crates").fields().forEachRemaining((stringJsonNodeEntry ->
                registerCrate(new Crate(stringJsonNodeEntry.getKey(), stringJsonNodeEntry.getValue()))
        ));


        crateGUI = new CrateGUI(inventoryManager, this);
    }

    private void registerCrate(Crate crate) {
        crates.put(crate.getId(), crate);
    }

    public Crate getCrateById(String crateId) {
        return crates.getOrDefault(crateId, null);
    }

    public Collection<Crate> getCrates() {
        return crates.values();
    }
}
