public class Rule {
    private int line;
    private String production;

    @Override
    public String toString() {
        return "line='" + line + '\'' +
                ", production='" + production + '\'' +
                "\n";
    }

    public Rule(String i, String j){
        line = Integer.parseInt(i);
        production = j.trim();
    }

    public String getProduction() {
        return production;
    }

}
