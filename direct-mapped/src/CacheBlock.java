class CacheBlock
{
	private int offset; // =4 (HARDCODED HERE)
	private int index;	// index
	private int valid;	// valid bit
	private long tag;	// tag
	private int dirty;	// dirty bit

	// data fields for verbose mode
	int order;
	
	CacheBlock()
	{	this(0, 0, 0, 0);
	}

	CacheBlock(int index, int valid, int dirty, long tag)
	{
		this.offset = 4;
		this.index 	= index;
		this.valid	= valid;
		this.dirty	= dirty;
		this.tag	= tag;
	}
	
	public int getValid(){ 	return valid; }
	public long getTag(){ 	return tag; }
	public int getDirty(){ 	return dirty; }
	
	public void setIndex(int newIndex){ index = newIndex;}
	public void setValid(){				valid = 1; }
	public void setTag(long newTag){	tag = newTag; }
	public void setDirty(int newDirty){	dirty = newDirty; }
	
	public void print()
	{
		System.out.println(index +"\t"+ valid +"\t"+ dirty +"\t"+ tag);
	}
}