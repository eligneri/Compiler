/*
 * This class is for production rules. Each rule corresponds to one line of a Backus-Naur grammar
 */
public class Rule
{
    private int Line;
    private String Production;

    @Override
    public String toString()
    {
        return "line='" + Line + '\'' +
                ", production='" + Production + '\'' +
                "\n";
    }

    public Rule(String i, String j)
    {
        Line = Integer.parseInt(i);
        Production = j.trim();
    }

    //Getter
    public String GetProduction()
    {
        return Production;
    }

}
