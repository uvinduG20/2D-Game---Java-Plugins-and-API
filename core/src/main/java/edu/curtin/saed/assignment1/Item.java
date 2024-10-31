package edu.curtin.saed.assignment1;

import java.util.List;

public class Item {
    private String name;
    private List<Location> locations;
    private String message;

    public Item(String name, List<Location> locations, String message) {
        this.name = name;
        this.locations = locations;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public String getMessage() {
        return message;
    }
}
