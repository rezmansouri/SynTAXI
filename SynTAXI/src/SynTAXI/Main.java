package SynTAXI;

import CoffeeLEX.CoffeeLexer;
import CoffeeLEX.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        CoffeeLexer coffeeLexer = new CoffeeLexer("lexicalRules.json");
        Token headOfLinkedList = coffeeLexer.analyze("program.c");
        Parser parser = new Parser("syntaxGrammar.json", headOfLinkedList);
        String taxiMessage = parser.parse() ? "Program Compiled Successfully." : "Program Compilation Failed with Syntax Error.";
        getOutOfTaxi(headOfLinkedList,taxiMessage);
        printCredits();
    }

    private static void getOutOfTaxi(Token head, String msg) {
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Value", "Type", "Row", "Column");
        List<String> seperators = Arrays.asList("-----", "----", "---", "-----");
        rows.add(headers);
        rows.add(seperators);
        Token p = new Token();
        p.setNextToken(head);
        while (p.hasNext()) {
            p = p.getNextToken();
            rows.add(Arrays.asList(p.getValue(), p.getType(), p.getRowNumber(), p.getColumnNumber()));
        }
        System.out.print("CoffeeLEX Output: \n" + formatAsTable(rows)+"_________________________________\n");
        System.out.println("SynTAXI Output: \n"+msg+"\n_________________________________");
    }

    private static String formatAsTable(List<List<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }
        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }

    private static void printCredits () {
        System.out.println("CoffeeLex © / SynTAXI © - 2021");
        System.out.println("Developed By Reza Mansouri");
    }
}
