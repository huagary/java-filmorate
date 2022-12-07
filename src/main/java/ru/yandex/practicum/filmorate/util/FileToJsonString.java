package ru.yandex.practicum.filmorate.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileToJsonString {
    public static String readFile(String path, Charset encoding) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get(path), encoding);
        for (String line : lines) {
            sb.append(line);
        }
        return sb.toString().replaceAll(" ", "").replaceAll("\t", "");
    }
}
