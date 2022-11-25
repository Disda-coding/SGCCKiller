package pojo;

import java.util.ArrayList;
//不够pojo 得优化

public class Question {


    public ArrayList<String> ops;
    private String title;
    private String answer;
    private String type;
    private double errTimes;
    private String explains;

    public Question() {
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Question) {
            Question anotherQuestion = (Question) obj;
            //比较题干是否相同
            return !this.getTitle().equals(anotherQuestion.getTitle());
        }
        return true;
    }


    @Override
    public int hashCode() {
        return this.getTitle().hashCode();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getErrTimes() {
        return errTimes;
    }

    public void setErrTimes(double errTimes) {
        this.errTimes = errTimes;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public ArrayList<String> getOps() {
        return ops;
    }

    public void setOps(ArrayList<String> ops) {
        this.ops = ops;
    }
}
