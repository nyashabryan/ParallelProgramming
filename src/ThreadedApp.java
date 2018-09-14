import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.lang.Thread;

/**
 * 
 * @author Nyasha Bryan Katemauswa <address nyashabryan23@gmail.com>
 * @version 1.0
 */
public class ThreadedApp{

    private static int terrainXSize;
    private static int terrainYSize;
    static int numberOfTrees;
    static float[][] gridSunlightHours;
    private static int SEQUENCIAL_CUTOFF;
    /**
     * The application main method.
     * @param args
     */
    public static void main(String[] args){

        System.out.println("Starting");
        List<Tree> trees = LoadMap();

        System.out.println("Calculating tree hours and average.....");
        System.gc();
        ThreadedApp.SEQUENCIAL_CUTOFF = trees.size() + 1;
        System.out.println(SEQUENCIAL_CUTOFF);
        // Computations start
        

        calculateTreeHours(trees);
        for (Tree tree: trees){
            //System.out.println(tree.sunlight);
        }

        long initTime = System.currentTimeMillis();
        float average = calculateSunlightAverage(trees);
        
        long duration = System.currentTimeMillis() - initTime;
        
        System.out.println("Printing results.....");
        printResults(average, trees, duration);
        
    }

    public static float calculateSunlightAverage(List<Tree> trees){
        AverageHoursCalculator runner = new AverageHoursCalculator(trees);
        runner.run();
        try{
            runner.join();
        } catch (InterruptedException e){
            System.out.println(e);
            return 0;
        }
        return runner.average;
    }

    public static class AverageHoursCalculator extends Thread{

        List<Tree> trees;
        float average;

        AverageHoursCalculator(List<Tree> trees){
            this.trees = trees;
        }

        public void run(){
            if (trees.size() > SEQUENCIAL_CUTOFF){
                AverageHoursCalculator left = new AverageHoursCalculator(
                    trees.subList(0, trees.size()/2)
                );
                AverageHoursCalculator right = new AverageHoursCalculator(
                    trees.subList(trees.size()/2, trees.size()-1)
                );

                left.start();
                right.start();
                try{
                    left.join();
                } catch(InterruptedException e){
                    System.out.println(e);
                }
                
                try{
                    right.join();
                }catch(InterruptedException e){
                    System.out.println(e);
                }
                
                this.average = left.average + right.average;
            }else{
                float total = 0;
                for (Tree tree : trees) {
                    total += tree.sunlight;
                }
                this.average = total /((float)ThreadedApp.numberOfTrees);
            }
        }

    }

    /**
     * Calculates the amount of sunlight that each of the trees had.
     */
    public static void calculateTreeHours(List<Tree> trees){
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
    public static List<Tree> LoadMap(){
        System.out.println("Loading Map.....");
        BufferedReader reader;
        try {
            reader =  new BufferedReader(new FileReader("sample_input.txt"));
            String line1;
            line1 = reader.readLine();
            terrainXSize = (int) Integer.parseInt(line1.split(" ")[0]);
            terrainYSize = (int) Integer.parseInt(line1.split(" ")[1]);
            
            System.out.println("Making Grid.....");
            makeGrid(reader.readLine().split(" "));                
            String line3 = reader.readLine();
            numberOfTrees = (int) Integer.parseInt(line3.trim());
            

        
            System.out.println("Scanning lines");
            List<String> treeLines = new ArrayList<String>();
            int i = 0;
            String xline;
            while (true){
                i++;
                xline = reader.readLine();
                if (xline == null) break;
                treeLines.add(xline);
            }
            reader.close();

            System.out.println("Making trees.....");
            return makeTrees(treeLines);
        
        }catch (FileNotFoundException e){
            System.out.println(e.toString());
            return new ArrayList<Tree>();
        }catch (IOException e){
            System.out.println(e);
            return new ArrayList<Tree>();
        }
    }


    /**
     * Makes and fills in the gridSunlightHours static variable from a line
     * of input.
     * @param line String array of the sunlight hours as floats
     */
    public static void makeGrid(String[] line){
        
        gridSunlightHours = new float[terrainXSize][terrainYSize];
        System.out.println(line.length);
        System.out.println(terrainXSize);
        System.out.println(terrainYSize);
        for (int i = 0; i < terrainXSize; i++){
            for(int j = 0; j < terrainYSize; j++){
                gridSunlightHours[i][j] = Float.parseFloat(line[(i)* (terrainYSize) + j]);
            }
        }
    }

    /**
     * Makes and fills the 
     * @param treeLines
     */
    public static List<Tree> makeTrees(List<String> treeLines){
        List<Tree> trees = new ArrayList<Tree>();
        Tree tree;
        if (treeLines ==  null) return trees;
        for(String line: treeLines){
            tree = Tree.newTree(line.split(" "));
            if (tree != null) trees.add(tree);
        }

        return trees;
    }

    /**
     * Method to print the results of the experiment.
     * @param average The average exposure hours of the trees in the terrain.
     * @param trees The list of the trees in the terrain.
     * @param duration The time it took for all the computation in the experiment.
     */
    public static void printResults(float average, List<Tree> trees, long duration){
        System.out.println("Computation took " + Long.toString(duration));
        System.out.println(average);
        System.out.println(trees.size());
        for (Tree tree: trees){
        //    System.out.println(tree.sunlight);
        }
    }

}