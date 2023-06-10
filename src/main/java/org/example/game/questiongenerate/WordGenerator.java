package org.example.game.questiongenerate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class WordGenerator {
    private final File wordsFile ;
    Random random;

    public WordGenerator(String filePath) {
        this.wordsFile = new File(filePath);
        this.random = new Random();
    }

    private String getRandomWordFromFile(File wordsFile) {
        if (!wordsFile.exists()) try {
            throw new FileNotFoundException("Words file not found: " + wordsFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(wordsFile);
            return jsonNode.get(getRandomIndex(0, jsonNode.size())).asText();
        } catch (IOException e) {
            throw new RuntimeException("Error reading words file: " + wordsFile.getAbsolutePath(), e);
        }
    }
    private int getRandomIndex(int start, int end){
        if (start >= end) throw new IllegalArgumentException("Invalid range: start >= end");
        return  this.random.nextInt((end - start) + start);
    }

    public String getWord() {
        return getRandomWordFromFile(wordsFile);
    }
}
