package osf.utils;




import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.logging.Level;

public class ItemUtil {
    public static String itemStackToJson(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack craftItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = new NBTTagCompound();
        compound = craftItem.save(compound);
        return compound.toString();
    }

    public static String getItemStackDefaultName(ItemStack item) {
        //net.minecraft.server.v1_8_R3.ItemStack craftItem = CraftItemStack.asNMSCopy(item);
        return getItemStackDefaultNameReflection(item);
    }

    public static String getItemStackDefaultNameReflection(ItemStack item) {
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
        Method getNameItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "getName");
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object craftItemDefaultName;

        try {
            nmsItemStackObj = asNMSCopyMethod.invoke(null, item);
            craftItemDefaultName = getNameItemStackMethod.invoke(nmsItemStackObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to serialize itemstack to nms item", t);
            return null;
        }
        return craftItemDefaultName.toString();
    }

    public static String getItemStackName(ItemStack item) {
        if (item.getItemMeta().hasDisplayName())
            return item.getItemMeta().getDisplayName();
        else
            return getItemStackDefaultNameReflection(item);
    }
    public static String itemStackToJsonReflection(ItemStack item) {
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, item);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to serialize itemstack to nms item", t);
            return null;
        }

        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }
}
