package service;

import pojo.Question;

import java.util.ArrayList;

public interface QuestionManagerService {
    void resetErrTimes(Question question);

    void increaseErrTimes(Question question);

    String sortOps( String ans);

    Question createQuestion(String title, String answer, String type, ArrayList<String> ops, String explains, double errTimes,double datetime,double maxDate, String del);

    void resetDate(Question que);
}
