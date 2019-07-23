package osf.shop;



import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import osf.gui.ClickableItem;
import osf.gui.InventoryManager;
import osf.gui.SmartInventory;
import osf.gui.content.InventoryContents;
import osf.gui.content.InventoryProvider;
import osf.shop.categories.*;
import osf.utils.ItemBuilder;

public class ShopGUI implements InventoryProvider {
    public static SmartInventory INVENTORY;

    public ShopGUI(InventoryManager manager) {
        INVENTORY = SmartInventory.builder()
                .id("shop")
                .provider(this)
                .manager(manager)
                .size(6, 9)
                .title(ChatColor.BLUE + "OSF Shop")
                .build();
        new FarmingShop(manager);
        new FoodShop(manager);
        new OreShop(manager);
        new PotionShop(manager);
        new MobDropShop(manager);
        new SpawnerShop(manager);
        new BlockShop(manager);
        new CombatShop(manager);
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        ItemBuilder borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 15).displayName(" ");
        contents.fillBorders(ClickableItem.empty(borderItem.grab()));
        //Categories
        ItemBuilder spawners = new ItemBuilder(Material.MOB_SPAWNER).displayName(ChatColor.RED + "Spawners");
        contents.set(1, 4, ClickableItem.of(spawners.grab(), e -> SpawnerShop.INVENTORY.open(player)));

        ItemBuilder farming = new ItemBuilder(Material.DIAMOND_HOE).displayName(ChatColor.GREEN + "Farming");
        contents.set(2, 3, ClickableItem.of(farming.grab(), e -> FarmingShop.INVENTORY.open(player)));

        ItemBuilder food = new ItemBuilder(Material.COOKED_BEEF).displayName(ChatColor.AQUA + "Food");
        contents.set(2, 5, ClickableItem.of(food.grab(), e -> FoodShop.INVENTORY.open(player)));
        ItemBuilder ores = new ItemBuilder(Material.DIAMOND_BLOCK).displayName(ChatColor.YELLOW + "Ores");
        contents.set(3, 2, ClickableItem.of(ores.grab(), e -> OreShop.INVENTORY.open(player)));

        ItemBuilder potions = new ItemBuilder(Material.POTION).displayName(ChatColor.LIGHT_PURPLE + "Potions");
        contents.set(3, 4, ClickableItem.of(potions.grab(), e -> PotionShop.INVENTORY.open(player)));

        ItemBuilder mobdrops = new ItemBuilder(Material.BONE).displayName(ChatColor.DARK_GRAY + "Mob Drops");
        contents.set(3, 6, ClickableItem.of(mobdrops.grab(), e -> MobDropShop.INVENTORY.open(player)));

        ItemBuilder combat = new ItemBuilder(Material.DIAMOND_SWORD).displayName(ChatColor.DARK_RED + "Combat");
        contents.set(4, 3, ClickableItem.of(combat.grab(), e -> CombatShop.INVENTORY.open(player)));

        ItemBuilder blocks = new ItemBuilder(Material.GRASS).displayName(ChatColor.GRAY + "Blocks");
        contents.set(4, 5, ClickableItem.of(blocks.grab(), e -> BlockShop.INVENTORY.open(player)));
}

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
