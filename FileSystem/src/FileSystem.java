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
				case "append":	append(args);
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
				case "seek": seek(args);
					break;
				case "write": write(args);
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
				"3. append <filename> <file_to_read>\n" +
				"4. mkdir <directory_name>\n" +
				"5. read <file_name>\n" +
				"6. save <file_name\n" +
				"7. open <file_name>\n" +
				"8. close <file_name>\n" +
				"9. seek <file_name> <start_byte> <end_byte> - Return inclusive of start and end byte";
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
		if(args.length < 2){
			System.out.println("Specify more parameters");
			return;
		}
		String nameToRead = args[2];
		try{
			String currentDir = System.getProperty("user.dir");
			try{				
				fileToRead = new BufferedReader(new FileReader(currentDir+"/src/"+nameToRead));
			}catch(Exception e){
				System.out.println("File to read does not exist.");
				return;
			}
			this.currentDirectory.removeFile(args[1]);
			File f = new File(args[1], this.currentDirectory.getDirectoryPath());
			this.currentDirectory.addFile(f);
			this.append(args);
		}finally{
			if(fileToRead != null)
				fileToRead.close();
		}
	}
	
	public void append(String[] args) throws IOException{
		BufferedReader fileToRead = null;
		if(args.length < 2){
			System.out.println("Specify more parameters");
			return;
		}
		String nameToRead = args[2];
		try{
			String currentDir = System.getProperty("user.dir");
			try{				
				fileToRead = new BufferedReader(new FileReader(currentDir+"/src/"+nameToRead));
			}catch(Exception e){
				System.out.println("File to read does not exist.");
				return;
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
			
			int memPointer = toWrite.initWrite();
			if(memPointer == -10){
				memPointer = this.memoryIndex;
			}
			toWrite.incrementLevel();
			
			if(memPointer % 4 == 0)
				toWrite.setPointer(memPointer);
			int c;
			int counter = toWrite.getSizeBytes() % 4;
			while((c = fileToRead.read()) != -1){
				this.memory.data[memPointer] = (char)c;
				this.memoryUse[memPointer] = true;
				toWrite.incrementSize();
				memPointer += 1;
				if(memPointer > this.memoryIndex){
					this.memoryIndex++;
				}
				counter += 1;
				
				if(counter > 3){
					counter = 0;
					/*The next block where you go to write is in use*/
					if(this.memoryUse[memPointer] == true)
						memPointer = this.memoryIndex;
					toWrite.setPointer(memPointer);
				}
			}
			if((this.memoryIndex % 4) != 0)
				this.memoryIndex = this.memoryIndex + 4 - (this.memoryIndex % 4);
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
			int level = f.getLevel();
			for(int i = 0;i < (level + 1); i++){
				if(i == 8){
					int[][] inodeMulti1 = f.getMulti1();
					for(int j = 0;j < 4; j++){
						for(int k = 0;k < 4; k++){
							if((inodeMulti1[0][j] + k) <= this.memoryIndex )
								System.out.print(this.memory.data[inodeMulti1[0][j] + k]);
						}
					}
				}else if(i == 9){
					int[][][] inodeMulti2 = f.getMulti2();
					for(int j = 0;j < 4;j++){
						for(int k = 0;k < 4;k++){
							for(int p = 0;p < 4;p++){
								if((inodeMulti2[0][j][k] + p) <= this.memoryIndex )
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
									if((inodeMulti3[0][j][k][p] + q) <= this.memoryIndex )
										System.out.print(this.memory.data[inodeMulti3[0][j][k][p] + q]);
								}
							}
						}
					}
				}else{
					int[] inode = f.getInode();
					for(int j = 0; j < 4; j++){
						if((inode[i] + j) < this.memoryIndex )
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
	
	public void setMemory(Memory m){
		this.memory = m;
	}
	
	public void seek(String[] args){
		String name = args[1];
		int startByte = Integer.valueOf(args[2]);
		int endByte = Integer.valueOf(args[3]);
		
		File f = this.currentDirectory.getFile(name);
		int level = f.getLevel();
		int fileSizeBytes = level*4;
		if(level > 7){
			switch(level){
				case 8: fileSizeBytes = fileSizeBytes + 4*4;
					break;
				case 9: fileSizeBytes = fileSizeBytes + 4*4 + 4*4*4;
					break;
				case 10: fileSizeBytes = fileSizeBytes + 4*4 + 4*4*4 + 4*4*4*4;
					break;
				default: System.out.println("Filesize error");
					return; 
			}
		}
		
		if(startByte > fileSizeBytes || endByte > fileSizeBytes){
			System.out.println("Parameters exceed file size.");
			return;
		}
		
		int i = startByte/4;
		int byteCount = 0;
		while(i < level){
			if(i == 8){
				int[][] inodeMulti1 = f.getMulti1();
				for(int j = 0;j < 4; j++){
					for(int k = 0;k < 4; k++){
						if(byteCount > endByte){
							System.out.println();
							return;
						}	
						if(byteCount >= startByte)
							System.out.print(this.memory.data[inodeMulti1[0][j] + k]);
						byteCount++;
					}
				}
			}else if(i == 9){
				int[][][] inodeMulti2 = f.getMulti2();
				for(int j = 0;j < 4;j++){
					for(int k = 0;k < 4;k++){
						for(int p = 0;p < 4;p++){
							if(byteCount > endByte){
								System.out.println();
								return;
							}
							if(byteCount >= startByte)
								System.out.print(this.memory.data[inodeMulti2[0][j][k] + p]);
							byteCount++;
						}
					}
				}
			}else if(i == 10){
				int[][][][] inodeMulti3 = f.getMulti3();
				for(int j = 0;j < 4; j++){
					for(int k = 0;k < 4;k++){
						for(int p = 0;p < 4;p++){
							for(int q = 0;q < 4; q++){
								if(byteCount > endByte){
									System.out.println();
									return;
								}
								if(byteCount >= startByte)
									System.out.print(this.memory.data[inodeMulti3[0][j][k][p] + q]);
								byteCount++;
							}
						}
					}
				}
			}else{
				int[] inode = f.getInode();
				for(int j = 0; j < 4; j++){
					if(byteCount > endByte){
						System.out.println();
						return;
					}
					if(byteCount >= startByte)
						System.out.print(this.memory.data[inode[i] + j]);
					byteCount++;
				}
			}
			i++;
		}
	}
}
