import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


/**
 * @author Nyasha Bryan Katemauswa <address nyashabryan23@gmail.com>
 * @version 1.0
 * 
 * 
 */
public class Serial{

    private static int terrainXSize;
    private static int terrainYSize;
    static int numberOfTrees;
    static List<Tree> trees = new List<Tree>();
    static float[][] gridSunlightHours;

    /**
     * Serial Application Main Method.
     * 
     * @param args
     */
    public static void main(String[] args){

        System.out.println("Starting");
        LoadMap();

        calculateTreeHours();
        int average = calculateSunlightAverage();
        printResults();

    }

    /**
     * Take in the terrain details from a file or System.in
     * and use it to build the characteristics of the terrrain in the
     * static variables. 
     */
    public static void LoadMap(){
        Scanner scanner = new Scanner(System.in);
        String line1 = scanner.nextLine();
        terrainXSize = (int) Integer.parseInt(line1.split(" ")[0]);
        terrainYSize = (int) Integer.parseInt(line1.split(" ")[1]);
        
        makeGrid(scanner.nextLine().split(" "));

        String line3 = scanner.nextLine();
        numberOfTrees = (int) Integer.parseInt(line3.trim());
        
        List<String> treeLines = new ArrayList<String>();
        while (scanner.hasNext()){
            treeLines.add(scanner.nextLine());
        }
        makeTrees(treeLines);

    }

    /**
     * Makes and fills in the gridSunlightHours static variable from a line
     * of input.
     * @param line String array of the sunlight hours as floats
     */
    public static void makeGrid(String[] line){
        for (int i = 0; i < terrainXSize; i++){
            for(int j = 0; i < terrainYSize; j++){
                gridSunlightHours[i][j] = Float.parseFloat(line[i+j]);
            }
        }
    }

    /**
     * Makes and fills the 
     * @param treeLines
     */
    public static void makeTrees(List<String> treeLines){
        if (treeLines ==  null) return ;
        String[] details;
        for(String line: treeLines){
            trees.add(new Tree(line.split(" ")));
        }
    }

    /**
     * Calculates the amount of sunlight that each of the trees had.
     */
    public static void calculateTreeHours(){
        for (Tree tree: trees){
            for (int i = tree.xCorner; i < tree.xCorner + tree.canopy;
                i++){
                if (i > terrainXSize - 1) continue;
                for (int j = tree.yCorner; i < tree.yCorner + tree.canopy;
                i++){
                    if (j > terrainYSize -1) continue;
                    tree.sunlight += gridSunlightHours[i][j];
                }
                
            }
        }
    }
    
    /**
     * Calculates the average tree sunlight hours in the terrain.
     * @return average trees sunlight hours 
     */
    public static float calculateSunlightAverage(){
        float total = 0;
        List<Trees> trees2 = trees;
        for (Tree tree : trees2) {
            total += trees2.sunlight;
        }

        return total /((float)numberOfTrees);
    }

    /**
     * Checks if the map has been loaded or not. 
     * @return boolean.
     */
    public static boolean checkMapLoaded(){
        return (sunlightHours || terrainXSize || terrainYSize ||
            numberOfTrees || trees );
    }


    /**
     * Tree class to represent all the tree objects in the terrain.
     */
    public class Tree{
        
        int xCorner;
        int yCorner;
        int canopy;
        float sunlight;

        /**
         * Tree constructor using the integer values of the tree 
         * characteristics.
         * @param xCorner
         * @param yCorner
         * @param canopy
         */
        public Tree(int xCorner, int yCorner, int canopy){
            this.xCorner = xCorner;
            this.yCorner = yCorner;
            this.canopy = canopy;
        }

        /**
         * Another constructor for the Tree object. 
         * @param details integer array containing the xCorner, yCorner 
         * and the canopy values of the Tree. 
         */
        public Tree(int[] details){
            if (details.length < 3){
                Tree(0, 0, 0);
            }
            else{
                Tree(details[0], details[1], details[2]);
            }
        }

        /**
         * Another constructor for the Tree object.
         * @param details string array containing the xCorner, yCorner and
         * the canopy values of the tree.
         */
        public Tree(String[] details){
            if (details.length < 3){
                Tree(0, 0, 0);
            }else{
                Tree(
                    (int)Integer.parseInt(details[0]),
                    (int)Integer.parseInt(details[1]),
                    (int)Integer.parseInt(details[2])
                );
            }
        }
    }
}