package osf.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.pmw.tinylog.Logger;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


public class ConcurrentUtil {

    private JavaPlugin plugin;

    public ConcurrentUtil(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
    }

    public void async(Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public <T> void async(Callable<T> callable, Consumer<T> consumer) {
        async(() -> {
            try {
                consumer.accept(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("Exception during async Callable");
            }
        });
    }

    public <T> void async(Consumer<T> consumer, T data) {
        async(() -> consumer.accept(data));
    }


}
