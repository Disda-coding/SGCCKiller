package service;

import org.apache.poi.ss.usermodel.Sheet;
import pojo.Question;

import java.util.List;

public interface QuestionManagerService {
    /**
     * 从excel中读取并设置开始做题的位置，如果无默认1开始
     * @param newBeg
     * @param sheet
     * @return
     */
    public int setDefaultBeg(int newBeg, Sheet sheet);

    /**
     * 设置新的cursor，返回旧的cursor
     * @param temp
     * @param newBeg
     * @return
     */
    public int getAndSetMem(int temp, int newBeg);

    /**
     * 得到目前的cursor
     * @param sheet
     * @return
     */
    public int getCurrentBeg(Sheet sheet);

    /**
     * 获取下个cursor
     * @param size
     * @param sheet
     * @return
     */
    public int getNextBeg(int size,Sheet sheet);

    /**
     * 返回默认的开始cursor并设置新的cursor
     * @param size
     * @param sheet
     * @return
     */
    public int getAndSetDefaultBeg(int size,Sheet sheet);

    /**
     * 获得最大做错错题的次数
     * @param questions
     * @return
     */
    public double getMaxErrTimes(List<Question> questions);

}
