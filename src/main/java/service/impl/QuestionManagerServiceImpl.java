package service.impl;

import pojo.Question;
import service.QuestionManagerService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-25 10:23
 */
public class QuestionManagerServiceImpl implements QuestionManagerService {

    private QuestionManagerServiceImpl() {

    }

    public static class SingleQuestionManagerServImpl{
        private static final QuestionManagerService INSTANCE = new QuestionManagerServiceImpl();
    }

    public static QuestionManagerService getInstance(){
        return SingleQuestionManagerServImpl.INSTANCE;
    }

    @Override
    public void resetErrTimes(Question question) {
        question.setErrTimes(0.0);
    }

    @Override
    public void increaseErrTimes(Question question) {
        double errTimes = question.getErrTimes()+1;
        question.setErrTimes(errTimes);
    }

    @Override
    public String sortOps(String ans) {
        char[] ch = ans.toCharArray();
        Arrays.sort(ch);
        return new String(ch);
    }

    @Override
    public Question createQuestion(String title, String answer, String type, ArrayList<String> ops, String explains, double errTimes, double datetime, double maxDate, String del) {
        Question question = new Question();
        question.setTitle(title.trim());
        question.setType(type);
        if(type.equals("多选题")){
            String ans = answer;
            if(del != null) {
                ans = answer.replaceAll(del, "");
            }
            question.setAnswer(sortOps(ans));
        }else if(type.contains("是非")||type.contains("判断")||type.contains("对错")){
            if(answer.contains("√")||answer.contains("对")||answer.contains("T")||answer.contains("t")||answer.contains("A")||answer.contains("a")||answer.contains("1")||answer.contains("正确")) {
                question.setAnswer("T");
            } else {
                question.setAnswer("F");
            }
        }
        else {
            question.setAnswer(answer);
        }
        question.setExplains(explains);
        question.setOps(ops);
        question.setErrTimes(errTimes);
        question.setDate(datetime);
        return question;
    }

    @Override
    public void resetDate(Question que) {
        que.setDate(0.0);
    }
}