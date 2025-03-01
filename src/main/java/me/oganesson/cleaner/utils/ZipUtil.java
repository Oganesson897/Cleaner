package me.oganesson.cleaner.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static void unzip(String zipFilePath, String destDir) {
        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("instance.cfg")) continue;
                File destFile = new File(destDirectory, entry.getName());

                if (entry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    File parentDir = destFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zis.closeEntry();
            }

            new File(zipFilePath).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
