# jsoup-scraper #

Simple multi-thread scraper implementation to extract data into csv file

### What is this repository for? ###

* Quick summary
This project is used to scrape a website to retrieve records and save it in csv format.
It is implemented to support parallel execution with multiple threads.

* Version
1.0.0 - initial implementation

### How do I get set up? ###

* Summary of set up

* Configuration


* Dependencies
org.jsoup:jsoup			-> used for html parsing
com.opencsv:opencsv		-> used for writing to csv file
org.apache.commons:commons-lang3-> used for working with string

* Local Deployment instructions
  mvn install
  mvn exec:java -Dscrape.initialRecord=870801 -Dscrape.recordsCount=5000 -Dscrape.threadCount=50

Note: 
  scrape.initialRecord <- what is the initial record number
  scrape.recordsCount <- how many records to be scraped
  scrape.threadCount <- how many threads to be run in parellel

* Deployment to aws linux machine and running scraper
1. Change permission of pen file and ssh into server
	chmod 400 jeevan1.pem
	ssh -i <pemfile> <username>@<ipaddress>

2. Make new directory and cd into that directory
	mkdir scraper
	cd scraper

3. update all packages and install java and maven
	sudo apt-get update	
	
	//install java and check installation
	sudo apt install default-jre
  	java -version		// check java version

	// install maven and check installation
  	sudo apt install maven
  	mvn -version
  
4. Clone repo, cd into it
  	git clone https://github.com/Jeevan-adiga/McaScraper.git
  	cd McaScraper

5. Run scraper
	mvn install
  	mvn exec:java -Dscrape.initialRecord=870801 -Dscrape.recordsCount=5000 -Dscrape.threadCount=50

Note: 
  scrape.initialRecord <- what is the initial record number
  scrape.recordsCount <- how many records to be scraped
  scrape.threadCount <- how many threads to be run in parellel

6. collect results
  cd target
	result.csv  <- records for which data is extracted
	error.csv   <- records for which records are not present/application provided error msg

7. Download files
	scp -i jeevan1.pem ubuntu@52.66.246.29:/home/ubuntu/scraper/McaScraper/target/result.csv .
	scp -i jeevan1.pem ubuntu@52.66.246.29:/home/ubuntu/scraper/McaScraper/target/error.csv .