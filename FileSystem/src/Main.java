import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException{  
		Memory memory;
		FileSystem fs = null;
		String currentDir = System.getProperty("user.dir");
		currentDir = currentDir + "/src/";
		/*Loading saved version of filesystem and memory so that consistency over sessions is achieved*/
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(currentDir + "filesystem.dat"));
			fs=(FileSystem)in.readObject();
			in.close();
			
			ObjectInputStream inM=new ObjectInputStream(new FileInputStream(currentDir + "memory.dat"));
			memory=(Memory)inM.readObject();
			inM.close();
		} catch (Exception e){
			fs = new FileSystem();
			memory = new Memory();
		}
		
		fs.setMemory(memory);
		Scanner sc = new Scanner(System.in);
	
		System.out.println("Welcome to the IT308 Filesystem");
		while(true){
			System.out.print("$ ");
			String[] str = sc.nextLine().trim().split(" ");
			if(str[0].equals("quit")){
				break;
			}
			fs.execute(str);
		}
		System.out.print("Do you want to save changes? ");
		/*Saving versions of filesystem and memory for future sessions*/
		if(sc.nextLine().equals("yes") || sc.nextLine().equals("y")){
			FileOutputStream fout = new FileOutputStream(currentDir + "filesystem.dat");
			ObjectOutputStream out = new ObjectOutputStream(fout);

			out.writeObject(fs);
			out.flush();
			out.close();
		
			FileOutputStream foutM = new FileOutputStream(currentDir + "memory.dat");
			ObjectOutputStream outM = new ObjectOutputStream(foutM);

			outM.writeObject(memory);
			outM.flush();
			outM.close();
		}else{
			System.out.println("Changes not saved");
		}
		
	}
}
