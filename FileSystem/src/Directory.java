import java.util.ArrayList;


public class Directory {
	private String DirectoryName;
	private String DirectoryPath;
	private ArrayList<File> files;
	private ArrayList<Directory> subDirectories;
	private Directory parent;
	
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
}
