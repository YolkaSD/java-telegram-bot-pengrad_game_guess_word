package org.example.game.questionlist;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.QuestionDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class QuestionDTOListImpl implements QuestionDTOListInterface {

    private static final QuestionDTOListImpl instance = new QuestionDTOListImpl();

    private QuestionDTOListImpl(){}

    public static QuestionDTOListImpl getInstance() {
        return instance;
    }

    @Override
    public List<QuestionDTO> readLinesFromJson(String jsonPath) {
        File file = new File(jsonPath);
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
