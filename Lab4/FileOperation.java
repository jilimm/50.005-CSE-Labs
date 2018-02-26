// package Week5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.lang.NumberFormatException;

public class FileOperation {
	private static File currentDirectory = new File(System.getProperty("user.dir"));
	private static ArrayList<File> fileFoundList;
	public static void main(String[] args) throws java.io.IOException {

		String commandLine;

		BufferedReader console = new BufferedReader
				(new InputStreamReader(System.in));

		while (true) {
			// read what the user entered
			System.out.print("jsh>");
			commandLine = console.readLine();

			// clear the space before and after the command line
			commandLine = commandLine.trim();

			// if the user entered a return, just loop again
			if (commandLine.equals("")) {
				continue;
			}
			// if exit or quit
			else if (commandLine.equalsIgnoreCase("exit") | commandLine.equalsIgnoreCase("quit")) {
				System.exit(0);
			}

			// check the command line, separate the words
			String[] commandStr = commandLine.split(" ");
			ArrayList<String> command = new ArrayList<String>();
			for (int i = 0; i < commandStr.length; i++) {
				command.add(commandStr[i]);
			}

			// TODO: implement code to handle create here
			if (command.size()==2&&command.get(0).equalsIgnoreCase("create")){
				Java_create(currentDirectory, command.get(1));
			}
			else if (command.size()==2&&command.get(0).equalsIgnoreCase("delete")){
				Java_delete(currentDirectory,command.get(1));
			}
			else if (command.size()==2&&command.get(0).equalsIgnoreCase("display")){
				Java_cat(currentDirectory,command.get(1));
			}
			
			else if (command.size()==1&&command.get(0).equalsIgnoreCase("list")){
				Java_ls(currentDirectory, null, null);
			}
			else if (command.size()==2&&command.get(0).equalsIgnoreCase("list")&&command.get(1).equalsIgnoreCase("property")){
				Java_ls(currentDirectory, "property", null);
			}else if (command.size()==3&&command.get(0).equalsIgnoreCase("list")&&command.get(1).equalsIgnoreCase("property")){
				Java_ls(currentDirectory, "property", command.get(2));
			}
			else if (command.size()==2&&command.get(0).equalsIgnoreCase("find")){
				fileFoundList = new ArrayList<File>();
				boolean empty = Java_find(currentDirectory, command.get(1));
				if (!empty){
					System.out.println("The directory "+currentDirectory.getAbsolutePath()+" is emty.");
				}else{
					if (fileFoundList.size()==0){
						System.out.println("No files containing '"+command.get(1)+"' found");
					}
					else{
						for (File file:fileFoundList){
							System.out.println(file.getAbsolutePath());
						}
					}
					fileFoundList.clear();	
				}						
			}else if(command.size()==3&&command.get(0).equalsIgnoreCase("tree")){
				try{
					int depth = Integer.parseInt(command.get(1));
					String sort = command.get(2);
					Java_tree(currentDirectory,depth,sort);
				}catch(Exception e){
					System.out.println(command.get(1)+" is not a valid integer");
					System.out.println(e);
				}

			}else if(command.size()==2&&command.get(0).equalsIgnoreCase("tree")){
				try{
					int depth = Integer.parseInt(command.get(1));
					Java_tree(currentDirectory,depth,null);
				}catch(Exception e){
					System.out.println(command.get(1)+" is not a valid integer");
					System.out.println(e);
				}
			}else if(command.size()==1&&command.get(0).equalsIgnoreCase("tree")){
				Java_tree(currentDirectory,Integer.MAX_VALUE,null);
			}
			else{
				System.out.println("command not handled");

			// TODO: implement code to handle list here
			
			// TODO: implement code to handle find here

			// TODO: implement code to handle tree here

			// other commands
				ProcessBuilder pBuilder = new ProcessBuilder(command);
				pBuilder.directory(currentDirectory);
				try{
					Process process = pBuilder.start();
					// obtain the input stream
					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);

					// read what is returned by the command
					String line;
					while ( (line = br.readLine()) != null)
						System.out.println(line);

					// close BufferedReader
					br.close();
				}
				// catch the IOexception and resume waiting for commands
				catch (IOException ex){
					System.out.println(ex);
					continue;
				}
			}
		}
	}

	/**
	 * Create a file
	 * @param dir - current working directory
	 * @param command - name of the file to be created
	 */
	public static void Java_create(File dir, String name) {
		File file = new File(dir, name);
		try{
			file.createNewFile();	
		}catch(Exception e){
			System.out.println(e);

		}
		
	}

	/**
	 * Delete a file
	 * @param dir - current working directory
	 * @param name - name of the file to be deleted
	 */
	public static void Java_delete(File dir, String name) {
		File file = new File(dir, name);
		file.delete();
	}

	/**
	 * Display the file
	 * @param dir - current working directory
	 * @param name - name of the file to be displayed
	 */
	public static void Java_cat(File dir, String name) {
		File file = new File(dir, name);
		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader in = new BufferedReader(fileReader);
			String line;
			while((line = in.readLine())!=null){
				System.out.println(line);
			}
			in.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

	/**
	 * Function to sort the file list
	 * @param list - file list to be sorted
	 * @param sort_method - control the sort type
	 * @return sorted list - the sorted file list
	 */
	private static File[] sortFileList(File[] list, String sort_method) {
		// sort the file list based on sort_method
		// if sort based on name
		if (sort_method==null){
			return list;
		}else if (sort_method.equalsIgnoreCase("name")) {
			Arrays.sort(list, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return (f1.getName()).compareTo(f2.getName());
				}
			});
		}else if (sort_method.equalsIgnoreCase("size")) {
			Arrays.sort(list, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.length()).compareTo(f2.length());
				}
			});
		}
		else if (sort_method.equalsIgnoreCase("time")) {
			Arrays.sort(list, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});
		}else if (sort_method==null){
			
		}
		System.out.println("ERROR: invalid sort method.");
		System.out.println("sort methods are: name, size, time");
		return list;
	}

	/**
	 * List the files under directory
	 * @param dir - current directory
	 * @param display_method - control the list type
	 * @param sort_method - control the sort type
	 */
	public static void Java_ls(File dir, String display_method, String sort_method) {
		// TODO: list files
		File[] fileList = dir.listFiles();
		File[] fileSorted = null;
		if (display_method==null){
			for (File file: fileList){
				System.out.println(file.getName());
			}
		} else if(display_method.equalsIgnoreCase("property") && sort_method==null){
			for (File file : fileList){
				System.out.println(file.getName()+"	"+"Size: "+file.length()+"	"+new Date(file.lastModified()));
			}
		}else if(display_method.equalsIgnoreCase("property") && sort_method.equalsIgnoreCase("name")){
			fileSorted = sortFileList(fileList,"name");
			for (File file : fileSorted){
				System.out.println(file.getName()+"	"+"Size: "+file.length()+"	"+new Date(file.lastModified()));
			}
		}else if(display_method.equalsIgnoreCase("property") && sort_method.equalsIgnoreCase("size")){
			fileSorted = sortFileList(fileList,"size");
			for (File file : fileSorted){
				System.out.println(file.getName()+"	"+"Size: "+file.length()+"	"+new Date(file.lastModified()));
			}
		}else if(display_method.equalsIgnoreCase("property") && sort_method.equalsIgnoreCase("time")){
			fileSorted = sortFileList(fileList,"time");
			for (File file : fileSorted){
				System.out.println(file.getName()+"	"+"Size: "+file.length()+"	"+new Date(file.lastModified()));
			}
		}else{
			System.out.println("ERROR: invalid display command");
		}
	}

	/**
	 * Find files based on input string
	 * @param dir - current working directory
	 * @param name - input string to find in file's name
	 * @return flag - whether the input string is found in this directory and its subdirectories
	 */
	public static boolean Java_find(File dir, String name) {
		boolean flag = false;

		if (dir.listFiles().length==0){
			flag = false;
		}else{
			File[] fileList = dir.listFiles();
			//initiatie DFS
			for (File file : fileList){
				DFS(file, name);
			}
			flag = true;
		}
		return flag;
	}

	public static void DFS(File dir, String substring){
		if (dir.isDirectory()&&dir.listFiles().length>0){
			for (File file:dir.listFiles()){
				//recurse if it is a non-empty directory, so we can search the subdirectories
				DFS(file,substring);
			}
		}else if (!dir.isDirectory()&&dir.getAbsolutePath().toLowerCase().contains(substring.toLowerCase())){
			//if the file not directory and contains text
			fileFoundList.add(dir);
		}else{
			//do nothing
		}
	}

	/**
	 * Print file structure under current directory in a tree structure
	 * @param dir - current working directory
	 * @param depth - maximum sub-level file to be displayed
	 * @param sort_method - control the sort type
	 */
	public static void Java_tree(File dir, int depth, String sort_method) {		
		File[] fileSorted = sortFileList(dir.listFiles(),sort_method);
		//initialise tree DFS
		for (File file: fileSorted){
			System.out.println(file.getName());
			if (file.isDirectory()&&file.listFiles().length>0){
				tree_DFS(file, depth, sort_method, 0);
			}
		}
	}

	//DFS in each individual directory
	public static void tree_DFS(File dir, int depth, String sort_method, int check) {
		String addOn="";
		//front stuff for recursion
		
		for (int i=0;i<check;i++){
			addOn+="	";
		}
		if (check>0){
			addOn+="|-";
		}
		if (check<depth&&check>0){
			System.out.println(addOn+dir.getName());
		}
		//recursing for subdirectories
		if (dir.isDirectory()&&dir.listFiles().length>0){
			File[] sortedFile = sortFileList(dir.listFiles(),sort_method);
			for (File file:sortedFile){
				tree_DFS(file,depth,sort_method,check+1);				
			}
		}


		
	}
}