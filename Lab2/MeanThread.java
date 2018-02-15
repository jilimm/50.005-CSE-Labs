import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


public class MeanThread {
	// Note: you should pass the file as a first command line argument at runtime.  
	private static final int DATA_SIZE = 1048576;
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		// TODO: read data from external file and store it in an array
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
		ArrayList<MeanMultiThread> arrayOfThread = new ArrayList<MeanMultiThread>();
		ArrayList<Double> temporalMeans = new ArrayList<Double>();
		for (int i=0; i<DATA_SIZE; i+=subArrayListLength){
			ArrayList<Integer> list = new ArrayList<Integer>(integerArray.subList(i,i+subArrayListLength));
			arrayOfSubList.add(list);
		}

		// TODO: start recording time
		long startTime = System.currentTimeMillis();

		// TODO: create N threads and assign subArrays to the threads so that each thread computes mean of 
		    // its repective subarray. For example,
		
		for (ArrayList<Integer> sublist : arrayOfSubList){
			arrayOfThread.add(new MeanMultiThread(sublist));
		}
		//Tip: you can't create big number of threads in the above way. So, create an array list of threads. 
		
		// TODO: start each thread to execute your computeMean() function defined under the run() method
		   //so that the N mean values can be computed. for example, 
		for (MeanMultiThread thread : arrayOfThread){
			thread.start();
		}

		for (MeanMultiThread thread : arrayOfThread){
			try{
				thread.join();
			// TODO: show the N mean values
			System.out.println("Temporal mean value of thread "+arrayOfThread.indexOf(thread)+" is "+thread.getMean());
			// TODO: store the temporal mean values in a new array so that you can use that 
		    /// array to compute the global mean.
			temporalMeans.add(thread.getMean());
			} catch (Exception e){
				e.getStackTrace();
			}
		}

		// TODO: compute the global mean value from N mean values. 
		double globalsum =0;
		for (double d : temporalMeans){
			globalsum+=d;
		}

		double globalmean = globalsum/NumOfThread;
		
		// TODO: stop recording time and compute the elapsed time 
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: "+(endTime-startTime)+" milliseconds.");
		System.out.println("The global mean value is "+globalmean);
		
	}
}
//Extend the Thread class

class MeanMultiThread extends Thread {
	
	private ArrayList<Integer> list;
	private double mean;

	MeanMultiThread(ArrayList<Integer> array) {
		this.list = array;
	}
	public double getMean() {
		return mean;
	}
	public void run() {
		double size = list.size();
		double sum = 0;
		for (Integer i : list){
			sum+=i;
		}
		this.mean = sum / size;
	}
	
	
}
