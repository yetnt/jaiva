package com.jaiva.interpreter.libs.math;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

import java.util.ArrayList;

public class MathUtils extends BaseLibrary {
    public static String path = "math/utils";

    public MathUtils() {
        super(LibraryType.LIB, "math/utils");

        vfs.put("m_gcd", new FGcd());
        vfs.put("m_lcm", new FLcm());
    }

    private ArrayList<Integer> toIntegerList(ArrayList<Object> params, String funcName, BaseFunction func, IConfig<Object> config, Scope scope, int lineNumber) throws Exception {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof Integer) {
                nums.add((Integer) p);
            } else {
                Object possible = Primitives.toPrimitive(p, false, config, scope);
                if (possible instanceof Integer possibleInt) {
                    if (possibleInt < 0) {
                        throw new InterpreterException.WtfAreYouDoingException(scope, funcName + " is only defined for non-negative integers", lineNumber);
                    }
                    nums.add(possibleInt);
                } else {
                    throw new InterpreterException.FunctionParametersException(scope, func, i + "", possible, Integer.class, lineNumber);
                }
            }
        }
        return nums;
    }


    /**
     * Computes the GCD of two integers using the Euclidean algorithm.
     *
     * @param a First integer
     * @param b Second integer
     * @return GCD of a and b
     */
    private static int computeGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return computeGCD(b, a % b);
    }

    public class FGcd extends BaseFunction {
        public FGcd() {
            super("m_gcd",
                    new TFunction("m_gcd", new String[]{"<-nums"}, null, -1,
                            JDoc.builder()
                                    .addDesc("Calculates the greatest common divisor (GCD) of a list of numbers.")
                                    .addParam("nums", "array", "Variable amount of numbers to calculate the GCD for.", true)
                                    .addReturns("The greatest common divisor of the provided numbers.")
                                    .addExample("""
                                            khuluma(m_gcd(10, 20, 42, 20))! @ Outputs: 2
                                            khuluma(m_gcd(54, 24, 36))! @ Outputs: 6
                                            """)
                                    .sinceVersion("5.0.0")
                                    .build()
                    )
            );
        }


        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            if (params.size() < 2)
                throw new InterpreterException.FunctionParametersException(scope, this, params.size(), 2, tFuncCall.lineNumber);

            ArrayList<Integer> nums = toIntegerList(params, "gcd", this, config, scope, tFuncCall.lineNumber);

            int gcd = nums.get(0);
            for (int i = 1; i < nums.size(); i++) {
                gcd = computeGCD(gcd, nums.get(i));
                if (gcd == 1) {
                    break; // GCD is 1, no need to continue
                }
            }
            return gcd;

        }
    }

    public class FLcm extends BaseFunction {
        public FLcm() {
            super("m_lcm",
                    new TFunction("m_lcm", new String[]{"<-nums"}, null, -1,
                            JDoc.builder()
                                    .addDesc("Calculates the least common multiple (LCM) of a list of numbers.")
                                    .addParam("nums", "array", "Variable amount of numbers to calculate the LCM for.", true)
                                    .addReturns("The least common multiple of the provided numbers.")
                                    .addExample("""
                                            khuluma(m_lcm(4, 5, 6))! @ Outputs: 60
                                            khuluma(m_lcm(7, 3, 14))! @ Outputs: 42
                                            """)
                                    .sinceVersion("5.0.0")
                                    .build()
                    )
            );
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            if (params.size() < 2)
                throw new InterpreterException.FunctionParametersException(scope, this, params.size(), 2, tFuncCall.lineNumber);

            ArrayList<Integer> nums = toIntegerList(params, "lcm", this, config, scope, tFuncCall.lineNumber);

            int lcm = nums.get(0);
            for (int i = 1; i < nums.size(); i++) {
                int num = nums.get(i);
                lcm = (lcm * num) / computeGCD(lcm, num);
            }
            return lcm;
        }
    }
}
