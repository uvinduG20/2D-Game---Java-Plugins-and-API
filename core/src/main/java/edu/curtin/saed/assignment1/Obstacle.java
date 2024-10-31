package edu.curtin.saed.assignment1;

import java.util.List;

public class Obstacle {
    private List<Location> locations;
    private List<String> requiredItems;

    public Obstacle(List<Location> locations, List<String> requiredItems) {
        this.locations = locations;
        this.requiredItems = requiredItems;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<String> getRequiredItems() {
        return requiredItems;
    }
}
