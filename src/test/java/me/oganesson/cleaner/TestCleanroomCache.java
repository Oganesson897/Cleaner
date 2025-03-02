package me.oganesson.cleaner;

import me.oganesson.cleaner.cleanroom.CleanroomGetter;

import java.io.IOException;

public class TestCleanroomCache {

    public static void main(String[] args) {
        System.out.println(CleanroomGetter.processUrl("latest").toString());
    }

}
