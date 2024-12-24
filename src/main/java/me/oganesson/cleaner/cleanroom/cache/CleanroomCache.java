package me.oganesson.cleaner.cleanroom.cache;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CleanroomCache {

    private static final Path userHome = Paths.get(System.getProperty("user.home"), ".cleanroom", "cache");
    private static final File userHomeFile = new File(userHome.toString());

    public static File checkCache(String version) {
        if (!userHomeFile.exists()) {
            userHomeFile.mkdirs();
            return null;
        }

        if (userHomeFile.isDirectory()) {
            var list = userHomeFile.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory() && file.getName().equals(version)) {
                        return file.listFiles()[0];
                    }
                }
            }
        }
        return null;
    }

    public static void tryToSave(String version, File file) throws IOException {
        if (!userHomeFile.exists()) userHomeFile.mkdirs();

        if (userHomeFile.isDirectory()) {
            var cache = new File(Paths.get(userHome.toString(), version, file.getName()).toUri());
            if (cache.exists()) return;
            FileUtils.copyFile(file, cache);
        }
    }

}
