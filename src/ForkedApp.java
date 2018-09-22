import java.util.List;
import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.ArrayList;
import java.lang.RuntimeException;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * @author Nyasha Bryan Katemauswa <address nyashabryan23@gmail.com>
 * @version 1.0
 */
public class ForkedApp{

    private static int terrainXSize;
    private static int terrainYSize;
    static int numberOfTrees;
    static float[][] gridSunlightHours;
    private static int SEQUENCIAL_CUTOFF;
    static final ForkJoinPool pool = new ForkJoinPool();
    private static int SET_NUMBER;
    private static int THREAD_NUMBER;
    private static String INPUT_FILE = "sample_input.txt";

    /**
     * The application main method.
     * @param args
     */
    public static void main(String[] args){

        if (args.length > 1){
            SET_NUMBER = Integer.parseInt(args[0]);
            THREAD_NUMBER = Integer.parseInt(args[1]);
        }else{
            SET_NUMBER = 0;
            THREAD_NUMBER = 2;
        }
        numberOfTrees = SET_NUMBER;
        Tree[] trees = LoadMap();
        SEQUENCIAL_CUTOFF = trees.length/THREAD_NUMBER;
        
        // Computations start
        calculateTreeHours(trees);
        System.gc();

        long initTime = System.currentTimeMillis();
        int sum = calculateSunlightSum(trees);
        long duration = System.currentTimeMillis() - initTime;
        float average = sum /(float) numberOfTrees;
        printResults(average, duration);
        
    }

    public static int calculateSunlightSum(Tree[] trees){
        return pool.invoke(new SumHoursCalculator(trees, 0, trees.length));
    }

    public static class SumHoursCalculator extends RecursiveTask<Integer>{
        Tree[] trees;
        int low;
        int high;

        SumHoursCalculator(Tree[] trees, int low, int high){
            this.trees = trees;
            this.low = low;
            this.high = high;
        }

        protected Integer compute(){
            if (high - low > SEQUENCIAL_CUTOFF){
                SumHoursCalculator left = new SumHoursCalculator(
                    trees, low, (high + low)/2
                );
                SumHoursCalculator right = new SumHoursCalculator(
                    trees, (high + low)/2, high
                );

                right.fork();
                int leftAns = left.compute();
                int rightAns = 0;
                try{
                    rightAns = right.join();
                } catch(RuntimeException e){
                    System.out.println(e);
                }
                return leftAns + rightAns;

            }else{
                int sum = 0;
                for (int i = low; i < high; i++)
                sum += trees[i].sunlight;
                return sum;
            }     
        }

    }

    /**
     * Calculates the amount of sunlight that each of the trees had.
     */
    public static void calculateTreeHours(Tree[] trees){
        for (Tree tree: trees){
            for (int i = tree.xCorner; i < tree.xCorner + tree.canopy;
                i++){
                if (i > terrainXSize - 1) continue;
                for (int j = tree.yCorner; j < tree.yCorner + tree.canopy;
                j++){
                    if (j > terrainYSize -1) continue;
                    tree.sunlight += gridSunlightHours[i][j];
                }
                
            }
        }
    }

    /**
     * Take in the terrain details from a file or System.in
     * and use it to build the characteristics of the terrrain in the
     * static variables. 
     */
    public static Tree[] LoadMap(){
        BufferedReader reader;
        try {
            reader =  new BufferedReader(new FileReader(INPUT_FILE));
            String line1 = reader.readLine();
            terrainXSize = (int) Integer.parseInt(line1.split(" ")[0]);
            terrainYSize = (int) Integer.parseInt(line1.split(" ")[1]);
            
            makeGrid(reader.readLine().split(" "));                
            String line3 = reader.readLine();
            if (numberOfTrees == 0)
                numberOfTrees = (int) Integer.parseInt(line3.trim());
            
            List<String> treeLines = new ArrayList<String>();
            String xline;
            System.out.println(numberOfTrees);
            for (int i = 0; i < numberOfTrees; i++){
                xline = reader.readLine();
                if (xline == null || xline == "") break;
                treeLines.add(xline);
            }
            if (treeLines.size() != numberOfTrees)
                System.out.println("Number of trees not equal to treeLines");
            reader.close();
            return makeTrees(treeLines);
        
        }catch (FileNotFoundException e){
            System.out.println(e.toString());
            return new Tree[numberOfTrees];
        }catch (IOException e){
            System.out.println(e);
            return new Tree[numberOfTrees];
        }
    }

    /**
     * Makes and fills in the gridSunlightHours static variable from a line
     * of input.
     * @param line String array of the sunlight hours as floats
     */
    public static void makeGrid(String[] line){
        
        gridSunlightHours = new float[terrainXSize][terrainYSize];
        for (int i = 0; i < terrainXSize; i++){
            for(int j = 0; j < terrainYSize; j++){
                gridSunlightHours[i][j] = Float.parseFloat(line[(i)* (terrainYSize) + j]);
            }
        }
    }

    /**
     * Makes and fills the trees array.
     * @param treeLines
     */
    public static Tree[] makeTrees(List<String> treeLines){
        Tree[] trees = new Tree[numberOfTrees];
        if (treeLines ==  null) return trees;
        Tree tree = null;
        String[] line;
        for (int i = 0; i < numberOfTrees; i++){
            line = treeLines.get(i).split(" ");
            if (line.length > 2) tree = Tree.newTree(line);
            if (tree != null) trees[i] = tree;
        }
        return trees;
    }

    /**
     * Method to print the results of the experiment.
     * @param average The average exposure hours of the trees in the terrain.
     * @param duration The time it took for all the computation in the experiment.
     */
    public static void printResults(float average, long duration){
        System.out.print(numberOfTrees);
        System.out.print(",");
        System.out.print(average);
        System.out.print(",");
        System.out.print(Long.toString(duration));
        System.out.print(",");
        System.out.println(THREAD_NUMBER);
    }

    public static void printTrees(Tree[] trees){
        for(Tree tree: trees){
            System.out.println(tree);
        }
    }
}
