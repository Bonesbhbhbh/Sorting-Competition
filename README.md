# Sorting-Competition-Materials-2024
Materials and results for the UMN Morris CSci 3501 sorting competition Fall 2024

# Table of contents
* [Goal of the competition](#goal)
* [The data](#data)
* [How is the data generated](#generating)
* [How do you need to sort the data](#sortingRules)
* [Setup for sorting](#setup)
* [Submision deadlines](#deadlines)
* [Scoring](#scoring)
* [Results of the final competition](#final)
* [Presentations](#presentation)


## Goal of the competition <a name="goal"></a>

The Sorting Competition is a multi-lab exercise on developing the fastest sorting algorithm for a given type of data. By "fast" we mean the actual running time and not the Big-Theta approximation. The solutions are developed in Java and will be ran on a single processor.

## The data  <a name="data"></a>

You are sorting permutations of numeric elements (for a reminder of the definition of *permuation* see below).  

The data will be provided as a text file-- each line will contain a comma separated list of values ranging from 1 to $n$ where $n$ is the number of elements in that list.  In any given line there will no repeated values.  Not all lines will have the same number of elements.  The number of elements in each line will produced using the ceiling of an **exponential distribution** with a **rate parameter** ranging from 0.001 to 0.1.  (If that doesn't mean much to you, that's okay-- it just means that shorter sequences are more likely than longer sequences and *really long* sequences are vanishingly rare... feel free to probe the data to get a better idea of its characteristics) 

The output should be a similar text file, but with the sequences ordered using the procedure outlined below.  A text file could have anywhere from 100,000 to 1,000,000 entries

For example one might see the first few lines in the data file as

```
3,1,2
1,2,3
1,5,3,2,4
    .
    .
    .
```

## Comparing Sequences

Two sequences, $A = (a_1, a_2, \ldots, a_n)$, and $B = (b_1, b_2, \ldots, b_m)$ are ordered as follows:

1. $A < B$ if $a_1 < b_1$
2. $A > B$ if $a_1 > b_1$
3. If $a_1 = b_1$ then repeat these steps using the next entries in the sequences (ie using $a_2 and b_2$)

As we continue in this fashion only one of three things can occur:

1. We encounter $a_i \ne b_i$, in which case $A\lt B$ if $a_i \lt b_i$ and $A\gt B$ if $a_i \gt b_i$
2. The two sequences are of the same length and have all the same values (this means $A$=$B$)
3. The shorter sequence is identical to the longer for all the *shorter* sequences entries. (this is a tie-- see below for tie-breaking)

### Tie breaking

In the event of a tie we compare the number of **even** values in A and in B.  The sequence with the lowest number of evens comes before the one with the larger number of evens.

In the event of ANOTHER tie, we compare the number of **odd** values in $A$ and $B$.  The one with the **highest** number of odds comes *before* the one with the lower number of odds.

### Examples

Let's see this in action:

* Comparing $A=(1,3,2,4)$ to $B=(1,2,3)$

1. **First Position:** We start by comparing their first entries. Both entries are 1 so we move to the next position
2. **Second position:** Here we see that $3 > 2$.  

**Therefore: $A > B$**

* Comparing $A=(1,3,2,4)$ to $B=(1,3,2)$

1. **First Position:** We start by comparing their first entries. Both entries are 1 so we move to the next position
2. **Second Position:** Both entries are 3 so we move to the next position
3. **Third Position:** Both entries are 2.  We can't move to the entry because $B$ has run out of entries.
4. **First Tie-Breaker:** Sequence $A$ has 2 even numbers, while sequence $B$ only has 1.  

**Therefore: $A>B$**

* Comparing $A=(1,3,2,4,5)$ to $B=(1,3,2,4)$

1. **First Position:** Both entries are 1 so we move to the next position
2. **Second Position:** Both entries are 3 so we move to the next position
3. **Third Position:** Both entries are 2 so we move to the next position
4. **Fourth Postion:** Both entries are 4, but we can't move to the entry because $B$ has run out of entries.
5. **First Tie-Breaker:** Sequence $A$ has 2 even numbers, and sequence $B$ also has 2 even numbers.  Thus we must move to the second tie-breaker.
6. **Second Tie-Breaker:** Sequence $A$ has 3 odd numbers while sequence $B$ has 2.  

**Therefore: $A < B$** (remember the second tie-breaker is won by the sequence with the *fewest* number of odd entries)

### Permutations

Here's a brief reminder of the meaning of permutation:

A **permutation** of a set is a *specific ordering* of the elements of that set.  So if, for example, the set was $\{1, 2, 3, \}$ then there are 6 distinct permutations of that set:

1. `123`
2. `132`
3. `213`
4. `231`
5. `312`
6. `321`


## Setup for sorting <a name="setup"></a>

The file [Group0.java](src/Group0.java) provides a template for the setup for your solution. Your class will be called `GroupN`, where `N` is the group number that is assigned to your group. The template class runs the sorting method once before the timing for the [JVM warmup](https://www.ibm.com/developerworks/library/j-jtp12214/index.html). It also pauses for 10ms before the actual test to let any leftover I/O or garbage collection to finish. Since the warmup and the actual sorting are done on the same array (for no reason other than simplicity), the array is cloned from the same input data. 

The data reading, the array cloning, the warmup sorting, and writing out the output are all outside of the timed portion of the method, and thus do not affect the total time. 

You may **not** use any **global variables** that **depend on your data**. You may, however, have global constants that are initialized to fixed values (no computation!) before the data is being read and stay the same throughout the run. These constants may be arrays of no more than 100 `long` numbers or equivalent amount of memory. For instance, if you are storing an array of objects that contain two `long` fields, you can only have 50 of them. 
We consider one `long` to be the same as two `int` numbers, so you can store an array of 200 `int` numbers. 
Strings use 16 bits (half of the size of an `int`) for *each* character and 16 bytes (128 bits) for the memory reference and other info for the string *itself*.  
If in doubt about specific cases, please discuss with me. 

The method in the [Group0.java](src/Group0.java) files that you may modify is the `sort` method. It must take an array of arrays of Integers (or ints) (for example, `int[][]`). 
The method can sort in place (thus be `void`) or return type of the method can be what it is now or return the result in an array of another type.
If you are returning an array, the following rules have to be followed:
* Your `sort` method return type needs to be changed to whatever  array you are returning. Consequently you would need to change the call in `main` to store the resulting array. 
* Your return type has to be an array (not an array list!) and it has to have one element per element of the original array. That element must contain the integer value(s) of the given element. 
For example, you may create your own class (I will call it `Data` as an example) that has the `int` value of a number plus some other fields. Then you will be returning an array of `Data`. 
You may not, however, create an array of just binary representations of all numbers, return that array, and convert back to decimal upon printing.   
* If you are returning a different type of an array, such as `Data`, you need to supply a method to write out your resulting array into a file. The method will access the `int []` value field using a `get` method or directly (if it's accessible) and write it to a file. 
The file has to be exactly the same as in the prototype implementation; they will be compared using `diff` system command. 

If you are not changing the return type, you don't need to modify anything other than `sort` method and any methods/classes called from it. 

Even though you are not modifying anything other than the `sort` method, you still need to submit your entire class: copy the template, rename the Java class to your group number, and change the`sort` method. You may use supplementary classes, just don't forget to submit them. Make sure to add your names in comments when you submit. 

Your program must print **the only value**, which is the **time** (as it currently does). Any other printed output disqualifies your submission. If you use test prints, make sure to turn them off for submission. 

**Important:** if the sorting times may be too small to distinguish groups based on just one run of the sorting, so I may loop over the sorting section multiple times. If this is the case, I will let you know no later than a day after the preliminary competition and will modify `Group0` file accordingly.  

## Scoring <a name="scoring"></a>

The programs are tested on a few (between 1 and 3) data sets. For each data set each group's program is run three times, the median value counts. The groups are ordered by their median score for each data file and assigned places, from 1 to N. 

The final score is given by the sum of places for all data sets. If the sum of places is equal for two groups, the sum of median times for all the runs resolves the tie. So if one group was first for one data set and third for the other one (2 sets total being run), it scored better than a group that was third for the first data set and second for the other. However, if one group was first for the first set and third for the other one, and the second group was second in both, the sum of times determines which one of them won since the sum of places is the same (1 + 3 = 2 + 2). 

If a program has a compilation or a runtime error, doesn't sort correctly, or prints anything other than the total time in milliseconds, it gets a penalty of 1000000ms for that run. 

## System specs <a name="specs"></a>

The language used is Java that's installed in the CSci lab. It's ran on a single CPU core. 

I will post a script for running this program (with a correctness check and all), but for now a couple of things to know: run your program out of `/tmp` directory to avoid overhead of communications with the file server, and pin your program to a single core, i.e. run it like this:
``taskset -c 0 java GroupN``

##  Submision deadlines <a name="deadlines"></a>

The first preliminary competition will be run on Thursday October 24th in the lab. The purpose of it is mostly to check the correctness and to get a sense for your timing. 

The second preliminary round will be in the lab on Monday November 4th (due Sunday Nov 3rd) 

The final competition will be on Thursday November 14th in the lab (due Wed Nov 13th)

The dates for other related assignments (code review and presentations) will be announced later. 

## Results <a name="final"></a>
Here is where the results of the final competition will go:

// What Each Function
// main() Method:

// Coordinates everything—reads input, runs sorting, measures time, and saves the output.
// readData() Method:

// Reads the sequences from the input file and stores them as a 2D array of integers.
// sort() Method:

// Organizes sequences into buckets, sorts them, and combines them back into a sorted list.
// SortingCompetitionComparator Class:

// Handles custom sorting logic (like tie-breaking with even/odd numbers).
// writeOutResult() Method:

// Saves the sorted sequences to an output file.
