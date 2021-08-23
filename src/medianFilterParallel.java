import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;





public class medianFilterParallel{
    public static double totalTime;

     /**
   * This is the main method which makes use of applyFilter 
   * and printToFile methods.
   * @param args input from the terminal (eg. <input_file>  <filter_size> <output_file>)
   * @return Nothing (prints to terminal).
   * @exception FileNotFoundException On file error.
   * @see FileNotFoundException
   */
    public static void main(String[] args){

        String filename = args[0];
        String outputName = args[2];
        int filterSize = Integer.parseInt(args[1]);
        int filtersPerThread = 15000; //Integer.parseInt(args[3]);
        
        String line ="";
        String item="";
        ArrayList<Double> items = new ArrayList<Double>();
        try{
            File myFile = new File(filename);
            Scanner lines = new Scanner(myFile);
            int length = Integer.parseInt(lines.nextLine());
            while (lines.hasNextLine()){
                line = lines.nextLine();
                Scanner itemScanner = new Scanner(line);
                itemScanner.useDelimiter("\\s* \\s*");
                itemScanner.next();
                item=itemScanner.next();
                item=item.replaceAll(",",".");
                items.add(Double.parseDouble(item));
            }

        } catch(FileNotFoundException e){
            System.out.println("Oopsie daisy we could not find "+filename);
            System.exit(0);
        }
        int cores = Runtime.getRuntime().availableProcessors();
        filtersPerThread = (int) Math.ceil(8*items.size()/(filterSize*cores*1.98));
        System.out.println("Filters per thread: " + filtersPerThread);
        System.out.println("Cores: " +cores);
        double averageTime=0.00;
        ArrayList<Double> filteredList = new ArrayList<Double>();
       
        for (int i=0;i<20; i++){
            System.gc();
            filteredList=(applyFilter(items,filterSize, filtersPerThread));
            averageTime+=totalTime;
            System.out.println(i+ " Time taken = "+Double.toString(totalTime) + " milliseconds");
            System.gc();
        } System.out.println("avg Time taken = "+Double.toString(averageTime/20.0) + " milliseconds");
        items.clear();
        printToFile(filteredList, outputName);

    }


    /**
   * This applies a median filter using the applyParrallelFilter function found in the medianThread class below
   * @param input the input arraylist that you want to apply the median filter on
   * @param filterSize the filter size
   * @param filtersPerThread the number of filters per thread 
   * @return resultant ArrayList of Double with filter applied to it
   */


    public static ArrayList<Double> applyFilter(ArrayList<Double> input,int filterSize, int filtersPerThread){
        ArrayList<Double> result = new ArrayList<Double>();
        int border= (int) Math.ceil(filterSize/2);
        ArrayList<Double> localItems = new ArrayList<Double>();
        int size = (input.size());
        
        long startTime = System.nanoTime();

        
        ArrayList<Double> filteredItems = new ArrayList<Double>(input);
         medianThread.applyParallelFilter(input,filteredItems,filterSize,filtersPerThread,border);
       
        
         totalTime= (System.nanoTime()-startTime)/1000000.00;

       return filteredItems;

    }

   /**
   * This prints an arraylist of double to an output text file
   * @param input the input arraylist that you want to print
   * @param outputName the name of the text file that will be produced or overwritten
   * @return nothing to System.out or any return types
   */

    public static void printToFile(ArrayList<Double> input, String outputName){

       try{
        FileWriter output = new FileWriter(outputName);
       
            output.write(Integer.toString(input.size())+"\n");
            for(int i=0;i<input.size();i++){
                output.write(String.format(i + " "+ "%.5f",input.get(i))+"\n");  
            }
            output.close();

       }catch(IOException e){
           System.out.println("Damn bro, looks like I can't do that one");
       }




        

    }





}

class medianThread extends RecursiveAction {

    ArrayList<Double> localItems;
    ArrayList<Double> target;
    ArrayList<Double> items;
    int lo;
    int hi;
    int threshhold;
    int border;
   
    /**
   * implementation of the forkjoin framework's constructor.
   * @param items the input arraylist 
   * @param target the arraylist that will hold the results (using pass by reference)
   * @param lo starting index
   * @param hi ending index
   * @param threshhold the sequential cutoff
   * @param border border 
   * @return nothing to System.out or any return types
   */

     public medianThread(ArrayList<Double> items,ArrayList<Double> target, int lo, int hi, int threshhold,int border){
        this.target = target;
        this.items = items;
        this.lo=lo;
        this.hi=hi;
        this.threshhold=threshhold;
        this.border=border;
    }

     /**
   * implementation of the forkjoin framework's compute method.
   * @param 
   * @return nothing to System.out or any return types
   */
    protected void compute(){
        if(hi-lo<threshhold){
            //System.out.println(hi + " " +lo);
            localItems = new ArrayList<Double>();
            for (int i= lo;i<hi;i++){
              // String debug = ("point "+ i +": " );
                for (int x =i-border;x<i+border+1;x++){
                    // debug+=(items.get(x)+" ");
                    localItems.add(items.get(x));         
            }
               
                Collections.sort(localItems);
                target.set(i,localItems.get(border));

            // System.out.println(debug + " choose " + localItems.get(border));
                localItems.clear();   
        }

        }else{
            medianThread left = new medianThread(items,target,lo,lo+(hi-lo)/2,threshhold,border);
            medianThread right = new medianThread(items,target,lo+(hi-lo)/2,hi+1,threshhold,border);
            left.fork();
            right.fork();
            
            //left.join();
            //right.join();
           
            
           


        }

    }
 /**
   * This function actually executes and applies the median filter using the ForkJoin Framework
   * @param items the input arraylist that you want to base the filter on
   * @param target the target arraylist that you want the results to be stored on (using pass by reference)
   * @param filterSize the filter size
   * @param filtersPerThread the number of filters per thread (used to calculated the threshhold/sequential cuttoff)
   * @param border the size of the border created by the filter size (border = 1 for filter size of 3)
   * @return nothing to System.out or any return types
   */

    public static void applyParallelFilter(ArrayList<Double> items,ArrayList<Double> target, int filterSize, int filtersPerThread, int border){
        int hi = items.size()-border;
        int lo = border;
        int threshhold = (filtersPerThread)*filterSize;

        medianThread firstThread= new medianThread(items,target, lo, hi, threshhold, border);
        //firstThread.compute();
        int cores = Runtime.getRuntime().availableProcessors();
         ForkJoinPool mypool= new ForkJoinPool(cores);
         mypool.invoke(firstThread);

         mypool.commonPool().shutdown();
         
        
        
    }

    
}
