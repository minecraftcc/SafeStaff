package ml.bmlzootown;


/**
 * Created by Brandon on 2/21/2016.
 */
public enum BlockHeights {
    BED_BLOCK (0.5625),
    BREWING_STAND (0.875),
    CAKE_BLOCK (0.4375),
    CAULDRON (0.3125),
    ENCHANTMENT_TABLE (0.75),
    SKULL (0.5),
    FENCE (1.5),
    DIODE_BLOCK_ON (0.125),
    DIODE_BLOCK_OFF (0.125),
    WATER_LILY (0.015625),
    SOUL_SAND (0.875),
    COBBLE_WALL (1.5),
    FLOWER_POT (0.375),
    REDSTONE_COMPARATOR_ON (0.125),
    REDSTONE_COMPARATOR_OFF (0.125),
    DAYLIGHT_DETECTOR (0.375),
    HOPPER (0.625),
    CHEST (0.875),
    TRAPPED_CHEST (0.875),
    GRASS_PATH (0.625),
    FENCE_GATE (1.5);

    private final Double height;

    BlockHeights(Double height) {
        this.height = height;
    }

    public Double getHeights() {
        return this.height;
    }

}
