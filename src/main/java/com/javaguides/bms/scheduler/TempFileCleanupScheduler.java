package com.javaguides.bms.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

@Component
public class TempFileCleanupScheduler {

    private static final String TEMP_DIR = "C:/Dev Works/Pal3_BarangayMS_Backend/uploads/comm-reports-media/temp";
    private static final long EXPIRATION_TIME = 2 * 60 * 1000;

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void cleanupTempFiles() {

        try {
            Path tempPath = Paths.get(TEMP_DIR);
            if (!Files.exists(tempPath)) return;

            Files.walk(tempPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            FileTime lastModifiedTime = Files.getLastModifiedTime(file);
                            long age = System.currentTimeMillis() - lastModifiedTime.toMillis();

                            if (age>EXPIRATION_TIME) {
                                Files.deleteIfExists(file);
                                System.out.println("Deleted temp file: " + file.getFileName());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


            Files.walk(tempPath)
                    .sorted(Comparator.reverseOrder())
                    .filter(Files::isDirectory)
                    .forEach(dir -> {
                        try {
                            if (!dir.equals(tempPath) && Files.list(dir).findAny().isEmpty()) {
                                Files.deleteIfExists(dir);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}