import java.util.ArrayList;
import java.io.Serializable; 


public class Directory implements Serializable{
	private String DirectoryName;
	private String DirectoryPath;
	private ArrayList<File> files;
	private ArrayList<Directory> subDirectories;
	private Directory parent;
	
	public Directory(String DirectoryName, String DirectoryPath, Directory parent){
		this.DirectoryName = DirectoryName;
		this.DirectoryPath = DirectoryPath;
		this.parent = parent;
		this.files = new ArrayList<File>();
	}
	
	public String getName(){
		return this.DirectoryName;
	}
	
	public String getDirectoryPath(){
		return this.DirectoryPath;
	}
	
	public boolean addFile(File f){
		return this.files.add(f);
	}
	
	public boolean deleteFile(File f){
		return this.files.remove(f);
	}
	
	public boolean addDirectory(Directory d){
		return this.subDirectories.add(d);
	}
	
	public boolean deleteDirectory(Directory d){
		return this.subDirectories.remove(d);
	}
	
	public Directory getParent(){
		return this.parent;
	}
	
	/*Returns an open file in the directory*/
	public File getFile(String name){
		for(File f: this.files){
			if(f.getName().equals(name))
				return f;
		}
		return null;
	}
	
	public boolean removeFile(String name){
		File f = this.getFile(name);
		return this.files.remove(f);
	}
	
	public int numFiles(){
		return this.files.size();
	}
	
	/*Prints the open files and subdirectories*/
	public void printContent(){
		System.out.println("Open files: ");
		if(this.files != null){
			for(File f: this.files){
				System.out.println(f.getName());
			}
		}
		System.out.println();
		System.out.println("Sub-directories: ");
		if(this.subDirectories != null){
			for(Directory d: this.subDirectories){
				System.out.println(d.getName());
			}
		}
	}
}
