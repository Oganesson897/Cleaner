package me.oganesson.cleaner.cleanroom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.oganesson.cleaner.cleanroom.cache.CleanroomCache;
import me.oganesson.cleaner.utils.UnzipUtil;
import net.minecraftforge.fml.common.LoaderException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CleanroomGetter {

    static final Logger LOGGER = LogManager.getLogger("Cleaner");
    static final Gson GSON = new GsonBuilder().create();

    public static void downloadCleanroomMMC(String version, String path) {
        File file = CleanroomCache.checkCache(version);
        File mmc = new File(path, String.format("CleanroomMMC-%s.zip", version));
        if (file == null) {
            if (file.mkdirs()) {
                if (version.contains("build")) {
                    processActionCRMMC(file);
                } else {
                    try {
                        String url = processReleaseCRMMC();
                        FileUtils.copyURLToFile(URI.create(url).toURL(), file);
                    } catch (IOException e) {throw new RuntimeException(e);}
                }
            }
            try {
                CleanroomCache.tryToSave(version, mmc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                FileUtils.copyFile(file, mmc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        UnzipUtil.unzip(file.getPath(), path);
        mmc.delete();
    }

    public static String processReleaseCRMMC() {
        try {
            var map = get(new URL("https://api.github.com/repos/CleanroomMC/Cleanroom/releases/latest"));
            var assets = (List<Map<String, Object>>) map.get("assets");
            for (Map<String, Object> entries : assets) {
                if (((String) entries.get("name")).contains("Cleanroom-MMC")) {
                    return (String) entries.get("browser_download_url");
                }
            }
            return "";
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processActionCRMMC(File target) {
        try {
            var map = get(new URL("https://api.github.com/repos/CleanroomMC/CleanroomMMC/actions/artifacts"));
            var artifacts = (List<Map<String, Object>>) map.get("artifacts");
            var urlString = (String) artifacts.get(1).get("archive_download_url");

            HttpURLConnection connection = null;
            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            FileOutputStream fileOutputStream = null;

            var token = "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";

            try {
                URL url = new URL(urlString);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github+json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    inputStream = connection.getInputStream();
                    bufferedInputStream = new BufferedInputStream(inputStream);
                    fileOutputStream = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedInputStream != null) bufferedInputStream.close();
                    if (fileOutputStream != null) fileOutputStream.close();
                    if (connection != null) connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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

    public static String getLastActionVersion() {
        try {
            var map = get(new URL("https://api.github.com/repos/CleanroomMC/Cleanroom/actions/artifacts"));
            var artifacts = (List<Map<String, Object>>) map.get("artifacts");
            return ((String) artifacts.get(1).get("name")).replace("universal-", "");
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
