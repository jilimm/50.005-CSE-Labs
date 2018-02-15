import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.lang.Math;
import java.io.BufferedReader;



public class MedianThread {
	private static final int DATA_SIZE = 1048576;
	public static void main(String[] args) throws InterruptedException, FileNotFoundException  {
		// TODO: read data from external file and store it in an array
	       // Note: you should pass the file as a first command line argument at runtime.

		String FileName = String.valueOf(args[0]);
		ArrayList<Integer> integerArray = new ArrayList<Integer>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(FileName));
			String currentLine = br.readLine(); //everything in the txt file is 1 line OH SHIT WADDUP
			String [] stringArray;
			stringArray = currentLine.substring(1).split("\\s+");
			for (String stringNumb : stringArray){
				integerArray.add(Integer.parseInt(stringNumb));
			}
		}catch(Exception e) {
			e.getStackTrace();
		}
	
	// define number of threads
	int NumOfThread = Integer.valueOf(args[1]);// this way, you can pass number of threads as 	
	     // a second command line argument at runtime.
	
	// TODO: partition the array list into N subArrays, where N is the number of threads
	int subArrayListLength = DATA_SIZE/NumOfThread;
	ArrayList<ArrayList<Integer>> arrayOfSubList = new ArrayList<ArrayList<Integer>>();
	ArrayList<MedianMultiThread> arrayOfThread = new ArrayList<MedianMultiThread>();
	ArrayList<MedianMultiThread> arrayOfMergeThread = new ArrayList<MedianMultiThread>();

	int loopsToMerge = (int) (Math.log(NumOfThread)/Math.log(2));

	for (int i=0; i<DATA_SIZE; i+=subArrayListLength){
			ArrayList<Integer> list = new ArrayList<Integer>(integerArray.subList(i,i+subArrayListLength));
			arrayOfSubList.add(list);
	}

	// TODO: start recording time
	long startTime = System.currentTimeMillis();
	// TODO: create N threads and assign subArrays to the threads so that each thread sorts
	    // its repective subarray. For example,
	
	for (ArrayList<Integer> sublist : arrayOfSubList){
			arrayOfThread.add(new MedianMultiThread(sublist));
	}
	//Tip: you can't create big number of threads in the above way. So, create an array list of threads. 
	
	// TODO: start each thread to execute your sorting algorithm defined under the run() method, for example, 
	for (MedianMultiThread thread : arrayOfThread){
			thread.start();
	}

	for (MedianMultiThread thread : arrayOfThread){
		try{
			thread.join();
		} catch (Exception e){
			e.getStackTrace();
		}
	}
	
	// TODO: use any merge algorithm to merge the sorted subarrays and store it to another array, e.g., sortedFullArray. 
	for (int i=0;i<loopsToMerge;i++){
		//instantiate array of threads to sort the merged lists
		for (int j=0; j<arrayOfThread.size(); j++){
			ArrayList<Integer> mergedSortedArray = new ArrayList<Integer>();
			mergedSortedArray.addAll(arrayOfThread.get(j).getInternal());
			mergedSortedArray.addAll(arrayOfThread.get(j++).getInternal());
			System.out.println("merged size is " + mergedSortedArray.size());
			System.out.println("j is "+j);
			arrayOfMergeThread.add(new MedianMultiThread(mergedSortedArray));
		}
		System.out.println(arrayOfMergeThread.size());
		//run all threads
		for (MedianMultiThread thread : arrayOfMergeThread){
			thread.start();
		}
		//joing all threads
		for (MedianMultiThread thread : arrayOfMergeThread){
			try{
				thread.join();
			} catch (Exception e){
				e.getStackTrace();
			}
		}

		//redo the arraylists 	
		arrayOfThread = new ArrayList<MedianMultiThread>(arrayOfMergeThread);
		arrayOfMergeThread.clear();
		System.out.println(i);
	}
	

	//TODO: get median from sortedFullArray
	
	    //e.g, computeMedian(sortedFullArray

	int finalArraySize = arrayOfThread.get(0).getInternal().size();
	System.out.println("final merged size is " + finalArraySize);
	double median = computeMedian(arrayOfThread.get(0).getInternal());

	// TODO: stop recording time and compute the elapsed time 
	long endTime = System.currentTimeMillis();
	long runningTime = endTime-startTime;
	// TODO: printout the final sorted array
	
	// TODO: printout median
	System.out.println("The Median value is "+median);
	System.out.println("Running time is " + runningTime + " milliseconds\n");


	Collections.sort(arrayOfThread.get(0).getInternal());
	double newMedian = computeMedian(arrayOfThread.get(0).getInternal());
	System.out.println("new median is "+newMedian);
	
	}

	public static double computeMedian(ArrayList<Integer> inputArray) {
		int inputArraySize = inputArray.size();
		if (inputArraySize%2==0){
			System.out.println("middle is "+inputArray.get(inputArraySize/2));
			System.out.println("another middle is "+inputArray.get(inputArraySize/2+1));
			return (inputArray.get(inputArraySize/2)+inputArray.get(inputArraySize/2+1))/2;
		}
		else{
			System.out.println(inputArray.get(inputArraySize/2+1));
			return (inputArray.get(inputArraySize/2+1));
		}
	}
}

// extend Thread
class MedianMultiThread extends Thread {
	private ArrayList<Integer> list;

	public ArrayList<Integer> getInternal() {
		return list;
	}

	MedianMultiThread(ArrayList<Integer> array) {
		this.list = array;
	}

	public void run() {
		mergeSort(list);
	}
	
	// TODO: implement merge sort here, recursive algorithm
	public void mergeSort(ArrayList<Integer> array) {
		Collections.sort(array); 
	}
}