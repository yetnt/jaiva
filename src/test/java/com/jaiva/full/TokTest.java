package com.jaiva.full;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.Main;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFuncReturn;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TImport;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token.TVoidValue;

public class TokTest {

    private static String fileJIV = "C:\\Users\\ACER\\Documents\\code\\jaiva\\target\\test-classes\\file2.jiv";

    @Test
    void run1() {
        try {
            // run main directly
            // Main.main(new String[] { fileJIV, "-d" });

            // or invoke the required things so we can customize the environment.
            ArrayList<Token<?>> tokens = Main.parseTokens(fileJIV, false);

            // Now the pain begins...

            // Check for 3 tokens
            Assertions.assertEquals(3, tokens.size(), "More or less than 3 tokens???");

            // Check the first token
            {
                // The first token should be a TImport token
                Assertions.assertInstanceOf(TImport.class, tokens.getFirst().getValue(),
                        "The first token is not of the TImport class");
                TImport i = (TImport) tokens.getFirst().getValue();

                // Line number check
                Assertions.assertEquals(4, i.lineNumber, "Import is not on line 4");

                // check that it's path contains "debug.jiv"
                Assertions.assertTrue(i.filePath.contains("debug.jiv"),
                        "Path in TImprot doesn't contain \"debug.jiv\"");

                // No symbols where defined (wildcard import)
                Assertions.assertTrue(i.symbols.isEmpty(), "TImport contains specific symbols when it shouldn't");
            }

            // Check the second token
            {
                // The second token, should be a TFunction token on line 7
                Assertions.assertInstanceOf(TFunction.class, tokens.get(1).getValue(),
                        "The second token is not of the TFunction class");
                TFunction f = (TFunction) tokens.get(1).getValue();

                Assertions.assertEquals(7, f.lineNumber);

                // The name should be "F~infinity" with no args.
                Assertions.assertEquals("F~infinity", f.name, "Function is not \"F~infinity\"");
                Assertions.assertTrue(f.args.length == 0 || f.args[0].equals(""),
                        "Function was defined with arguments??");

                // function body starts a line 7 and ends at line 9
                Assertions.assertEquals(7, f.body.lineNumber,
                        "Function body starts on a different line number other than 7");
                Assertions.assertEquals(9, f.body.lineNumberEnd,
                        "Function body ends on a different line number other than 9");

                // It's body should contain 1 token.
                Assertions.assertEquals(1, f.body.lines.size(), "Function body contains more or less than 1 token.");
                // That token should be a TFuncReturn
                Assertions.assertInstanceOf(TFuncReturn.class, ((Token) f.body.lines.getFirst()).getValue(),
                        "First line of function body is not a return statement.");
                TFuncReturn return1 = (TFuncReturn) ((Token) f.body.lines.getFirst()).getValue();
                // This TFuncReturn shold be on line number 8.
                Assertions.assertEquals(8, return1.lineNumber, "Return statement is not on line 8.");

                // the return should hold a TVarRef
                Assertions.assertInstanceOf(TVarRef.class, ((Token) return1.value).getValue(),
                        "Return value is not a variable reference.");

                TVarRef varRef = (TVarRef) ((Token) return1.value).getValue();
                // both the TVarRef and TFuncReturn should be on the same line. Line 8.
                Assertions.assertEquals(8, varRef.lineNumber, "Variable reference is not on line 8.");
                // the TVarRef should hold the string in it's varName "infinity"
                Assertions.assertEquals("infinity", varRef.varName,
                        "Variable reference does not reference a symbol with the name \"infinity\"");

            }

            // and now the function call

            {
                // Check that it does indeed exist as is a TFuncCall
                Assertions.assertInstanceOf(TFuncCall.class, tokens.getLast().getValue(),
                        "The last token is not of the TFuncCall class");
                TFuncCall f = (TFuncCall) tokens.getLast().getValue();

                // Line number check (We'll be chekcing the line numnebr so often, might as well
                // save it as a constant)
                final int LINE_NUMBER = 14;

                // Currently looking at:
                /*
                 * d_emit(infinity(idk - 1, -10 + 3)(true && aowa)(1 / 10)("string things" !=
                 * 10e4))
                 */
                Assertions.assertEquals(LINE_NUMBER, f.lineNumber, "d_emit function call is not on line number 14");
                Assertions.assertEquals("d_emit", f.functionName, "d_emit function call is not \"d_emit\"");
                Assertions.assertEquals(1, f.args.size(), "d_emit was given more or less args than expected.");
                Assertions.assertInstanceOf(TFuncCall.class, ((Token) f.args.getFirst()).getValue());

                TFuncCall call4 = (TFuncCall) ((Token) f.args.getFirst()).getValue();
                // Currently looking at:
                /*
                 * infinity(idk - 1, -10 + 3)(true && aowa)(1 / 10)("string things" != 10e4)
                 * 
                 * which means a function who's name is inner TFuncCall,
                 * who's args is a TStatement ("string things" != 10e4)
                 */
                Assertions.assertEquals(LINE_NUMBER, call4.lineNumber, "Last call, is not on line 14");
                Assertions.assertEquals(1, call4.args.size(), "Last call, has more or less args than 1");
                Assertions.assertInstanceOf(TStatement.class, (TStatement) ((Token) call4.args.getFirst()).getValue(),
                        "Last call's arguments is not a TStatement.");
                TStatement lastCallStatement = (TStatement) ((Token) call4.args.getFirst()).getValue();
                Assertions.assertEquals(LINE_NUMBER, lastCallStatement.lineNumber,
                        "Last call's TStatement is not on line 14");
                Assertions.assertEquals("!=", lastCallStatement.op,
                        "Last call's TStatement's operator is not !=");
                Assertions.assertEquals("string things", lastCallStatement.lHandSide,
                        "Last call's TStatement's left hand side is incorrect.");
                Assertions.assertEquals(10e4, lastCallStatement.rHandSide,
                        "Last call's TStatement's right hand side is incorrect.");
                Assertions.assertInstanceOf(TFuncCall.class, ((Token) call4.functionName).getValue(),
                        "Last call's function name is not a function call.");

                TFuncCall call3 = (TFuncCall) ((Token) call4.functionName).getValue();
                // Currently looking at:
                /*
                 * infinity(idk - 1, -10 + 3)(true && aowa)(1 / 10)
                 * 
                 * which means a function who's name is inner TFuncCall,
                 * who's args is a TStatement (1 / 10)
                 */
                Assertions.assertEquals(LINE_NUMBER, call3.lineNumber, "3rd call, is not on line 14");
                Assertions.assertEquals(1, call3.args.size(), "3rd call, has more or less args than 1");
                Assertions.assertInstanceOf(TStatement.class, (TStatement) ((Token) call3.args.getFirst()).getValue(),
                        "3rd call's arguments is not a TStatement.");
                TStatement call3Statement = (TStatement) ((Token) call3.args.getFirst()).getValue();
                Assertions.assertEquals(LINE_NUMBER,
                        call3Statement.lineNumber,
                        "3rd call's TStatement is not on line 14");
                Assertions.assertEquals("/",
                        call3Statement.op,
                        "3rd call's TStatement's operator is not /");
                Assertions.assertEquals(1,
                        call3Statement.lHandSide,
                        "3rd call's TStatement's left hand side is incorrect.");
                Assertions.assertEquals(10,
                        call3Statement.rHandSide,
                        "3rd call's TStatement's right hand side is incorrect.");
                Assertions.assertInstanceOf(TFuncCall.class, ((Token) call3.functionName).getValue(),
                        "3rd call's function name is not a function call.");

                TFuncCall call2 = (TFuncCall) ((Token) call3.functionName).getValue();
                // Currently looking at:
                /*
                 * infinity(idk - 1, -10 + 3)(true && aowa)
                 * 
                 * which means a function who's name is inner TFuncCall,
                 * who's args is a TStatement (true && aowa)
                 */
                Assertions.assertEquals(LINE_NUMBER, call2.lineNumber, "2nd call, is not on line 14");
                Assertions.assertEquals(1, call2.args.size(), "2nd call, has more or less args than 1");
                Assertions.assertInstanceOf(TStatement.class, (TStatement) ((Token) call2.args.getFirst()).getValue(),
                        "2nd call's arguments is not a TStatement.");
                TStatement call2Statement = (TStatement) ((Token) call2.args.getFirst()).getValue();
                Assertions.assertEquals(LINE_NUMBER,
                        call2Statement.lineNumber,
                        "2nd call's TStatement is not on line 14");
                Assertions.assertEquals("&&",
                        call2Statement.op,
                        "2nd call's TStatement's operator is not &&");
                Assertions.assertEquals(true,
                        call2Statement.lHandSide,
                        "2nd call's TStatement's left hand side is incorrect.");
                Assertions.assertEquals(false,
                        call2Statement.rHandSide,
                        "2nd call's TStatement's right hand side is incorrect.");
                Assertions.assertInstanceOf(TFuncCall.class, ((Token) call2.functionName).getValue(),
                        "2nd call's function name is not a function call.");

                TFuncCall call1 = (TFuncCall) ((Token) call2.functionName).getValue();
                // Currently looking at:
                /*
                 * infinity(idk - 1, -10 + 3)
                 * 
                 * which means a function who's name is finally, a string "infinity"
                 * who's has 2 args (idk - 1, -10 + 3)
                 * arg1: idk - 1
                 * arg2: -10 + 3 (Where -10 is itself a TStatement, being 10 * -1)
                 */
                Assertions.assertEquals(LINE_NUMBER, call1.lineNumber, "First call, is not on line 14");
                Assertions.assertEquals(2, call1.args.size(), "First call, has more or less args than 1");
                Assertions.assertInstanceOf(TStatement.class, (TStatement) ((Token) call1.args.getFirst()).getValue(),
                        "First call's 1st arg is not a TStatement.");
                Assertions.assertInstanceOf(TStatement.class, (TStatement) ((Token) call1.args.getLast()).getValue(),
                        "First call's 2nd arg is not a TStatement.");
                TStatement call1Statement1 = (TStatement) ((Token) call1.args.getFirst()).getValue();
                TStatement call1Statement2 = (TStatement) ((Token) call1.args.getLast()).getValue();

                Assertions.assertEquals(LINE_NUMBER,
                        call1Statement1.lineNumber,
                        "First call's TStatement (arg1) is not on line 14");
                Assertions.assertEquals(LINE_NUMBER,
                        call1Statement2.lineNumber,
                        "First call's TStatement (arg2) is not on line 14");
                // ARG 1 CHECKING
                Assertions.assertEquals("-",
                        call1Statement1.op,
                        "First call's TStatement's (arg1) operator is not -");
                Assertions.assertInstanceOf(TVoidValue.class,
                        call1Statement1.lHandSide,
                        "First call's TStatement's (arg1) left hand side is incorrect.");
                Assertions.assertEquals(1,
                        call1Statement1.rHandSide,
                        "First call's TStatement's (arg1) right hand side is incorrect.");
                // ARG 2 CHECKING
                Assertions.assertEquals("+",
                        call1Statement2.op,
                        "First call's TStatement's (arg2) operator is not -");
                Assertions.assertInstanceOf(TStatement.class,
                        ((Token) call1Statement2.lHandSide).getValue(),
                        "First call's TStatement's (arg2) left hand side is incorrect.");
                TStatement st2lhs = (TStatement) ((Token) call1Statement2.lHandSide).getValue();

                Assertions.assertEquals(LINE_NUMBER,
                        st2lhs.lineNumber,
                        "First call's TStatement (arg2) is not on line 14");
                Assertions.assertEquals("*",
                        st2lhs.op,
                        "First call's TStatement's (arg2) left hand side's inner operator is not *");
                Assertions.assertEquals(-1,
                        st2lhs.lHandSide,
                        "First call's TStatement's (arg2) left hand side's lhs is incorrect.");
                Assertions.assertEquals(10,
                        st2lhs.rHandSide,
                        "First call's TStatement's (arg2) right hand side's rhs is incorrect.");

                Assertions.assertEquals(3,
                        call1Statement2.rHandSide,
                        "First call's TStatement's (arg2) right hand side is incorrect.");

                Assertions.assertEquals("infinity", call1.functionName,
                        "First call's function name is not a function call.");
            }

        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }
}
