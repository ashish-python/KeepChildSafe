package com.parentapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Helper class to load json files into memory for testing.
 */

public final class JsonFileLoader {
    // Opens the json file and returns it as a String
    public static String loadJsonFile(String fileName, ClassLoader classLoader) throws IOException {
        URL jsonUrl = classLoader.getResource(fileName);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(jsonUrl.openStream()));

        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();

        return sb.toString();
    }
}

