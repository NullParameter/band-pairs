# Problem

Given an input file, where each line consists of a comma separated list of band names, 
find each pair of bands that occurs twice on a single line at least fifty times throughout
the input file.  Then output those pairs of bands to another file.

# Solution

## Brute Force

I started by implementing a simple brute force solution, inline in the main method, 
that took each line, concatenated each pair of Bands, and then used that as the key to a Map, 
where the value was an integer that got incremented for each time that the pair is seen.  
It's simple, and straight forward, but definitely not storage efficient, because it would 
store a string of essentially every band pair that existed in the entire file.  

When processing the file, Map lookups and inserts are fast, so no real concerns there.  
The sort and pairing are much more heavy, but on this size of data set they're negligible.

One downfall with this implementation is that it isn't super testable outside of being tested as 
a whole, because everything is just kind of there, in a few lines.  That being said, if the 
implementation is small enough, then the code should be easy to reason about, and in depth tests 
aren't quite necessary (though still desirable).   

If I knew that this was the extent to which the data set would ever get, I'd probably just end here, with a 
working solution, and only come back later if we decided that it was too inefficient for whatever reason.

## Undirected Graph

Since I had a lot more time, I decided to take a second look at the problem and realized that with
each Band being connected to some number of other Bands, we were essentially just building a Graph,
where each Band was a Node in the graph.  And from there, the occurrences of the connection were essentially 
the weight of their connection in the Graph.

I considered implementing a whole Graph, with Nodes and Edges being well defined entities, but decided
that this was overkill for the task at hand.  Instead I ended up linking the nodes using a couple of maps.
Looking at the data as a graph definitely brought a little more clarity to the problem.  However, it
didn't end up really saving any space in memory, because we're still storing each pair of bands.

This one has a few more moving parts, that probably don't add a whole lot more value over the simple
brute force solution.  And, since we're still just doing map lookups for everything, we're still
hitting almost the exact same performance.

This was the point at which I added the strategy pattern, just because I felt like keeping both implementations.
This also made it a bit simpler to test the main class independently of the actual logic for finding band pairs.



# How to Run

Code can be executed through Gradle from the root directory of the project, supplying both the input and output filepaths, like so:
`./gradlew run -Pinput="src/main/resources/input.txt" -Poutput="output.csv"`

If input or output files are not supplied, then defaults will be used.

You can also specify the strategy that you'd like to use, where valid options include BRUTE or GRAPH.  GRAPH is the default.