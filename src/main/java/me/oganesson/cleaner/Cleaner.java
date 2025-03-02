package me.oganesson.cleaner;

import me.oganesson.cleaner.cleanroom.CleanroomGetter;
import me.oganesson.cleaner.cleanroom.launcher.prism.PrismLauncherCleaner;
import me.oganesson.cleaner.utils.EnvCheckUtil;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Cleaner")
public class Cleaner implements IFMLLoadingPlugin, IFMLCallHook {

    File mcLocation;

    @Override
    public String getSetupClass() {
        return "me.oganesson.cleaner.Cleaner";
    }

    @Override
    public Void call() throws IOException {
        if (ConfigHolder.done) return null;

        if (EnvCheckUtil.isCleanroom()) {
            // Mod download TODO
        } else {
            String version = Objects.equals(ConfigHolder.cleanroom.version, "latest") ? CleanroomGetter.getLastReleaseVersion() : ConfigHolder.cleanroom.version;
            CleanroomGetter.downloadToCache(version);
            //Prism
            PrismLauncherCleaner.clean(mcLocation, version);
        }

        ConfigHolder.done = true;
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        mcLocation = (File) data.get("mcLocation");
    }

    @Override public String[] getASMTransformerClass() {return new String[0];}
    @Override public String getModContainerClass() {return null;}
    @Override public String getAccessTransformerClass() {return null;}
}
