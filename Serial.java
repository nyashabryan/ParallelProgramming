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
    static float[][] gridSunlightHours;

    /**
     * Serial Application Main Method.
     * 
     * @param args
     */
    public static void main(String[] args){

        System.out.println("Starting");
        List<Tree> trees = new ArrayList<Tree>();
        trees = LoadMap();

        calculateTreeHours(trees);
        float average = calculateSunlightAverage(trees);
        printResults(average);

    }

    /**
     * Take in the terrain details from a file or System.in
     * and use it to build the characteristics of the terrrain in the
     * static variables. 
     */
    public static List<Tree> LoadMap(){
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
        scanner.close();        
        return makeTrees(treeLines);
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
    public static List<Tree> makeTrees(List<String> treeLines){
        List<Tree> trees = new ArrayList<Tree>();
        if (treeLines ==  null) return trees;
        Tree tree =  new Tree(0, 0, 0);
        trees.add(tree);
        for(String line: treeLines){
            trees.add(new Tree(line.split(" ")));
        }

        return trees;
    }

    /**
     * Calculates the amount of sunlight that each of the trees had.
     */
    public static void calculateTreeHours(List<Tree> trees){
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
    public static float calculateSunlightAverage(List<Tree> trees){
        float total = 0;
        for (Tree tree : trees) {
            total += tree.sunlight;
        }
        return total /((float)numberOfTrees);
    }

    /**
     * Checks if the map has been loaded or not. 
     * @return boolean.
     */
    public static boolean checkMapLoaded(){
        return (gridSunlightHours != null &&
                terrainXSize != 0 &&
                terrainYSize != 0 &&
                numberOfTrees != 0);
    }

    public static void printResults(float average){
        System.out.println(average);
    }
}