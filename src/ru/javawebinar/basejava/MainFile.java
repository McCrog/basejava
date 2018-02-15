package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) {
        File filePath = new File("./.gitignore");
        try {
            System.out.println(filePath.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File baseDirectory = new File("./src/ru/javawebinar/basejava");

        recursiveBypass(baseDirectory);
    }

    private static void recursiveBypass(File file) {
        for (File childFile : Objects.requireNonNull(file.listFiles())) {
            if (childFile.isFile()) {
                System.out.println("File: " + childFile.getName());
            } else if (childFile.isDirectory()) {
                System.out.println("Directory: " + childFile.getName());
                recursiveBypass(new File(childFile.getPath()));
            }
        }
    }
}