package rtu.mirea.spo2;

import java.util.ArrayList;
import java.util.HashMap;

public class EBNFParser {
    static ArrayList<Pair<String, String>> terms;
    static HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> productions;
    static Pair<String, String> current_prod = new Pair("", "");

    static int i = 0;
    static String term;
    public static void nextTerm(){
        if(!current_prod.equals(new Pair<>("", "")) && term != "IS" && term != "EOL"){
            productions.get(current_prod).add(terms.get(i));
        }
        i++;
        if(i < terms.size())
            term = terms.get(i).getFirst();
    }

    public static void error(String msg){
        System.out.println(msg);
        System.exit(10);
    }

    public static boolean accept(String t){
        if(term.equals(t)){
            nextTerm();
            return true;
        }
        return false;
    }

    public static boolean expect(String t){
        if(accept(t))
            return true;
        error("expect: unexpected term " + t.toString() + " at " + i);
        return false;
    }

    public static boolean expr(){
        if(accept("TERMINAL")){
            ;
        }
        else if (accept("NONTERMINAL")){
            ;
        }
        else if(accept("L_BR")){
            expr();
            expect("R_BR");
        }

//        if(accept("OR")){
//            ;
//        }
//        else if(accept("OP")){
//            ;
//        }
//        while(accept("OR") || accept("OP"))
//            expr();
        while(!accept("EOL")){
            if(accept("OR")){
                ;
            }
            else if(accept("OP")){
                ;
            }
            if(term == "R_BR")
                return true;
            if(i == 174)
                System.out.println(i);
            if(expr())
                return true;
        }
        return true;
    }

    public static void production(){
        if(accept("TERMINAL")){
            current_prod = new Pair("TERM_PROD", terms.get(i-1).getSecond());
        }
        else if (accept("NONTERMINAL")){
            current_prod = new Pair("NONTERM_PROD", terms.get(i-1).getSecond());
        }
        else{
            error("production: syntax error");
        }
        productions.put(current_prod, new ArrayList<>());
        expect("IS");
        expr();
        current_prod = new Pair("", "");
//        expect("EOL");
    }

    public static void lang(){
        while(i < terms.size()){
            production();
//            nextTerm();
        }
    }



    public static HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> getProductions(ArrayList<Pair<String, String>> terms){
        EBNFParser.terms = terms;
        term = terms.get(i).getFirst();
        productions = new HashMap<>();
        lang();


        return productions;
    }
}
