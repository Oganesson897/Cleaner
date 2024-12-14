package me.oganesson.cleaner.json;

import java.util.ArrayList;
import java.util.List;

public class PackConfig {

    public PackConfig(String version) {
        components.add(new Component.LWJGL());
        components.add(new Component.Minecraft());
        components.add(new Component.Cleanroom(version));
    }

    List<Component> components = new ArrayList<>();
    int formatVersion = 1;
}