package com.yetnt;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Main {
    public static void main(String[] args) {
        File myObj = new File("C://Users//ACER//Documents//code//jaiva//src//main//resources//test2.jiv");
        try {
            ArrayList<Token<?>> tokens = new ArrayList<>();
            Tokenizer.MultipleLinesOutput m = null;
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String previousLine = "";
                String line = myReader.nextLine();
                Object something = Tokenizer.readLine(line, previousLine, m);
                if (something instanceof Tokenizer.MultipleLinesOutput) {
                    m = (Tokenizer.MultipleLinesOutput) something;
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    tokens.addAll((ArrayList<Token<?>>) something);

                }
            }

            for (Token<?> t : tokens) {
                System.err.println(t.toString());
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