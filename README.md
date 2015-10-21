# GraphSearch

Implementation of the different graph search algorithms v.i.z BFS, DFS and UCS to determine the path for the waterflow through the
pipes
|------|
|Input:|
|------|

First line of this file represents the number of test cases. The next line will be the beginning of the 1st test case. 
Each test case ends with an empty line. Each test case consists of the following lines:

<task> algorithm that you are supposed to use for this case
<source> name of the source node
<destinations> names of the destination nodes
<middle nodes> names of the middle nodes
<#pipes> represents the number of pipes
<graph> represents start-end nodes, lengths and off-times of pipes
<start-time> the time when water will start flowing

Task:
-----
This field indicates which algorithm you are going to use to solve the problem. The input is either “BFS”, “DFS” or “UCS” (without the double quotes)
There are multiple definitions for these algorithms. In this homework we ask you to use the definition in subsection 3.4 of “Artificial Intelligence, A modern Approach, 3rd edition” (starting at page 81). The reference implementation used to create the problem solutions will use these algorithms. Hence beware that using different algorithms may result in incorrect solutions.

Source:
-------
This is the name of the source of water.

Destinations:
-------------
This is a space separated line consisting of names of the destination (goal) nodes.

Middle-nodes:
-------------
This is a space separated line consisting of the middle nodes, i.e. nodes that are neither source nor destination. (It may be an empty line which means there are no middle nodes.)

#Pipes:
-------
This number represents the number of pipes in the system.

Graph:
------
This section contains #pipes number of lines. Each line in this section represents one pipe of the system. Format of each line is as following: (There is one space between each field)
<start> <end> <length> <#off-periods> < period1 > …. <periodn>
Example: S E 10 3 10-12 15-16 25-29
It means that this pipe starts from point S , ends in point E , has the length 10, and it has 3 off-periods. It is not working from time 10 to 12, 15 to 16 and 25 to 29. Period 10-12 means that if we are at time 10, 11 or 12 we cannot use this pipe as the next pipe. Some pipes may always work. In that case, the 4th field for these pipes will be 0. Please note that the pipes are unidirectional, i.e. for a pipe that has starting point A and ending point B, the water can flow from A to B only and not in the reverse direction.
The pipe length will be 1 for both BFS and DFS (i.e. pipe length is ignored by these algorithms and is always assumed to be 1). Also, ignore the off-periods for these algorithms, i.e. when using BFS-DFS; assume that all pipes work all the time.

Start-time:
----------
This is an integer denoting the time when water will start flowing from the source point.

Ex:
2
UCS
AA
BA
CA DA
3
AA BA 10 1 1-2
AA CA 2 2 3-4 5-6
CA BA 4 0
0

UCS
A
B
C D
3
A B 10 1 1-2
A C 2 2 3-4 5-6
C B 4 0
0


|-------|
|Output:|
|-------|

BA 6
B 6
