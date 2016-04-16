import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileSystem{
	/*20MB of memory with block size being 4 bytes*/
	public char[] memory = new char[1024*1024*20]; 
	public boolean[] memoryUse = new boolean[1024*1024*20];
	public Directory currentDirectory;
	private int memoryIndex;
	
	public FileSystem(){
		for(int i = 0; i < memory.length; i++){
			this.memory[i] = '\0';
			this.memoryUse[i] = false;
		}
		this.memoryIndex = 0;
		this.currentDirectory = null;
		
		/*FOR TESTING - REMOVE LATER*/
		this.currentDirectory = new Directory("root", "/", null);
		/**/
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
				case "mkdir": createDirectory(args);
					break;
				case "read": readFile(args);
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
				"3. write <filename> <file_to_read>\n" +
				"4. mkdir <directory_name>\n" +
				"5. read <file_name>";
		System.out.println(str);
	}
	
	public boolean create(String[] args){
		String name = args[1];
		File f = new File(name, this.currentDirectory.getDirectoryPath());
		return this.currentDirectory.addFile(f);
	}
	
	public void write(String[] args) throws IOException{
		BufferedReader fileToRead = null;
		String nameToRead = args[2];
		try{
			String currentDir = System.getProperty("user.dir");
//			System.out.println(currentDir+"/src/"+nameToRead);
			fileToRead = new BufferedReader(new FileReader(currentDir+"/src/"+nameToRead));
			/*Need to do the following:
			 * 1. Open file using args[1]
			 * 2. Find the pointer from which to write (OR)
			 * 3. Add the pointer at which writing to the pointer list
			 * */
			File toWrite = this.currentDirectory.getFile(args[1].trim());
			if(toWrite.isPointerFull() == true){
				toWrite.incrementLevel();
				toWrite.setPointerFull(false);
				
				toWrite.setPointer(this.memoryIndex);
				int c;
				int counter = 0;
				while((c = fileToRead.read()) != -1){
					this.memory[this.memoryIndex] = (char)c;
					this.memoryIndex += 1;
					counter += 1;
					
					if(counter > 3){
						counter = 0;
						toWrite.incrementLevel();
						toWrite.setPointerFull(false);
						toWrite.setPointer(this.memoryIndex);
					}
				}
			}
		}finally{
			if(fileToRead != null)
				fileToRead.close();
		}
	}
	
	public void createDirectory(String[] args){
		/*TO FILL*/
	}
	
	public void readFile(String[] args){
		String name = args[1];
		File f = this.currentDirectory.getFile(name);
		
		if(f != null){
			int level = f.getInodeLevel();
			for(int i = 0;i < (level + 1); i++){
				if(i == 8){
					int[][] inodeMulti1 = f.getMulti1();
					for(int j = 0;j < 4; j++){
						for(int k = 0;k < 4; k++){
							System.out.print(this.memory[inodeMulti1[0][j]] + k);
						}
					}
				}else if(i == 9){
					int[][][] inodeMulti2 = f.getMulti2();
					for(int j = 0;j < 4;j++){
						for(int k = 0;k < 4;k++){
							for(int p = 0;p < 4;p++){
								System.out.print(this.memory[inodeMulti2[0][j][k]] + p);
							}
						}
					}
				}else if(i == 10){
					int[][][][] inodeMulti3 = f.getMulti3();
					for(int j = 0;j < 4; j++){
						for(int k = 0;k < 4;k++){
							for(int p = 0;p < 4;p++){
								for(int q = 0;q < 4; q++){
									System.out.print(this.memory[inodeMulti3[0][j][k][p]] + q);
								}
							}
						}
					}
				}else{
					int[] inode = f.getInode();
					for(int j = 0; j < 4; j++){
						System.out.print(this.memory[inode[i] + j]);
					}
				}
			}
			System.out.println();
		}else{
			System.out.println("File does not exist!!");
		}
	}
}
