package com.nikpappas.sketch.fractal;

import com.nikpappas.number.ComplexAny;

import java.util.function.BiFunction;

import static java.lang.Math.abs;
import static java.lang.String.format;

@FunctionalInterface
interface MandelbrotFunction extends BiFunction<ComplexAny, ComplexAny, ComplexAny> {
}

public class MandelbrotSet {
    private final int maxIterations;

    private static final int DEFAULT_ITERATIONS = 10000;
    private static final MandelbrotFunction SQUARE = (cur, c) -> (cur.square()).add(c);
    private static final MandelbrotFunction CUBE = (cur, c) -> ((cur.square()).multiply(cur)).add(c);
    private final MandelbrotFunction function;
    private final String functionRepresentation;


    public MandelbrotSet() {
        this(DEFAULT_ITERATIONS);
    }

    public MandelbrotSet(int maxIterations) {
        this(maxIterations, SQUARE, "z^2");
    }

    private MandelbrotSet(int maxIterations, MandelbrotFunction function, String functionRepresentation) {
        this.maxIterations = maxIterations;
        this.function = function;
        this.functionRepresentation = functionRepresentation;
    }

    public boolean contains(ComplexAny c) {
        return isMandelbrot(c);
    }

    private boolean isMandelbrot(ComplexAny c) {
        double limit = 2;
        ComplexAny cur = c;
        for (int i = 0; i < maxIterations; i++) {
//            System.out.println(i);
            cur = function.apply(cur, c);
//            System.out.println(cur);
            if (cur.abs() > (limit)) {
                return false;
            }
        }
        return true;
    }

    public static MandelbrotSet ofSquare() {
        return new MandelbrotSet(DEFAULT_ITERATIONS, SQUARE, "z^2");
    }

    public static MandelbrotSet ofSquare(int numIterations) {
        return new MandelbrotSet(numIterations, SQUARE, "z^2");
    }

    public static MandelbrotSet ofCube() {
        return ofCube(DEFAULT_ITERATIONS);
    }

    public static MandelbrotSet ofCube(int numIterations) {
        return new MandelbrotSet(numIterations, CUBE, "z^3");
    }

    public static MandelbrotSet ofPower(int pow, int maxIterations) {
        if (!(1 < pow && pow < 22)) {
            throw new IllegalArgumentException("The mandelbrot function power needs to be between 2 and 9 inclusive");
        }
        MandelbrotFunction manFun = (cur, c) -> (cur.pow(pow)).add(c);

        return new MandelbrotSet(maxIterations, manFun, "z^" + pow);
    }

    public static MandelbrotSet ofPower(int pow) {
        return ofPower(pow, DEFAULT_ITERATIONS);
    }

    public static MandelbrotSet ofPoly(int... coefficients) {
        if (0 == coefficients.length || coefficients.length > 10) {
            throw new IllegalArgumentException("The mandelbrot parameters need to be more than one and up to 10 inclusive");
        }
        MandelbrotFunction manFun = (cur, c) -> {
            ComplexAny toRet = cur.multiply(0);
            int i = 0;
            while (i < coefficients.length) {
                toRet = toRet.add((cur.pow(i + 1)).multiply(coefficients[i]));
                i++;
            }
            return toRet.add(c);
        };

        return new MandelbrotSet(DEFAULT_ITERATIONS, manFun, printsCoefficients(coefficients));
    }

    private static String printsCoefficients(int[] coefficients) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] == 0) {
                continue;
            }
            if (sb.length() > 0) {
                if (coefficients[i] > 0) {
                    sb.append(" + ");
                } else {
                    sb.append(" - ");
                }
            } else if (coefficients[i] < 0) {
                sb.append("-");
            }
            if (abs(coefficients[i]) == 1) {
                sb.append(format("z^%d", i + 1));
            } else {
                if (i == 0) {
                    sb.append(format("%d*z", abs(coefficients[i])));
                } else {
                    sb.append(format("%d*z^%d", abs(coefficients[i]), i + 1));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MandelbrotSet{" +
                "maxIterations=" + maxIterations +
                ", function=" + "zn+1=" + functionRepresentation + " + c" +
                '}';
    }
}
