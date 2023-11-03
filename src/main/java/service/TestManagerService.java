package service;

import pojo.Question;

import java.util.ArrayList;

public interface TestManagerService {
    /**
     * 从excel中读取并设置开始做题的位置，如果无默认1开始
     *
     * @param newBeg
     * @return
     */
    int setDefaultBeg(int newBeg);

    /**
     * 设置新的cursor，返回旧的cursor
     *
     * @param temp
     * @param newBeg
     * @return
     */
    int getAndSetMem(int temp, int newBeg);

    /**
     * 得到目前的cursor
     *
     * @return
     */
    int getCurrentBeg();

    /**
     * 获取下个cursor
     *
     * @param size
     * @return
     */
    int getNextBeg(int size);

    /**
     * 返回默认的开始cursor并设置新的cursor
     *
     * @param size
     * @return
     */
    int getAndSetDefaultBeg(int size);

    /**
     * 获得最大做错错题的次数
     *
     * @return
     */
    double getMaxErrTimes();

    /**
     * 加载题库内的题目传入Question对象数组
     *
     * @return
     */
    double[] getTestData();

    /**
     * 将选项打乱顺序
     *
     * @param queTitle
     * @param queAns
     * @param queType
     * @param ops
     * @param explains
     * @param errTimes
     * @param datetime
     * @param del
     * @return
     */
    Question mixOrder(String queTitle, String queAns, String queType, ArrayList<String> ops, String explains, double errTimes,double datetime,double maxDate, String del);

    void printTotal(Double times);


    void printExplains(Question que);

    /**
     * @Method testError
     * @Author disda
     * @Description 用于测试错误次数大于times的方法
     * @Return void
     * @Exception
     * @Date 2021/12/3 2:32 下午
     */
    public void testError(Double times) throws InterruptedException;

    void setExcelService(String filePath, int index);

    void printAll();

    void test(int beg, int end, Double times, boolean random,double[] maxVariablemaxVariable) throws InterruptedException;

    void testAll() throws InterruptedException;

    void removeErrTimes();

    public void randomTest() throws InterruptedException;

    public ExcelService getExcelService();

    public void resetCursor();

    void testByIntelligence(double[] maxVariable) throws InterruptedException;
}
