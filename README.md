# javacc-perf-diag

JavaCC is a very good parser generator. But it contains tools used at the wrong places results in very inperformant parsers. This project aims to deliver some statistic identifiying those hotspots of a parser.

The tool uses *AspectJ* to link into an existing parser without using the possibility of logging some parser results.

Some statistics were delivered combining some values of a real parsing process.

## Example JSqlParser

The motivation developing this project were some very inperformant parsing processes of JSqlParser. 
