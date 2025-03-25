
import java.util.ArrayList;
import java.util.Arrays;

import com.yetnt.tokenizer.ContextDispatcher;;

public class playground {

    public static void main(String[] args) {
        ArrayList<String> testCases = new ArrayList<>(Arrays.asList(
                "()",
                "arr[2]",
                "arr[2 - 4]",
                "func(arr[2]) - arr[200]",
                "[]",
                "",
                "a",
                "1 + a",
                "a + func(b)",
                "1 + a - (a / b)",
                ")",
                "(",
                "func()",
                "func(func(d))",
                "(1 - c) + n",
                "func(a + b)",
                "func(a + (b - a))",
                "func(func(a - b))",
                "func(func(a) | b)",
                "func((1 > 1) | func(10))",
                "(1 > 1) | func(10)",
                "func(a) + b",
                "func(func(b)) - a",
                "func(a + func(c)) + v",
                "func(2) > func(4)",
                "func(func(b - g)) - func(c)",
                "func(c) - func(c) -func(c) - func(func(c))",
                "func(l) | (f > b)",
                "arr[func(c) - func(c) -func(c) - func(func(c))]",
                "func(c) - func(c) -func(c) - arr[func(c)]"));

        testCases.forEach(c -> {
            System.out.println();
            ContextDispatcher d = new ContextDispatcher(c);
            System.out.println(d.bits + " " + c + "\t\t| " + d.printCase() + " | " + d.toBitString());
        });

    }
}
