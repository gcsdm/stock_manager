// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SearchStock.java

import info.DayPriceInfo;
import java.util.Hashtable;
import java.util.Iterator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchStock
{

//    public SearchStock(String code, Execute exec)
//    {
//        this.code = "";
//        this.exec = null;
//        this.code = code;
//        this.exec = exec;
//    }
//
//    public void run()
//    {
//        exec.updateCodePrice(code, nowPrice(code));
//    }

    public static Hashtable dayPrice(String stockCode, String checkDate)
    {
        Hashtable arr = new Hashtable();
        try
        {
            Document doc = getDocumentDayPrice(stockCode);
            Elements links = doc.select("table[class=type2]").select("tr");
            Iterator iterator = links.iterator();
            while(iterator.hasNext()) 
            {
                Element link = (Element)iterator.next();
                Elements td = link.select("td");
                if(td == null || td.size() != 7)
                    continue;
                try
                {
                    int idx = 0;
                    String dateS = td.get(idx++).select("span").get(0).ownText().toString().replaceAll("\\.", "");
                    if(!dateS.equals(checkDate))
                        continue;
                    DayPriceInfo info = new DayPriceInfo();
                    info.setDateStr(dateS);
                    info.setClosePrice(td.get(idx++).select("span").get(0).ownText().replaceAll(",", ""));
                    int status = 1;
                    Elements em = td.get(idx).select("span[class=tah p11 red02]");
                    if(em.size() == 0)
                    {
                        status = -1;
                        em = null;
                        em = td.get(idx).select("span[class=tah p11 nv01]");
                        if(em.size() == 0)
                            status = 0;
                        else
                            info.setGapPrice(em.get(0).ownText());
                    } else
                    {
                        info.setGapPrice(em.get(0).ownText());
                    }
                    info.setGapPrice(info.getGapPrice() * status);
                    idx++;
                    info.setOpenPrice(td.get(idx++).select("span").get(0).ownText().replaceAll(",", ""));
                    info.setHighPrice(td.get(idx++).select("span").get(0).ownText().replaceAll(",", ""));
                    info.setLowPrice(td.get(idx++).select("span").get(0).ownText().replaceAll(",", ""));
                    info.setTradeVolume(td.get(idx++).select("span").get(0).ownText().replaceAll(",", ""));
                    arr.put(dateS, info);
                    break;
                }
                catch(Exception exception) { }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return arr;
    }

    private static int nowPrice(String stockCode)
    {
        String price = "-2";
        try
        {
            Document doc = getDocumentNowPrice(stockCode);
            Elements links = doc.select("div[id=content]").select("div[class=today").select("span[class=blind]");
            Iterator iterator = links.iterator();
            if(iterator.hasNext())
            {
                Element link = (Element)iterator.next();
                price = link.ownText();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        price = price.replaceAll(",", "");
        return Integer.parseInt(price);
    }

    private static Document getDocumentNowPrice(String stockCode)
    {
        Document doc = null;
        int count = 0;
        do
        {
            count++;
            try
            {
                String url = (new StringBuilder("http://finance.naver.com/item/main.nhn?code=")).append(stockCode).toString();
                doc = Jsoup.connect(url).get();
                break;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                try
                {
                    Thread.sleep(2000L);
                }
                catch(Exception exception) { }
            }
        } while(true);
        return doc;
    }

    private static Document getDocumentDayPrice(String stockCode)
    {
        Document doc = null;
        int count = 0;
        do
        {
            count++;
            try
            {
                String url = (new StringBuilder("http://finance.naver.com/item/sise_day.nhn?code=")).append(stockCode).toString();
                doc = Jsoup.connect(url).get();
                break;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                try
                {
                    Thread.sleep(2000L);
                }
                catch(Exception exception) { }
            }
        } while(true);
        return doc;
    }

    private String code;
    private Execute exec;
}
