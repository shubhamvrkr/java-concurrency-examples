# Java Concurrency Examples
Note: This examples are taken from mastering concurrency programming with java 8 book.
## Source contents
The source code covers example of different synchronization mechanism, examples using different concurrency frameworks such as executor, fork/join and streams.
Each package in the source resembles different concurrency topic supported in java 8. The source code also contains relevant data files used for each example. some data files might be compressesed, so kindly unzip it before running the example.
### Below are the description of each topic
- <b>synchronization</b> : Demonstrate basic synchronization mechanism using synchronization keyword in java
- <b>semaphore</b>: Demonstrate basic synchronization mechanism using semaphore in java where you can set how many threads can simultaneously acquire the lock on the object.
- <b>locks</b> :  Demonstrate usage of diffrent types of locks such as ReentrantLock, ReentrantReadWriteLock, StampedLock, CountDownLatch, CyclicBarrier, Phaser. Read the comments for more details.
- <b>chapter2.knearestneighbour</b> : Demonstrate usage of executor frameworks in java using java's default executor.
- <b>chapter3.executoradvances</b> : Demonstrate advanced usage of executor service by extending default executor and overriding necessary functions of executor such as beforeExecute(), afterExecute(), newTaskFor().
- <b>chapter4.callableandfutures</b> : Demonstrate usage of callable and futures objects in java 8. The future task/tasks are sent to the executor frameworks to execute. Also demonstrate usage of ExecutorCompletionSerivce. It provides a mechanism to allow decoupling production of task and consumption of the result of those task.
- <b>chapter5.phaseradvanced</b> : Demonstrate usage of phaser class from java 8 feature. This is used to execute multiple threads in phases. Like all threads wait for other to complete phase 1 before proceeding to phase two. Example 2 shows how we can execute some logic after finishing of each phase by threads by overriding onAdvance method of Phaser class.
- <b>chapter6.forkandjoin</b> : Demonstrate usage of fork and join framework in java. This framework is best suited for divide and conquer algorithms where task is divided into multiple sub task and parent task waits for their completion. This wait is "non blocking" as it uses work stealing algorithm so the parent thread will take some pending task from the queue and execute (implementation is based on continuation fetching without context switch). Example  1 demonstrate usage of RecursiveAction class that doesnt return a result, Example 2 demonstrate usage of RecursiveTask class that returns a result. Example 3 demonstrate usage of CountedCompleter class that triggers other task once they are completed.
In a lot of cases CountedCompleter won't be programmed to return any value. But if it has to return any, the getRawResult(T) method should be overridden.<br>
<i>Good reads on fork/join internal</i>:
  - http://www.coopsoft.com/ar/CalamityArticle.html#faulty
  - http://moi.vonos.net/java/forkjoin/
- <b>chapter7.mapreduce</b> : Demonstrate usage of streams  frameworks in java. Examples Demonstrate usage of reduce, for each where one can provide their own implementation of accumulator and combiner. check multipleFilterDataPredicate funtion in ConcurrentStatistics class for implementing custom predicate that can be passed to filter function of streams.
- <b>chapter8.mapcollect</b> : Demonstrate usage of streams  frameworks in java. Examples Demonstrate usage of collect, for each where one can provide their own implementation of accumulator and combiner. All 3 version of collect is being covered with custom implementation of accumulator and combiner. check bidirectionalCommonContacts funtion in ConcurrentSocialNetwork class for implementing custom collector for collect.
