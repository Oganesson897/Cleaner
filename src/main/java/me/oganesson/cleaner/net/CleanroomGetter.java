package me.oganesson.cleaner.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class CleanroomGetter {

    static final Logger LOGGER = LogManager.getLogger("Cleaner");
    static final Gson GSON = new GsonBuilder().create();

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
        return title;
    }

}
