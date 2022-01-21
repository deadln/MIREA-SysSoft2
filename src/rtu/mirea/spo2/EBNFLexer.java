package rtu.mirea.spo2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Идея представления выражений: хранить выражения в HashMap(Имя терминала/нетерминала : выражение)
 */

public class EBNFLexer {
    static HashMap<String, String> tokenPatterns;
    static HashMap<String, Integer> tokenPriorities;

    static void initTokenMaps(){
        // Регулярные выражения токенов
        tokenPatterns = new HashMap<>();
        tokenPatterns.put("^([a-z_]{0,})$", "NONTERMINAL");
        tokenPatterns.put("^[A-Z_]{1,}$|^'[a-zа-я]{1,}'$|^'\\+'$|^'-'$|^'\\*'$|^'/'$|^'#'$|^'\\='$|^'\\=\\='$|^'>'$|^'>='$|^'<'$|^'<='$|^'\\('$|^'\\)'$|^'\\{'$|^'\\}'$|^';'$", "TERMINAL");
//        tokenPatterns.put("^[A-Z_]{1,}$|^'[a-zа-я]{1,}'$|^'\\+'$|^'-'$|^'\\*'$|^'/'$|^'#'$|^'\\='$|^'\\=\\='$|^'>'$|^'>='$|^'<'$|^'<='$|^'\\('$|^'\\)'$|^'\\{'$|^'\\}'$|^';'$", "TERMINAL");
        tokenPatterns.put("^\\+$|^\\*$|^\\?$", "OP");
        tokenPatterns.put("^\\|$", "OR");
        tokenPatterns.put("^\n$|^\r$", "EOL");
        tokenPatterns.put("^=$", "IS");
        tokenPatterns.put("^\s$", "WS"); // Нужен ли?
        tokenPatterns.put("^\\($", "L_BR");
        tokenPatterns.put("^\\)$", "R_BR");
        tokenPatterns.put("^\0$", "EOF");




//        tokenPatterns.put("^([a-zA-Z_]{1}[a-zA-Z_0-9]{0,})$|^([а-яА-Я_]{1}[а-яА-Я_0-9]{0,})$", "VAR");
//        tokenPatterns.put("^\\+$|^-$|^\\*$|^/$|^#$", "OP");
//        tokenPatterns.put("^\\==$|^>$|^>=$|^<$|^<=$", "LOGICAL_OP");
//        tokenPatterns.put("^=$", "ASSIGN_OP");
//        tokenPatterns.put("^0|[1-9&&[^\s]][0-9&&[^\s]]*$", "NUMBER");
//        tokenPatterns.put("^ако$", "IF_KW");
//        tokenPatterns.put("^инако$", "ELSE_KW");
//        tokenPatterns.put("^покаместъ$", "WHILE_KW");
//        tokenPatterns.put("^делати$", "DO_KW");
//        tokenPatterns.put("^изъявити$", "PRINT_KW");
//
//        tokenPatterns.put("^\s$", "WS");
//        tokenPatterns.put("^\\($", "L_BR");
//        tokenPatterns.put("^\\)$", "R_BR");
//        tokenPatterns.put("^\\{$", "L_S_BR");
//        tokenPatterns.put("^\\}$", "R_S_BR");
//        tokenPatterns.put("^;$", "SEP");
//        tokenPatterns.put("^целый$|^плавающий$|^срока$|^символъ$|^суть$|^испис$|^замет$", "VAR_TYPE");
//        // Приоритеты токенов
//        tokenPriorities = new HashMap<>();
//        tokenPriorities.put("WS", 0);
//        tokenPriorities.put("VAR", 1);
//        tokenPriorities.put("VAR_TYPE", 2);
//        tokenPriorities.put("IF_KW", 3);
//        tokenPriorities.put("ELSE_KW", 3);
//        tokenPriorities.put("WHILE_KW", 3);
//        tokenPriorities.put("DO_KW", 3);
//        tokenPriorities.put("PRINT_KW", 3);

    }

