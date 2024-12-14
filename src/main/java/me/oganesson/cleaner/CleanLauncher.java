package me.oganesson.cleaner;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@SuppressWarnings("unused")
public class CleanLauncher implements IFMLCallHook, Runnable {

    static final Logger LOGGER = LogManager.getLogger("Cleaner");
    File mcLocation;
    static boolean cleanable = false;

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

        if (cleanable) {
            File root = mcLocation.getParentFile();
            if (root.exists()) {
                var listFile = Arrays.stream(root.list()).toList();
                if (listFile.contains("mmc-pack.json")) {

                }
            }
        }
    }
}
