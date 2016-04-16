import java.io.Serializable; 

public class File implements Serializable{
	private String FileName;
	private String FilePath;
	private int[] inode;
	private int[][] inodeMulti1;
	private int[][][] inodeMulti2;
	private int[][][][] inodeMulti3;
	private int inodePointer; /*The pointer till which inode has been populated*/
	private boolean pointerFull; /*Is the current inode pointer full?*/
	private int inodeLevel;
	private int level2, level3;
	
	public File(String FileName, String FilePath ){
		this.FileName = FileName;
		this.FilePath = FilePath;
		this.inode = new int[8];
		this.inodePointer = 0;
		this.pointerFull = true;
		this.inodeLevel = -1;
		
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
	
	public void setPointer(int memoryIndex){
		if(this.isMultilevel()){
			int level = this.getInodeLevel();
			
			switch(level){
				/*CASE 8*/
				case 8: if(this.inodeMulti1 == null){
					this.inodePointer = 0;
					this.inodeMulti1 = new int[1][4]; //Block size is 4
					this.inodeMulti1[0][this.inodePointer] = memoryIndex;
					
					this.inodePointer += 1;
				}else if(this.inodePointer < 3){
					this.inodeMulti1[0][this.inodePointer] = memoryIndex;
					
					this.inodePointer += 1;
				}else{
					this.incrementLevel();
					this.setPointer(memoryIndex);
				}
					break;
				/*CASE 9*/
				case 9: if(this.inodeMulti2 == null){
					this.inodePointer = 0;
					this.level2 = 0;
					this.inodeMulti2 = new int[1][4][4];
					this.inodeMulti2[0][this.level2][this.inodePointer] = memoryIndex;
					
					this.inodePointer += 1;
					this.level2 += 1;
				}else if(this.level2 < 4 && this.inodePointer < 4){
					this.inodeMulti2[0][this.level2][this.inodePointer] = memoryIndex;

					this.inodePointer += 1;
					this.level2 += 1;
				}else if(this.level2 > 3 && this.inodePointer < 4){
					this.incrementLevel();
					this.setPointer(memoryIndex);
					
				}else if(this.level2 < 4 && this.inodePointer > 3){
					this.inodePointer = 0;
					this.level2 += 1;
					this.setPointer(memoryIndex);
				}else{
					this.incrementLevel();
					this.setPointer(memoryIndex);
				}
					break;
				/*CASE 10*/
				case 10: if(this.inodeMulti3 == null){
					this.inodePointer = 0;
					this.level2 = 0;
					this.level3 = 0;
					this.inodeMulti3 = new int[1][4][4][4];
					this.inodeMulti3[0][this.level2][this.level3][this.inodePointer] = memoryIndex;
				}else if(this.level2 < 4 && this.level3 < 4 && this.inodePointer < 4){
					this.inodeMulti3[0][this.level2][this.level3][this.inodePointer] = memoryIndex;
				}else if(this.level2 < 4 && this.level3 < 4 && this.inodePointer > 3){
					this.level3 += 1;
					this.inodePointer = 0;
					this.setPointer(memoryIndex);
				}else if(this.level2 < 4 && this.level3 > 3){
					this.level2 += 1;
					this.level3 = 0;
					this.setPointer(memoryIndex);
				}else{
					this.incrementLevel();
					this.setPointer(memoryIndex);
				}
					this.inodePointer += 1;
					this.level2 += 1;
					this.level3 += 1;
					break;
				default: System.out.println("Error setting inode pointer");
					break;
			}
		}else{
			this.inode[this.inodePointer] = memoryIndex;
			this.inodePointer += 1;
		}
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
	
	public int getLevel(){
		return this.inodeLevel;
	}
	
	public int[][] getMulti1(){
		return this.inodeMulti1;
	}
	
	public int[][][] getMulti2(){
		return this.inodeMulti2;
	}
	
	public int[][][][] getMulti3(){
		return this.inodeMulti3;
	}
}
