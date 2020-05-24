import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ZZZ {
    public static void main(String[] args) throws IOException {
        List<String> l1 = Files.readAllLines(Paths.get("D:\\kaggle\\input\\vk-cup-2019\\data.csv"));
        l1 = l1.subList(1, l1.size());
        String[] s = new String[l1.size()];
        for (int i = 0; i < s.length; i++) {
            String x = l1.get(i);
            s[i] = x.substring(x.indexOf(';') + 1);
        }
        List<String> l2 = Files.readAllLines(Paths.get("D:\\kaggle\\input\\vk-cup-2019\\train.csv"));
        l2 = l2.subList(1, l2.size());
        List<String> res = new ArrayList<>();
        for (int i = 0; i < l2.size(); i++) {
            String x = l2.get(i);
            String[] p = x.split(";");
            int ind = Integer.parseInt(p[0]) - 1;
            res.add(s[ind] + "\t" + p[1]);
        }
        Files.write(Paths.get("D:\\kaggle\\input\\vk-cup-2019\\train.tsv"), res);

        List<String> test = new ArrayList<>();
        for (int i = 30000; i <s.length ; i++) {
            test.add(s[i]);
        }
        Files.write(Paths.get("D:\\kaggle\\input\\vk-cup-2019\\test.tsv"), test);
    }
}
