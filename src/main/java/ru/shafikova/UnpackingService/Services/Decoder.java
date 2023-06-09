package ru.shafikova.UnpackingService.Services;

import java.util.ArrayList;
import java.util.List;

public class Decoder {
    public String unpack(List<String> line) {

        List<String> dictionary = new ArrayList<>();
        for (int i = 0; i <= 255; i++) dictionary.add(String.valueOf((char) i));

        StringBuilder unpackedLine = new StringBuilder();
        String previousLine = dictionary.get(0);
        String currentLine;
        String lineToDict;
        for (int i = 1; i < line.size(); i++) {
            unpackedLine.append(previousLine);

            int currentIndex = Integer.parseInt(line.get(i));
            if (currentIndex >= dictionary.size()) {
                currentLine = previousLine + previousLine.charAt(0);
            } else {
                currentLine = dictionary.get(currentIndex);
            }

            lineToDict = previousLine + currentLine.charAt(0);
            dictionary.add(lineToDict);

            previousLine = currentLine;
        }
        unpackedLine.append(previousLine);

        System.out.println(unpackedLine);

        return unpackedLine.toString();
    }
}
