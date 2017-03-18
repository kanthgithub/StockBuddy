package Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

/**
 * Created by jaipr on 13-03-2017.
 */

public class StockAPI {
    public JSONObject getStock(final String[] symbols) {
        JSONArray jsonArray = new JSONArray();

        Map<String, Stock> stocks = null;
        List<Stock> stockList = new ArrayList<Stock>();

        try {
            stocks = YahooFinance.get(symbols);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String symbol : symbols) {
            stockList.add(stocks.get(symbol));

            JSONObject stockObj = new JSONObject();
            try {
                for (Stock s : stockList) {
                    stockObj.put("Symbol", s.getSymbol());
                    stockObj.put("Price", s.getQuote().getPrice());
                    stockObj.put("OpenPrice", s.getQuote().getOpen());
                    stockObj.put("PrevClose", s.getQuote().getPreviousClose());
                    stockObj.put("HighPrice", s.getQuote().getDayHigh());
                    stockObj.put("LowPrice", s.getQuote().getDayLow());
                    stockObj.put("Volume", s.getQuote().getVolume());
                    stockObj.put("Change", s.getQuote().getChangeInPercent());
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(stockObj);

        }

        JSONObject stockObject = new JSONObject();
        try {
            stockObject.put("Stock", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stockObject;
    }

    public Stock getHistoricalStock(String symbol) {
        Stock stock = null;
        try {
            stock = YahooFinance.get(symbol, Interval.MONTHLY);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }
}
