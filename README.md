# Monotonic Cycle DP
This program is able to use dynamic program to calculate the optimal montone cycle path for a given set of vertices

## Usage
```
javac *.java
java MonotoneDP [input-file] 
```

## Example
The input is a list of X,Y coordinats and the output is an ordered list along the optimal monotonic cycle.
```
➜  java MonotoneDP sevenPt.txt
Optimal DP monotone cycle length: 25.5840
0.0000,     6.0000
2.0000,     3.0000         3.6056
5.0000,     4.0000         6.7678
7.0000,     5.0000         9.0039
8.0000,     2.0000        12.1662
6.0000,     1.0000        14.4022
1.0000,     0.0000        19.5013
                          25.5840
```
