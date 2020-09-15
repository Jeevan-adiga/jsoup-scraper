package com.mca.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mca.beans.ErrorDetails;
import com.mca.beans.ResultsRecord;
import com.mca.utils.CsvWriterUtils;
import com.mca.utils.Preference;

/**
 * Main Scraping logic
 */
class McaScraper implements Runnable {

    private String din;
    private String baseUrl = Preference.getPreference("scrape.baseurl");
    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";

    public McaScraper(String din) {
        super();
        this.din = din;
    }

    /**
     * threads run method - calls scrape method
     */
    public void run() {
        try {
            scrapeData(this.din);
        } catch (java.lang.NullPointerException ex) {
            // do nothing
        }
    }

    /**
     * main scraping logic
     *
     * @param dIn
     */
    public void scrapeData(String dIn) {

        System.out.println("Scraping record with DIN:" + din);
        // send din and parse response to Document object
        Document document = null;
        try {

            Response loginForm = Jsoup.connect(baseUrl + "/mcafoportal/viewDirectorMasterData.do")
                    .method(Connection.Method.GET)
                    .execute();

            Response response =
                    Jsoup.connect(baseUrl + "/mcafoportal/showdirectorMasterData.do")
                            .userAgent(userAgent)
                            .timeout(120 * 1000)
                            .method(Method.POST)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                            .data("din", dIn)
                            .data("showdirectorMasterData", "Submit")
                            .cookies(loginForm.cookies())
                            .followRedirects(true)
                            .execute();

            Thread.sleep(2000);

            //parse the document from response
            document = response.parse();
//			System.out.println(document.outerHtml());
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorDetails error = new ErrorDetails();
            error.setDin(dIn);
            error.setErrorMsg("Record to be retried: error occured during search | ExceptionName:" + ex.getClass().getSimpleName() + "|msg:" + ex.getMessage());
            CsvWriterUtils.writeErrorDataLineByLine(error);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//      System.out.println(document.outerHtml());

        // check for errormsg, output to error.csv file
        // MCA21 Error Page - Your request could not be processed. There has been an internal application error.
        Element errorPageMsg = document.selectFirst("#companiesact1 h1");
        if (errorPageMsg != null) {
            ErrorDetails error = new ErrorDetails();
            error.setDin(dIn);
            error.setErrorMsg("Record to be retried:" + errorPageMsg.text());
            CsvWriterUtils.writeErrorDataLineByLine(error);
        }

        // check for errormsg, output to error.csv file
        // all other error msgs
        Element errorMsg = document.selectFirst("#msg_overlay li");
        if (errorMsg != null) {
            ErrorDetails error = new ErrorDetails();
            error.setDin(dIn);
            error.setErrorMsg(errorMsg.text());
            CsvWriterUtils.writeErrorDataLineByLine(error);
        }

        // retrieve main elements
        Element din = document.selectFirst("#directorData tr:nth-child(1) > td:nth-child(2)");
        Element name = document.selectFirst("#directorData tr:nth-child(2) > td:nth-child(2)");
        Element companyTable = document.selectFirst("#companyData");
        Element llpTable = document.selectFirst("#llpData");

        // check for errormsg, output to error.csv file
        // condition to check if both table dont have any data
        if (companyTable.selectFirst("tr:nth-child(1) > td:nth-child(1)").text().contains("exists for a Director")
                && llpTable.selectFirst("tr:nth-child(1) > td:nth-child(1)").text().contains("exists for a Director")) {
            ErrorDetails error = new ErrorDetails();
            error.setDin(din.ownText());
            error.setErrorMsg("Data not present in both companyTable and LlpTable");
            CsvWriterUtils.writeErrorDataLineByLine(error);
        }

        // fetch company records and write to csv
        List<ResultsRecord> companyRecords = getRecordsFromTable(din.ownText(), name.ownText(), "Company", companyTable);
        CsvWriterUtils.writeResultsDataLineByLine(companyRecords);

        // fetch llp records and write to csv
        List<ResultsRecord> llpRecords = getRecordsFromTable(din.ownText(), name.ownText(), "LLP", llpTable);
        CsvWriterUtils.writeResultsDataLineByLine(llpRecords);
    }

    /**
     * Method to retrieve records from table based on parameter passed
     *
     * @param din
     * @param name
     * @param category
     * @param table
     * @return
     */
    public static List<ResultsRecord> getRecordsFromTable(String din, String name, String category, Element table) {
        List<ResultsRecord> records = new ArrayList<ResultsRecord>();
        if (!table.selectFirst("tr:nth-child(1) > td:nth-child(1)").text().contains("exists for a Director")) {
            Elements rows = table.select("tbody > tr");
            for (Element row : rows) {
                ResultsRecord record = new ResultsRecord();
                record.setDin(din);
                record.setName(name);
                record.setCategory(category);
                record.setCin(row.select("td:nth-child(1)").text());
                record.setCompanyName(row.select("td:nth-child(2)").text());
                record.setBeginDate(row.select("td:nth-child(3)").text());
                record.setEndDate(row.select("td:nth-child(4)").text());
                record.setActiveCompliance(row.select("td:nth-child(5)").text());
                records.add(record);
            }
        }
        return records;
    }
}