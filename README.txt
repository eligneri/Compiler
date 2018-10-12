CS331 Compilers Fall 2018
Vassar College
Ellis Igneri

To test the lexer, uncomment and run the first section of the main method in the compiler test class. This will analyze the test file Lexer_Test.txt, which I based off of example program 3. Since I have not yet included error recovery, there are a few things you can modify in the test file that will trigger errors that halt the program, these are indicated in comments.

To test the parser, uncomment and run the second section of the main method in CompilerTest. This will parse the sample project.

To test the symbol table, run the main method in CompilerTest. This will load 6 values into the table and then dump the stack.

10/12/18
-Added Symbol Table and requisite subclasses
-Removed capital letters from allowed characters

9/30/18
-Modified Token class to include line number
-Fixed issue where Lexer wouldn't tokenize single characters right before end of file
-Fixed issue where Lexer wouldn't tokenize numbers of the form XeY correctly
