package rtu.mirea.spo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EBNFParser {
    static ArrayList<Pair<String, String>> terms;
    static HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> productions;
    static ArrayList<Pair<String, String>> productions_order;
    static HashSet<Pair<String, String>> fragments;
    static Pair<String, String> current_prod = new Pair("", "");

    static int i = 0;
    static String term;
    static boolean fragment_flag = false;
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
        if(accept("FRAGMENT")){
            fragment_flag = true;
        }
        if(accept("TERMINAL")){
            current_prod = new Pair("TERM_PROD", terms.get(i-1).getSecond());
            if(fragment_flag)
                fragments.add(current_prod);
        }
        else if (accept("NONTERMINAL")){
            current_prod = new Pair("NONTERM_PROD", terms.get(i-1).getSecond());
        }
        else{
            error("production: syntax error at " + i);
        }
        productions.put(current_prod, new ArrayList<>());
        productions_order.add(current_prod);
        fragment_flag = false;
        expect("IS");
        if(accept("REGEX")){
            expect("EOL");
        }
        else{
            expr();
        }
        current_prod = new Pair("", "");
//        expect("EOL");
    }

    public static void lang(){
        while(i < terms.size()){
            production();
//            nextTerm();
        }
    }



    public static ArrayList<Pair<Pair<String, String>, ArrayList<Pair<String, String>>>> getProductions(ArrayList<Pair<String, String>> terms){
        EBNFParser.terms = terms;
        term = terms.get(i).getFirst();
        productions = new HashMap<>();
        fragments = new HashSet<>();
        productions_order = new ArrayList<>();
        lang();
        System.out.println("FRAGMENTS " + fragments);

        ArrayList<Pair<Pair<String, String>, ArrayList<Pair<String, String>>>> res = new ArrayList<>();
        for(var prod : productions_order){
            res.add(new Pair(prod, productions.get(prod)));
        }


        return res;
    }
}
