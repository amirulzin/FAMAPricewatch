package com.mynation.famapricewatch.data.parser;

import android.support.annotation.NonNull;

import com.mynation.famapricewatch.data.Commodity;
import com.mynation.famapricewatch.data.StateData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 *
 */

public class FAMAPriceDataParser {

    private static final boolean LOG_EXCEPTIONS = true;
    private static final String firstIdentifier = "Pusat :";
    private static final String secondIdentifier = "Nama Varieti";
    private static final String dataTableParentTag = "tbody";
    private static final String dataRowIdentifier = "id";
    private static final String previousReportIdentifier = "HARGA TERDAHULU";

    public static ArrayList<StateData> parse(String htmlBody, FailureListener failureListener) {
        ArrayList<StateTable> dataRows = null;
        try {
            dataRows = mapStateTables(htmlBody);
        } catch (ParseException e) {
            if (LOG_EXCEPTIONS) e.printStackTrace();
            if (e.getReason() == ParseException.Reason.NO_KEY_TABLE || e.getReason() == ParseException.Reason.NO_SECONDKEY_TABLE)
                if (e.getPreviousUrl() != null && failureListener != null) {
                    failureListener.onCurrentTableUnavailable(e.getPreviousUrl());
                }
        } catch (Exception e) {
            if (LOG_EXCEPTIONS) e.printStackTrace();
        }
        if (dataRows != null) {
            return parseStateDatas(dataRows);
        } else
            return new ArrayList<>(); //Return empty on failure
    }

    private static ArrayList<StateTable> mapStateTables(String htmlBody) throws ParseException {
        if (htmlBody == null) throw new ParseException(ParseException.Reason.NULL);
        else if (htmlBody.isEmpty()) throw new ParseException(ParseException.Reason.EMPTY_BODY);

        Document document = Jsoup.parse(htmlBody);
        Element body = document.body();

        //Get past report redirect link if current report has error
        String pastReportUrl = null;
        Elements hrefs = body.select("a[href]");
        for (Element element : hrefs) {
            String text = element.text();
            if (text.contains(previousReportIdentifier)) {
                pastReportUrl = FAMAEndpoint.getPreviousPriceReportUrl(element.attr("href"));
                break;
            }
        }


        //Gather an insertion first state IDs
        Elements firstIdPoints = body.getElementsContainingOwnText(firstIdentifier);
        if (firstIdPoints.isEmpty())
            throw makeParseException(ParseException.Reason.NO_KEY_TABLE, pastReportUrl);


        //Prepare output collector
        final ArrayList<StateTable> stateTables = new ArrayList<>(firstIdPoints.size());

        //Fill state ids
        for (int i = 0; i < firstIdPoints.size(); i++) {

            final Element idPoint = firstIdPoints.get(i);
            final String rawStateId = idPoint.ownText();
            stateTables.add(new StateTable(rawStateId.substring(firstIdentifier.length(), rawStateId.length()).trim()));
        }

        //Orderly map state data to row elements
        Elements secondIdPoints = body.getElementsContainingOwnText(secondIdentifier);
        if (secondIdPoints.isEmpty())
            throw makeParseException(ParseException.Reason.NO_SECONDKEY_TABLE, pastReportUrl);
        else if (secondIdPoints.size() != firstIdPoints.size())
            throw makeParseException(ParseException.Reason.TABLES_SIZE_UNPAIRED, pastReportUrl);

        for (int i = 0; i < secondIdPoints.size(); i++) {
            final Element idPoint = secondIdPoints.get(i);
            final Element parent = JsoupParserUtil.findFirstParentWithTag(idPoint, dataTableParentTag);
            if (parent != null) {
                final Elements rows = parent.getElementsByAttribute(dataRowIdentifier);
                stateTables.get(i).setUnparsedDataTable(rows);
            }
        }

        return stateTables;
    }

    @NonNull
    private static ParseException makeParseException(ParseException.Reason reason, String pastReportUrl) {
        ParseException parseException = new ParseException(reason);
        parseException.setPreviousUrl(pastReportUrl);
        return parseException;
    }

    private static ArrayList<StateData> parseStateDatas(ArrayList<StateTable> stateTables) {
        final ArrayList<StateData> outList = new ArrayList<>(stateTables.size());
        for (final StateTable stateTable : stateTables) {

            final StateData stateData = new StateData();
            stateData.setCentreName(stateTable.getStateId());
            stateData.setCommodities(stateTable.parseRows());
            outList.add(stateData);
        }

        return outList;
    }

    public interface FailureListener {
        void onCurrentTableUnavailable(String previousSuccessfulDataUrl);
    }

    private static class StateTable {
        String stateId;
        Elements rows;

        StateTable(String stateId) {
            this.stateId = stateId;
        }

        String getStateId() {
            return stateId;
        }

        void setUnparsedDataTable(Elements unparsedDataRow) {
            this.rows = unparsedDataRow;
        }

        ArrayList<Commodity> parseRows() {
            final ArrayList<Commodity> outList = new ArrayList<>(rows.size());
            for (int i = 0; i < rows.size(); i++) {
                final Element row = rows.get(i);
                outList.add(parseSingleRow(row));
            }
            return outList;
        }

        Commodity parseSingleRow(Element row) {
            return new Commodity(
                    row.child(0).ownText(),
                    row.child(1).ownText(),
                    row.child(2).ownText(),
                    row.child(3).ownText(),
                    row.child(4).ownText(),
                    row.child(5).ownText()
            );
        }
    }

    private static class ParseException extends Throwable {
        private final Reason reason;
        private String previousUrl;

        ParseException(Reason reason) {
            super(reason.name());
            this.reason = reason;
        }

        public String getPreviousUrl() {
            return previousUrl;
        }

        public void setPreviousUrl(String previousUrl) {
            this.previousUrl = previousUrl;
        }

        public Reason getReason() {
            return reason;
        }

        private enum Reason {
            NULL, EMPTY_BODY, NO_KEY_TABLE, NO_SECONDKEY_TABLE, TABLES_SIZE_UNPAIRED
        }
    }
}


