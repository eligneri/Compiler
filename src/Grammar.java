import java.util.*;
import java.io.*;

public class Grammar {
    private LinkedList<Rule> rules = new LinkedList<>();

    public Grammar(File file){
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String num;
            String pro;
            while ((line = bufferedReader.readLine()) != null) {
                num = line.substring(0,line.indexOf(':')).trim();
                pro = line.substring(line.indexOf('=') + 1);
                rules.add(new Rule(num,pro));
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getProduction(int i){
        Rule rule = rules.get(i - 1);
        return rule.getProduction();
    }

}
