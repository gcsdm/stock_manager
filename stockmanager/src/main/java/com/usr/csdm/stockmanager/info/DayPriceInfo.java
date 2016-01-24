// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DayPriceInfo.java

package com.usr.csdm.stockmanager.info;


public class DayPriceInfo
{

    public DayPriceInfo()
    {
    }

    public int getHighPrice()
    {
        return highPrice;
    }

    public void setHighPrice(int highPrice)
    {
        this.highPrice = highPrice;
    }

    public void setHighPrice(String highPrice)
    {
        setHighPrice(replaceComma(highPrice));
    }

    public int getLowPrice()
    {
        return lowPrice;
    }

    public void setLowPrice(int lowPrice)
    {
        this.lowPrice = lowPrice;
    }

    public void setLowPrice(String lowPrice)
    {
        setLowPrice(replaceComma(lowPrice));
    }

    public int getOpenPrice()
    {
        return openPrice;
    }

    public void setOpenPrice(int openPrice)
    {
        this.openPrice = openPrice;
    }

    public void setOpenPrice(String openPrice)
    {
        setOpenPrice(replaceComma(openPrice));
    }

    public int getClosePrice()
    {
        return closePrice;
    }

    public void setClosePrice(int closePrice)
    {
        this.closePrice = closePrice;
    }

    public void setClosePrice(String closePrice)
    {
        setClosePrice(replaceComma(closePrice));
    }

    public int getGapPrice()
    {
        return gapPrice;
    }

    public void setGapPrice(int gapPrice)
    {
        this.gapPrice = gapPrice;
    }

    public void setGapPrice(String gapPrice)
    {
        setGapPrice(replaceComma(gapPrice));
    }

    public int getTradeVolume()
    {
        return tradeVolume;
    }

    public void setTradeVolume(int tradeVolume)
    {
        this.tradeVolume = tradeVolume;
    }

    public void setTradeVolume(String tradeVolume)
    {
        setTradeVolume(replaceComma(tradeVolume));
    }

    public String getDateStr()
    {
        return dateStr;
    }

    public void setDateStr(String dateStr)
    {
        this.dateStr = dateStr;
    }

    private int replaceComma(String value)
    {
        int val = -1;
        if(value != null)
            try
            {
                val = Integer.parseInt(value.trim().replaceAll(",", ""));
            }
            catch(Exception exception) { }
        return val;
    }

    private int openPrice;
    private int closePrice;
    private int highPrice;
    private int lowPrice;
    private int gapPrice;
    private int tradeVolume;
    private String dateStr;
}
