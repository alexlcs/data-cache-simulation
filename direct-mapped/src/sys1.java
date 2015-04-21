import java.io.*;
/*
	!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	!!!                                                       !!!
	!!!	  NOTE: for proper indentation use 1 tab = 4 spaces   !!!
	!!!                                                       !!!
	!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
*/
class sys1
{
	// input data
	private String 		filename;		// name of tracefile
	private int 		cacheSize;		// cache size
	private boolean		verbose;		// verbose on/off
	private int			start;			// starting address for verbose
	private int			end;			// ending addres for verbose
	// tag index offset
	private int			offsetSize;
	private int			indexSize;
	private int			tagSize;
	// other
	private ReadFile	readFile;		// object to read xex file format
	private int			rows;			// # of rows in cache
	private CacheBlock	cacheBlock[];	// cache
	private int			blockSize = 16;	// number of bytes in one block for data (HARDCODED HERE)
	private int			length = 100000;// # of rows in file (HARDCODED HERE)

	/*
		HOW TO UNDERSTAND stasArray[]:
		------------------------------
		[0]  = dataReads 		o
		[1]  = dataWrites 		o
		[2]  = dataAccesses		o
		
		[3]  = dataReadMisses 	x
		[4]  = dataWriteMisses 	x
		[5]  = dataMisses 		x
		
		[6]  = dirtyDataReadMisses 		x
		[7]  = dirtyDataWriteMisses 	x
		
		[8]  = numberOfBytesReadFromMemory 	x
		[9]  = numberOfBytesWrittenToMemory	x
		
		[10] = totalAccessTimeForReads 	x
		[11] = totalAccessTimeForWrites	x
		
		[12] = dataCacheMissRate x (read misses + write misses) / total access
	*/
	private int statsArray[];

	sys1(String args[])
	{
		filename	= args[0];
		cacheSize	= Integer.parseInt(args[1]);
		if( args.length > 2 )
		{	if( args[2].equals("-v") )
			{	verbose = true;	
				System.out.println("Verbose mode: ON\n");
				try
				{	start	= Integer.parseInt(args[3]);
					end		= Integer.parseInt(args[4]);
				}catch(Exception e)
				{	System.out.println("Not enough arguments or wrong arguments.");
					System.exit(0);
				}
			}	
		}else{ verbose = false; }
		
		// compute offset, index, tag
		offsetSize	= (int)( Math.log10(blockSize)/Math.log10(2) );
		indexSize	= (int)( Math.log10(cacheSize*1024/blockSize)/Math.log10(2));
		tagSize		= 32 - offsetSize - indexSize;

		// read file into arrays that are inside ReadFile.java
		System.out.println("Calculating...");
		readFile = new ReadFile(filename);

		// compute # of rows in cache & instantiate cacheBlock
		rows = (cacheSize*1024)/16;
		cacheBlock = new CacheBlock[rows];
		for(int i=0; i<cacheBlock.length; i++) cacheBlock[i] = new CacheBlock();

		//System.out.println("offset: " + offsetSize + " | index: " + indexSize + " | tag: " + tagSize + " | rows: " + cacheBlock.length);

		// instantiate statsArray
		statsArray = new int[13];
		for(int i=0; i<statsArray.length; i++) statsArray[i] = 0;

		// STATS: find data reads and writes
		for(int i=0; i<length; i++)
		{	int t = readFile.getRW(i);
			if(t == 0) statsArray[0]++;
			else statsArray[1]++;
		}
		System.out.println("Reads: " + statsArray[0] + " | Writes: " + statsArray[1] + " | Total: " + (statsArray[0]+statsArray[1]));

		// STATS
		for(int i=0; i<length; i++)
		{
			for(int j=0; j<rows; j++)
			{
				// if valid bit is off
				if(cacheBlock[j].getValid() == 0)
				{
					cacheBlock[j].setValid();
					if(readFile.getRW(i) == 0) statsArray[3]++;
					else if(readFile.getRW(i) == 1) statsArray[4]++;
					
					break;
				}
				else
				{
					
				}
			}
		}
		
		System.out.println(statsArray[3] + " _ " + statsArray[4]);
	}

	private long indexOf(long n)
	{
		long index = n >>> offsetSize;
		int mask = (int)Math.pow(2, indexSize) - 1;
		return index & mask;
	}

	private long tagOf(long n)
	{	return n >>> (indexSize + offsetSize);
	}

	public static void main(String args[])
	{
		if( args.length < 2 )
		{
			System.out.println("Usage: java sys1 [filename] [cachesize] {-v} {ic1} {ic2}");
		}
		else
		{
			sys1 s = new sys1(args);
		}
	}
}

/*
	System.out.println(Long.toBinaryString(readFile.getDataAddress(88)));
	System.out.println(Long.toBinaryString(indexOf(readFile.getDataAddress(88))));
	System.out.println(Long.toBinaryString(tagOf(readFile.getDataAddress(88))));
*/