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

        filtersPerThread = (int) Math.ceil(0.42*items.size()/filterSize);
        //System.out.println("Filters per thread: " + filtersPerThread);
        double averageTime=0.00;
        ArrayList<Double> filteredList = new ArrayList<Double>();
       
        for (int i=0;i<20; i++){
            filteredList=(applyFilter(items,filterSize, filtersPerThread));
            averageTime+=totalTime;
            System.out.println(i+ " Time taken = "+Double.toString(totalTime) + " milliseconds");
        } System.out.println("avg Time taken = "+Double.toString(averageTime/20.0) + " milliseconds");

        printToFile(filteredList, outputName);

    }



    public static ArrayList<Double> applyFilter(ArrayList<Double> input,int filterSize, int filtersPerThread){
        ArrayList<Double> result = new ArrayList<Double>();
        int border= (int) Math.ceil(filterSize/2);
        ArrayList<Double> localItems = new ArrayList<Double>();
        int size = (input.size());
        
        long startTime = System.nanoTime();

      /*  for (int i= 0; i<border;i++){
            result.add(input.get(i)); 
        }*/

        
        ArrayList<Double> filteredItems = new ArrayList<Double>(input);
        medianThread.applyParallelFilter(input,filteredItems,filterSize,filtersPerThread,border);
        //result.addAll(filteredItems);
        

      /*  for (int i= size-border; i<size;i++){
            result.add(input.get(i));
        }*/
        
         totalTime= (System.nanoTime()-startTime)/1000000.00;

        

       // System.out.println(filteredItems.subList(0, 10));
        return filteredItems;
    }





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

     public medianThread(ArrayList<Double> items,ArrayList<Double> target, int lo, int hi, int threshhold,int border){
        this.target = target;
        this.items = items;
        this.lo=lo;
        this.hi=hi;
        this.threshhold=threshhold;
        this.border=border;
    }

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
            //System.out.println(hi);
            //System.out.println(hi/2);
            //left.join();
            right.fork();
            //left.join();
            //right.join();
           
            
           


        }

    }

    public static void applyParallelFilter(ArrayList<Double> items,ArrayList<Double> target, int filterSize, int filtersPerThread, int border){
        int hi = items.size()-border;
        int lo = border;
        int threshhold = (filtersPerThread)*filterSize;

        medianThread firstThread= new medianThread(items,target, lo, hi, threshhold, border);
        //firstThread.compute();
        new ForkJoinPool().invoke(firstThread);

    }

    
}
