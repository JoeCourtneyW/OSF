package osf.crates;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import osf.OSF;
import osf.crates.crateitems.CrateReward;
import osf.gui.ClickableItem;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryContents;
import osf.gui.content.InventoryProvider;
import osf.gui.content.SlotIterator;
import osf.gui.content.SlotPos;
import osf.player.PlayerModel;
import osf.utils.ItemBuilder;
import osf.utils.MathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CrateGUI implements InventoryProvider {
    public static SmartInventory INVENTORY;
    public static HashMap<String, CrateContentsGUI> crateContentsGUIMap;
    private CrateManager crateManager;


    public CrateGUI(InventoryManager inventoryManager, CrateManager crateManager) {
        crateContentsGUIMap = new HashMap<>();
        this.crateManager = crateManager;
        INVENTORY = SmartInventory.builder()
                .id("crates")
                .provider(this)
                .manager(inventoryManager)
                .size(3, 9)
                .title(ChatColor.BLUE + "OSF Crates")
                .build();
        Collection<Crate> crates = crateManager.getCrates();
        for (Crate crate : crates) {
            crateContentsGUIMap.put(crate.getId(), new CrateContentsGUI(inventoryManager, crate));
        }
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        ItemBuilder borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 15).displayName(" ");
        contents.fillBorders(ClickableItem.empty(borderItem.grab()));

        Collection<Crate> crates = crateManager.getCrates();

        ClickableItem[] items = new ClickableItem[crates.size()];
        contents.pagination().setItemsPerPage(7);

        //Crates
        int counter = 0;
        for (Crate crate : crates) {
            items[counter] = ClickableItem.of(getCrateDisplayItem(crate, player), e -> {
                if (e.isLeftClick()) {
                    if(PlayerModel.getPlayerModel(player).getCrateKeys(crate.getId()) > 0) {
                        new RouletteGUI(INVENTORY.getManager(), crate).INVENTORY.open(player);
                        PlayerModel.getPlayerModel(player).useCrateKey(crate.getId());
                    }else {
                        player.sendMessage(OSF.PREFIX + ChatColor.RED + "You do not have any keys for this crate!");
                    }
                    e.setCurrentItem(getCrateDisplayItem(crate, player));
                } else if (e.isRightClick()) {
                    crateContentsGUIMap.get(crate.getId()).INVENTORY.open(player);
                }
            });
            counter++;
        }

        contents.pagination().setItems(items);
        SlotIterator shopIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 1));
        shopIterator.allowOverride(false);
        contents.pagination().addToIterator(shopIterator);

        //Page navigators
        if (counter > 7) {
            ItemBuilder previous = new ItemBuilder(Material.ARROW).displayName(ChatColor.AQUA + "Previous Page");
            contents.set(2, 3, ClickableItem.of(previous.grab(),
                    e2 -> INVENTORY.open(player, contents.pagination().previous().getPage())));
            ItemBuilder next = new ItemBuilder(Material.ARROW).displayName(ChatColor.AQUA + "Next Page");
            contents.set(2, 5, ClickableItem.of(next.grab(),
                    e2 -> INVENTORY.open(player, contents.pagination().next().getPage())));
        }
    }

    public ItemStack getCrateDisplayItem(Crate crate, Player player) {
        ItemBuilder builder = new ItemBuilder(crate.getDisplayItem())
                .displayName(ChatColor.AQUA + crate.getName())
                .addLoreLine(ChatColor.WHITE + "Keys: " + ChatColor.GREEN + crate.getKeysForPlayer(PlayerModel.getPlayerModel(player)));
        builder.addLoreLine(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Right click" + ChatColor.YELLOW + " to show possible rewards");
        builder.addLoreSpacer();
        for (String descriptionLine : crate.getDescription()) {
            builder.addLoreLine(descriptionLine);
        }
        return builder.grab();
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public class CrateContentsGUI implements InventoryProvider {

        public SmartInventory INVENTORY;
        private Crate crate;


        public CrateContentsGUI(InventoryManager manager, Crate crate) {
            this.crate = crate;
            INVENTORY = SmartInventory.builder()
                    .id("crateContents")
                    .provider(this)
                    .manager(manager)
                    .size(6, 9)
                    .title(crate.getName() + " | Contents")
                    .build();
        }

        @Override
        public void init(Player player, InventoryContents contents) {
            ItemBuilder borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 15).displayName(" ");
            contents.fillBorders(ClickableItem.empty(borderItem.grab()));

            for (CrateReward crateReward : crate.getCrateRewards()) {
                contents.add(ClickableItem.empty(crateReward.getDisplayItem()));
            }

            contents.set(5, 4, ClickableItem.of(
                    new ItemBuilder(Material.BARRIER).displayName(ChatColor.RED + "Back to Crates").grab(),
                    e2 -> CrateGUI.INVENTORY.open(player)));

        }

        @Override
        public void update(Player player, InventoryContents contents) {

        }
    }

    public class RouletteGUI implements InventoryProvider {
        public SmartInventory INVENTORY;
        private Crate crate;


        public RouletteGUI(InventoryManager manager, Crate crate) {
            this.crate = crate;
            INVENTORY = SmartInventory.builder()
                    .id("crateRoulette")
                    .provider(this)
                    .manager(manager)
                    .closeable(false)
                    .size(3, 9)
                    .title(crate.getName() + "")
                    .build();
        }

        @Override
        public void init(Player player, InventoryContents contents) {
            ItemBuilder borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 15).displayName(" ");
            contents.fillRow(0, ClickableItem.empty(borderItem.grab()));
            contents.fillRow(2, ClickableItem.empty(borderItem.grab()));
            contents.set(0, 4, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 14).displayName(" ").grab()));
            contents.set(2, 4, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 14).displayName(" ").grab()));
            for (int i = 0; i < 9; i++) {
                contents.set(1, i, ClickableItem.empty(crate.getRandomReward().getDisplayItem()));
            }
        }

        @Override
        public void update(Player player, InventoryContents contents) {
            int state = contents.property("state", 0);
            contents.setProperty("state", state + 1);
            if (state % 3 != 0)
                return;
            if(state < 150) {
                for (int i = 8; i > 0; i--) {
                    contents.set(1, i, contents.get(1, i - 1).get());
                }
                if(state == 135) {
                    CrateReward reward = crate.getRandomReward();
                    contents.setProperty("reward", reward);
                    contents.set(1, 0, ClickableItem.empty(reward.getDisplayItem()));
                } else {
                    contents.set(1, 0, ClickableItem.empty(crate.getRandomReward().getDisplayItem()));
                }
            } else if(state == 150) {
                CrateReward reward = contents.property("reward");
                String message = reward.giveToPlayer(player);
                player.sendMessage(OSF.PREFIX + ChatColor.GRAY +"You received " + ChatColor.translateAlternateColorCodes('&', message) + ChatColor.GRAY + " from the " + crate.getName());
                INVENTORY.setCloseable(true);
            }
        }
    }
}
