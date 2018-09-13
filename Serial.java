import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Serial{

    private static int terrainXSize;
    private static int terrainYSize;
    static int numberOfTrees;
    static List<Trees> trees;
    static float[][] gridSunlightHours;

    public static void main(String[] args){

        System.out.println("Starting");
        LoadMap();

        calculateTreeHours();
        int average = calculateSunlightAverage();
        printResults();

    }

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

    public static void makeGrid(String[] line){
        for (int i = 0; i < terrainXSize; i++){
            for(int j = 0; i < terrainYSize; j++){
                gridSunlightHours[i][j] = Float.parseFloat(line[i+j]);
            }
        }
    }

    public static void makeTrees(List<String> treeLines){
        if (treeLines ==  null) return ;
        String[] details;
        for(String line: treeLines){
            trees.add(new Tree(line.split(" ")));
        }
    }
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
    
     */
    public static float calculateSunlightAverage(){
        float total = 0;
        List<Trees> trees2 = trees;
        for (Tree tree : trees2) {
            total += trees2.sunlight;
        }

        return total /((float)numberOfTrees);
    }


    public static boolean checkMapLoaded(){
        return (sunlightHours || terrainXSize || terrainYSize ||
            numberOfTrees || trees );
    }

    public class Tree{
        
        int xCorner;
        int yCorner;
        int canopy;
        float sunlight;
        public Tree(int xCorner, int yCorner, int canopy){
            this.xCorner = xCorner;
            this.yCorner = yCorner;
            this.canopy = canopy;
        }

        public Tree(int[] details){
            if (details.length < 3){
                Tree(0, 0, 0);
            }
            else{
                Tree(details[0], details[1], details[2]);
            }
        }

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