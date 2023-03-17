package control;

import java.nio.file.spi.FileSystemProvider;

public class CallByValueExample {
    public static void main(String[] args) {
        int x = 10;
        int y = 20;
        int[] z = {10, 20};
        x = swap(x, y);
        swap2(z);
        System.out.println("z stack = " + z);
        System.out.println("x = " + x); // x = 10
        System.out.println("y = " + y); // y = 20
        System.out.println(z[0] + ", " + z[1]);
    }
    
    //int a = x, int b = y;
    //syso(a) -> 10;
    public static int swap(int a, int b) {
    	System.out.println("a, b stack = " + a + ", " + b);
        a = 30;
        b = 40;
        return a;
    }
    //int[] c = z
    //syso(c) ->  [I@7d6f77cc
    public static void swap2(int[] c) {
    	System.out.println("c stack = " + c);
        c[0] = 30;
        c[1] = 40;
    }
    
    
}