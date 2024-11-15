import java.io.File;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть шлях: ");
        String directoryPath = scanner.next();
        System.out.println("Введіть розширення: ");
        String fileExtension = scanner.next();
        int fileCount = numberOfFiles(directoryPath, fileExtension);
        System.out.printf("Кількість файлів з розширенням %s і шляхом \"%s\": %d", fileExtension, directoryPath, fileCount);
    }
    private static int numberOfFiles(String directoryPath, String fileExtension) {
        ForkJoinPool pool = new ForkJoinPool();
        WorkStealing task = new WorkStealing(new File(directoryPath), fileExtension);
        return pool.invoke(task);
    }
}
