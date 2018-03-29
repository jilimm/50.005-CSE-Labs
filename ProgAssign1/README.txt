Lim Jing Yun
1002261
Class 2


PURPOSE
ProcessManagement.java uses ProcessBUilder in java to traverse a driected acylic graph. This assignment was completed without threads. Thus only processes with no dependencies are run first, then processes who parents have completed are executed. 
When two processes are eligible to run. The process with the smalles numerical ID is started first. 


COMPILING CODE
compile using javac in Bash on Ubuntu on Windows

WHAT IT DOES
input file is declared at line 22 of ProcessManagement.java file
ParseFile parses instructions in instruction file, into appropirate commands, input/output files etc.
ProcessManagement java file manages the processes and the graph nodes. 
It executes root nodes first. Child node are only run when all parent nodes have been executed. 
It does this by arranging all nodes in numerical order (smallest to largest).
THe program will leep looping through the nodes of the graphs (from smallest to largest). If a program has no dependencies or its dependcies are done, it is marked as runnable and executed.
The program will keep going over this loop until all nodes in the graph have been executed. 
