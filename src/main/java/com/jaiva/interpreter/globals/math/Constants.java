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
    public Constants() {
        super(GlobalType.CONTAINER);
        // This is a container class for the Math class, so prefix everything with "m_"
        vfs.put("m_e", new MapValue(new VE(container)));
        vfs.put("m_pi", new MapValue(new VPi(container)));
        vfs.put("m_tau", new MapValue(new VTau(container)));
    }

    /**
     * Represents a variable that holds the mathematical constant π (pi).
     * <p>
     * The {@code VPi} class extends {@link BaseVariable} and provides a variable
     * named {@code m_pi} that contains the value of π.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_pi} - Represents the mathematical constant π (approximately
     * 3.14159).</li>
     * </ul>
     * </p>
     */
    class VPi extends BaseVariable {
        VPi(Token<?> container) {
            super("m_pi", container.new TNumberVar("m_pi", java.lang.Math.PI, -1, "The mathematical constant π (pi)"),
                    java.lang.Math.PI);
            this.freeze();
        }
    }

    /**
     * Represents a variable that holds the mathematical constant e (Euler's
     * number).
     * <p>
     * The {@code VE} class extends {@link BaseVariable} and provides a variable
     * named {@code m_e} that contains the value of e.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_e} - Represents the mathematical constant e (approximately
     * 2.71828).</li>
     * </ul>
     * </p>
     */
    class VE extends BaseVariable {
        VE(Token<?> container) {
            super("m_e",
                    container.new TNumberVar("m_e", java.lang.Math.E, -1,
                            "The mathematical constant e (Euler's number)"),
                    java.lang.Math.E);
            this.freeze();
        }
    }

    /**
     * Represents a variable that holds the mathematical constant τ (tau).
     * <p>
     * The {@code VTau} class extends {@link BaseVariable} and provides a variable
     * named {@code m_tau} that contains the value of τ, which is equal to 2π.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_tau} - Represents the mathematical constant τ (approximately
     * 6.28318).</li>
     * </ul>
     * </p>
     */
    class VTau extends BaseVariable {
        VTau(Token<?> container) {
            super("m_tau", container.new TNumberVar("m_tau", java.lang.Math.TAU, -1,
                    "The mathematical constant τ (tau), which is equal to 2π"),
                    java.lang.Math.TAU);
            this.freeze();
        }
    }

    /**
     * Represents a variable that holds the mathematical constant φ (phi), also
     * known as the golden ratio.
     * <p>
     * The {@code VPhi} class extends {@link BaseVariable} and provides a variable
     * named {@code m_phi} that contains the value of φ, which is approximately
     * 1.61803.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_phi} - Represents the golden ratio φ (approximately
     * 1.61803).</li>
     * </ul>
     * </p>
     */
    class VPhi extends BaseVariable {
        VPhi(Token<?> container) {
            super("m_phi", container.new TNumberVar("m_phi", (1 + java.lang.Math.sqrt(5)) / 2, -1,
                    "The golden ratio φ (phi)"), (1 + java.lang.Math.sqrt(5)) / 2);
            this.freeze();
        }
    }
}
