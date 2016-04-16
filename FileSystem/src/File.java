
public class File {
	private String FileName;
	private String FilePath;
	private int[] inode;
	private int inodePointer; /*The pointer till which inode has been populated*/
	private boolean pointerFull; /*Is the current inode pointer full?*/
	private int inodeLevel;
	
	public File(String FileName, String FilePath ){
		this.FileName = FileName;
		this.FilePath = FilePath;
		this.inode = new int[10];
		this.inodePointer = 0;
		this.pointerFull = false;
		this.inodeLevel = 0;
		
		for(int i = 0;i < this.inode.length; i++)
			this.inode[i] = -1;
	}
	
	public String getName(){
		return this.FileName;
	}
	
	public String getFilePath(){
		return this.FilePath;
	}
	
	public int[] getInode(){
		return this.inode;
	}
	
	public boolean isPointerFull(){
		return this.pointerFull;
	}
	
	public void setPointerFull(boolean b){
		this.pointerFull = b;
	}
	
	public void incrementPointer(){
		this.inodePointer += 1;
	}
	
	public void setPointer(int memoryIndex){
		if(this.isMultilevel()){
			int level = this.getInodeLevel();
			
			switch(level){
				case 8: if(this.inode[this.inodePointer] == -1){
					this.inode[this.inodePointer] = new int[4]; // 4 is the block size
				}
					break;
				case 9: break;
				case 10: break;
			}
		}else{
			
		}
		this.inode[this.inodePointer] = memoryIndex;
		this.inodePointer += 1;
	}
	
	public int getInodeLevel(){
		return this.inodeLevel;
	}
	
	public void incrementLevel(){
		this.inodeLevel += 1;
	}
	
	public boolean isMultilevel(){
		return this.inodeLevel > 7;
	}
}
