package com.nikpappas.number;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;

/**
 * Is quite slower than com.nikpappas.number.Complex due to its higher precision.
 */
public class ComplexBD extends Number implements ComplexAny {
    public static final ComplexBD ZERO = ComplexBD.of(BigDecimal.ZERO, BigDecimal.ZERO);
    private final MathContext MATH_CONTEXT = new MathContext(16);
    public final BigDecimal real;
    public final BigDecimal imaginary;

    private ComplexBD(BigDecimal real, BigDecimal imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexAny add(ComplexAny toAdd) {
        return of(real.doubleValue() + toAdd.real(),
                imaginary.doubleValue() + toAdd.imaginary());
    }

    public ComplexBD add(ComplexBD toAdd) {
        return of(real.add(toAdd.real), imaginary.add(toAdd.imaginary));
    }

    public ComplexBD subtract(ComplexBD toSubtract) {
        return of(real.subtract(toSubtract.real), imaginary.subtract(toSubtract.imaginary));
    }

    /**
     * @param toMultiply
     * @return (ar + ai * i)*(br + bi*i) =
     * ar*br-ai*bi , br*ai+ar*bi
     */
    public ComplexAny multiply(ComplexAny toMultiply) {
        BigDecimal ar = real;
        BigDecimal ai = imaginary;
        BigDecimal br = valueOf(toMultiply.real());
        BigDecimal bi = valueOf(toMultiply.imaginary());


        return of((ar.multiply(br)).subtract(ai.multiply(bi)), (ai.multiply(br)).add(bi.multiply(ar)));
    }

    public ComplexAny square() {
        return this.multiply(this);
    }

    @Override
    public int compareTo(ComplexAny o) {
        return Comparator.comparingDouble(ComplexAny::abs).compare(this, o);
    }

    @Override
    public int intValue() {
        return (int) abs();
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return (float) abs();
    }

    @Override
    public double doubleValue() {
        return abs();
    }

    public double abs() {
        return ((real.multiply(real)).add(imaginary.multiply(imaginary))).sqrt(MATH_CONTEXT).doubleValue();
    }

    @Override
    public double imaginary() {
        return imaginary.doubleValue();
    }

    @Override
    public double real() {
        return real.doubleValue();
    }

    @Override
    public ComplexAny multiply(int i) {
        return ComplexBD.of(real.multiply(valueOf(i)), imaginary.multiply(valueOf(i)));
    }

    public static ComplexBD max(ComplexBD a, ComplexBD b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static ComplexBD min(ComplexBD a, ComplexBD b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public static ComplexBD of(BigDecimal real, BigDecimal imaginary) {
        return new ComplexBD(
                Optional.ofNullable(real).orElse(BigDecimal.ZERO),
                Optional.ofNullable(imaginary).orElse(BigDecimal.ZERO)
        );
    }

    public static ComplexBD of(double real, double imaginary) {
        return of(valueOf(real), valueOf(imaginary));
    }

    public static ComplexBD of(long real, long imaginary) {
        return of(valueOf(real), valueOf(imaginary));
    }

    public static ComplexBD of(int real, int imaginary) {
        return of(valueOf(real), valueOf(imaginary));
    }

    public static ComplexBD of(float real, float imaginary) {
        return of(valueOf(real), valueOf(imaginary));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexBD complex1 = (ComplexBD) o;

        if (!real.equals(complex1.real)) return false;
        return imaginary.equals(complex1.imaginary);
    }

    @Override
    public int hashCode() {
        int result = real.hashCode();
        result = 31 * result + imaginary.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{"
                + real +
                ", " + imaginary +
                "i}";
    }
}
