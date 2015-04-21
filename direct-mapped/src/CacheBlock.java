class CacheBlock
{
	private int id;		// only used by k-way associative
	private int offset; // is 4 (HARDCODED HERE)
	private int index;	// index
	private int valid;	// valid bit
	private int tag;	// tag
	private int dirty;	// dirty bit

	// data fields for verbose mode
	int order;
	
	CacheBlock()
	{	this(-1, -1, 0, -1, 0);
	}

	CacheBlock(int id, int offset, int valid, int tag, int dirty)
	{
		this.id		= id;
		this.offset = offset;
		this.valid	= valid;
		this.tag	= tag;
		this.dirty	= dirty;
	}
	
	public int getValid(){ 	return valid; }
	public int getTag(){ 	return tag; }
	public int getDirty(){ 	return dirty; }
	
	public void setValid(){				valid = 1; }
	public void setTag(long newTag){	tag = (int)newTag; }
	public void setDirty(int newDirty){	dirty = newDirty; }
}