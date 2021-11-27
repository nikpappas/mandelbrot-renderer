package com.nikpappas.number;


import java.util.Comparator;

/**
 * A simple implementation of Complex numbers in java
 * Implements comparable by comparing the absolute value of each complex number
 */
public class Complex extends Number implements ComplexAny {
    public static final Complex ZERO = Complex.of(0, 0);
    public final double real;
    public final double imaginary;

    private Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexAny add(Complex toAdd) {
        return of(real + toAdd.real, imaginary + toAdd.imaginary);
    }

    public Complex subtract(Complex toSubtract) {
        return of(real - toSubtract.real, imaginary - toSubtract.imaginary);
    }

    /**
     * @param toMultiply
     * @return (ar + ai * i)*(br + bi*i) =
     * ar*br-ai*bi , br*ai+ar*bi
     */
    public ComplexAny multiply(ComplexAny toMultiply) {
        double ar = real;
        double ai = imaginary;
        double br = toMultiply.real();
        double bi = toMultiply.imaginary();


        return of((ar * br) - ai * bi, ai * br + bi * ar);
    }

    @Override
    public ComplexAny multiply(int i) {
        return Complex.of(this.real * i, this.imaginary * i);
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
        return Math.sqrt((real * real) + (imaginary * imaginary));
    }

    @Override
    public ComplexAny add(ComplexAny c) {
        return of(real + c.real(), imaginary + c.imaginary());
    }

    @Override
    public double imaginary() {
        return imaginary;
    }

    @Override
    public double real() {
        return real;
    }


    public static Complex max(Complex a, Complex b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static Complex min(Complex a, Complex b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public static Complex of(double real, double imaginary) {
        return new Complex(real, imaginary);
    }


    public static Complex of(long real, long imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex of(int real, int imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex of(float real, float imaginary) {
        return new Complex(real, imaginary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complex complex = (Complex) o;

        if (Double.compare(complex.real, real) != 0) return false;
        return Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(real);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(imaginary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
