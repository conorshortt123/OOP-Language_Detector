-----------------------------------------
A Multithreaded Language Detector: README
-----------------------------------------
Author: Conor Shortt
Module: Object Oriented Programming
-----------------------------------------

PROJECT OUTLINE:
------------------------------------------------------------------------------------
This API utilizes ngrams to rapidly compare a query text file against a subject text
database to determine the language of the query file.
------------------------------------------------------------------------------------

MAIN FEATURES & DESIGN

----------------------------------------------------------------------------------------------------
Runner:
The runner class calls the menu.runMenu function and begins the program.

Menu:
The API contains a console-based menu-driven UI which allows the user to input the names
of the language data set (WiLi), the query text file, and the length of the k-mers the user
wishes to be used when parsing the data sets.

Parser:
The data sets are then passed to the parser class. The language data set is parsed using
concurrent threading which allows for much quicker and more efficient parsing. The query
file is subsequently parsed using a seperate method. Both data sets are broken into n-grams
and then added to their respective maps/databases.

Database:
The database class is the brain of the API. It contains all of the necessary methods to create
the maps, utilizing the comparable interface, concurrent hash map, etc. The database contains
the method add which allows the k-mers that are being parsed to be concurrently added to
a map, their frequencies are also taken into account. The database is then resized and sorted
in terms of the top 300 language entries by using the respective methods.

LanguageEntry:
The LanguageEntry class contains all the necessary get and set methods for each entry, it also
implements the comparable interface to compare frequencies and set/get rank.

Language:
The language enum is a collection of the 235 languages from the language data set. It simplifies the
manipulation of the language data.
----------------------------------------------------------------------------------------------------

EXTRAS

----------------------------------------------------------------------------------------------------
- User can choose to input a manual number for the size of each k-mer.
- Before program ends, user can choose to read in another query file. As the language data set has
already been parsed, this operation is extremely quick.