package morse;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class DecipheringTheMorse {

    public static void main(String[] args) {
        Input input = Input.fromFilename("test/morse-hello.input");
        //run(input, EnumeratePrefixes::count_sentences);
        run(input, EnumeratePrefixesMemoization::count_sentences);
    }

    private static void run(Input input, Function<Input, Integer> fun) {
        long startTime = System.nanoTime();
        int res = fun.apply(input);
        long endTime = System.nanoTime();

        System.out.println("# Result : " + res);
        System.out.println("# Time   : " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }
}
