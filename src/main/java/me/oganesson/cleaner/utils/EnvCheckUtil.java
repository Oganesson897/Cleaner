package me.oganesson.cleaner.utils;

public class EnvCheckUtil {

    private static Boolean isCleanroom;

    public static boolean isCleanroom() {
        if (isCleanroom == null) {
            try {
                Class.forName("com.cleanroommc.common.CleanroomContainer");
                return isCleanroom = true;
            } catch (ClassNotFoundException e) {
                return isCleanroom = false;
            }
        } else {
            return isCleanroom;
        }
    }

}
