# jsoup-scraper #

Simple multi-thread scraper implementation to extract data into csv file

### What is this repository for? ###
- This project is used to scrape a website to retrieve records 
- Records extracted will be saved in csv format.
- It is implemented to support parallel execution with multiple threads.

### How do I get set up? ###
**Dependencies**
- `org.jsoup:jsoup` used for html parsing`
- `com.opencsv:opencsv` used for writing to csv file
- `org.apache.commons:commons-lang3` used for working with string

**Local Deployment instructions**
	`git clone https://github.com/Jeevan-adiga/jsoup-scraper.git`
	`mvn clean install`			
	`mvn exec:java -Dscrape.initialRecord=870801 -Dscrape.recordsCount=5000 -Dscrape.threadCount=50` 		// to run scraper without creating jar
  `mvn clean package` 				// Generate jar file
  `java -jar -Dscrape.baseurl={baseUrl} -Dscrape.initialRecord=870801 -Dscrape.recordsCount=1 -Dscrape.threadCount=1 jsoup-scraper-1.0.0-jar-with-dependencies.jar`						 // Run scraper using the program
  
**NOTE:**
  scrape.initialRecord <- what is the initial record number
  scrape.recordsCount <- how many records to be scraped
  scrape.threadCount <- how many threads to be run in parellel

### Deployment to aws linux machine and running scraper ###
1. Change permission of pen file and ssh into server
	chmod 400 certificate.pem
	ssh -i {pemfile} {username}@{ipaddress}
2. Make new directory and cd into that directory
	mkdir scraper
	cd scraper
3. update all packages and install java and maven
	sudo apt-get update	            // update all packages
	sudo apt install default-jre    //install java 
  	java -version		            // check java version
  	sudo apt install maven          // install maven
  	mvn -version                    // check maven installation
4. copy scraper package(jar) file into aws machine
    scp -i certificate.pem jsoup-scraper-1.0.0-jar-with-dependencies.jar {username}@{ipaddress}:{path}/scraper
5. Run scraper
$java -jar -Dscrape.baseurl={baseUrl} -Dscrape.initialRecord=870801 -Dscrape.recordsCount=1 -Dscrape.threadCount=1 jsoup-scraper-1.0.0-jar-with-dependencies.jar
6. collect results
	result.csv  <- records for which data is extracted
	error.csv   <- records for which records are not present/application provided error msg
7. Download files
	scp -i certificate.pem {username}@{ipaddress}:{path}/result.csv .
	scp -i certificate.pem {username}@{ipaddress}:{path}/error.csv .