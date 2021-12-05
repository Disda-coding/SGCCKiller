public class Text {
    private String title;
    private String answer;
    private double errTimes;

    public Text(String title, String answer, double errTimes) {
        this.title = title;
        this.answer = answer;
        this.errTimes = errTimes;
    }

    public String getTitle() {
        return title;
    }

    public String getAnswer() {
        return answer;
    }

    public double getErrTimes() {
        return errTimes;
    }
}
