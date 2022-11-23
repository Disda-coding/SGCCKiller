package pojo;

/**
 * @program: QnA
 * @description: configuration
 * @author: Disda
 * @create: 2022-11-22 16:04
 */
public class Configuration {
    int styleCell, enable_explain, titleCell, typeCell, ans, optBeg, optEnd, explain, errCell, linesize, easy, median, hard, qStart, calTime, isOrder, record, sample, includes_judge, memory;

    public void setStyleCell(int styleCell) {
        this.styleCell = styleCell;
    }

    public void setEnable_explain(int enable_explain) {
        this.enable_explain = enable_explain;
    }

    public void setTitleCell(int titleCell) {
        this.titleCell = titleCell;
    }

    public void setTypeCell(int typeCell) {
        this.typeCell = typeCell;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }

    public void setOptBeg(int optBeg) {
        this.optBeg = optBeg;
    }

    public void setOptEnd(int optEnd) {
        this.optEnd = optEnd;
    }

    public void setExplain(int explain) {
        this.explain = explain;
    }

    public void setErrCell(int errCell) {
        this.errCell = errCell;
    }

    public void setLinesize(int linesize) {
        this.linesize = linesize;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public void setqStart(int qStart) {
        this.qStart = qStart;
    }

    public void setCalTime(int calTime) {
        this.calTime = calTime;
    }

    public void setIsOrder(int isOrder) {
        this.isOrder = isOrder;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public void setIncludes_judge(int includes_judge) {
        this.includes_judge = includes_judge;
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
                ", typeCell=" + typeCell +
                ", ans=" + ans +
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
    public Configuration(){}
    public Configuration(int styleCell, int enable_explain, int titleCell, int typeCell, int ans, int optBeg, int optEnd, int explain, int errCell, int linesize, int easy, int median, int hard, int qStart, int calTime, int isOrder, int record, int sample, int includes_judge, int memory) {
        this.styleCell = styleCell;
        this.enable_explain = enable_explain;
        this.titleCell = titleCell;
        this.typeCell = typeCell;
        this.ans = ans;
        this.optBeg = optBeg;
        this.optEnd = optEnd;
        this.explain = explain;
        this.errCell = errCell;
        this.linesize = linesize;
        this.easy = easy;
        this.median = median;
        this.hard = hard;
        this.qStart = qStart;
        this.calTime = calTime;
        this.isOrder = isOrder;
        this.record = record;
        this.sample = sample;
        this.includes_judge = includes_judge;
        this.memory = memory;
    }

    public int getStyleCell() {
        return styleCell;
    }

    public int getEnable_explain() {
        return enable_explain;
    }

    public int getTitleCell() {
        return titleCell;
    }

    public int getTypeCell() {
        return typeCell;
    }

    public int getAns() {
        return ans;
    }

    public int getOptBeg() {
        return optBeg;
    }

    public int getOptEnd() {
        return optEnd;
    }

    public int getExplain() {
        return explain;
    }

    public int getErrCell() {
        return errCell;
    }

    public int getLinesize() {
        return linesize;
    }

    public int getEasy() {
        return easy;
    }

    public int getMedian() {
        return median;
    }

    public int getHard() {
        return hard;
    }

    public int getqStart() {
        return qStart;
    }

    public int getCalTime() {
        return calTime;
    }

    public int getIsOrder() {
        return isOrder;
    }

    public int getRecord() {
        return record;
    }

    public int getSample() {
        return sample;
    }

    public int getIncludes_judge() {
        return includes_judge;
    }

    public int getMemory() {
        return memory;
    }

}