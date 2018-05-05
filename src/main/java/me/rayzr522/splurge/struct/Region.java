package me.rayzr522.splurge.struct;

import me.rayzr522.splurge.util.MapBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;
import java.util.Objects;

public class Region implements ConfigurationSerializable {
    private Location min;
    private Location max;

    public Region(Location min, Location max) {
        Objects.requireNonNull(min, "min cannot be null!");
        Objects.requireNonNull(max, "max cannot be null!");
        Validate.isTrue(min.getWorld().equals(max.getWorld()), "min and max must be in the same world!");

        this.min = min;
        this.max = max;

        recalculateBounds();
    }

    public static Region deserialize(Map<String, Object> data) {
        Validate.isTrue(data.get("min") instanceof Location, "min must be a location!");
        Validate.isTrue(data.get("max") instanceof Location, "max must be a location!");

        return new Region((Location) data.get("min"), (Location) data.get("max"));
    }

    public Location getMin() {
        return min;
    }

    public void setMin(Location min) {
        this.min = min;
        recalculateBounds();
    }

    public Location getMax() {
        return max;
    }

    public void setMax(Location max) {
        this.max = max;
        recalculateBounds();
    }

    private void recalculateBounds() {
        double minX = Math.min(min.getX(), max.getX());
        double minY = Math.min(min.getY(), max.getY());
        double minZ = Math.min(min.getZ(), max.getZ());

        double maxX = Math.max(min.getX(), max.getX());
        double maxY = Math.max(min.getY(), max.getY());
        double maxZ = Math.max(min.getZ(), max.getZ());

        min.setX(minX);
        min.setY(minY);
        min.setZ(minZ);

        max.setX(maxX);
        max.setY(maxY);
        max.setZ(maxZ);
    }

    @Override
    public Map<String, Object> serialize() {
        return MapBuilder.<String, Object>ofHashMap()
                .put("min", min)
                .put("max", max)
                .build();
    }
}
