package org.example.configuration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.QuestionDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Configuration {

    public static List<QuestionDTO> readLinesFromJson() {
        File file = new File("src/main/resources/wordsAndDescriptions.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<QuestionDTO> questionDTOList;
        try {
            questionDTOList = objectMapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return questionDTOList;
    }
}
