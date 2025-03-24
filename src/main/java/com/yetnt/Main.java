package com.yetnt;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

import com.yetnt.tokenizer.BlockChain;
import com.yetnt.tokenizer.FindEnclosing;
import com.yetnt.tokenizer.Token;
import com.yetnt.tokenizer.Tokenizer;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        File myObj = new File("C://Users//ACER//Documents//code//jaiva//src//main//resources//test2.jiv");
        try {
            ArrayList<Token<?>> tokens = new ArrayList<>();
            FindEnclosing.MultipleLinesOutput m = null;
            BlockChain b = null;
            Scanner myReader = new Scanner(myObj);
            String previousLine = "";
            while (myReader.hasNextLine()) {
                String line = (b != null ? b.getCurrentLine() : myReader.nextLine());
                // System.out.println(line);
                // System.out.println(line);
                Object something = Tokenizer.readLine(line, (b != null ? "" : previousLine), m, b);
                if (something instanceof FindEnclosing.MultipleLinesOutput) {
                    m = (FindEnclosing.MultipleLinesOutput) something;
                    b = null;
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    b = null;
                    tokens.addAll((ArrayList<Token<?>>) something);
                } else if (something instanceof BlockChain) {
                    m = null;
                    b = (BlockChain) something;
                } else {
                    b = null;
                    m = null;
                }
                previousLine = line;
            }

            for (Token<?> t : tokens) {
                System.out.println(t.toString());
                // System.out.println(t.getValue().getContents(0));
            }
            System.out.println(tokens.size());
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("errrorrrr");
            e.printStackTrace();
        }
    }
}