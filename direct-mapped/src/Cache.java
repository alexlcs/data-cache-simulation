class Cache
{
	private int id;		// only used by k-way associative
	private int offset; // is 4 (HARDCODED)
	private int index;	// index
	private int valid;	// valid bit
	private int tag;	// tag
	private int dirty;	// dirty bit

	// data fields for verbose mode
	int order;
	
	Cache()
	{	Cache(-1, 4, -1, -1, -1);
	}

	Cache(int id, int offset, int valid, int tag, int dirty)
	{
		this.id		= id;
		this.valid	= valid;
		this.tag	= tag;
		this.dirty	= dirty;
	}
}