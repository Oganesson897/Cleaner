package me.oganesson.cleaner.cleanroom;

import me.oganesson.cleaner.Cleaner;
import me.oganesson.cleaner.ConfigHolder;
import me.oganesson.cleaner.utils.LogUtil;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CleanroomCache {

    private static final Path cacheDir = ConfigHolder.cleanroom.cacheDir.isEmpty() ? Paths.get(System.getProperty("user.home"), ".cleanroom", "cache") : Paths.get(ConfigHolder.cleanroom.cacheDir);
    private static final File cacheDirFile = new File(cacheDir.toString());

    public static boolean checkCache(String version) {
        if (!cacheDirFile.exists()) {
            cacheDirFile.mkdirs();
            return false;
        }

        if (cacheDirFile.isDirectory()) {
            var list = cacheDirFile.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory() && file.getName().equals(version)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    public static File read(String version, String name) {
        if (cacheDirFile.isDirectory()) {
            var cache = new File(Paths.get(Paths.get(System.getProperty("user.home"), ".cleanroom", "cache").toString(), version, name).toUri());
            if (cache.exists()) {
                return cache;
            }
        }
        LogUtil.LOGGER.warn("Cache not found for version: {}", version);
        return null;
    }

    public static void save(String version, String name, URL url) throws IOException {
        if (cacheDirFile.isDirectory()) {
            var cache = new File(Paths.get(Paths.get(System.getProperty("user.home"), ".cleanroom", "cache").toString(), version, name).toUri());
            FileUtils.copyURLToFile(url, cache);
            LogUtil.LOGGER.info("Saved {} - {} to cache", version, name);
        }
    }

}
