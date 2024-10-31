package edu.curtin.saed.assignment1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GridAreaIcon {
    private ImageView icon;

    public GridAreaIcon(String imageName) {
        String resourcePath = "/" + imageName;
        if (getClass().getResourceAsStream(resourcePath) != null) {
            Image image = new Image(getClass().getResourceAsStream(resourcePath));
            this.icon = new ImageView(image);
            this.icon.setFitWidth(64); // Adjust icon size for better visibility
            this.icon.setFitHeight(64);
        } else {
            System.err.println("Error: Image file not found for " + imageName);
            this.icon = new ImageView(); // Empty icon if image is not found
        }
    }

    public ImageView getIcon() {
        return icon;
    }
}
