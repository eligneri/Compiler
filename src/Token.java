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
        return (this.key.equals(t.key) && this.value.equals(t.value)) || (this.key.equals(t.key) && this.intValue.equals(t.intValue));
    }
}
