import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.Scanner;

public class OperatedFile extends File {

    public OperatedFile(String path) {
        super(path);
    }

    public String copy() throws NoSuchFileException {
        return copyInner("");
    }

    public String copy(String pathNewFile) throws NoSuchFileException {
        return copyInner(pathNewFile);
    }

    private String copyInner(String pathNewFile) throws NoSuchFileException {
        Path originalPath = Paths.get(this.getAbsoluteFile().getPath());
        StringBuilder copyString = new StringBuilder();
        String newDirectory = (pathNewFile.equals("")) ? getAbsoluteFile().getParent()+"\\" : pathNewFile;
        int i = 1;
        if (this.getName().lastIndexOf(".") == -1)
            copyString
                    .append(newDirectory)
                    .append(getName())
                    .append("_copy")
                    .append(i);
        else copyString
                .append(newDirectory)
                .append(this.getName().substring(0, this.getName().lastIndexOf(".")))
                .append("_copy")
                .append(i)
                .append(this.getName().substring(this.getName().lastIndexOf(".")));
        while (new File(copyString.toString()).exists()) {
            i++;
            copyString.delete(copyString.lastIndexOf("_copy") + 5, copyString.lastIndexOf("_copy") + 5 + String.valueOf(i - 1).length());
            copyString.insert(copyString.lastIndexOf("_copy") + 5, i);
        }
        Path copyPath = Paths.get(copyString.toString());
        try {
            Files.copy(originalPath, copyPath);
        } catch (NoSuchFileException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (new File(copyPath.toString()).isFile()) Files.delete(copyPath);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return copyPath.toString();
    }

    public void write(String data) {
        // Метод с лекции, не требуется для задачи
        writeInner(data, false);
    }

    public void writeAdd(String data) {
        // Метод с лекции, не требуется для задачи
        writeInner(data, true);
    }

    private void writeInner(String data, boolean isAdd) {
        // Метод с лекции, не требуется для задачи
        try {
            if (!this.isFile()) this.createNewFile();
            PrintWriter printWriter = new PrintWriter(new FileWriter(this, isAdd));
            printWriter.println(data);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        // Метод с лекции, не требуется для задачи
        StringBuilder result = new StringBuilder();
        try {
            Scanner fileReader = new Scanner(this);
            while (fileReader.hasNext())
                result
                        .append(fileReader.nextLine())
                        .append("\n")
                        ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
