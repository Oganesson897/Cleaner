package me.oganesson.cleaner.json;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Component {

    @SerializedName("cachedName")
    String cachedName;
    @SerializedName("cachedVersion")
    String cachedVersion;
    String uid;
    String version;

    public static class LWJGL extends Component {
        @SerializedName("cachedVolatile")
        boolean cachedVolatile = true;
        @SerializedName("dependencyOnly")
        boolean dependencyOnly = true;

        public LWJGL() {
            super("LWJGL 3", "3.3.1", "org.lwjgl3", "3.3.1");
        }
    }

    public static class Minecraft extends Component {
        List<Required> cachedRequires = new ArrayList<>();
        boolean important = true;

        public Minecraft() {
            super("Minecraft", "1.12.2", "net.minecraft", "1.12.2");
            cachedRequires.add(new Required("3.3.1", null, "org.lwjgl3"));
        }
    }

    public static class Cleanroom extends Component {
        List<Required> cachedRequires = new ArrayList<>();

        public Cleanroom(String version) {
            super("Cleanroom", version, "net.minecraftforge", version);
            cachedRequires.add(new Required(null, "1.12.2", "net.minecraft"));
        }
    }

}
