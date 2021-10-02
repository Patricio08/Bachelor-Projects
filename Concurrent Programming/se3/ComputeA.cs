using System;
using System.Threading;
using System.Threading.Tasks;
	
public async Task<R> Compute(T[] elems, R initial){
	Task<R>[] baTasks = new Task<R>[elems.Length + 1];
	baTasks[0] = Task.FromResult(initial);

	for(int i = 1; i < baTasks.Length; i++)
	{
		int j = i;
		baTasks[j] = Task.Run(async () =>
		{
			//Await A() and then B()
			T ba = await B(await A(elems[j]));
			//Only after B(A) will be be requiring the prev value
			//Not associative
			R acc = await baTasks[j - 1];
			return await C(ba, acc);
		});
	}
	//Return the last acc value from last task
	return await baTasks[elems.Length];
}	
