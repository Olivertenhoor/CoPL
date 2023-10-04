import java.util.ArrayList;

public class Parser {
    private ArrayList<Token> tokenList = new ArrayList<Token>();

    private int iterator;

    private int openPars;

    private boolean error; // To show whether there's an error in the recursion

    private boolean next() {
        if(iterator+1 < tokenList.size()) {
            iterator++;
            System.out.println("Next is true");
            return true;
        }
            System.out.println("Next is false");
        return false;
    }

    public Parser() {
        iterator = 0;
        openPars = 0;
        error = false;
    }

    public void addToken(Token nieuwToken) {
        tokenList.add(nieuwToken);
    }

    public void printList() {
        for (Token token : tokenList) {
        System.out.println(token.value);
        }
    }

    public boolean parse() {
        if(tokenList.isEmpty()) {
            System.out.println("Expression is empty");
            return false;
        }
        iterator = 0;
        openPars = 0;
        expr();
        return (!error && openPars == 0);
    }

    private void expr1() {
        System.out.println("expr1");
        System.out.println("iterator: " + iterator + ", tokenList size: " + tokenList.size());
        if(iterator == tokenList.size() || tokenList.get(iterator).isParClose()){
            System.out.println("Nested expression finished");
            return;
        }
        System.out.println("Continuing in expr1");
        lexpr();
        if(error) {
            return;
        }
        expr1();
    }

    private void lexpr() {
        System.out.println("lexpr");
        if(tokenList.get(iterator).isLambda()) {
            if(next() && tokenList.get(iterator).isVar()) {
                System.out.println("var");
                lexpr();
            } 
            else {
                error = true;
                System.out.println("(missing variable after lambda)");
            }
        }   
        else {
            pexpr();
        }
    }

    private void pexpr() {
        System.out.println("pexpr");
        if(tokenList.get(iterator).isParOpen()) {
            openPars++;
            if(!next()) {
                return;
            }
            expr();
            if(error) {
                return;
            } // There's an error in the code we don't continue
            if(tokenList.get(iterator).isParClose()) {
                openPars--;
                iterator++;                
            }

            else {
                error = true;
                System.out.println("Missing closing Parantesis");
            }
        }

        else if(tokenList.get(iterator).isVar()) {
            iterator++;
            System.out.println("var");
        }
    
        else if(tokenList.get(iterator).isParClose()){
            System.out.println("(missing expression after opening parenthesis)");
            error = true;
        }
    }
    
    private void expr() {
        System.out.println("expr");
        lexpr();
        if(error) {
            return;
        } // Don't continue after an error
        expr1();
    }
}