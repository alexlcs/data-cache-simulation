import java.util.Scanner;
import java.io.*;

class ReadFile
{
	private File file;
	private Scanner scanner;
	private int rw[];			// read = 0; write = 1
	private long dataAddress[];	// data address
	private int numOfBytes[];	// number of byts read or written
	private int index;

	ReadFile(String filename)
	{
		// instantiate all data fields
		file = new File(filename);
		try
		{	scanner = new Scanner(file);
		}catch(Exception e)
		{	System.out.println("File not found.");
			System.exit(0);
		}
		rw			= new int[100000];
		dataAddress	= new long[100000];
		numOfBytes	= new int[100000];
		index = 0;

		// read file into arrays
		while(scanner.hasNext())
		{
			scanner.next();		// skip instruction addres
			rw[index]			= ( scanner.next().equals("R") ? 0:1 );
			dataAddress[index]	= hexstringToInt(scanner.next());
			numOfBytes[index++]	= Integer.parseInt(scanner.next());
			scanner.next(); 	// skip data
			//System.out.println(index +": "+ rw[index-1] +" "+ dataAddress[index-1] +" "+ numOfBytes[index-1]);
		}
		scanner.close();
	}

	public int getRW(int n)
	{
		return rw[n];
	}

	public long getDataAddress(int n)
	{
		return dataAddress[n];
	}

	public int getNumOfBytes(int n)
	{
		return numOfBytes[n];
	}

	private long hexstringToInt(String s)
	{
		// convert string to array of ints
		long arr[] = new long[8];
		long value = 0;
		int counter = 0;

		// initialize array with 0
		for(int i=0; i<arr.length; i++) arr[i] = 0;

		// store string into array backwords until see 'x'
		counter = s.length()-1;
		for(int i=7; i>=0; i--)
		{	
			arr[i] = Long.parseLong(("" + s.charAt(counter)), 16);
			counter--;
			if(s.charAt(counter) == 'x') break;
		}

		// add values in array into a number
		counter = 1;
		for(int i=arr.length-1; i>=0; i--)
		{
			value += arr[i]*counter;
			counter *= 16;
		}
		return value;
	}
	
}