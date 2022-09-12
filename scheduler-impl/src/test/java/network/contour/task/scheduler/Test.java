package network.contour.task.scheduler;

public class Test {
    public static void main(String[] args) {
        System.out.println(fibonacci(7));
    }

    public static int fibonacci(int n) {
        int val;
        if (n == 0 || n == 1) {
            val = 1;
        }
        else {
            val = fibonacci(n-1) + fibonacci(n-2);
        }
        return val;

    }
}
