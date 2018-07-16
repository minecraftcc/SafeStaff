package ml.bmlzootown.Tasks;

import ml.bmlzootown.SafeStaff;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Brandon on 2/28/2016.
 */
public class AirFreshener {
    public static BukkitTask air;

    public static void airFreshener(final Player p, final SafeStaff pl) {

        air = SafeStaff.plugin.getServer().getScheduler().runTaskTimer(SafeStaff.plugin, new Runnable()  {

            @Override
            public void run() {
                Block phead = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1.0, p.getLocation().getZ()).getBlock();
                if (phead.getType() == Material.AIR) {
                    killTask(air);
                } else {
                    p.setRemainingAir(p.getMaximumAir());
                }
            }
        }, 0L, 5L);
    }

    public static void killTask(BukkitTask t) {
        t.cancel();
    }

}
