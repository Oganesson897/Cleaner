package me.oganesson.cleaner;

import me.oganesson.cleaner.cleanroom.CleanroomGetter;
import me.oganesson.cleaner.dialog.StartDialog;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Cleaner")
public class Cleaner implements IFMLLoadingPlugin, IFMLCallHook {

    File mcLocation;

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return "me.oganesson.cleaner.Cleaner";
    }

    @Override
    public void injectData(Map<String, Object> data) {
        mcLocation = (File) data.get("mcLocation");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() {
        File root = mcLocation.getParentFile();
        if (root.exists()) {
            if (new File(root, "instance.cfg").exists()) {
                CleanroomGetter.downloadCleanroomMMC(StartDialog.of(), root.getPath());
                throw new RuntimeException("Cleaned, Restart please");
            }
        }
        return null;
    }
}
