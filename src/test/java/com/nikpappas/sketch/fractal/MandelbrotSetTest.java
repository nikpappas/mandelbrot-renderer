package com.nikpappas.sketch.fractal;

import com.nikpappas.number.Complex;
import com.nikpappas.number.ComplexAny;
import com.nikpappas.number.ComplexBD;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MandelbrotSetTest {
    @Test
    public void test() {
        List<ComplexAny> nums = asList(
                ComplexBD.ZERO,
                ComplexBD.of(-2, 0.25),
                ComplexBD.of(1, 1),
                Complex.of(-1.1650715075437499, 0.25150289888027455),
                Complex.of(-0.08919061151472318, -0.5001491951293944),
                Complex.of(-0.15374008927351923, -0.7114103932335603),
                Complex.of(-0.27067080256248355, 0.24967244595241223)
        );
        nums.forEach(x -> {

            boolean is = MandelbrotSet.ofSquare().contains(x);
            System.out.println(x + ": " + is);
        });
    }

    @Test
    public void testRAndom() {
        boolean is = false;
        while (!is) {
            double rand1 = Math.random() * 4 - 2;
            double rand2 = Math.random() * 4 - 2;
            Complex c = Complex.of(rand1, rand2);
            is = MandelbrotSet.ofSquare().contains(c);
            System.out.println(c + ": " + is);
        }

    }

    @Test
    public void testToString1() {
        MandelbrotSet mandel = MandelbrotSet.ofPoly(1, 1, 1, 1);
        assertEquals("MandelbrotSet{maxIterations=10000, function=zn+1=z^1 + z^2 + z^3 + z^4 + c}", mandel.toString());
    }

    @Test
    public void testToString2() {
        MandelbrotSet mandel = MandelbrotSet.ofPoly(-1, 1, -1, 4);
        assertEquals("MandelbrotSet{maxIterations=10000, function=zn+1=-z^1 + z^2 - z^3 + 4*z^4 + c}", mandel.toString());
    }

    @Test
    public void testToStringSquare() {
        MandelbrotSet mandel = MandelbrotSet.ofSquare(100);
        assertEquals("MandelbrotSet{maxIterations=100, function=zn+1=z^2 + c}", mandel.toString());
    }

    @Test
    public void testToStringCube() {
        MandelbrotSet mandel = MandelbrotSet.ofCube(2333);
        assertEquals("MandelbrotSet{maxIterations=2333, function=zn+1=z^3 + c}", mandel.toString());
    }

}