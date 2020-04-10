# cs361-NFA
# Project 2: NFA

* Author: Chris Miller and Zachary Gillenwater
* Class: CS361 Section 002
* Semester: Spring 2020

## Overview

This program simulates a Non-Deterministic Automata or NFA, and can create a DFA using an inputted NFA.

## Compiling and Using

Running the program can be done through the NFADriver.java file.

To compile, execute the following command in the main project directory:
```
$ javac fa/dfa/NFADriver.java
```

Run the compiled class with the command:
```
$ java fa.dfa.NFADriver ./tests/p2tc0.txt  
$ java fa.dfa.NFADriver ./tests/p2tc1.txt 
$ java fa.dfa.NFADriver ./tests/p2tc2.txt 
$ java fa.dfa.NFADriver ./tests/p2tc3.txt 

```
For a custom made test file, you will need the end, start, and other states, then the transition table. 
Then it will tell if the provided strings are accepted.  

## Discussion

For this project, it was relativally easy to do once we properly understood the process of the search and the queue.
We initially had issues with the generation of the new DFA states, but figured it out relativally quickly.

## Testing

We ran the NFADriver class given to us with the p2tc files, as well as some custom made test string files.

## Extra Credit

N/A

## Sources used

https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/

----------
This README template is using Markdown. To preview your README output, you can copy your file contents to a Markdown editor/previewer such as [https://stackedit.io/editor](https://stackedit.io/editor).
