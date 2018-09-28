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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }
}
