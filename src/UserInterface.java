import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class UserInterface {

    private static String cd = "";
    private static String toCD = "";

    public static void start() {
        Scanner in = new Scanner(System.in);
        help();
        String commandString = "";
        while (!commandString.toLowerCase().equals("exit")) {
            System.out.println("Введите комманду:");
            commandString = in.nextLine();
            switch (commandString.toLowerCase()) {
                case "help":
                    help();
                    break;
                case "copy":
                    copy();
                    break;
                case "cd":
                    setCD();
                    break;
                case "get cd":
                    getCD();
                    break;
                case "to cd":
                    setToCD();
                    break;
                case "get to cd":
                    getToCD();
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Комманда не распознана. Повторите ввод");
            }
        }
    }

    private static void help() {
        System.out.println("Список доступных команд");
        System.out.println("help     \t\t\t\tПосмотреть список доступных команд");
        System.out.println("copy     \t\t\t\tОткрыть меню копирования");
        System.out.println("cd       \t\t\t\tУстановить каталог по умолчанию, в котором нужно искать файлы и папки для копирования (если не задан можно использовать полный путь до файлов и папок)");
        System.out.println("get cd   \t\t\t\tПосмотреть каталог по умолчанию");
        System.out.println("to cd    \t\t\t\tУстановить каталог для новых файлов/папок (если не задан, то новые файлы и папки создаются рядом с исходными)");
        System.out.println("get to cd\t\t\t\tПосмотреть каталог для новых файлов/папок");
        System.out.println("exit     \t\t\t\tВыход");
    }

    private static void copy() {
        String path = "";
        String answer= "";
        Scanner in = new Scanner(System.in);
        System.out.println("Введите путь до папки или файла, который хотите копировать:");
        path = in.nextLine();
        if (path.equals(""))
            while (path.equals("")) {
                System.out.println("Введите непустой путь до файла:");
                path = in.nextLine();
            }
        OperatedFile operatedFile = new OperatedFile(path);
        if (!operatedFile.exists()){
            path = cd + path;
            operatedFile = new OperatedFile(path);
        }
        System.out.println("Начать копирование файла \""+operatedFile.getName()+"\"? (Y/N)");
        answer = in.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            OperatedFile finalOperatedFile = operatedFile;
            new Thread() {
                @Override
                public void run() {
                    String copyPath;
                    try {
                        //System.out.println("Копирование запущено");
                        copyPath = (toCD.equals("")) ? finalOperatedFile.copy() : finalOperatedFile.copy(toCD);
                        File copyFile = new File(copyPath);
                        if (finalOperatedFile.isFile())
                            System.out.println("Файл \"" + finalOperatedFile.getName() + "\" успешно скопирован. Название нового файла: \"" + copyFile.getName() + "\"");
                        else
                            System.out.println("Папка \"" + finalOperatedFile.getName() + "\" успешно скопирована. Название новой папки: \"" + copyFile.getName() + "\"");
                        if (!copyFile.exists()) System.out.println("Ошибка! Скопированный файл/папка не найден");
                    } catch (NoSuchFileException e) {
                        System.out.println("Файл \"" + finalOperatedFile.getName() + "\" не найден");
                    } catch (InvalidPathException e) {
                        System.out.println("Некорректный путь до файла");
                    }
                }
            }.start();
        }
        else System.out.println("Копирование отменено");
    }

    private static void setCD() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите путь до каталога по умолчанию, в котором нужно искать файлы и папки для копирования:");
        cd = in.nextLine();
        if (cd.equals(""))
            while (cd.equals("")) {
                System.out.println("Введите непустой путь до каталога:");
                cd = in.nextLine();
            }
        File file = new File(cd);
        if (file.isDirectory()) {
            if (!cd.substring(cd.length()-1).equals("\\")) cd += "\\";
            System.out.println("Каталог по умолчанию установлен");
        }
        else {
            System.out.println("Указанный путь не найден. Каталог по умолчанию сброшен");
            cd = "";
        }
    }

    private static void getCD() {
        System.out.print("Текущий каталог по умолчанию: ");
        if (cd.equals("")) System.out.println("<отсутствует>");
        else System.out.println(cd);
    }

    private static void setToCD() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите путь до каталога для новых файлов:");
        toCD = in.nextLine();
        if (toCD.equals(""))
            while (toCD.equals("")) {
                System.out.println("Введите непустой путь до каталога:");
                toCD = in.nextLine();
            }
        File file = new File(toCD);
        if (file.isDirectory()) {
            if (!toCD.substring(toCD.length()-1).equals("\\")) toCD += "\\";
            System.out.println("Каталог для новых файлов установлен");
        }
        else {
            System.out.println("Указанный путь не найден. Каталог для новых файлов сброшен");
            toCD = "";
        }
    }

    private static void getToCD() {
        System.out.print("Текущий каталог для новых файлов: ");
        if (toCD.equals("")) System.out.println("<отсутствует>");
        else System.out.println(toCD);
    }
}
