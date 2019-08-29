package com.mca.scraper;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;

import com.mca.utils.Preference;

/**
 * Starting point for scrape, if nothing is passed in command line, 
 * values from default.properties will be taken for scraping
 */
class ThreadStarter {
	
    public static void main (String[] args) {
    	
    	long initialValue = Long.parseLong(Preference.getPreference("scrape.initialRecord"));
    	long recordCount =Long.parseLong(Preference.getPreference("scrape.recordsCount"));
    	int parellel = Integer.parseInt(Preference.getPreference("scrape.threadCount"));
    	
    	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(parellel);
    	for (long i = 0; i < recordCount; i++) {
    		String din = StringUtils.leftPad(String.valueOf(initialValue), 8, "0");
    		initialValue = initialValue + 1;
    		
    		McaScraper task = new McaScraper(din);
            executor.execute(task);
        }
        executor.shutdown();
    }
}