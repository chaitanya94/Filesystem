public class FileSystem {
	public static void main(String[] args){
		if(args[0]==""){
			help();
		}else{
			switch(args[0]){
				case "create": break;
					
				default: break;
			}
		}
	}
	
	public static void help(){
		String str = "Welcome to the IT308 Filesystem. Please specify some input parameters. Some useful commands are:\n" +
				"1. help";
		System.out.println(str);
	}
}
