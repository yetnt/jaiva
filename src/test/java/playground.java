import java.util.ArrayList;

import com.yetnt.*;

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
        String stuff = "maak koo = \"df\"! maak lol = 233!";
        try {
            @SuppressWarnings("unchecked")
            ArrayList<Token<?>> m = (ArrayList<Token<?>>) Tokenizer.readLine(stuff, "");
            System.out.println(m.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
