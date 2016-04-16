import java.io.FileInputStream;
import java.io.IOException;

public class FileSystem{
	/*20MB of memory with block size being 4 bytes*/
	public char[] memory = new char[1024*1024*20]; 
	public boolean[] memoryUse = new boolean[1024*1024*20];
	public Directory currentDirectory;
	private long memoryIndex;
	
	public FileSystem(){
		for(int i = 0; i < memory.length; i++){
			this.memory[i] = '\0';
			this.memoryUse[i] = false;
		}
		this.memoryIndex = 0;
	}
	
	public void execute(String[] args) throws IOException{
		
		if(args[0]==""){
			help();
		}else{
			switch(args[0]){
				case "create": create(args);
					break;
				case "write":	write(args);
					break;
				default: help();
					break;
			}
		}
	}
	
	public void help(){
		String str = "Welcome to the IT308 Filesystem. Please specify some input parameters. Some useful commands are:\n" +
				"1. help\n" +
				"2. create <filename>\n" +
				"3. write <filename> <file_to_read>";
		System.out.println(str);
	}
	
	public boolean create(String[] args){
		String name = args[1];
		File f = new File(name, this.currentDirectory.getDirectoryPath());
		return this.currentDirectory.addFile(f);
	}
	
	public void write(String[] args) throws IOException{
		FileInputStream fileToRead = null;
		String nameToRead = args[2];
		try{
			fileToRead = new FileInputStream(nameToRead);
			/*Need to do the following:
			 * 1. Open file using args[1]
			 * 2. Find the pointer from which to write (OR)
			 * 3. Add the pointer at which writing to the pointer list
			 * */
		}finally{
			if(fileToRead != null)
				fileToRead.close();
		}
	}
}
