# javacc-perf-diag

JavaCC is a very good parser generator. But it contains tools used at the wrong places results in very inperformant parsers. This project aims to deliver some statistic identifiying those hotspots of a parser.

The tool uses *AspectJ* to link into an existing parser. The logging possibilities of JavaCC are not used.

Some statistics were delivered combining some values of a real parsing process. Mainly you are able to identify the productions with the most complex lookahead. *Excessive lookaheads are one of the main reasons to produce poor parser performance.*

## Example JSqlParser

The motivation developing this project were some very inperformant parsing processes of JSqlParser. 
