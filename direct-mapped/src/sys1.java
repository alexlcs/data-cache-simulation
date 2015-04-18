import java.io.*;

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
	private Cache		cacheBlock[];	// cache
	private int			blockSize = 16;	// number of bytes in one block for data (HARDCODED HERE)

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
		//System.out.println("offset: " + offsetSize + " | index: " + indexSize + " | tag: " + tagSize);

		// read file into arrays
		System.out.println("Calculating...");
		readFile = new ReadFile(filename);

		System.out.println(readFile.getRW(3) +" "+ readFile.getDataAddress(3) +" "+ readFile.getNumOfBytes(3));
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