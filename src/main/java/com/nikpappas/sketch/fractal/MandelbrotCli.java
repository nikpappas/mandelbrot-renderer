package com.nikpappas.sketch.fractal;

import com.nikpappas.number.ComplexBD;
import com.nikpappas.utils.collection.Couple;
import com.nikpappas.utils.collection.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class MandelbrotCli {
    private final MandelbrotSet mandelbrot;

    public MandelbrotCli(MandelbrotSet mandelbrot) {
        this.mandelbrot = mandelbrot;
    }

    public static void main(String[] args) throws IOException {
        Map<Integer, String> argMap = range(0, args.length)
                .mapToObj(x -> Pair.of(x, args[x]))
                .collect(toMap(x -> x._1, x -> x._2));

        Map<String, String> flagDict = argMap.entrySet().stream()
                .filter(x -> x.getValue().startsWith("--"))
                .collect(toMap(Map.Entry::getValue, x -> argMap.getOrDefault(x.getKey() + 1, "")));


        MandelbrotCli app = new MandelbrotCli(getMandelbrot(flagDict));
        app.run();

    }

    private void run() throws IOException {
        Couple<Integer> size = Couple.of(1000, 1000);
        BufferedImage img = new BufferedImage(size._1, size._2, TYPE_INT_RGB);
        double scale = 2 * .00000000000005;
        Couple<Double> limitsX = Couple.of(-scale, scale);
        Couple<Double> limitsY = Couple.of(-limitsX._1 * size._1 / size._2, limitsX._1 * size._1 / size._2);
        Couple<Double> offsets = Couple.of(-0.0100012500 - .0000000000001, -0.800000001 + .0000000000002);
        range(0, size._1).parallel().forEach(i -> {
            double x = mapPixelToCoord(limitsX, size._1, i) + offsets._1;
            range(0, size._2).parallel().forEach(j -> {
                if (i == size._1 / 2 && j == size._2 / 2) {
                    img.setRGB(i, j, 0);
                    return;
                }
//                System.out.println(i + " " + x);
                double y = -mapPixelToCoord(limitsY, size._2, j) + offsets._2;
                ComplexBD c = ComplexBD.of(x, y);
                int it = mandelbrot.iterationsToConverge(c);
                if (it < 0) {
                    img.setRGB(i, j, MAX_VALUE);
                } else {
                    int hex = colourToHex(45, (int) (it * 255f / mandelbrot.maxIterations), 45);
                    img.setRGB(i, j, hex);
                }
            });
        });

        ImageIO.write(img, "png", new File("snaps/MandelCliOut.png"));
    }

    private int colourToHex(int r, int g, int b) {
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    private static MandelbrotSet getMandelbrot(Map<String, String> flagDict) {
        int iterations = parseInt(flagDict.getOrDefault("iterations", "560"));
        String mandeldef = flagDict.getOrDefault("equation", "square");

        switch (mandeldef) {
            case "square":
                return MandelbrotSet.ofSquare(iterations);
            case "cube":
                return MandelbrotSet.ofCube(iterations);
            case "pow":
                int pow = parseInt(flagDict.getOrDefault("pow", "6"));
                return MandelbrotSet.ofPower(pow, iterations);
            case "poly":
                int[] coefs = Arrays.stream(flagDict.getOrDefault("coefs", "1 0 3 -4 5").split(" ")).mapToInt(Integer::parseInt).toArray();
                return MandelbrotSet.ofPoly(coefs);
            default:
                throw new IllegalArgumentException("Could not find e legal mandelbrot definition.");
        }
    }


    double mapPixelToCoord(Couple<Double> limits, int size, int idx) {
        return idx * (limits._2 - limits._1) / size - limits._2;
    }
}
