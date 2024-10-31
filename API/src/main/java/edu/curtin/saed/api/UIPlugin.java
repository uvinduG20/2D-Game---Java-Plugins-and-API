    package edu.curtin.saed.api;

    public interface UIPlugin extends Plugin {
        PluginButtonListener getButtonListener();  // UIPlugin requires a button listener
    }
