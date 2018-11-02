CS331 Compilers Fall 2018
Vassar College
Ellis Igneri

To test the lexer, uncomment and run the first section of the main method in the compiler test class. This will analyze the test file Lexer_Test.txt, which I based off of example program 3. Since I have not yet included error recovery, there are a few things you can modify in the test file that will trigger errors that halt the program, these are indicated in comments.

The parser is currently being tested as part of testing semantic actions.

To test the symbol table, uncomment and run the third section of the main method in CompilerTest. This will load 6 values into the table and then dump the stack.

To test semantic actions, run main method in CompilerTest. NOTE: I tested all provided sample files and ran into problems with file 6. I think the sample files take advantage of actions we haven't implemented yet as there was a "bge" TVI code and nowhere in the specifications does bge get generated.
If you would like to test error handling, uncomment lines in phase2-1_ns.vas.txt

11/2/18
-Added Semantic Actions for basic expressions

10/26/18
-Began implementing semantic actions
-Updated Symbol Table objects with more robust debugging information
-Added augmented grammar

10/12/18
-Added Symbol Table and requisite subclasses
-Removed capital letters from allowed characters

9/30/18
-Modified Token class to include line number
-Fixed issue where Lexer wouldn't tokenize single characters right before end of file
-Fixed issue where Lexer wouldn't tokenize numbers of the form XeY correctly
