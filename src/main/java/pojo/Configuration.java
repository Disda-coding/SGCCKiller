package pojo;

/**
 * @program: QnA
 * @description: configuration
 * @author: Disda
 * @create: 2022-11-22 16:04
 */
public class Configuration {
    int styleCell, enable_explain, titleCell, quesType, ans, composite, date,
            optBeg, optEnd, explain, errCell, linesize, easy,
            median, hard, qStart, calTime, isOrder, record,
            sample, includes_judge, memory;
    double ratio;

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    double coef;
    String del;

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getStyleCell() {
        return styleCell;
    }

    public void setStyleCell(int styleCell) {
        this.styleCell = styleCell;
    }

    public int getEnable_explain() {
        return enable_explain;
    }

    public void setEnable_explain(int enable_explain) {
        this.enable_explain = enable_explain;
    }

    public int getTitleCell() {
        return titleCell;
    }

    public void setTitleCell(int titleCell) {
        this.titleCell = titleCell;
    }

    public int getQuesType() {
        return quesType;
    }

    public void setQuesType(int quesType) {
        this.quesType = quesType;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }

    public int getComposite() {
        return composite;
    }

    public void setComposite(int composite) {
        this.composite = composite;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getOptBeg() {
        return optBeg;
    }

    public void setOptBeg(int optBeg) {
        this.optBeg = optBeg;
    }

    public int getOptEnd() {
        return optEnd;
    }

    public void setOptEnd(int optEnd) {
        this.optEnd = optEnd;
    }

    public int getExplain() {
        return explain;
    }

    public void setExplain(int explain) {
        this.explain = explain;
    }

    public int getErrCell() {
        return errCell;
    }

    public void setErrCell(int errCell) {
        this.errCell = errCell;
    }

    public int getLinesize() {
        return linesize;
    }

    public void setLinesize(int linesize) {
        this.linesize = linesize;
    }

    public int getEasy() {
        return easy;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public int getqStart() {
        return qStart;
    }

    public void setqStart(int qStart) {
        this.qStart = qStart;
    }

    public int getCalTime() {
        return calTime;
    }

    public void setCalTime(int calTime) {
        this.calTime = calTime;
    }

    public int getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(int isOrder) {
        this.isOrder = isOrder;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public int getIncludes_judge() {
        return includes_judge;
    }

    public void setIncludes_judge(int includes_judge) {
        this.includes_judge = includes_judge;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "styleCell=" + styleCell +
                ", enable_explain=" + enable_explain +
                ", titleCell=" + titleCell +
                ", quesType=" + quesType +
                ", ans=" + ans +
                ", composite=" + composite +
                ", date=" + date +
                ", optBeg=" + optBeg +
                ", optEnd=" + optEnd +
                ", explain=" + explain +
                ", errCell=" + errCell +
                ", linesize=" + linesize +
                ", easy=" + easy +
                ", median=" + median +
                ", hard=" + hard +
                ", qStart=" + qStart +
                ", calTime=" + calTime +
                ", isOrder=" + isOrder +
                ", record=" + record +
                ", sample=" + sample +
                ", includes_judge=" + includes_judge +
                ", memory=" + memory +
                '}';
    }


    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }
}