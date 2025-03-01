package me.oganesson.cleaner.cleanroom.launcher.prism;

import me.oganesson.cleaner.ConfigHolder;
import me.oganesson.cleaner.cleanroom.CleanroomCache;
import me.oganesson.cleaner.cleanroom.CleanroomGetter;
import me.oganesson.cleaner.utils.ZipUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PrismLauncherCleaner {

    public static void clean(File mcLocation) throws IOException {
        File root = mcLocation.getParentFile();
        String version = Objects.equals(ConfigHolder.cleanroom.version, "latest") ? CleanroomGetter.getLastReleaseVersion() : ConfigHolder.cleanroom.version;
        if (root.exists()) {
            if (new File(root, "instance.cfg").exists()) {
                loadCleanroomMMC(version, root.getPath());
                throw new RuntimeException("Cleaned, Restart please");
            }
        }
    }

    public static void loadCleanroomMMC(String version, String path) throws IOException {
        File file = CleanroomCache.read(version, "Cleanroom-MMC-instance-%s.zip");
        File mmc = new File(path, String.format("CleanroomMMC-%s.zip", version));
        if (file == null) {
            CleanroomGetter.downloadToCache(version);
        } else {
            try {
                FileUtils.moveFile(file, mmc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ZipUtil.unzip(file.getPath(), path);
    }

}
