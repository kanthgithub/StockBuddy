package com.example.jaipr.stockbuddy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by jaipr on 13-03-2017.
 */

public class StockAPI {
    public JSONObject getStock(final List<String> symbolList)
    {
        JSONArray jsonArray = new JSONArray();

        for(String symbol : symbolList)
        {
            Stock stock = null;
            try {
                stock = YahooFinance.get(symbol);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BigDecimal price = stock.getQuote().getPrice();
            BigDecimal change = stock.getQuote().getChangeInPercent();
            BigDecimal peg = stock.getStats().getPeg();
            BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

            System.out.println("Symbol :: "+stock.getSymbol());
            System.out.println("Price :: "+stock.getQuote().getPrice());
            System.out.println("High Price :: "+stock.getQuote().getDayHigh());
            System.out.println("Low Price :: "+stock.getQuote().getDayLow());
            System.out.println("Volume :: "+stock.getQuote().getVolume());
            System.out.println("Prev Close :: "+stock.getQuote().getPreviousClose());
            System.out.println("Open price :: "+stock.getQuote().getOpen());

            JSONObject stockObj = new JSONObject();
            try {
                stockObj.put("Symbol", stock.getSymbol());
                stockObj.put("Price", stock.getQuote().getPrice());
                stockObj.put("OpenPrice", stock.getQuote().getOpen());
                stockObj.put("PrevClose", stock.getQuote().getPreviousClose());
                stockObj.put("HighPrice", stock.getQuote().getDayHigh());
                stockObj.put("LowPrice",stock.getQuote().getDayLow());
                stockObj.put("Volume",stock.getQuote().getVolume());

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
        return  stockObject;
    }
}
