package ml.bmlzootown.Tasks;

import ml.bmlzootown.SafeStaff;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Brandon on 2/28/2016.
 */
public class AntiBurnCreme {

    public static BukkitTask burn;

    public static void noBurn(final Player p, final Double health, final Integer food) {

        burn = SafeStaff.plugin.getServer().getScheduler().runTaskTimer(SafeStaff.plugin, new Runnable()  {

            @Override
            public void run() {
                    Block phead = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1.0, p.getLocation().getZ()).getBlock();
                    Block pbod =  p.getLocation().getBlock();
                    Block pbelow = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ()).getBlock();
                    if (phead.getType() == Material.LAVA) {
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                        p.setFoodLevel(20);
                        p.setFireTicks(0);
                    } else if (pbod.getType() == Material.LAVA) {
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                        p.setFoodLevel(20);
                        p.setFireTicks(0);
                    } else if (pbelow.getType() == Material.LAVA) {
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                        p.setFoodLevel(20);
                        p.setFireTicks(0);
                    } else {
                        killTask(burn);
                        p.setHealth(health);
                        p.setFoodLevel(food);
                    }
            }
        }, 0L, 1L);
    }

    public static void killTask(BukkitTask t) {
        t.cancel();
    }

}
