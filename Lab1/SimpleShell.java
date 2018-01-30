import java.io.*;
import java.util.LinkedList;
public class SimpleShell {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		ProcessBuilder pb = new ProcessBuilder();
		LinkedList<String> hist = new LinkedList<String>();
		Boolean lastCommandCall = false;
		Boolean numCommandCall=false;
		Integer numCommand =0;
		
		while(true){
			if (lastCommandCall){
				commandLine=hist.get(0);
			}else if(hist.size()>0 && numCommandCall && numCommand>0){
				commandLine=hist.get(numCommand-1);
			}else{
				//read what user has entered
				System.out.print("jsh>");
				commandLine = console.readLine();
			}
			
			
			if (commandLine.equals("")){
				continue;
			}
			//dont add to history if you nvr type anything or if you call previous command
			if (!commandLine.equals("!!")&& !commandLine.matches("[0-9]+") && !commandLine.equals("history") && !commandLine.equals("") && !lastCommandCall && !numCommandCall) {
				hist.addFirst(commandLine);
			} else {
				lastCommandCall=false;
				numCommandCall=false;
			}
			try {
				String [] commandList=commandLine.split(" ");
				pb.command(commandList);
				
				//obtaining the current directory
				File currentDir = pb.directory();
				if (currentDir==null) currentDir=new File("");
				
				//getting home directory 
				File homeDir = new File(System.getProperty("user.home"));
				
				//obtaining parent directory
				File parentDir = new File(currentDir.getAbsolutePath()).getParentFile();
				
				
				//parsing the cd commands
				if (commandList.length==1 && commandList[0].equals("cd")){
					pb.directory(homeDir);
					//System.out.println("now dir is "+pb.directory().getAbsolutePath());
				}else if (commandList[0].equals("cd")&& commandList.length==2){
					if (commandList[1].equals("..")){ 
						//go to parent directory
						pb.directory(parentDir);
						}
					else if (commandList[1].equals(".")){
						pb.directory(currentDir);
						}
					else if (commandList[1].equals("~")){
						pb.directory(homeDir);
						}
					else {
						String newDirPath = currentDir.getAbsolutePath()+"/"+commandList[1];
						File newDir = new File(newDirPath);
							if (newDir.exists()){
							pb.directory(newDir);
							}else {
							System.out.println("error new directory is invlid "+newDirPath);
							}
					}					
				}else if (commandList.length==1 && commandList[0].equals("!!")){
					//System.out.println(hist.get(0));
					lastCommandCall=true;
					continue;
				}else if(commandList.length==1 && commandList[0].equals("history")){
					for (int i=0;i<hist.size(); i++){ 
						System.out.println(String.valueOf(i+1)+" "+hist.get(i));
					} 
				}else if(commandList.length==1 && commandList[0].matches("[0-9]+")){
					//check if the integer is within the size of the history
					if (hist.size()==0){
						System.out.print("history is empty");
					}else if (Integer.valueOf(commandList[0])<=hist.size()&&Integer.valueOf(commandList[0])>0){
						numCommandCall=true;
						numCommand=Integer.valueOf(commandList[0]);
						continue;
					}else{
						System.out.println("The number must be between 1 to "+String.valueOf(hist.size())+", inclusive.");
					}
				}else{
					//System.out.println("HERE: currentdir is "+pb.directory().getAbsolutePath());
					Process p  = pb.start();
					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					for (String line; (line = br.readLine()) !=null;) {
						System.out.println(line);
					}
					br.close();
				}
			} catch (IOException e){
				//continue;
				System.out.println(e.getMessage());
			}
		}
		

	}

}
