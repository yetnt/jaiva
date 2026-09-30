package com.jaiva.interpreter.libs.math;

import com.jaiva.interpreter.libBuilders.var.VariableBuilder;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TNumberVar;

/**
 * Container class for mathematical constants.
 */
@JaivaLibrary(path = "math/const", description = "The math constants that'll never change")
public class MathConst extends BaseLibrary {
    /**
     * Default Constructor
     */
    public MathConst() {
        super();
        // This is a container class for the MathBase class, so prefix everything with "m_"
        add(new VE(), new VPi(), new VTau(), new VPhi());
    }

    /**
     * "m_pi" constant π (pi).
     */
    static class VPi extends BaseVariable {
        /**
         * Pi Constructor
         *
         */
        VPi() {
            super(
                    VariableBuilder.start()
                            .name("m_pi")
                            .value(java.lang.Math.PI)
                            .docs(
                                    JDoc.builder()
                                            .addDesc("The mathematical constant π (pi)")
                                            .addNote("It's just java.lang.Math.PI")
                            )
            );
            freeze();
        }
    }

    /**
     * "m_e" constant e (Euler's number).
     */
    static class VE extends BaseVariable {
        /**
         * Eulers number constructor
         *
         */
        VE() {
            super(
                    VariableBuilder.start()
                            .name("m_e")
                            .value(java.lang.Math.E)
                            .docs(
                                    JDoc.builder()
                                            .addDesc("The mathematical constant e (Euler's number)")
                                            .addNote("Just java.lang.Math.E")
                                            .addExample("""
                                            khuluma(2 ^ m_e)! @ approximately 7.38905609893065
                                            """)
                            )
            );
            freeze();
        }
    }

    /**
     * "m_tau" constant τ (tau) (2π).
     */
    static class VTau extends BaseVariable {
        /**
         * Tau constructor
         *
         */
        VTau() {
            super(
                    VariableBuilder.start()
                            .name("m_tau")
                            .value(java.lang.Math.TAU)
                            .docs(
                                    JDoc.builder()
                                            .addDesc("The mathematical constant τ (tau), which is equal to 2π")
                                            .addNote("Just java.lang.Math.TAU")
                                            .addExample("""
                                            @ Using tau to calculate the circumference of a circle with radius 5
                                            maak radius <- 5!
                                            maak circumference <- m_tau * radius!
                                            khuluma(circumference)! @ approximately 31.41592653589793
                                            """)
                            )
            );
            freeze();
        }
    }

    /**
     * "m_phi" constant φ (phi), also
     * known as the golden ratio.
     */
    static class VPhi extends BaseVariable {
        /**
         * Phi constructor
         *
         */
        VPhi() {
            super(
                    VariableBuilder.start()
                            .name("m_phi")
                            .value((1 + java.lang.Math.sqrt(5)) / 2)
                            .docs(
                                    JDoc.builder()
                                            .addDesc("The golden ratio φ (phi)")
                                            .addExample("""
                                            @ Calculating the golden rectangle dimensions
                                            maak shortSide <- 10!
                                            maak longSide <- shortSide * m_phi!
                                            khuluma("Long side of the golden rectangle: " + longSide)! @ approximately 16.18033988749895
                                            """)
                                            .addNote("No note here.")
                            )
            );
            freeze();
        }
    }
}
