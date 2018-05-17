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

## Bloom Filters

I had some more time, so I started toying around with bloom filters based upon the statement in the instructions
about being able to use probability and predict the probable outcome.  I've done some work with bloom filters in
the past, and they have some interesting uses.  I'm not entirely convinced that they're perfect for the job here,
mostly due to the fact that they add a lot of complexity to the problem that isn't really necessary.

Assuming that the data set gets large though, they do bring down the storage costs a lot, due to not needing to 
store all of the pairs of strings as in the other solutions.  Instead, they store byte arrays representing the 
data set.  This also ends up adding a whole lot of computational complexity due to the bloom filter needing to execute 
a series of hashing algorithms against each entry as well.  So, you're trading some code complexity and compute time for
using less memory.  If we thought that memory was going to be a bottleneck, then this may very well be a good path
to take.

That being said, I was only toying around with it and didn't actually get around to completing it.  
It's here for the sake of showing the possibility, but it probably doesn't actually come up with the right answer 
right now.  Personally, I think this is overkill given the current stated requirements, and I'd much rather prefer 
an exact answer, as opposed to a probable guess.  And, the bloom filter parameters would have to continue to be tweaked, 
to meet whatever requirements we have for hitting the correct amount of probable. 


# How to Run

## Application

Code can be executed through Gradle from the root directory of the project, supplying both the input and output filepaths, like so:
`./gradlew run -Pinput="src/main/resources/input.txt" -Poutput="output.csv"`

If input or output files are not supplied, then defaults will be used.

You can also specify the strategy that you'd like to use, where valid options include BRUTE and GRAPH.  GRAPH is the default.
(As noted above, BLOOM is also an option, but that code isn't fully tested, and might not actually return the 
correct results yet.)

You can also change the occurrence count that we're looking for.  Default is 50.  
If you'd like to change it, pass a `-Plimit=` property to Gradle.   

## Tests

Tests can be executed through Gradle from the root directory of the project, like this:
`./gradlew test`


I likely would have added a few more tests, but my time was a little limited and, quite honestly, I got a little distracted 
having fun with rediscovering bloom filters.