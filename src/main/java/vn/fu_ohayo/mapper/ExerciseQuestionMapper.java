package vn.fu_ohayo.mapper;


import org.mapstruct.Mapper;

import org.mapstruct.MappingTarget;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;

import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ExerciseQuestion;


@Mapper(componentModel = "spring")
public interface ExerciseQuestionMapper {
    ExerciseQuestion ExerciseQuestionRequestToExerciseQuestion(@MappingTarget ExerciseQuestion ExerciseQuestion, ExerciseQuestionRequest ExerciseQuestionRequest);
    ExerciseQuestionResponse toExerciseQuestionResponse(ExerciseQuestion ExerciseQuestion);
    AnswerQuestion AnswerQuestionRequestToAnswerQuestion(@MappingTarget AnswerQuestion AnswerQuestion, AnswerQuestionRequest AnswerQuestionRequest);
    AnswerQuestionResponse toAnswerQuestionResponse(AnswerQuestion AnswerQuestion);
}
