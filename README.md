# WebCrawler by Tim Remple

A collection of documentation.

#####Key decisions:
* Implement this using Java, since that is quite common; 
and Java 8 since no rationale for using a later version.
* Am implementing this as a simple command line app.
* Given command line packaging, no major need to use something like the Spring framework.
  * Could have used Spring as a dependency injection framework, but strikes author as overkill.
* Decided to use _jsoup_ to do HTML page retrieval and parsing.
  * Have used _jsoup_ in other projects, both at work and hobby.
  * _Could_ try to retrieve a page as a String, and then parse links to other pages and media out of it.
  * In practice, that is extremely problematic to try to implement, for example, with reguar expressions.
  * Since _jsoup_ is pretty widely used, it is relaively well vetted; 
  MUCH more so than some regular expression "hack."
* Decided to limit pages visited to 50,000; a standard limit in XML sitemaps.
* Decided to limit the depth of pages examined to at most 9; meaning pages at a depth of 9 will not be examined.
  
#####Output format:
* The root of the domain is listed first:

  `0 <URL of domain>`
  
* Subordinate pages are shown by level of depth:

  `1  .  <URL of first sub-page>`
  
  `2  .  .  <URL of first sub-sub-page>`
  
  `3  .  .  .  <URL of first sub-sub-sub page>`
  
  `1  .  <URL of second sub-page>`

* For a given page, before the sub-pages that are in the domain are listed, links to pages outside the domain are shown:

  `0  : <URL of page outside domain>`
  
  `0  : <another URL of page outside domain>`
  
* Next, links to media are shown:

  `0  * <URL to a media file>`
  
  `0  * <URL to another media file>`
  
* Then, sub-pages are listed.

#####Building and Testing
`mvn clean compile test`

Then to test command line error processing:

`cd src/test/shell`

`./test_main.sh`

Tests are sufficient to cover 95% of lines in `CrawlChildPages` and 100% in `DisplayResults` classes.
91% of lines in `ProcessPage` class were covered; only lines 69 and 78 not covered.

#####Operation
From root directory of project:

`mvn -q exec:java -Dexec.mainClass="us.remple.Crawler" -Dexec.args="https://www.domain.com"`
