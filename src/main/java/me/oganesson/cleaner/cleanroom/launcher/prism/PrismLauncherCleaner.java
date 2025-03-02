package me.oganesson.cleaner.cleanroom.launcher.prism;

import me.oganesson.cleaner.cleanroom.CleanroomCache;
import me.oganesson.cleaner.utils.ZipUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class PrismLauncherCleaner {

    public static void clean(File mcLocation, String version) {
        File root = mcLocation.getParentFile();
        String entry = Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length-1].getClassName();
        if (entry.startsWith("org.prismlauncher") || entry.startsWith("org.multimc") || entry.startsWith("org.polymc")) {
            loadCleanroomMMC(version, root.getPath());
            throw new RuntimeException("[Cleaner - Cleanroom Replacer] Cleaned PrismLauncher instance, Restart manually please!");
        }
    }

    public static void loadCleanroomMMC(String version, String path) {
        File file = CleanroomCache.read(version, "CleanroomMMC.zip");
        File mmc = new File(path, String.format("CleanroomMMC-%s.zip", version));
        try {
            FileUtils.copyFile(file, mmc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ZipUtil.unzip(file.getPath(), path);
    }

}
