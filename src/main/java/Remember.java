import utils.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class Remember {
    double yes = -1;
    double not = 1;
    double notSure = 0;
    private double max_err = 0.0;
    private ArrayList<Text> texts = new ArrayList();
    ExcelUtils eu;
    int lineszie = 75;

    public void remAll() {
        int size = texts.size() - 1;
        int beg = 1, end = size;
        Scanner input = new Scanner(System.in);
        System.out.println("请输入题号范围，必须小于" + size + " 并用空格or-相连，回车默认全部！");
        String in = input.nextLine();
        String[] nums = in.split(" |-");
        if (nums.length == 2) {
            beg = Integer.valueOf(nums[0]);
            end = Integer.valueOf(nums[1]);
        }
        rem(beg, end, 0);
    }

    private void rem(int beg, int end, int times) {
        Scanner input = new Scanner(System.in);
        int all = 0;
        int err = 0;
        for (int i = beg - 1; i <= end; i++) {
            Text text = texts.get(i);
            if (text.getErrTimes() < times) continue;
            all++;
            System.out.print("[" + i + "]. ");
            // 太长解决方案
            Utils.printLongStuff(text.getTitle(), lineszie);
            System.out.println("请输入你的答案：会打“1”，不会打其他");
            String ans = input.nextLine();
            if (ans.equals("1")) {
                System.out.println("回答正确");
                System.out.println("正确答案：\n" + text.getAnswer());
            } else {
                System.out.println(text.getAnswer());
                System.out.println();
                err++;
                eu.getCellByCaseName(text.getTitle(), 0, 2, 1.0, 0);
            }
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String url = ExcelUtils.class.getClassLoader().getResource("Rem.xlsx").getPath().substring(1);
        Remember ex = new Remember();
        ex.eu = new ExcelUtils(url, 0);
        ex.max_err = ex.eu.getRemData(ex.texts);
        ex.remAll();

        FileOutputStream fos = new FileOutputStream(url);
        ex.eu.writeExcel(fos);
    }
}
