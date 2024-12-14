package me.oganesson.cleaner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.oganesson.cleaner.dialog.StartDialog;
import me.oganesson.cleaner.json.PackConfig;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@SuppressWarnings("unused")
public class CleanLauncher implements IFMLCallHook, Runnable {

    static final Logger LOGGER = LogManager.getLogger("Cleaner");
    File mcLocation;
    public static boolean cleanable = false;
    public static String version;

    Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void injectData(Map<String, Object> data) {
        mcLocation = (File) data.get("mcLocation");
    }

    @Override
    public Void call() throws InterruptedException {
        LOGGER.info("Try to clean instance");
        new Thread(this).start();
        latch.await();
        return null;
    }

    @Override
    public void run() {
        StartDialog dialog = new StartDialog();
        dialog.setVisible(true);

        String cleanroomVersion;

        if (cleanable) {
            File root = mcLocation.getParentFile();
            if (root.exists()) {
                var listFile = Arrays.stream(root.list()).toList();

                if (listFile.contains("mmc-pack.json")) {
                    var cfg = root.listFiles(((dir, name) -> name.equals("mmc-pack.json")))[0];
                    try {
                        GSON.toJson(new PackConfig(version), new FileWriter(cfg));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        }
    }
}
