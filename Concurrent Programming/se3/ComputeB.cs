using System;
using System.Threading;
using System.Threading.Tasks;


private readonly int maxPermits = 10;

public async Task<R> Compute(T[] elems, R initial){
	Task<R>[] baTasks = new Task<R>[elems.Length + 1];
	SemaphoreAsync semaphore = new SemaphoreAsync(0, maxPermits);
	CancellationTokenSource tokenSource = new CancellationTokenSource();
	baTasks[0] = Task.FromResult(initial);
	

	for(int i = 1; i < baTasks.Length && i < maxPermits + 1; i++)
	{
		int j = i;
		baTasks[j] = Task.Run(async () =>
		{
			await semaphore.AcquireAsync(1, tokenSource.Token)
			//Await A() and then B()
			T a = await A(elems[j]);
			
			//Release semaphore
			semaphore.Release(1);
			T ba = await B(a);
			//Only after B(A) will be be requiring the prev value
			//Not associative
			R acc = await baTasks[j - 1];
			return await C(ba, acc);
		});
	}
	//Return the last acc value from last task
	return await baTasks[elems.Length];
}	
