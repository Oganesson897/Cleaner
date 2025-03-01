package me.oganesson.cleaner.cleanroom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CleanroomGetter {

    static final Logger LOGGER = LogManager.getLogger("Cleaner - Cleanroom");
    static final Gson GSON = new GsonBuilder().create();

    public static void downloadToCache(String version) throws IOException {
        version = version.equals("latest") ? getLastReleaseVersion() : version;
        if (CleanroomCache.checkCache(version)) return;
        for (Map.Entry<String, String> entry : processUrl(version).entrySet()) {
            String name = entry.getKey();
            String url = entry.getValue();
            CleanroomCache.save(version, name, new URL(url));
        }
    }

    public static String getLastReleaseVersion() {
        try {
            var map = get(new URL("https://api.github.com/repos/CleanroomMC/Cleanroom/releases/latest"));
            return (String) map.get("name");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> processUrl(String version) {
        var tag = version.contains("latest") ? "latest" : "tags/" + version;
        Map<String, String> result = new HashMap<>();
        try {
            var map = get(new URL("https://api.github.com/repos/CleanroomMC/Cleanroom/releases/" + tag));
            var assets = (List<Map<String, Object>>) map.get("assets");
            for (Map<String, Object> entries : assets) {
                result.put((String) entries.get("name"), (String) entries.get("browser_download_url"));
            }
            return result;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> get(URL url) {
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
                return (Map<String, Object>) GSON.fromJson(content.toString(), Map.class);
            }
            connection.disconnect();
        } catch (Exception e) {
            LOGGER.warn("Check internet connect! nightly.link cannot access!");
        }
        return new HashMap<>();
    }

}
