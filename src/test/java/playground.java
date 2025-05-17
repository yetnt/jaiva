import java.util.ArrayList;
import java.util.Arrays;

import com.jaiva.utils.Find;

public class playground {

    public static void main(String[] args) {
        String sent = "-1 * 10 - -5 / 4";
        System.out.println(Find.sanitizeStatement(sent, new ArrayList<>(Arrays.asList(0, 3, 8, 10, 13))));
    }
}
