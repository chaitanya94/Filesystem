import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException{
		FileSystem fs = new FileSystem();
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
	}
}
