package me.oganesson.cleaner;

import com.cleanroommc.configanytime.ConfigAnytime;
import net.minecraftforge.common.config.Config;

@Config(modid = "cleaner")
public class ConfigHolder {

    public static Cleanroom cleanroom = new Cleanroom();
    public static Mod mod = new Mod();

    public static class Cleanroom {
        @Config.Comment("Enable Cleanroom Replacer.")
        public boolean enabled = false;

        @Config.Comment({
                "Target version of Cleanroom Loader.",
                "Use the tag or 'latest' to specify."
        })
        public String version = "latest";

        @Config.Comment({
                "Cache dir of Cleanroom Loader.",
                "Default: %USER_HOME%/.cleanroom/cache"
        })
        public String cacheDir = "";
    }

    public static class Mod {
        @Config.Comment({
                "https://console.curseforge.com/#/api-keys",
                "You should create a new API key and paste it here."
        })
        public String curseforgeApiKey = "$2a$10$ZS5BrrbTqsJdNOKmlZCozuCo49ULIeuq7qvgwQP3fBeJ5SJWHGck2";
    }

    @Config.Comment("It should not be changed manually unless you wish to initialize it again.")
    public static boolean done = false;

    static {
        ConfigAnytime.register(ConfigHolder.class);
    }

}
