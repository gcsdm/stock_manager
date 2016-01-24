// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DBUtil.java

package common.db;

import java.sql.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.sql.DataSource;

public class DBUtil
{

    private DBUtil()
    {
    }

    public static Connection getMySQLConnection(String database, String userName, String pwd)
    {
        return getMySQLConnection("localhost", 3306, database, userName, pwd, "utf-8");
    }

    public static Connection getMySQLConnection(String host, String database, String userName, String pwd)
    {
        return getMySQLConnection(host, 3306, database, userName, pwd, "utf-8");
    }

    public static Connection getMySQLConnection(String host, int port, String database, String userName, String pwd)
    {
        return getMySQLConnection(host, port, database, userName, pwd, "utf-8");
    }

    public static Connection getMySQLConnection(String host, int port, String database, String userName, String pwd, String characterEncoding)
    {
        Connection conn = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection((new StringBuilder("jdbc:mysql://")).append(host).append(":").append(port).append("/").append(database).append("?useUnicode=true&characterEncoding=").append(characterEncoding).toString(), userName, pwd);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return conn;
    }

    private static synchronized void addDataSource(String dataSourceName)
        throws NamingException
    {
        if(!htDataSource.containsKey(dataSourceName))
        {
            InitialContext ic = new InitialContext();
            Context context = (Context)ic.lookup("java:/comp/env");
            htDataSource.put(dataSourceName, (DataSource)context.lookup(dataSourceName));
        }
    }

    public static void setAutoCommit(Connection conn, boolean isAutoCommit)
    {
        try
        {
            conn.setAutoCommit(isAutoCommit);
        }
        catch(Exception exception) { }
    }

    public static void commit(Connection conn)
    {
        try
        {
            conn.commit();
        }
        catch(Exception exception) { }
        setAutoCommit(conn, true);
    }

    public static void rollback(Connection conn)
    {
        try
        {
            conn.rollback();
        }
        catch(Exception exception) { }
        setAutoCommit(conn, true);
    }

    public static Connection getConnection(String dataSourceName)
        throws Exception
    {
        if(!htDataSource.contains(dataSourceName))
            addDataSource(dataSourceName);
        return ((DataSource)htDataSource.get(dataSourceName)).getConnection();
    }

    public static void close(Connection obj)
    {
        if(obj != null)
        {
            try
            {
                if(!obj.getAutoCommit())
                    obj.setAutoCommit(true);
            }
            catch(Exception exception) { }
            try
            {
                obj.close();
            }
            catch(Exception exception1) { }
        }
    }

    public static void close(ResultSet obj)
    {
        if(obj != null)
            try
            {
                obj.close();
            }
            catch(Exception exception) { }
    }

    public static void close(PreparedStatement obj)
    {
        if(obj != null)
            try
            {
                obj.close();
            }
            catch(Exception exception) { }
    }

    private static Hashtable htDataSource = new Hashtable();

}
