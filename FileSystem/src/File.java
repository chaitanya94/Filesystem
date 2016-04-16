
public class File {
	private String FileName;
	private String FilePath;
	
	public File(String FileName, String FilePath ){
		this.FileName = FileName;
		this.FilePath = FilePath;
	}
	
	public String getName(){
		return this.FileName;
	}
	
	public String getFilePath(){
		return this.FilePath;
	}
}
