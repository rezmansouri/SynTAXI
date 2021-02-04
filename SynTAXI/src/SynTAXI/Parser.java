package SynTAXI;

import CoffeeLEX.Token;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private Token currentToken;
    private JSONObject grammarJSON;
    private BufferedReader reader;
    private Map<String, String[][]> rulesMap;
    private final String MAIN_TERM = "Program";


    Parser(String grammarFilePath, Token currentToken) throws IOException {
        this.currentToken = currentToken;
        this.grammarJSON = getRulesFromFile(grammarFilePath);
        rulesMap = new HashMap<>();
        Iterator iterator = grammarJSON.keys();
        while (iterator.hasNext()) {
            String term = iterator.next().toString();
            JSONArray termRulesJSON = grammarJSON.getJSONArray(term);
            String[][] termRules = new String[termRulesJSON.length()][];
            for (int j = 0; j < termRules.length; j++) {
                JSONArray thisRuleJSON = termRulesJSON.getJSONArray(j);
                termRules[j] = new String[thisRuleJSON.length()];
                for (int k = 0; k < thisRuleJSON.length(); k++) {
                    termRules[j][k] = thisRuleJSON.getString(k);
                }
            }
            rulesMap.put(term, termRules);
        }
    }

    private JSONObject getRulesFromFile(String rulesPath) throws IOException {
        reader = new BufferedReader(new FileReader(rulesPath));
        StringBuilder fileContent = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            fileContent.append(line);
            line = reader.readLine();
        }
        return new JSONObject(fileContent.toString());
    }

    public boolean parse() throws Exception {
        parse(MAIN_TERM);
        return true;
    }

    private void parse(String term) throws Exception {
        StringAndInt stringAndInt = whichRule(term);
        String[] rules = rulesMap.get(stringAndInt.string)[stringAndInt.integer];
        for (int i = 0; i < rules.length; i++) {
            if (rulesMap.containsKey(rules[i])) {
                parse(rules[i]);
            } else {
                if (!currentToken.getType().equals(rules[i])) {
                    throw new TaxiException("Syntax Error: Expected " + rules[i] + " At row: " + currentToken.getRowNumber() + " Column: " + currentToken.getColumnNumber());
                }
            }
            if (!nextToken()) return;
        }
    }

    private StringAndInt whichRule(String currentTerm) throws Exception {
        String[][] currentTermRules = rulesMap.get(currentTerm);
        for (int i = 0; i < currentTermRules.length; i++) {
            if (rulesMap.containsKey(currentTermRules[i][0]))
                return whichRule(currentTermRules[i][0]);
            else if (currentToken.getType().equals(currentTermRules[i][0])) {
                return new StringAndInt(currentTerm, i);
            }
        }
        throw new Exception("Term: "+currentTerm+" Not found");
    }

    private boolean nextToken() {
        if (currentToken.getNextToken()==null) return false;
        currentToken = currentToken.getNextToken();
        return true;
    }
}