package org.example.game.questionlist;

import org.example.QuestionDTO;

import java.util.List;

public interface QuestionDTOListInterface {
    List<QuestionDTO> readLinesFromJson(String jsonPath);
}
