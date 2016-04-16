import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException{  
		FileSystem fs = null;
		String currentDir = System.getProperty("user.dir");
		currentDir = currentDir + "/src/";
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(currentDir + "fileSystem.dat"));
			fs=(FileSystem)in.readObject();
			in.close();
		} catch (Exception e){
			fs = new FileSystem();
		}
		
		 
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
		if(sc.nextLine().equals("yes") || sc.nextLine().equals("y")){
			FileOutputStream fout = new FileOutputStream(currentDir + "fileSystem.dat");
			ObjectOutputStream out = new ObjectOutputStream(fout);

			out.writeObject(fs);
			out.flush();
			out.close();
		}else{
			System.out.println("Changes not saved");
		}
		
	}
}
