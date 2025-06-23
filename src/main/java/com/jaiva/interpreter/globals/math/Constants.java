package com.jaiva.interpreter.globals.math;

import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.globals.BaseGlobals;
import com.jaiva.interpreter.globals.GlobalType;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.Token;

/**
 * Container class for mathematical constants.
 */
public class Constants extends BaseGlobals {
    /**
     * Default Constructor
     */
    public Constants() {
        super(GlobalType.CONTAINER);
        // This is a container class for the Math class, so prefix everything with "m_"
        vfs.put("m_e", new MapValue(new VE(container)));
        vfs.put("m_pi", new MapValue(new VPi(container)));
        vfs.put("m_tau", new MapValue(new VTau(container)));
        vfs.put("m_phi", new MapValue(new VPhi(container)));
    }

    /**
     * "m_pi" constant π (pi).
     */
    class VPi extends BaseVariable {
        /**
         * Pi Constructor
         * 
         * @param container Token container.
         */
        VPi(Token<?> container) {
            super("m_pi", container.new TNumberVar("m_pi", java.lang.Math.PI, -1, "The mathematical constant π (pi)"),
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
         * @param container Token container.
         */
        VE(Token<?> container) {
            super("m_e",
                    container.new TNumberVar("m_e", java.lang.Math.E, -1,
                            "The mathematical constant e (Euler's number)"),
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
         * @param container Token container.
         */
        VTau(Token<?> container) {
            super("m_tau", container.new TNumberVar("m_tau", java.lang.Math.TAU, -1,
                    "The mathematical constant τ (tau), which is equal to 2π"),
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
         * @param container Token container
         */
        VPhi(Token<?> container) {
            super("m_phi", container.new TNumberVar("m_phi", (1 + java.lang.Math.sqrt(5)) / 2, -1,
                    "The golden ratio φ (phi)"), (1 + java.lang.Math.sqrt(5)) / 2);
            this.freeze();
        }
    }
}