    public static ArrayList<Pair<String, String>> getTokensList(String string) {
        initTokenMaps();

        string = string + "\0";
//        HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> terms = new HashMap<>();
        ArrayList<Pair<String, String>> terms = new ArrayList<>();
        StringBuilder accum = new StringBuilder();
        ArrayList<String> prevTokens = new ArrayList<>();
        boolean flag, term_filling = false;
        String fitting_token = "";
        Pair<String, String> current_term = new Pair("", "");

        for(int i = 0; i < string.length(); i++)
        {
            accum.append(string.charAt(i));
            System.out.println("ACCUM: " + accum);
            flag = false;
            for (var regex:
                    tokenPatterns.keySet()) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(accum);
                if(matcher.matches())
                {
                    System.out.println("REGEX: " + regex);
//                    if(prevTokens.size() > 0 && !flag)
//                    {
//                        prevTokens.clear();
//                    }
                    flag = true;
//                    prevTokens.add(tokenPatterns.get(regex));
                    fitting_token = tokenPatterns.get(regex);
                }
            }
            if(fitting_token == "" && accum.charAt(0) == '\''){
                int j = i + 1;
                while(j < string.length() && string.charAt(j) != '\'') {
                    accum.append(string.charAt(j));
                    j++;
                }
                accum.append(string.charAt(j));
                terms.add(new Pair("REGEX", accum.toString()));
                accum = new StringBuilder();
                i = j;
                continue;
            }
            if(fitting_token == "" && accum.length() > 0 && accum.charAt(0) == '\0')
//                terms.add(new Pair<>("EOL", "\n"));
                break;
            if(!flag)
            {
                if(fitting_token.length() == 0)
                {
                    System.out.println("ОШИБКА: токен не распознан");
                    System.exit(10);
                }
//                else if (prevTokens.size() > 1)
//                {
//                    String minn = prevTokens.get(0);
//                    for (var tok:
//                         prevTokens) {
//                        if(tokenPriorities.get(tok) > tokenPriorities.get(minn))
//                            minn = tok;
//                    }
//                    System.out.println("+TOKEN1: " + accum.substring(0, accum.length() - 1));
//                    tokens.add(new Pair<>(minn, accum.substring(0, accum.length() - 1)));
//                }
                else
                {
                    if(fitting_token.equals("WS")){ // || fitting_token.equals("IS")){
                        ;
                    }
//                    else if(fitting_token.equals("EOL")){
//                        term_filling = false;
//                    }
                    else{
//                        if(!term_filling && (fitting_token.equals("NONTERMINAL") || fitting_token.equals("TERMINAL"))){
//                            term_filling = true;
//                            current_term = new Pair(fitting_token, accum.substring(0, accum.length() - 1));
//                            terms.put(current_term, new ArrayList<>());
//                        }
//                        else{
//                            terms.get(current_term).add(new Pair(fitting_token, accum.substring(0, accum.length() - 1)));
//                        }
                        terms.add(new Pair(fitting_token, accum.substring(0, accum.length() - 1)));
                    }

                    System.out.println("+TOKEN: " + "(" + fitting_token + ", " + accum.substring(0, accum.length() - 1) + ")");
//                    tokens.add(new Pair<>(prevTokens.get(0), accum.substring(0, accum.length() - 1)));
                }
                accum = new StringBuilder();
//                prevTokens.clear();
                fitting_token = "";
                i--;
            }
        }

//        if(prevTokens.size() == 0)
//        {
//            System.out.println("ОШИБКА: токен не распознан");
//            System.exit(10);
//        }
//        else if (prevTokens.size() > 1)
//        {
//            System.out.println("ОШИБКА: неоднозначный токен");
//            System.exit(11);
//        }
//        else
//        {
//            System.out.println("+TOKEN: " + accum);
//            tokens.add(new Pair<String, String>(prevTokens.get(0), accum.toString()));
//        }
        terms.add(new Pair<>("EOL", "\n"));
        ArrayList<Pair<String, String>> spaceless_terms = new ArrayList<>();
        for(var term: terms){
            if(!term.getFirst().equals("WS") && !term.getSecond().equals("\r")){
                spaceless_terms.add(term);
            }
        }
//        for(var term: terms.keySet())
//        {
//            spaceless_terms.put(term, new ArrayList<>());
//            for(var subterm: terms.get(term)){
//                if(!subterm.getFirst().equals("WS") || !subterm.getFirst().equals("IS") || !subterm.getFirst().equals("EOL"))
//                    spaceless_terms.get(term).add(subterm);
//            }
////            if(!tok.getFirst().equals("WS"))
////                spaceless_tokens.add(tok);
//        }
        return spaceless_terms;
    }
}
