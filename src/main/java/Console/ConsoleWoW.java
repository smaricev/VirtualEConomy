package Console;

/**
 * Created by stjep on 7/20/2017.
 */
public class ConsoleWoW {
    public static void updateProgress(double progressPercentage,String done) {
        final int width = 50; // progress bar width in chars

        System.out.print("\r[");
        int i = 0;
        for (; i <= (int)(progressPercentage*width); i++) {
            System.out.print(".");
        }
        for (; i < width; i++) {
            System.out.print(" ");
        }
        System.out.print("] - " + done + "\n");
    }
}
