package com.test.webcrawler.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author prakashp7
 */
public class FileOperations {

    /**
     *  Read file the value from file
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFileAsString(final File file) throws IOException {
        if (file == null)
            throw new IOException("File is null");

        // read file into a buffer
        byte[] buffer = new byte[(int) file.length()];
        try (BufferedInputStream f = new BufferedInputStream(new FileInputStream(file))) {
            f.read(buffer);
        }
        return new String(buffer, "UTF-8");
    }
}
