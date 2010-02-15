DBTimetable 2.0
February 15, 2010

Program initiated by:
Emil Chan <http://dbtable.co.nr/>

Implementation revised by:
Kenneth Anguish <http://github.com/kenguish/>

This DBTimetable implementation is designed to be downloaded to a cellphone 
over-the-air by clicking on a JAD file.

The development environment is:
Windows 2003 Server
Eclipse 3.5 
DSDP Mobile Tools for Java  (MTJ) <http://www.eclipse.org/dsdp/mtj/> 
JDK 1.6.0_13

If you pull the sources from Github <https://github.com/kenguish/DBTimetable/tree>,
this directory would be the Eclipse workspace the program developed on with the 
following folders:

res/	- Timetable files in .txt and the outline.json file containing the route info
src/	- The actual Java source code of the program. JSON library is used to parse
		  the route information so as not to hard coded the timetable information for
		  lazy updates in future

If you are interested to extend the code, please feel free to contact the project
maintainer on Github.


