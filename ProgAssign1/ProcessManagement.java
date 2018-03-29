/**
 * Created by jit_biswas on 2/1/2018.
 */
import java.io.File;
import java.util.Collections;
import java.lang.*;
/*Programming Assignment 1
2 ∗ Author : Lim Jing YUn
3 ∗ ID : 1002261
4 ∗ Date : 09/03/2018
*/

//parent nodeID is smaller than child node ID
//runs process one at a time in numerical order
//

public class ProcessManagement {

    //set the working directory
    private static File currentDirectory = new File(System.getProperty("user.dir"));
    //set the instructions file
    private static File instructionSet = new File("graph-file.txt");
    public static Object lock=new Object(); 

   
    public static void main(String[] args) throws InterruptedException {

        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory.getAbsolutePath() + "/"+instructionSet));

        //sort the nodes in process graph by index so you can loop through it
        Collections.sort(ProcessGraph.nodes);

        ProcessGraph.printGraph();

        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
        OUTER_LOOP:
        while (!ProcessGraph.finished()){

        //this for loop will go through from the root nodes to the child nodes (parent nodes ID < child nodeIDs)
            //so it will proceed faster, dont have to iterate all hte way to end of the list to find the parent node
        for (ProcessGraphNode node: ProcessGraph.nodes){
            
            //mark nodes that can be run
            if (node.allParentsExecuted()&&!node.isExecuted()){
                node.setRunnable();
            }
            //node can be run and has not been executed
            if (node.isRunnable()){
                System.out.println("Node "+node.getNodeId()+" is runnable");
                //build the process associated with the node
                ProcessBuilder pb = new ProcessBuilder();
                pb.directory(currentDirectory);
                String[] commandList = node.getCommand().split(" ");
                pb.command(commandList);
                if (node.getInputFile()!=null){
                    File inputFile = node.getInputFile();
                    if (inputFile.exists()&&inputFile.isFile()){
                        pb.redirectInput(node.getInputFile());
                    }else{
                        //notify user an break out of while loop if the input file is not valid
                        System.out.println("The input file "+inputFile.getName()+" of node "+node.getNodeId()+" is not valid");
                        break OUTER_LOOP;
                    }
                    
                }
                if (node.getOutputFile()!=null){
                    pb.redirectOutput(node.getOutputFile());
                }
                try{
                    Process p = pb.start();
                    System.out.println("Node "+node.getNodeId()+" has started");
                    p.waitFor();
                    int Procresult = p.waitFor();
                    if (Procresult!=0){
                        System.out.println("ERROR: Process did not terminate normally. Check Node "+node.getNodeId());
                        //if process did not terminate normally break out of while loop
                        //prevents infinite loop, if process never terminate properly here, it will keep going back to pb.start and waitFor()
                        break OUTER_LOOP;
                    }                   
                    if (p.isAlive()){
                    //wait if p has not finished executing
                        System.out.println("Still waiting");
                        p.waitFor();
                    }else{
                        try{
                            int exitCode = p.exitValue();
                            //only mark the process executed if it has terminated normally without any errors
                            if (exitCode==0){
                                node.setExecuted();
                                System.out.println("Node "+node.getNodeId()+" has finished executing");
                                //after process has been executed it does not need to run again
                                //set to not runnable
                                node.setNotRunable();
                            }
                        }catch(Exception e){
                            System.out.println(e);
                            break OUTER_LOOP;
                        }
                    }
                }catch(Exception e){
                    System.out.println(e);
                    break OUTER_LOOP;
                }
            }
        }
        } 
       
        
       // ProcessGraph.printGraph();
       // ProcessGraph.printBasic();
        if (ProcessGraph.finished()){
            System.out.println("All process finished successfully");
        }
        
    }

}
