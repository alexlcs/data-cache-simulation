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
	private int 		missPenalty = 80; 

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

		// STATS: 
		//	find data reads and writes
		for(int i=0; i<length; i++)
		{	int t = readFile.getRW(i);
			if(t == 0) statsArray[0]++;
			else statsArray[1]++;
		}
		
		// give indexes to CacheBlock
		for(int i=0; i<cacheBlock.length; i++)
		{
			cacheBlock[i].setIndex(i);
		}
		
		// for every row in readFile
		for(int i=0; i<length; i++)
		{
			int index = indexOf(readFile.getDataAddress(i));
			
			//System.out.println("\n"+ (Long.toBinaryString(readFile.getDataAddress(i))) +"\n"+ Integer.toBinaryString(index));
			
			// if cache hit (tag match)
			if( tagOf(readFile.getDataAddress(i)) == cacheBlock[index].getTag() )
			{
				// if read
				if(readFile.getRW(i) == 0) 
				{ 
					cacheBlock[index].setDirty(0); 
				}
				// if write
				else 
				{ 
					cacheBlock[index].setDirty(1); 
				}
			}
			// else if cache miss
			else
			{
				// if clean miss (valid = 0 OR dirty = 0)
				if(cacheBlock[index].getValid() == 0 || cacheBlock[index].getDirty() == 0)
				{
					cacheBlock[index].setValid();
					cacheBlock[index].setTag(tagOf(readFile.getDataAddress(i))); // DO I SET TAG IN HERE ?
					// if read
					if(readFile.getRW(i) == 0) 
					{ 
						cacheBlock[index].setDirty(0); 
					}
					// if write
					else 
					{ 
						cacheBlock[index].setDirty(1); 
					}
					statsArray[3]++; // rmiss++
				}
				// if dirty miss (dirty = 1)
				else if(cacheBlock[index].getDirty() == 1)
				{
					// System.out.println("dirty miss");
					cacheBlock[index].setTag(tagOf(readFile.getDataAddress(i))); // DO I SET TAG IN HERE ?
					// if read
					if(readFile.getRW(i) == 0) 
					{ 
						cacheBlock[index].setDirty(0); 
						//statsArray[3]++;
					}
					// if write
					else 
					{ 
						cacheBlock[index].setDirty(1); 
						statsArray[7]++;
					}
					statsArray[6]++; // dirty rmiss ++
					statsArray[7]++; // drity wmiss ++
				}
			}
				
		}
		
		// TEST: print entire cache tabel
		for(int i=0; i<cacheBlock.length; i++)
		{
			//cacheBlock[i].print();
		}

		statsArray[5] = statsArray[3]+statsArray[4];
		System.out.println("loads " + statsArray[0] + " | stores " + statsArray[1] + " | total " + (statsArray[0]+statsArray[1]));
		System.out.println("rmiss " + statsArray[3] + " | wmiss " + statsArray[4] + " | total " + statsArray[5] );
		System.out.println("dirty rmiss " + statsArray[6] + " | dirty wmiss " + statsArray[7]);
		System.out.println("bytes read " + statsArray[8] + " | bytes written " + statsArray[9]);
		System.out.println("read time " + statsArray[10] + " | write time " + statsArray[11]);
		System.out.println("total time " + (statsArray[10]+statsArray[11]) );
		System.out.println("miss rate " + ((double)statsArray[5])/(double)statsArray[2] );
	}

	private int indexOf(long n)
	{
		long index = n >>> offsetSize;
		int mask = (int)Math.pow(2, indexSize) - 1;
		return (int)(index & mask);
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