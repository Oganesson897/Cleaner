package me.oganesson.cleaner.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

public class CleanroomGetter {

    static final Logger LOGGER = LogManager.getLogger("Cleaner");
    static final Gson GSON = new GsonBuilder().create();

    public static void downloadCleanroomJar(String version, String path) {
        File file = new File(path, String.format("cleanroom-%s-universal.jar", version));
        if (file.mkdirs()) {
            if (version.contains("build")) {
                String pathHead = "https://nightly.link/CleanroomMC/Cleanroom/workflows/BuildTest/experimental%2Ffoundation/universal-" + version + ".zip";
                try {
                    File cache = new File(file.getParent(), "cache.zip");
                    FileUtils.copyURLToFile(URI.create(pathHead).toURL(), cache);
                    try (ZipFile zipFile = new ZipFile(cache)) {
                        zipFile.stream().forEach(entry -> {
                            try {
                                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        cache.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {throw new RuntimeException(e);}
            } else {
                try {
                    var c = get(URI.create("https://api.github.com/repos/CleanroomMC/Cleanroom/releases/latest").toURL());
                    Map<String, Object> map = (Map<String, Object>) GSON.fromJson(c, Map.class);
                    ((List<Map<String, Object>>) map.get("assets")).forEach(entries -> {
                        if (entries.get("name").toString().contains("universal.jar")) {
                            try {
                                FileUtils.copyURLToFile(new URL(entries.get("browser_download_url").toString()), file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getLastReleaseVersion() {
        try {
            var c = get(new URL("https://api.github.com/repos/CleanroomMC/Cleanroom/releases/latest"));
            Map<String, Object> map = (Map<String, Object>) GSON.fromJson(c, Map.class);
            return (String) map.get("name");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLastActionVersion() {
        try {
            var c = get(new URL("https://nightly.link/CleanroomMC/Cleanroom/workflows/BuildTest/experimental%2Ffoundation"));
            return extractVersion(c);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String get(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    content.append(inputLine);
                }
                reader.close();
                String contentString = content.toString();
                return contentString;
            }
            connection.disconnect();
        } catch (Exception e) {
            LOGGER.warn("Check internet connect! nightly.link cannot access!");
        }
        return "";
    }

    private static String extractVersion(String htmlContent) {
        String title = "";
        String titleTagStart = "<tr><th><a rel=\"nofollow\" href=\"https://nightly.link/CleanroomMC/Cleanroom/workflows/BuildTest/experimental%2Ffoundation/universal-";
        String titleTagEnd = "\">universal-";
        int startIndex = htmlContent.indexOf(titleTagStart);
        int endIndex = htmlContent.indexOf(titleTagEnd);

        if (startIndex != -1 && endIndex != -1) {
            title = htmlContent.substring(startIndex + titleTagStart.length(), endIndex);
        }
        return title.replace("%2B", "+");
    }

}
