package CoffeeLEX;
/*
Developed By Reza Manosuri
std_reza_mansouri@khu.ac.ir
Hosted On GitHub at https://github.com/rezmansouri/CoffeeLEX
 */
public class Token {
    private String value;
    private String type;
    private Token nextToken;
    private int rowNumber;
    private int columnNumber;

    public Token(String value, String type, int rowNumber, int columnNumber) {
        this.value = value;
        this.type = type;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public Token() {
    }

    public void setNextToken(Token t) {
        this.nextToken = t;
    }

    public boolean hasNext() {
        return nextToken != null;
    }

    public Token getNextToken() {
        return nextToken;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getColumnNumber() {
        return "" + columnNumber;
    }

    public String getRowNumber() {
        return "" + rowNumber;
    }

    public String toString() {
        return "value: *" + value + "*\t\ttype: " + type + "\t\trow: " + rowNumber + "\t\tcolumn: " + columnNumber;
    }
}