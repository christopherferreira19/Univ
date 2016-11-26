package morse;

import morse.algo.MemoizationDicoTree;
import morse.algo.TabulationDicoTree;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class DecipheringTheMorse {

    public static void main(String[] args) {
        Input input = Input.fromFilename("test/morse-long.input");
        //Input input = Generator.generate(100, 15, 1, 1);
        //run(input, Recursive::count_sentences);
        //run("Memoization", input, 1, Memoization::count_sentences);
        //run("MemoizationDicoTree", input, 10000, MemoizationDicoTree::count_sentences);
        run("TabulationDicoTree", input, 10, TabulationDicoTree::count_sentences);
    }

    private static int run(String name, Input input, int times, Function<Input, Integer> fun) {
        long startTime = System.nanoTime();
        for (int i = 0; i < times - 1; i++) {
            fun.apply(input);
        }
        int res = fun.apply(input);
        long endTime = System.nanoTime();

        System.out.println("[" + name + "]");
        System.out.println("  # Result : " + res);
        System.out.println("  # Time   : " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

        return res;
    }
}
