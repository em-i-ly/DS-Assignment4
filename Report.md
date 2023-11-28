Assignment 4
------------

# Team Members

# GitHub link to your (forked) repository (if submitting through GitHub)
https://github.com/em-i-ly/DS-Assignment4.git


# Task 4

1. What is the causal consistency? Explain it using the happened-before relation.
> Causal consistency means keeping a clear cause-and-effect connection between events. It's like making sure events happen in a meaningful order. Lamport timestamps are important for this. They're like numbers that help organize events in different places. These timestamps not only keep events in order locally but also create a global order. This makes sure that events, knowing about each other, always happen in a consistent order, creating a cause-and-effect relationship. Lamport timestamps work by capturing both local order and a broader historical context, making it effective for arranging events in a way that keeps causality in distributed systems.

2. You are responsible for designing a distributed system that maintains a partial ordering of operations on a data store (for instance, maintaining a time-series log database receiving entries from multiple independent processes/sensors with minimum or no concurrency). When would you choose Lamport timestamps over vector clocks? Explain your argument. 
   What are the design objectives you can meet with both?
> Vector clocks and Lamport timestamps serve different purposes in designing a distributed system. Vector clocks are more versatile as they capture more information than Lamport timestamps. They help order events across nodes based on local order, history, and even real-time ordering of non-concurrent events. They uniquely identify events and can indicate when an event is the result of multiple concurrent events, which is helpful for merging data from different client events. However, vector clocks don't explicitly capture causality. On the other hand, Lamport timestamps are good for arranging events in historical order and providing a clear cause-and-effect relationship. They explicitly capture the "happened-before" relationship. But, Lamport timestamps have drawbacks, such as not deterministically achieving a total order of events and not preserving order in real-time for non-concurrent events. If real-time order preservation and handling concurrent events with merged branches is needed, then vector clocks may be a better choice. On the other hand, if maintaining a strict historical order and a clear causal relationship between events is crucial, then Lamport timestamps might be the preferred option.

3. Vector clocks are an extension of the Lamport timestamp algorithm. However, scaling a vector clock to handle multiple processes can be challenging. Propose some solutions to this and explain your argument.
> Vector clocks, which build on the Lamport timestamp idea, face a challenge when dealing with lots of processes in a system. The issue is that as you add more processes, the size of vector clocks grows a lot. This can use up a ton of bandwidth, storage, and computing power, especially in big distributed systems. To make vector clocks work better at a larger scale, different techniques can be used. For instance, you might compress them using techniques like bitmaps or sparse vectors. Another idea is to prune, which means getting rid of unnecessary parts of the vector clocks.
> 
> Sources used: chatGPT to help with comparator
> https://www.java67.com/2015/01/how-to-sort-hashmap-in-java-based-on.html#:~:text=In%20order%20to%20sort%20HashMap,passing%20your%20customized%20value%20comparator.
> https://www.java67.com/2014/11/java-8-comparator-example-using-lambda-expression.html
> https://stackoverflow.com/questions/42172825/how-to-sort-a-hashmaps-entries-by-comparing-values-where-each-value-is-an-int
> https://www.geeksforgeeks.org/compare-two-hashmap-objects-in-java/