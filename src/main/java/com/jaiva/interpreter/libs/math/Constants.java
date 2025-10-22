package com.jaiva.interpreter.libs.math;

import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.tokenizer.jdoc.JDoc;

/**
 * Container class for mathematical constants.
 */
public class Constants extends BaseLibrary {
    /**
     * Default Constructor
     */
    public Constants() {
        super(LibraryType.CONTAINER);
        // This is a container class for the Math class, so prefix everything with "m_"
        vfs.put("m_e", new MapValue(new VE()));
        vfs.put("m_pi", new MapValue(new VPi()));
        vfs.put("m_tau", new MapValue(new VTau()));
        vfs.put("m_phi", new MapValue(new VPhi()));
    }

    /**
     * "m_pi" constant π (pi).
     */
    class VPi extends BaseVariable {
        /**
         * Pi Constructor
         *
         */
        VPi() {
            super("m_pi", new TNumberVar("m_pi", java.lang.Math.PI, -1,
                            JDoc.builder()
                                    .addDesc("The mathematical constant π (pi)")
                                    .addNote("It's just java.lang.Math.PI")
                                    .build()
                    ),
                    java.lang.Math.PI);
            this.freeze();
        }
    }

    /**
     * "m_e" constant e (Euler's number).
     */
    class VE extends BaseVariable {
        /**
         * Eulers number constructor
         *
         */
        VE() {
            super("m_e",
                    new TNumberVar("m_e", java.lang.Math.E, -1,
                            JDoc.builder()
                                    .addDesc("The mathematical constant e (Euler's number)")
                                    .addNote("Just java.lang.Math.E")
                                    .build()
                    ),
                    java.lang.Math.E);
            this.freeze();
        }
    }

    /**
     * "m_tau" constant τ (tau) (2π).
     */
    class VTau extends BaseVariable {
        /**
         * Tau constructor
         *
         */
        VTau() {
            super("m_tau", new TNumberVar("m_tau", java.lang.Math.TAU, -1,
                            JDoc.builder()
                                    .addDesc("The mathematical constant τ (tau), which is equal to 2π")
                                    .addNote("Just java.lang.Math.TAU")
                                    .build()
                    ),
                    java.lang.Math.TAU);
            this.freeze();
        }
    }

    /**
     * "m_phi" constant φ (phi), also
     * known as the golden ratio.
     */
    class VPhi extends BaseVariable {
        /**
         * Phi constructor
         *
         */
        VPhi() {
            super("m_phi", new TNumberVar("m_phi", (1 + java.lang.Math.sqrt(5)) / 2, -1,
                    JDoc.builder()
                            .addDesc("The golden ratio φ (phi)")
                            .addNote("No note here.")
                            .build()
            ), (1 + java.lang.Math.sqrt(5)) / 2);
            this.freeze();
        }
    }
}
