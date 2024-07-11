package maze;



import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    Maze maze = null;
    Scanner scanner = new Scanner(System.in);

    public Main(){

    }

    public void displayMenu(){
        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        if(maze != null) {
            System.out.println("3. Save the maze");
            System.out.println("4. Display the maze");
            System.out.println("5. Find the escape");
        }
        System.out.println("0. Exit");
    }

    public void doMenu(){
        int choice = -1;
        while(choice != 0) {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            // not an available option
            if(maze == null && (choice == 3 || choice == 4 || choice == 5)) {
                choice = -1;
            }
            switch(choice) {
                case 1:
                    generateMaze();
                    break;
                case 2:
                    loadMaze();
                    break;
                case 3:
                    saveMaze();
                    break;
                case 4:
                    maze.print();
                    break;
                case 5:
                    findPath();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
            System.out.println();

        }
        System.out.println("Bye!");
    }

    private void findPath() {
        Maze copyMaze = copyMaze();
        copyMaze.findPath();
    }

    private void generateMaze(){
        System.out.println("Enter the size of a maze");
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] dimensions = str.split(" ");
        int row = 0;
        int col = 0;
        if(dimensions.length == 2) {
            row = Integer.parseInt(dimensions[0]);
            col = Integer.parseInt(dimensions[1]);
        } else if (dimensions.length == 1) {
            row = Integer.parseInt(dimensions[0]);
            col = row;
        }

        maze = new Maze(row, col);
        maze.generate();
        maze.setEntryPoint();
        maze.setExitPoint();
        maze.visualize();
        maze.print();
    }

    private Maze copyMaze()  {
        if (maze == null) return null;

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(maze);
            out.flush();
            byte[] ba = baos.toByteArray();
            out.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            ObjectInputStream in = new ObjectInputStream(bais);
            Maze copiedMaze = null;
            try {
                copiedMaze = (Maze) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            in.close();
            return copiedMaze;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void saveMaze(){
        if(maze == null) return;
        String fileName = scanner.nextLine();
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(maze);
            oos.close();
            fos.close();
        }catch (IOException e){
            System.out.printf("The file %s does not exist\n", fileName);
        }catch(Exception e){
            //System.out.println("Cannot load the maze. It has an invalid format");
        }
    }

    private void loadMaze(){
        String fileName = scanner.nextLine();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            maze = (Maze) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            System.out.printf("The file %s does not exist\n", fileName);
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load the maze. It has an invalid format");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.doMenu();
    }
}
