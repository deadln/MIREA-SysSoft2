package rtu.mirea.spo2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        // Чтение файла с исходным кодом
        StringBuilder sb = new StringBuilder();
        try(FileReader reader = new FileReader("input.txt"))
        {
            int c;
            while((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String source = sb.toString();
        System.out.println((int)source.charAt(12));

        System.out.println("****************************************РАБОТА ЛЕКСЕРА****************************************");
        ArrayList<Pair<String, String>> terms = EBNFLexer.getTokensList(source);
        System.out.println("**************");
        System.out.println("СПИСОК ТОКЕНОВ");
        System.out.println("**************");
        for(var term: terms){
            System.out.println(term);
        }
        System.out.println(terms.get(4));
        System.out.println(terms.get(5));
        HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> productions = EBNFParser.getProductions(terms);

        for(var production : productions.keySet()){
            System.out.println(production);
            for(var term : productions.get(production)){
                System.out.println("\t" + term);
            }
        }
    }
}
