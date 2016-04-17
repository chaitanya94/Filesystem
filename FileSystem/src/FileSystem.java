import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class FileSystem implements Serializable{
	/*20MB of memory with block size being 4 bytes*/
	public Memory memory; 
	public boolean[] memoryUse = new boolean[1024*1024*20];
	public Directory currentDirectory;
	private int memoryIndex;
	
	public FileSystem(Memory m){
		this.memory = m;
		this.memoryIndex = 0;
		this.currentDirectory = null;
		
		/*FOR TESTING - REMOVE LATER*/
		this.currentDirectory = new Directory("root", "/", null);
		/**/
	}
	
	public FileSystem(){
		this.memory = new Memory();
		this.memoryIndex = 0;
		this.currentDirectory = null;
		
		/*FOR TESTING - REMOVE LATER*/
		this.currentDirectory = new Directory("root", "/", null);
		/**/
	}
	
	public void execute(String[] args) throws IOException, ClassNotFoundException{
		
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
				case "save": saveFile(args);
					break;
				case "open": openFile(args);
					break;
				case "close": closeFile(args);
					break;
				case "checkLenFile": System.out.println(this.currentDirectory.numFiles());
					break;
				case "ls": listFile(args);
					break;
				default: help();
					break;
			}
		}
	}
	
	public void listFile(String[] args){
		Directory d = this.currentDirectory;
		d.printContent();
	}
	
	public void help(){
		String str = "Welcome to the IT308 Filesystem. Please specify some input parameters. Some useful commands are:\n" +
				"1. help\n" +
				"2. create <filename>\n" +
				"3. write <filename> <file_to_read>\n" +
				"4. mkdir <directory_name>\n" +
				"5. read <file_name>\n" +
				"6. save <file_name\n" +
				"7. open <file_name>\n" +
				"8. close <file_name>";
		System.out.println(str);
	}
	
	public boolean create(String[] args){
		String name = args[1];
		if(this.currentDirectory.getFile(name) != null){
			System.out.println("File already exits.");
			return false;
		}
		File f = new File(name, this.currentDirectory.getDirectoryPath());
		return this.currentDirectory.addFile(f);
	}
	
	public void write(String[] args) throws IOException{
		BufferedReader fileToRead = null;
		String nameToRead = args[2];
		try{
			String currentDir = System.getProperty("user.dir");
			try{				
				fileToRead = new BufferedReader(new FileReader(currentDir+"/src/"+nameToRead));
			}catch(Exception e){
				System.out.println("File to read does not exist.");
			}
			/*Need to do the following:
			 * 1. Open file using args[1]
			 * 2. Find the pointer from which to write (OR)
			 * 3. Add the pointer at which writing to the pointer list
			 * */
			File toWrite = this.currentDirectory.getFile(args[1].trim());
			if(toWrite == null){
				System.out.println("File does not exist.");
				return;
			}
			if(toWrite.isPointerFull() == true){
				toWrite.incrementLevel();
				toWrite.setPointerFull(false);
				
				toWrite.setPointer(this.memoryIndex);
				int c;
				int counter = 0;
				while((c = fileToRead.read()) != -1){
					this.memory.data[this.memoryIndex] = (char)c;
					this.memoryIndex += 1;
					counter += 1;
					
					if(counter > 3){
						counter = 0;
						toWrite.setPointer(this.memoryIndex);
						toWrite.setPointerFull(false);
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
		File f = null;
		try{			
			f = this.currentDirectory.getFile(name);
		}catch(Exception e){
			System.out.println("File does not exist.");
			return;
		}
		
		if(f != null){
			int level = f.getInodeLevel();
			for(int i = 0;i < (level + 1); i++){
				if(i == 8){
					int[][] inodeMulti1 = f.getMulti1();
					for(int j = 0;j < 4; j++){
						for(int k = 0;k < 4; k++){
							System.out.print(this.memory.data[inodeMulti1[0][j] + k]);
						}
					}
				}else if(i == 9){
					int[][][] inodeMulti2 = f.getMulti2();
					for(int j = 0;j < 4;j++){
						for(int k = 0;k < 4;k++){
							for(int p = 0;p < 4;p++){
								System.out.print(this.memory.data[inodeMulti2[0][j][k] + p]);
							}
						}
					}
				}else if(i == 10){
					int[][][][] inodeMulti3 = f.getMulti3();
					for(int j = 0;j < 4; j++){
						for(int k = 0;k < 4;k++){
							for(int p = 0;p < 4;p++){
								for(int q = 0;q < 4; q++){
									System.out.print(this.memory.data[inodeMulti3[0][j][k][p] + q]);
								}
							}
						}
					}
				}else{
					int[] inode = f.getInode();
					for(int j = 0; j < 4; j++){
						System.out.print(this.memory.data[inode[i] + j]);
					}
				}
			}
			System.out.println();
		}else{
			System.out.println("File does not exist!!");
		}
	}
	public void saveFile(String[] args) throws IOException{
		String currentDir = System.getProperty("user.dir");
		currentDir = currentDir + "/src/";
		String fileName = args[1];
		File f = this.currentDirectory.getFile(fileName);
		FileOutputStream fout = new FileOutputStream(currentDir + fileName + ".dat");
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(f);
		out.flush();
		out.close();
	}
	
	public void openFile(String[] args) throws IOException, ClassNotFoundException{
		String currentDir = System.getProperty("user.dir");
		currentDir = currentDir + "/src/";
		String fileName = args[1];
		File f = null;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					currentDir + fileName + ".dat"));
			f = (File) in.readObject();
			in.close();
			
			this.currentDirectory.addFile(f);
		}catch (Exception e){
			System.out.println("File does not exist. Please create file!");
		}
	}
	
	public void closeFile(String[] args) throws IOException{
		System.out.print("Do you want to save your changes? ");
		Scanner sc = new Scanner(System.in);
		
		if(sc.nextLine().equals("yes") || sc.nextLine().equals("y")){
			this.saveFile(args);
		}
		String name = args[1];
		this.currentDirectory.removeFile(name);
	}
	
	
}
