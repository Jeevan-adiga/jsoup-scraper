package com.mca.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.mca.beans.ErrorDetails;
import com.mca.beans.ResultsRecord;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CsvWriterUtils {

	/**
	 * Utility method to write result to file
	 * @param result
	 */
	public static void writeResultsDataLineByLine(List<ResultsRecord> result) {

		String scrapeResultPath = "result.csv";
		try {
	     Writer writer = new FileWriter(scrapeResultPath, true);
	     StatefulBeanToCsv<ResultsRecord> beanToCsv = new StatefulBeanToCsvBuilder<ResultsRecord>(writer).build();
	     beanToCsv.write(result);
	     writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Utility to write error msg to file
	 * @param error
	 */
	public static void writeErrorDataLineByLine(ErrorDetails error) {
		String scrapeErrorPath = "error.csv";
		try {
		     Writer writer = new FileWriter(scrapeErrorPath, true);
		     StatefulBeanToCsv<ErrorDetails> beanToCsv = new StatefulBeanToCsvBuilder<ErrorDetails>(writer).build();
		     beanToCsv.write(error);
		     writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CsvDataTypeMismatchException e) {
				e.printStackTrace();
			} catch (CsvRequiredFieldEmptyException e) {
				e.printStackTrace();
			}
	}
}
