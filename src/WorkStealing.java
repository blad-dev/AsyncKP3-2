import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class WorkStealing extends RecursiveTask<Integer> {
    private final File directory;
    private final String fileExtension;
    private static final int splitThreshold = 4;
    public WorkStealing(File directory, String fileExtension) {
        this.directory = directory;
        this.fileExtension = fileExtension;
    }
    private int handleSubDirectories(List<File> subDirectories){
        int numberOfFilesWithRightExtensions = 0;
        List<WorkStealing> subTasks = new ArrayList<>();
        if(subDirectories.size() >= splitThreshold){
            for (File subDirectory : subDirectories) {
                WorkStealing subTask = new WorkStealing(subDirectory, fileExtension);
                subTasks.add(subTask);
                subTask.fork();
            }
            for (WorkStealing subTask : subTasks) {
                numberOfFilesWithRightExtensions += subTask.join();
            }
        } else {
            for (File subDirectory : subDirectories) {
                WorkStealing subTask = new WorkStealing(subDirectory, fileExtension);
                subTask.fork();
                numberOfFilesWithRightExtensions += subTask.join();
            }
        }
        return numberOfFilesWithRightExtensions;
    }

    @Override
    protected Integer compute() {
        int numberOfLocalFilesWithRightExtensions = 0;

        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }

        List<File> subDirectories = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                subDirectories.add(file);
            } else if (file.getName().endsWith(fileExtension)) {
                ++numberOfLocalFilesWithRightExtensions;
            }
        }
        return handleSubDirectories(subDirectories) + numberOfLocalFilesWithRightExtensions;
    }
}