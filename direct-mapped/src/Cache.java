class Cache
{
	private int id;		// only used by k-way associative
	private int index;	// index
	private int valid;	// valid bit
	private int tag;	// tag
	private int dirty;	// dirty bit

	// data fields for verbose mode
	int order;
	
	Cache()
	{
		id		= -1;
		valid	= -1;
		tag		= -1;
		dirty	= -1;
	}

	Cache(int id, int valid, int tag, int dirty)
	{
		this.id		= id;
		this.valid	= valid;
		this.tag	= tag;
		this.dirty	= dirty;
	}
}