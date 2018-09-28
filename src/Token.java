public class Token{
    private String key;
    private String value;
    private Integer intValue;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }
}
