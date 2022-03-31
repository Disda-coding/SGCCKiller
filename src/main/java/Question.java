import java.util.ArrayList;
import java.util.Arrays;

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
        if (this.type.equals("多选题")) {
            String ans = answer;
            if(del != null)
                ans = answer.replaceAll(del, "");
            this.answer = sortOps(ans);
        }
        else if(this.type.contains("是非")||this.type.contains("判断")||this.type.contains("对错")){
            if(answer.contains("对")||answer.contains("T")||answer.contains("t")||answer.contains("A")||answer.contains("a")||answer.contains("1")||answer.contains("正确"))
                this.answer="T";
            else
                this.answer="F";
        }
        else
            this.answer = answer;
        this.explains = explains;
        this.ops = ops;
        this.errTimes = errTimes;
    }

    private String sortOps(String ans) {
        char[] ch = ans.toCharArray();
        Arrays.sort(ch);

        return new String(ch);
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
    public void increaseErrTimes(){
        this.errTimes++;
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
