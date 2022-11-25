package service;

import java.io.IOException;
import java.util.List;

public interface UIService {
    public String selectConfigPath(String[] names);
    void helloWorld();
    void sayGoodBye();
    void mainMenu(String url, List<String> names) throws InterruptedException, IOException;
}
