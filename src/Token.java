public class Token{
    String key;
    String value;
    Integer intValue;

    public Token(String x, String y) {
        key = x;
        value = y;
    }

    public Token(String x, int y) {
        key = x;
        intValue = y;
    }

    @Override public String toString(){
        //if(value == null || intValue == null){
        //    return "[\"" + key + "\", " + "null]";
        //}

        if(value != null) {
            return "[\"" + key + "\", \"" + value + "\"]";
        } else {
            return "[\"" + key + "\", " + intValue + "]";
        }
    }

    public boolean equals(Token t){
        return (this.key == t.key && this.value == t.value) || (this.key == t.key && this.intValue == t.intValue);
    }
}
