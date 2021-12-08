import java.util.ArrayList;

public class Question {


    private String title;
    private String answer;
    private String type;
    private double errTimes;


    private String explains;
    public ArrayList<String> ops;


    public Question(String title, String answer, String type, ArrayList<String> ops, String explains, double errTimes, String del) {
        this.title = title;
        this.type = type;
        if (this.type.equals("多选题") && del != null)
            this.answer = answer.replaceAll(del, "");

        else
            this.answer = answer;
        this.explains = explains;
        this.ops = ops;
        this.errTimes = errTimes;
    }

    public String getTitle() {
        return title;
    }

    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }

    public double getErrTimes() {
        return errTimes;
    }


    public String getExplains() {
        return explains;
    }

    //要重写equals方法以及hashCode方法，才能在set集合中保证题目不重复
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Question) {
            Question anotherQuestion = (Question) obj;
            //比较题干是否相同
            if (this.getTitle().equals(anotherQuestion.getTitle()))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.getTitle().hashCode();
    }
}
