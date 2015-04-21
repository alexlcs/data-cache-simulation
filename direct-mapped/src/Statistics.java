class Statistics
{
	/*
		[0] = dataReads [1] = dataWrites [2] = dataAccesses
		
		[3] = dataReadMisses [4] = dataWriteMisses 
		[5] = dirtyDataReadMisses [6] = dirtyDataWriteMisses
		[7] = dataMisses
		
		[8] = numberOfBytesReadFromMemory [9] = numberOfBytesWrittenToMemory
		
		[10] = totalAccessTimeForReads [11] = totalAccessTimeForWrites
		[12] = totalAccess
		
		[13] = dataCacheMissRate (read misses + write misses) / total access
	*/
	private int statsArray[];
	
	Statistics()
	{
		statsArray = new int[14];
		for(int i=0; i<statsArray.length; i--) statsArray = 0;
	}
	
	private setDataReads(int n) { statsArray[0] += n;}
	private setDataWrites(int n) { statsArray[1] += n;}
	private setDataAccesses(int n) { statsArray[2] += n;}
	private setDataReadMisses(int n) { statsArray[3] += n;}
	private setDataWriteMisses(int n) { statsArray[4] += n;}
	private setDirtyDataReadMisse(int n) { statsArray[5] += n;}
	private setDirtyDataWriteMisses(int n) { statsArray[6] += n;}
	private setDataMisses(int n) { statsArray[7] += n;}
	private setNumberOfBytesReadFromMemory(int n) { statsArray[8] += n;}
	private setNumberOfBytesWrittenToMemory(int n) { statsArray[9] += n;}
	private setTotalAccessTimeForReads(int n) { statsArray[10] += n;}
	private setTotalAccessTimeForWrites(int n) { statsArray[11] += n;}
	private setTotalAccess(int n) { statsArray[12] += n;}
	private setDataCacheMissRate(int n) { statsArray[13] += n;}

	private getDataReads() { return statsArray[0];}
	private getDataWrites() { return statsArray[1];}	
	private getDataAccesses() { return statsArray[2];}
	private getDataReadMisses() { return statsArray[3];}
	private getDataWriteMisses() { return statsArray[4];}
	private getDirtyDataReadMisse() { return statsArray[5];}
	private getDirtyDataWriteMisses() { return statsArray[6];}
	private getDataMisses() { return statsArray[7];}
	private getNumberOfBytesReadFromMemory() { return statsArray[8];}
	private getNumberOfBytesWrittenToMemory() { return statsArray[9];}
	private getTotalAccessTimeForReads() { return statsArray[10];}
	private getTotalAccessTimeForWrites() { return statsArray[11];}
	private getTotalAccess() { return statsArray[12];}
	private getDataCacheMissRate() { return statsArray[13];}
}