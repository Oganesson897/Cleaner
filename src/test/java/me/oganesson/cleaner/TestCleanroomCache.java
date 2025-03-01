package me.oganesson.cleaner;

import me.oganesson.cleaner.cleanroom.CleanroomGetter;

import java.io.IOException;

public class TestCleanroomCache {

    public static void main(String[] args) throws IOException {
        CleanroomGetter.downloadToCache("latest");
    }

}
