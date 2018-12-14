import java.util.LinkedList;
import java.io.*;

/*
 *This class reads a grammar in Backus-Naur from from a file and turns it into something usable by the compiler
 */
public class Grammar
{
    private LinkedList<Rule> rules = new LinkedList<>();

    public Grammar(File file)
    {
        try
        {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String num;
            String pro;

            //This statement breaks up rules into their number and production and adds it to a list

            while ((line = bufferedReader.readLine()) != null)
            {
                num = line.substring(0, line.indexOf(':')).trim();
                pro = line.substring(line.indexOf('=') + 1);
                rules.add(new Rule(num, pro));
            }
            fileReader.close();
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Returns the production rule associated with a number
    public String GetProduction(int i)
    {
        Rule rule = rules.get(i - 1);
        return rule.GetProduction();
    }

}
