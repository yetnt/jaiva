import java.util.ArrayList;

import com.yetnt.*;
import com.yetnt.Token.TBooleanVar;
import com.yetnt.Token.TIntVar;
import com.yetnt.Token.TStringVar;

public class playground {
    public static int findEnclosingCharIndex(String line, char start, char end) {
        int startCount = 0;
        int endCount = 0;
        boolean isStart = true;
        for (int i = 0; i < line.length(); i++) {
            if (start != end) {
                if (line.charAt(i) == start) {
                    startCount++;
                } else if (line.charAt(i) == end) {
                    endCount++;
                }
                if (startCount == endCount && startCount != 0) {
                    return i;
                }
            } else {
                if (line.charAt(i) == end) {
                    if (!isStart) {
                        return i;
                    }
                    isStart = !isStart;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String stuff = "maak lol\n<- aowa!\nmaak poop <- 100!";
        int lineNum = 0;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<Token<?>> tokens = (ArrayList<Token<?>>) Tokenizer.readLine(stuff, "");
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Token<TBooleanVar>.TBooleanVar token = (Token<TBooleanVar>.TBooleanVar) tokens.get(0).getValue();
            System.out.println(token.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
