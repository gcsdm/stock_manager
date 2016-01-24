/*****************************************************************************
 �ش� QueryMananger Ŭ������ �̱��� �������� ����Ͻø�? �ȵ˴ϴ�. .. �����Ͱ� ����
 �ش� QueryMananger Ŭ������ DBConnection �κ��� �����Ǿ� ���� �ʽ��ϴ�.
 JDK���� CachedRowSet ���� Ŭ������ ������.. �ܺο��� rowset.jar �޾� CLASSPATH�� �߰��� �־��? �մϴ�.
 �ش� Ŭ������ JDK 1.3.1 �� ���߾ ���� �Ǿ����ϴ�. (rowset.jar ����)



 # �⺻ ���? ����

 QueryManager.set(....)...
 1. set()...
  1.1 set(���̺��ʵ��?, ������, ���°�1)
   �Ϲ������� ����ϴ�? ����
   - ���°�1
     true : where ���� ��Ÿ����.. insert�ÿ��� where ���� �����Ƿ� ���? false�� �Ѵ�.
     false : ������ ���� ��Ÿ����.

  1.2 set(���̺��ʵ��?, ������, ���°�1, ���°�2)
   PreparedStatements���� ó������ ���ϴ� SQL������ ������ ó���� ���?
   - ���°�1
     ��
   - ���°�2
     DB������ ������ ��Ÿ����. PreparedStatement ���� �Է����� ���ϴ� �����ϰ��?... �� sql�� ������ ���ؼ�
     �����͸� �����Ҷ� ����ϴ�? �����̴�.
     ex1: SYSDATE, ������ ��... �Ǵ� SQL�� ���Ĺ��� to_date(��,'yyyymmddhh24miss') .. ���� ��
 ex2: qm.set("test1", "(select nvl(max(no),0)+1 next_no from xxx)", false, true)




 ----------------------------------------------------------
  tbl_test ���? ���̺��� �����Ҷ�

  create table tbl_test(
    id int(10) not null,
    name varchar2(20),
    reg_date date,
    primary key(id)
  );
 ----------------------------------------------------------

 ===========================================================
  1. ������ �Է�
 ===========================================================
   Connection conn = DB Connection ���� �ν��Ͻ� ����...
   QueryManager qm = new QueryManager();

    int id = 1;
    String name = "ȫ�浿";

    qm.set("id", id, false);
    qm.set("name", name, false);
    qm.set("reg_date", "SYSDATE", false, true); // ���� true�� Statement SQL�� �Ľ��ϴ� �κ��̴�.
    qm.insert(conn, "tbl_test");

    => insert into tbl_test(id, name, reg_date)
       values(?,?,SYSDATE);


 ===========================================================
  2. �������� ����
 ===========================================================
   Connection conn = DB Connection ���� �ν��Ͻ� ����...
   QueryManager qm = new QueryManager();

   type 1

    int id = 1;
    String name = "ȫ�浿";

    qm.set("name", name, false);
    qm.set("reg_date", "SYSDATE", false, true); // ���� true�� Statement SQL�� �Ľ��ϴ� �κ��̴�.

    qm.set("id", id, true);  <---- ������.. where...
    qm.update(conn, "tbl_test");


   type 2
    int id = 1;
    String name = "ȫ�浿";

    qm.set("name", name, false);
 qm.set("reg_date", "to_date(?,'YYYYMMDDHH24MISS')", "20040101121122", false);
    -> �ʵ��?, SQL���ǹ�(�����ͺκ���? ǥ��), ?�� ġȯ�� ������, ����������
    -> ������ ���� ������ SQL type �̹Ƿ� ���� true/false �Ķ���ʹ�? �ʿ����?.. ������ true ��

    qm.set("id", id, true);  <---- ������.. where...
    qm.update(conn, "tbl_test");


  ===========================================================
   3. �������� ����
  ===========================================================
   Connection conn = DB Connection ���� �ν��Ͻ� ����...
   QueryManager qm = new QueryManager();

    int id = 1;

    qm.set("id", id, true);  <---- ������.. where...
    qm.delete(conn, "tbl_test");



 ===========================================================
 4. ?
 ===========================================================
 �ϴ��� �޼ҵ��? set ���� �����Ͱ� ���� �ԷµǾ� �־��? �մϴ�.
 ## ����.. executeUpdate ���� ����κ�? (Dynamic SQL)

 - insert(DBĿ�ؼ�����, ���̺��?)
 - update(DBĿ�ؼ�����, ���̺��?)
 - delete(DBĿ�ؼ�����, ���̺��?)
 - executeUpdate(DBĿ�ؼ�����, �Ϲ� Statememt insert|update|delete ������)


 ? ���õ� DML ������ ������ ���?

 �۾������?
 Connection conn = ???;
 int effectRowCount = -1;

 (Binding SQL �� ���?)
 QueryManager qm = new QueryManager();
 String sql = "insert into tbl_test select * from tbl_test2 where pk_id=?"
 qm.setSQL(sql);
 qm.setVal("12345");
 effectRowCount = qm.executeUpdate(conn);
 : pstmt �ڵ����� �� �Ҹ�


 ## Select ���� �۾��� �� ���?
 - 1. Dynamic SQL : select(DBĿ�ؼ�����, SQL��)
  QueryManager qm = new QueryManager();
  RowSet rs = qm.select(conn, "select count(*) from tbl_test");

 - 2. Binding SQL �϶�...
  QueryManager qm = new QueryManager();
  qm.setSQL("select count(*) from tbl_test where id=? and name=?" and age=?);
  qm.setVal("testID");
  qm.setVal("ȫ�浿");
  qm.setVal(45);
  qm.select(conn) or qm.executeQuery(conn) <- RowSet Ÿ������ ����

 : pstmt �ڵ����� �� �Ҹ�

 ## ��ġ�۾��� �� ���?...
 - insertAddSQL(���̺��?)
 - updateAddSQL(���̺��?)
 - deleteAddSQL(���̺��?)


   QueryManager qm = new QueryManager();

 qm.set("field1", "testData1", false); // String type�� field1 �̸��� �ʵ忡 ������ ��Ī(���?)
   qm.set("field2", 1000, false);        // int type�� field2 �̸��� �ʵ忡 ������ ��Ī(���?)
   qm.set("field3", "to_date(?,'yyyymmdd')","20051212", false); // date type�� field3 �̸��� �ʵ忡 to_date('20051212','yyyymmdd') ������ ���?
   qm.set("field4", "SYSDATE", false, true); // date type field4 �̸��� �ʵ忡 SYSDATE ������ ���?

   qm.insertAddSQL("TEST_TABLE"); // ���� �����͸� TEST_TABLE ���̺� INSERT �ϱ� ���� �������? (* ���� ������ �ȵ�. �غ��۾�)


 qm.set("field1", "testData1", false); // String type�� field1 �̸��� �ʵ忡 ������ ��Ī(���?)
   qm.set("field2", 1000, false);        // int type�� field2 �̸��� �ʵ忡 ������ ��Ī(���?)
   qm.set("field3", "to_date(?,'yyyymmdd')","20051212", false); // date type�� field3 �̸��� �ʵ忡 to_date('20051212','yyyymmdd') ������ ���?
   qm.set("field4", "SYSDATE", true, true); // date type field4 �̸��� �ʵ忡 SYSDATE �����Ϳ� ��ġ�ϴ� �͸� ���� �Ǵ� �����۾�

   qm.updateAddSQL("TEST_TABLE");
   // ���� �����͸� TEST_TABLE ���̺� field4=SYSDATE �� �����͸� UPDATE �ϱ� ���� �������? (* ���� ������ �ȵ�. �غ��۾�)


   qm.set("field1", "testData1", true); // String type�� field1�̸��� �ʵ尪�� "testData1" �� ���ڵ� ����,���� �۾�
   qm.set("field2", 1000, false);        // int type�� field2 �̸��� �ʵ忡 ������ ��Ī(���?)
   qm.set("field3", "to_date(?,'yyyymmdd')","20051212", false); // date type�� field3 �̸��� �ʵ忡 to_date('20051212','yyyymmdd') ������ ���?
   qm.set("field4", "SYSDATE", false, true); // date type field4 �̸��� �ʵ忡 SYSDATE �����Ϳ� ��ġ�ϴ� �͸� ���� �Ǵ� �����۾�

   qm.deleteAddSQL("TEST_TABLE");
   // ���� �����͸� TEST_TABLE ���̺� field1='testData1' �� �����͸� DELETE �ϱ� ���� �������? (* ���� ������ �ȵ�. �غ��۾�)


   // �� 3���� �� ���� ó���۾��� ��ϵ�?
   int effectRowsCount = qm.executeUpdateSQLS(conn, qm.CHECK_ROWCOUNT);
   // ���� insert, update, delete ������ �����? (������ �ϳ��ϳ��� �����?)


   int effectRowsCount = qm.executeUpdateSQLS(conn, qm.NOCOUNT_UPDATE, qm.NOCOUNT_DELETE);
   int effectRowsCount = qm.executeUpdateSQLS(conn, qm.NOCOUNT_DELETE);
   int effectRowsCount = qm.executeUpdateSQLS(conn, qm.NOCOUNT_UPDATE);


 - executeBatch(Connection) ... -> xxxAddSQL() �� ���� ��ġ���? ���� ����
 : ������ ���� pstmt �ڵ����� �� �����? �Ҹ�

 ## ������ PreparedStatement�� �����? �ѹ��� ������ ���?...
 - insertAddSQL(Ŀ�ؼ�����, ���̺��?)
 - updateAddSQL
 - deleteAddSQL
 - executeUpdateSQLS(); -> xxxAddSQL �� ���� ��ϵ�? �������� ����. (�������? ī��Ʈ ����)
 : �����? ������ 1:1 pstmt ����.. �����? �Ҹ�


 ## �����? ����
 - getInsertSQL(���̺��?), getUpdateSQL(���̺��?), getDeleteSQL(���̺��?)
  �� �����ϸ� Statements ������ SQL���� �����Ѵ�.
  (�ӵ��� ������ �� ������ ������ �������? ����, �ʱ� ����ȭ �����? ������ ����ϱ�? ����
   , isDebugPrintStatementTypeSQL = true �� �����ϸ� �����߻� �ÿ��� �ڵ����� Statement Type�� SQL print ��
   , print ���� ������ printSQLInfo method�� �����Ͽ� ����Ұ�? (log4j ��..) )
 * ���ǻ��� : getXXSQL �� �Ŀ� �ٸ������� getXXSQL �� �����ҷ���
 * clear �޼ҵ带 �����Ͽ� ������ �����? �����? �����͸� �����Ͽ��� �Ѵ�.
 * (insert, update, delete, insertAddSQL.., insertAddSQL.. �����? �������? �޼ҵ带 �����ϸ�
 * �����? ���ÿ� �����? �ӽ� �����Ͱ� clear ������ getXXXSQL�� �������� ����. ���? ������)

 - printSQLInfo �޼ҵ� �κ��� �����Ͽ� SQL ���� ó������ ������ �����Ѵ�.


 ---common.db----------------------------
 | ���� �� �߰�����
 ----------------------------------------------------------
 - 2005.07.12
  1) executeUpdate(Connection conn, String sql) �޼ҵ�  �߰�
  2) executeQuery(Connection conn, String sql) �޼ҵ� �߰�
  3) executeQuery(PreparedStatement pstmt) �޼ҵ� �߰�
  4) Oracle�� varchar, char, varchar2 Ÿ�Կ��� �ѱ��Է½� �����ʰ��� ���� �κ� ����
     length 1333 �ʰ� : setCharacterStream ���?
     length 1333 ���� : setString ���?

 - 2005.07.13
  1) close(Connection conn, RowSet rs) �޼ҵ� �߰�
  2) close(Connection conn, ResultSet rs) �޼ҵ� �߰�



 ----------------------------------------------------------
 ��Ÿ �޼ҵ� ����߰�?...��.. �e... T,Ta (���� �������� : 2005.07.12)
 ----------------------------------------------------------


 *****************************************************************************/


package com.usr.csdm.stockmanager.common.db;

import java.sql.PreparedStatement;
import java.util.Vector;
import java.sql.Connection;
import javax.sql.RowSet;
import java.sql.Statement;
import java.sql.ResultSet;
import sun.jdbc.rowset.CachedRowSet;
import java.util.HashMap;
import java.io.StringReader;
//import com.feelingk.common.log.Logs;

/**
 * PreparedStatement Util Class
 * <p>Title: DB�������� �Լ� ����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005. 2.</p>
 * <p>Company: CSDM</p>
 * @author Seungmo-Cho
 * @version 1.0
 */

public class QueryManager {

  private Vector vc = null; // set Data info save
  private Vector vcQuerySQLVO = null; // SQLVO save
  private final boolean isDebugPrintStatementTypeSQL = true; // insert, update, delete ���࿡���� Statement ������ SQL �Ѹ���..
  private QuerySQLVO extendQuerySQLVO = null; // �������� SQL�� ������ �༮
  private final boolean isRunSQLView = false;
  /**
   * ������
   */
  public QueryManager() {
    this.vc = new Vector();
    this.vcQuerySQLVO = new Vector();
  }

  /**
   * �⺻������ �����մϴ�.
   */
  public void clear() {
    if (this.vc != null) {
      this.vc.clear();
    }
    if (this.vcQuerySQLVO != null) {
      this.vcQuerySQLVO.clear();
    }
  }

  /**
   * �۾��� �����͸� �����մϴ�.
   * @param fieldName String : �ʵ��?
   * @param value String : ������
   * @param status boolean : false : ��������, true : where.. ������
   */
  public void set(String filedName, String value, boolean status) {
    set(filedName, value, status, false);
  }

  /**
   * int setting
   * @param filedName String
   * @param value int
   * @param status boolean
   */
  public void set(String filedName, int value, boolean status) {
    set(filedName, value, status, false);
  }

  /**
   * float setting
   * @param filedName String
   * @param value float
   * @param status boolean
   */
  public void set(String filedName, float value, boolean status) {
    set(filedName, value, status, false);
  }

  /**
   * long setting
   * @param filedName String
   * @param value long
   * @param status boolean
   */
  public void set(String filedName, long value, boolean status) {
    set(filedName, value, status, false);
  }

  /**
   * �۾��� �����͸� �����մϴ�.
   * @param fieldName String : �ʵ��?
   * @param value Object : ������
   * @param status boolean : false : ��������, true : where.. ������
   */
  public void set(String filedName, Object value, boolean status) {
    set(filedName, value, status, false);
  }

  /**
   * double setting
   * @param filedName String
   * @param value double
   * @param status boolean
   */
  public void set(String filedName, double value, boolean status) {
    set(filedName, value, status, false);
  }

  /**
   * �۾��� �����͸� �����մϴ�.
   * @param fieldName String : �ʵ��?
   * @param sqlSyntax String : DB���� Syntax ����
   * @param value String : ������
   * @param status boolean : false : ��������, true : where.. ������
   */
  public void set(String filedName, String sqlSyntax, String value,
                  boolean status) {
    set(filedName, sqlSyntax, value, status, false);

  }

  /**
   * �۾��� �����͸� �����մϴ�.
   * @param fieldName String : �ʵ��?
   * @param sqlSyntax String : DB���� Syntax ����
   * @param value Object : ������
   * @param status boolean : false : ��������, true : where.. ������
   */
  public void set(String filedName, String sqlSyntax, Object value,
                  boolean status) {
    set(filedName, sqlSyntax, value, status, false);
  }

  /**
   * int setting
   * @param filedName String
   * @param sqlSyntax String : DB���� Syntax ����
   * @param value int
   * @param status boolean
   */
  public void set(String filedName, String sqlSyntax, int value, boolean status) {
    set(filedName, sqlSyntax, value, status, false);
  }

  /**
   * float setting
   * @param filedName String
   * @param sqlSyntax String : DB���� Syntax ����
   * @param value float
   * @param status boolean
   */
  public void set(String filedName, String sqlSyntax, float value,
                  boolean status) {
    set(filedName, sqlSyntax, value, status, false);
  }

  /**
   * long setting
   * @param filedName String
   * @param sqlSyntax String : DB���� Syntax ����
   * @param value long
   * @param status boolean
   */
  public void set(String filedName, String sqlSyntax, long value,
                  boolean status) {
    set(filedName, sqlSyntax, value, status, false);
  }

  /**
   * double setting
   * @param filedName String
   * @param sqlSyntax String : DB���� Syntax ����
   * @param value double
   * @param status boolean
   */
  public void set(String filedName, String sqlSyntax, double value,
                  boolean status) {
    set(filedName, sqlSyntax, value, status, false);
  }

  /**
   * int data setting
   * @param filedName String
   * @param value int
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, int value, boolean status, boolean fix) {
    set(filedName, null, value, status, fix);
  }

  /**
   * String data setting
   * @param filedName String
   * @param value String
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, String value, boolean status, boolean fix) {
    set(filedName, null, value, status, fix);
  }

  /**
   * float data setting
   * @param filedName String
   * @param value float
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, float value, boolean status, boolean fix) {
    set(filedName, null, value, status, fix);
  }

  /**
   * long data setting
   * @param filedName String
   * @param value long
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, long value, boolean status, boolean fix) {
    set(filedName, null, value, status, fix);
  }

  /**
   * double data setting
   * @param filedName String
   * @param value double
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, double value, boolean status, boolean fix) {
    set(filedName, null, value, status, fix);
  }

  /**
   * object data setting
   * @param filedName String
   * @param value Object
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, Object value, boolean status, boolean fix) {
    set(filedName, null, value, status, fix);
  }

  /**
   * Object setting
   * @param filedName String
   * @param valueSQL String
   * @param value Object
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, String valueSQL, Object value,
                  boolean status, boolean fix) {
    QueryVO vo = new QueryVO();
    checkVcValue(filedName);
    if (valueSQL != null && valueSQL.length() > 0) {
      if (valueSQL.indexOf("?") > -1) {

        vo.setFiled(filedName);
        vo.setValue(value);
        vo.setStatus(status);
        vo.setFix(fix);
        vo.setValueSQL(valueSQL);
      }
      else {
        vo.setFiled(filedName);
        vo.setValue(valueSQL);
        vo.setStatus(status);
        vo.setFix(true);
        vo.setValueSQL(null);
      }
    }
    else {
      vo.setFiled(filedName);
      vo.setValue(value);
      vo.setStatus(status);
      vo.setFix(fix);
      vo.setValueSQL(valueSQL);
    }

    vc.add(vo);
  }

  /**
   * int setting
   * @param filedName String
   * @param valueSQL String
   * @param value int
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, String valueSQL, int value, boolean status,
                  boolean fix) {
    QueryVO vo = new QueryVO();
    checkVcValue(filedName);
    if (valueSQL != null && valueSQL.length() > 0) {
      if (valueSQL.indexOf("?") > -1) {
        vo.setFiled(filedName);
        vo.setValue(value);
        vo.setStatus(status);
        vo.setFix(fix);
        vo.setValueSQL(valueSQL);
      }
      else {
        vo.setFiled(filedName);
        vo.setValue(valueSQL);
        vo.setStatus(status);
        vo.setFix(true);
        vo.setValueSQL(null);
      }
    }
    else {
      vo.setFiled(filedName);
      vo.setValue(value);
      vo.setStatus(status);
      vo.setFix(fix);
      vo.setValueSQL(valueSQL);
    }
    vc.add(vo);
  }

  /**
   * double setting
   * @param filedName String
   * @param valueSQL String
   * @param value double
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, String valueSQL, double value,
                  boolean status, boolean fix) {
    QueryVO vo = new QueryVO();
    checkVcValue(filedName);
    if (valueSQL != null && valueSQL.length() > 0) {
      if (valueSQL.indexOf("?") > -1) {
        vo.setFiled(filedName);
        vo.setValue(value);
        vo.setStatus(status);
        vo.setFix(fix);
        vo.setValueSQL(valueSQL);
      }
      else {
        vo.setFiled(filedName);
        vo.setValue(valueSQL);
        vo.setStatus(status);
        vo.setFix(true);
        vo.setValueSQL(null);
      }
    }
    else {
      vo.setFiled(filedName);
      vo.setValue(value);
      vo.setStatus(status);
      vo.setFix(fix);
      vo.setValueSQL(valueSQL);
    }
    vc.add(vo);
  }

  /**
   * float setting
   * @param filedName String
   * @param valueSQL String
   * @param value float
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, String valueSQL, float value,
                  boolean status, boolean fix) {
    QueryVO vo = new QueryVO();
    checkVcValue(filedName);
    if (valueSQL != null && valueSQL.length() > 0) {
      if (valueSQL.indexOf("?") > -1) {
        vo.setFiled(filedName);
        vo.setValue(value);
        vo.setStatus(status);
        vo.setFix(fix);
        vo.setValueSQL(valueSQL);
      }
      else {
        vo.setFiled(filedName);
        vo.setValue(valueSQL);
        vo.setStatus(status);
        vo.setFix(true);
        vo.setValueSQL(null);
      }
    }
    else {
      vo.setFiled(filedName);
      vo.setValue(value);
      vo.setStatus(status);
      vo.setFix(fix);
      vo.setValueSQL(valueSQL);
    }
    vc.add(vo);
  }

  /**
   * long setting
   * @param filedName String
   * @param valueSQL String
   * @param value long
   * @param status boolean
   * @param fix boolean
   */
  public void set(String filedName, String valueSQL, long value, boolean status,
                  boolean fix) {
    QueryVO vo = new QueryVO();
    checkVcValue(filedName);
    if (valueSQL != null && valueSQL.length() > 0) {
      if (valueSQL.indexOf("?") > -1) {
        vo.setFiled(filedName);
        vo.setValue(value);
        vo.setStatus(status);
        vo.setFix(fix);
        vo.setValueSQL(valueSQL);
      }
      else {
        vo.setFiled(filedName);
        vo.setValue(valueSQL);
        vo.setStatus(status);
        vo.setFix(true);
        vo.setValueSQL(null);
      }
    }
    else {
      vo.setFiled(filedName);
      vo.setValue(value);
      vo.setStatus(status);
      vo.setFix(fix);
      vo.setValueSQL(valueSQL);
    }
    vc.add(vo);
  }

  /**
   * �����͸� insert �մϴ�.
   * @param conn Connection DB Connection ���۷���
   * @param tableName String : ���̺��?
   * @throws Exception : SQL �۾����� ��������
   * @return int
   */
  public int insert(Connection conn, String tableName) throws Exception {
    return processOneSQL(conn, 'I', tableName);
  }

  /**
   * �����͸� update �մϴ�.
   * @param tableName String : ���̺��?
   * @param conn Connection : DB Connection ���۷���
   * @throws Exception : SQL �۾����� ��������
   * @return int :
   */
  public int update(Connection conn, String tableName) throws Exception {
    return processOneSQL(conn, 'U', tableName);
  }

  /**
   * �����͸� delete �մϴ�.
   * @param tableName String : ���̺��?
   * @param conn Connection : DB Connection ���۷���
   * @throws Exception : SQL �۾����� ��������
   * @return int :
   */
  public int delete(Connection conn, String tableName) throws Exception {
    return processOneSQL(conn, 'D', tableName);
  }

  /**
   * SQL�� �����մϴ�.
   * @param conn Connection
   * @param mode char
   * @param tableName String
   * @throws Exception
   * @return int
   */
  private int processOneSQL(Connection conn, char mode, String tableName) throws
      Exception {
    int effectRowsCount = -1;

    QuerySQLVO argQuerySQLVO = new QuerySQLVO();
    if (vc == null)throw new Exception("[processOneSQL] vc is null");
    QueryVO[] arrQueryVO = new QueryVO[vc.size()];
    vc.copyInto(arrQueryVO);
    vc.clear();

    argQuerySQLVO.setArrQueryVO(arrQueryVO);
    argQuerySQLVO.setMode(mode);
    argQuerySQLVO.setTableName(tableName);

    effectRowsCount = executeUpdate(conn, argQuerySQLVO);
    return effectRowsCount;
  }

  /**
   * �ܺ��� �Է����� �ۼ��� ������ ������ executeUpdate�� �����մϴ�.
   * @param conn Connection
   * @throws Exception
   * @return int
   */
  public int executeUpdate(Connection conn) throws Exception {

    if (vc != null) {
      QueryVO[] arrQueryVO = new QueryVO[vc.size()];
      vc.copyInto(arrQueryVO);
      extendQuerySQLVO.setArrQueryVO(arrQueryVO);
    }

    return executeUpdate(conn, extendQuerySQLVO);
  }

  /**
   * �������� ���� ������Ʈ�� �����Ͽ� ���? count�� �����մϴ�.
   * @param conn Connection
   * @param queryString String
   * @throws Exception
   * @return int
   */
  public int executeUpdate(Connection conn, String queryString) throws
      Exception {
    setSQL(queryString);

    return executeUpdate(conn, extendQuerySQLVO);
  }

  /**
   * �����? setVal, setSQL �޼ҵ忡 ���� insert, update �������� ����
   * @param conn Connection
   * @throws Exception
   */
  private int executeUpdate(Connection conn, QuerySQLVO argQuerySQLVO) throws
      Exception {
    PreparedStatement pstmt = null;
    int effectRowsCount = -1;

    try {
      if (argQuerySQLVO == null) {
        throw new Exception("[executeUpdate] argQuerySQLVO ��ü ������ �����ϴ�");
      }

      // �ش纯���� true �̸� �������� ����մϴ�?.
      if (isRunSQLView) printSQLInfo(getStmtSQL(argQuerySQLVO));

      pstmt = parsePstmtSQL(conn, argQuerySQLVO);
      effectRowsCount = pstmt.executeUpdate();
      close(pstmt);
    }
    catch (Exception ex) {
      printSQLInfo(ex.toString());
      if (argQuerySQLVO != null) {
        argQuerySQLVO.setStatementSql(getStmtSQL(argQuerySQLVO));
        printSQLInfo(argQuerySQLVO.getStatementSql());
      }
      close(pstmt);
      throw ex;
    }
    finally {
      argQuerySQLVO = null;
      close(pstmt);
    }
    return effectRowsCount;
  }

  /**
   * select ������ ������ pstmt SQL ����
   * @param pstmtSQL String
   */
  public void setSQL(String pstmtSQL) {
    checkVOObjects();
    extendQuerySQLVO = new QuerySQLVO();
    extendQuerySQLVO.setPreparedStatementSql(pstmtSQL);
    extendQuerySQLVO.setMode('E');
  }

  /**
   * ���ο� Ÿ���� SQL �۾���û�� QuerySQLVO�� QueryVO ��ü�� ���¸� üũ�Ͽ� ���忩�θ� Ȯ���մϴ�.
   */
  private void checkVOObjects() {
//    QueryVO[] arrQueryVO = null;

    // SQLVO�� null �̰ų� �����۾��� ���� Ÿ���� �������?
    // �� PreparedStatement SQL ����۾���? ���? ���? �����ϰ� ���ο������? ��Ŀ�� ���߾� �۾�
    // PreparedStatement SQL ����۾���? �ٷ� �������� �ٷ� �����Ͽ��� �ϸ�
    // �ٸ� ��ġ�� �۾��� �����ÿ��� �ݵ��? �ٸ� �޼ҵ忡 ���ؼ� Vector�� ��ϵǾ�� �Ѵ�.
    if (extendQuerySQLVO != null &&
        (extendQuerySQLVO.getMode() == 'X' || extendQuerySQLVO.getMode() == 'E')) {
      extendQuerySQLVO = null;
    }
//
//    if (extendQuerySQLVO != null && vc != null) {
//      arrQueryVO = new QueryVO[vc.size()];
//      vc.copyInto(arrQueryVO);
//      querySQLVO.setArrQueryVO(arrQueryVO);
//      vcQuerySQLVO.add(querySQLVO);
//      querySQLVO = null;
//    }

    if (vc != null) vc.clear();
  }

  /**
   * ������ ������ Ÿ�԰��� ������ ������������ Ȯ���մϴ�.
   * @param fieldInfo String
   */
  private void checkVcValue(String fieldInfo) {
    if (vc == null) vc = new Vector();
    if (vc.size() > 0 && vc.lastElement() != null) {

      // ���� ��ϵ�? �����Ϳ��� �ʵ������� ������..
      if (fieldInfo != null && fieldInfo.length() > 0) {
        // ���� �����Ϳ� �ʵ������� ������.. ���� QueryVO ���� ����
        if ( ( (QueryVO) vc.lastElement()).getFiled() == null
            || ( (QueryVO) vc.lastElement()).getFiled().length() == 0) {
          vc.clear();
        }
        // ���� ����Ϸ���? �����Ϳ� �ʵ������� ������ (String�� ���� PreparedStatement SQL �� �۾���)
      }
      else {
        // ������������ �������ܿ� �ʵ������� ������ �������� ���? ������
        if ( ( (QueryVO) vc.lastElement()).getFiled() != null
            && ( (QueryVO) vc.lastElement()).getFiled().length() > 0) {
          vc.clear();
        }
      }
    }
  }

  /**
   * ������ set method�� �̿����� �ʰ� �ܺ��� String ���� ��ϵ�? PreparedStatement SQL �ÿ���... ���� �����մϴ�.(select, update,delete, insert ��..)(
   * ���Լ��� �� Ÿ���� ���� �� �Է� Args�� Ÿ���� �������� �����մϴ�.
   * @param value String
   */
  public void setVal(String value) {
    checkVcValue("");
    set("", value, true);
  }

  /**
   * PreparedStatement SQL (select , insert, delete, update) �����? ? �� ����.. ���� �����մϴ�.
   * @param value int
   */
  public void setVal(int value) {
    checkVcValue("");
    set("", value, true);
  }

  /**
   * PreparedStatement SQL (select , insert, delete, update) �����? ? �� ����.. ���� �����մϴ�.
   * @param value long
   */
  public void setVal(long value) {
    checkVcValue("");
    set("", value, true);
  }

  /**
   * PreparedStatement SQL (select , insert, delete, update) �����? ? �� ����.. ���� �����մϴ�.
   * @param value float
   */
  public void setVal(float value) {
    checkVcValue("");
    set("", value, true);
  }

  /**
   * PreparedStatement SQL (select , insert, delete, update) �����? ? �� ����.. ���� �����մϴ�.
   * @param value double
   */
  public void setVal(double value) {
    checkVcValue("");
    set("", value, true);
  }

  /**
   * select �ÿ���... ���� �����մϴ�.
   * @param value Object
   */
  public void setVal(Object value) {
    checkVcValue("");
    set("", value, true);
  }

  /**
   * �����? setVal, setSQL �޼ҵ忡 ���� select �������� ����
   * @param conn Connection
   * @throws Exception
   * @return RowSet
   */
  public RowSet select(Connection conn) throws Exception {
    return executeQuery(conn, extendQuerySQLVO);
  }

  /**
   * �ٷ� ������ ��ϵ�? �ϳ��� �ش� SQL�� �����Ͽ� rowset�� �����մϴ�.
   * @param conn Connection
   * @throws Exception
   * @return RowSet
   */
  public RowSet executeQuery(Connection conn) throws
      Exception {
    return executeQuery(conn, extendQuerySQLVO);
  }

  /**
   * �ش� SQL�� �����Ͽ� rowset�� �����մϴ�.
   * @param conn Connection
   * @param extendQuerySQL String
   * @throws Exception
   * @return RowSet
   */
  public RowSet executeQuery(Connection conn, String extendQuerySQL) throws
      Exception {
    setSQL(extendQuerySQL);
    return executeQuery(conn, extendQuerySQLVO);
  }

  /**
   * PreparedStatement �� args ������ rowset���� �����մϴ�.
   * @param argsPstmt PreparedStatement
   * @throws Exception
   * @return RowSet
   */
  public RowSet executeQuery(PreparedStatement argsPstmt) throws
      Exception {
    return executeQuery(argsPstmt);
  }

  /**
   * �ش� SQL���� ������ ���������� �����մϴ�.
   * @param conn Connection
   * @param argsQuerySQLVO QuerySQLVO
   * @throws Exception
   * @return RowSet
   */
  private RowSet executeQuery(Connection conn, QuerySQLVO argsQuerySQLVO) throws
      Exception {
    RowSet rs = null;
    try {
      if (argsQuerySQLVO == null)throw new Exception("QuerySQLVO is null");
      if (vc != null) {
        QueryVO[] arrQueryVO = new QueryVO[vc.size()];
        vc.copyInto(arrQueryVO);
        argsQuerySQLVO.setArrQueryVO(arrQueryVO);
      }

      rs = executeQueryPstmt(parsePstmtSQL(conn, argsQuerySQLVO));
    }
    catch (Exception ex) {
      printSQLInfo(ex.toString());
      if (argsQuerySQLVO != null) {
        argsQuerySQLVO.setStatementSql(getStmtSQL(argsQuerySQLVO));
        printSQLInfo(argsQuerySQLVO.getStatementSql());
      }
      ex.printStackTrace();
      throw ex;
    }

    return rs;
  }

  /**
   * QuerySQLVO ������ �Ľ��Ͽ� �ش� PreparedStatement ��ü�� �����մϴ� (���ε� ������ ���� ����)
   * @param conn Connection
   * @param querySQLVO QuerySQLVO
   * @return PreparedStatement
   */
  private PreparedStatement parsePstmtSQL(Connection conn,
                                          QuerySQLVO querySQLVO) throws
      Exception {
    PreparedStatement pstmt = null;
    try {
      if (conn == null)throw new Exception(
          "[PreparedStatement] Connection is null");
      if (querySQLVO == null)throw new Exception(
          "[PreparedStatement] querySQLVO is null");
      querySQLVO.setPreparedStatementSql(getQueryString(querySQLVO));

      // �ش纯���� true �̸� �������� ����մϴ�?.
      if (isRunSQLView) printSQLInfo(getStmtSQL(querySQLVO));

      pstmt = conn.prepareStatement(querySQLVO.getPreparedStatementSql());
      pstmt = setDataValues(pstmt, querySQLVO);
    }
    catch (Exception ex) {
      throw ex;
    }

    return pstmt;
  }

  /**
   * select ���� �κ��� ó���մϴ�.
   * @param conn Connection : Ŀ�ؼ� ����
   * @param strSQL String : ������Ʈ��
   * @throws Exception : SQL ���� ���ܻ�Ȳ
   * @return RowSet : �ο��?.~
   */
  public RowSet select(Connection conn, String strSQL) throws Exception {
    setSQL(strSQL);
    return executeQuery(conn, extendQuerySQLVO);
  }

  /**
   * pstmt �� ������ ����Ʈ �� �ɴϴ�.
   * @param pstmt PreparedStatement
   * @throws Exception
   * @return RowSet
   */
  public RowSet executeQueryPstmt(PreparedStatement pstmt) throws
      Exception {
    CachedRowSet crs = null;

    try {
      crs = new CachedRowSet();
      crs.populate(pstmt.executeQuery());
      crs.beforeFirst();
      close(pstmt);

    }
    catch (Exception ex) {
      close(pstmt);
      throw ex;
    }
    finally {
      close(pstmt);
    }

    return (RowSet) crs;
  }

  /**
   * �ܺο� ���� PreparedStatement �������� Statement ������ �����Ͽ� �����մϴ�. (����׿�?)
   * @return String
   */
  public String getExecuteQuerySQL() {
    return getExecuteSQL();
  }

  /**
   * �ܺο� ���� PreparedStatement �������� Statement ������ �����Ͽ� �����մϴ�. (����׿�?)
   * @return String
   */
  public String getExecuteUpdateSQL() {
    return getExecuteSQL();
  }

  /**
   * �ܺο� ���� PreparedStatement �������� Statement ������ �����Ͽ� �����մϴ�. (����׿�?)
   * @return String
   */
  private String getExecuteSQL() {
    if (extendQuerySQLVO != null) {
      if (vc != null) {
        QueryVO[] arrQueryVO = new QueryVO[vc.size()];
        vc.copyInto(arrQueryVO);
        extendQuerySQLVO.setArrQueryVO(arrQueryVO);
      }
      return getStmtSQL(extendQuerySQLVO);
    }
    else {
      return null;
    }
  }

  /**
   * Statement type Insert �������� �����մϴ�
   * @param tableName String
   */
  public String getInsertSQL(String tableName) {
    String stmtSQL = null;
    QuerySQLVO argsQuerySQLVO = new QuerySQLVO();
    if (vc != null) {
      QueryVO[] arrQueryVO = new QueryVO[vc.size()];
      vc.copyInto(arrQueryVO);
      argsQuerySQLVO.setArrQueryVO(arrQueryVO);
    }

    try {
      argsQuerySQLVO.setMode('I');
      argsQuerySQLVO.setTableName(tableName);
      argsQuerySQLVO.setPreparedStatementSql(getQueryString(argsQuerySQLVO));
      stmtSQL = getStmtSQL(argsQuerySQLVO);
    }
    catch (Exception ex) {
      printSQLInfo(ex.toString());
      ex.printStackTrace();
      stmtSQL = "";
    }

    return stmtSQL;
  }

  /**
   * Statement type Update �������� �����մϴ�.
   * @param table String
   */
  public String getUpdateSQL(String tableName) {
    String stmtSQL = null;

    QuerySQLVO argsQuerySQLVO = new QuerySQLVO();
    if (vc != null) {
      QueryVO[] arrQueryVO = new QueryVO[vc.size()];
      vc.copyInto(arrQueryVO);
      argsQuerySQLVO.setArrQueryVO(arrQueryVO);
    }

    try {
      argsQuerySQLVO.setMode('U');
      argsQuerySQLVO.setTableName(tableName);
      argsQuerySQLVO.setPreparedStatementSql(getQueryString(argsQuerySQLVO));
      stmtSQL = getStmtSQL(argsQuerySQLVO);
    }
    catch (Exception ex) {
      printSQLInfo(ex.toString());
      ex.printStackTrace();
      stmtSQL = "";
    }
    return stmtSQL;
  }

  /**
   * Statement type Delete �������� �����մϴ�.
   * @param table String
   */
  public String getDeleteSQL(String tableName) {
    String stmtSQL = null;

    QuerySQLVO argsQuerySQLVO = new QuerySQLVO();
    if (vc != null) {
      QueryVO[] arrQueryVO = new QueryVO[vc.size()];
      vc.copyInto(arrQueryVO);
      argsQuerySQLVO.setArrQueryVO(arrQueryVO);
    }

    try {
      argsQuerySQLVO.setMode('D');
      argsQuerySQLVO.setTableName(tableName);
      argsQuerySQLVO.setPreparedStatementSql(getQueryString(argsQuerySQLVO));
      stmtSQL = getStmtSQL(argsQuerySQLVO);
    }
    catch (Exception ex) {
      printSQLInfo(ex.toString());
      ex.printStackTrace();
      stmtSQL = "";
    }

    return stmtSQL;
  }

  /**
   * set data�� PreparedStatements ������ �ش� ���������� ������ݴϴ�?.
   * @param querySQLVO QuerySQLVO
   * @return String
   */
  private String getQueryString(QuerySQLVO querySQLVO) throws Exception {

    QueryVO[] arrQueryVO = null;
    StringBuffer sb = new StringBuffer();
    StringBuffer sb1 = new StringBuffer();
    StringBuffer sb2 = new StringBuffer();

    char jobMode = querySQLVO.getMode();

    try {
      if (querySQLVO == null)throw new Exception(
          "[getQueryString] querySQLVO is null");
      if (!querySQLVO.isSetPstmtSQL()) {
        sb.append(querySQLVO.getPreparedStatementSql());
      }
      else {
        switch (jobMode) {

          //****************************************************/
          /*
           * ����϶�?..
           */
          case 'I':

            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), false);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {

                /*
                 * ���̺� �ʵ�κ�?
                 */
                if (sb1.length() > 0) {
                  sb1.append(", ");
                }
                sb1.append(arrQueryVO[i].getFiled());

                /*
                 * Values �κ�
                 */
                if (sb2.length() > 0) {
                  sb2.append(", ");
                }

                // ������ ���ϰ��?..  ex : SYSDATE
                if (arrQueryVO[i].isFix()) {
                  sb2.append(arrQueryVO[i].getValue());
                }
                else {
                  // �ش� ���ý��� ���յ� value�� �ְ� �Ǵ°��?...
                  if (arrQueryVO[i].getValueSQL() != null
                      && arrQueryVO[i].getValueSQL().length() > 0) {
                    sb2.append(arrQueryVO[i].getValueSQL());
                  }
                  else {
                    sb2.append("?");
                  }
                }
              } // end of for

              sb.append(" insert into ")
                  .append(querySQLVO.getTableName())
                  .append("(").append(sb1.toString())
                  .append(") values(")
                  .append(sb2.toString())
                  .append(")");
            }
            break;

            //****************************************************/
            /*
             * ������Ʈ �϶�..
             */
          case 'U':

            /*
             * ������Ʈ ������ �κ�
             */
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), false);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {

                if (sb1.length() > 0) {
                  sb1.append(", ");
                }

                if (arrQueryVO[i].isFix()) {
                  sb1.append(arrQueryVO[i].getFiled()).append("=").append(
                      arrQueryVO[i].getValue());
                }
                else {
                  if (arrQueryVO[i].getValueSQL() != null &&
                      arrQueryVO[i].getValueSQL().length() > 0) {
                    sb1.append(arrQueryVO[i].getFiled()).append("=").append(
                        arrQueryVO[i].getValueSQL());
                  }
                  else {
                    sb1.append(arrQueryVO[i].getFiled()).append("=?");
                  }
                }

              } // end of for
            }
            arrQueryVO = null;
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);

            /*
             * ������ �κ�
             */
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {

                /*
                 * ���̺� �ʵ�κ�?
                 */
                if (sb2.length() > 0) sb2.append(" and ");
                else sb2.append(" where ");

                if (arrQueryVO[i].isFix()) {
                  sb2.append(arrQueryVO[i].getFiled()).append("=").append(
                      arrQueryVO[i].getValue());
                }
                else {
                  if (arrQueryVO[i].getValueSQL() != null &&
                      arrQueryVO[i].getValueSQL().length() > 0) {
                    sb2.append(arrQueryVO[i].getFiled()).append("=").append(
                        arrQueryVO[i].getValueSQL());
                  }
                  else {
                    sb2.append(arrQueryVO[i].getFiled()).append("=?");
                  }
                }

              } // end of for

              sb.append(" update ")
                  .append(querySQLVO.getTableName()).append(" set ")
                  .append(sb1.toString())
                  .append(sb2.toString());
            }
            break;

            //****************************************************/
            /*
             * �����϶�...
             */
          case 'D':
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);

            /*
             * ������ �κ�
             */
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {

                /*
                 * ���̺� �ʵ�κ�?
                 */
                if (sb2.length() > 0) sb2.append(" and ");
                else sb2.append(" where ");

                if (arrQueryVO[i].isFix()) {
                  sb2.append(arrQueryVO[i].getFiled()).append("=").append(
                      arrQueryVO[i].getValue());
                }
                else {
                  if (arrQueryVO[i].getValueSQL() != null &&
                      arrQueryVO[i].getValueSQL().length() > 0) {
                    sb2.append(arrQueryVO[i].getFiled()).append("=").append(
                        arrQueryVO[i].getValueSQL());
                  }
                  else {
                    sb2.append(arrQueryVO[i].getFiled()).append("=?");
                  }
                }

              } // end of for
              sb.append(" delete from ").append(querySQLVO.getTableName()).
                  append(
                  sb2.toString());
            } // end of if(arrQueryVO != null)
            break;

            // PreparedStatement SQL�� ����Ͽ���? ���?
          case 'E':
            sb.append(querySQLVO.getPreparedStatementSql());
            break;
        }
      } // end of else [if(!querySQLVO.isSetPstmtSQL())]
    }
    catch (Exception ex) {
      printSQLInfo(ex.toString());
      ex.printStackTrace();
      throw ex;
    }

    return sb.toString();
  }

  /**
   * PreparedStatement �� �ش� ���� �����մϴ�.
   * @param pstmt PreparedStatement
   * @param querySQLVO QuerySQLVO
   * @throws Exception
   * @return PreparedStatement
   */
  private PreparedStatement setDataValues(PreparedStatement pstmt,
                                          QuerySQLVO querySQLVO) throws
      Exception {

    QueryVO[] arrQueryVO = null;

    int i = 0;
    int j = 0;
    int index = 1;
    char mode = querySQLVO.getMode();
    StringReader stringReader = null;

    try {

      if (pstmt == null)throw new Exception(
          "[setDataValues] PreparedStatement is null");
      if (pstmt == null)throw new Exception(
          "[setDataValues] querySQLVO is null");

      try {
        pstmt.clearParameters();
      }
      catch (Exception ex) {
      }

      switch (mode) {

        case 'I':
          index = 1;
          arrQueryVO = getObject(querySQLVO.getArrQueryVO(), false);
          if (arrQueryVO != null) {
            for (i = 0; i < arrQueryVO.length; i++) {

              if (!arrQueryVO[i].isFix()) {
                if (arrQueryVO[i].getValue() instanceof String) {

                  if (arrQueryVO[i].getValue().toString().getBytes().length >
                      1333) {
                    stringReader = null;
                    stringReader = new StringReader(arrQueryVO[i].getValue().
                        toString());
                    pstmt.setCharacterStream(index, stringReader,
                                             arrQueryVO[i].getValue().toString().
                                             length());
                  }
                  else {
                    pstmt.setString(index, arrQueryVO[i].getValue().toString());
                  }

                }
                else if (arrQueryVO[i].getValue() instanceof Integer) {
                  pstmt.setInt(index,
                               ( (Integer) arrQueryVO[i].getValue()).intValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Double) {
                  pstmt.setDouble(index,
                                  ( (Double) arrQueryVO[i].getValue()).
                                  doubleValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Long) {
                  pstmt.setLong(index,
                                ( (Long) arrQueryVO[i].getValue()).longValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Float) {
                  pstmt.setFloat(index,
                                 ( (Float) arrQueryVO[i].getValue()).floatValue());
                }
                else {
                  pstmt.setObject(index, arrQueryVO[i].getValue());
                }
                index++;
              } // end of if(!arrQueryVO[i].isFix())
            } // end of for
          }
          break;

        case 'U':
          arrQueryVO = getObject(querySQLVO.getArrQueryVO(), false);
          index = 1;
          if (arrQueryVO != null) {
            for (i = 0; i < arrQueryVO.length; i++) {
              if (!arrQueryVO[i].isFix()) {

                if (arrQueryVO[i].getValue() instanceof String) {
                  if (arrQueryVO[i].getValue().toString().getBytes().length >
                      1333) {
                    stringReader = null;
                    stringReader = new StringReader(arrQueryVO[i].getValue().
                        toString());
                    pstmt.setCharacterStream(index, stringReader,
                                             arrQueryVO[i].getValue().toString().
                                             length());
                  }
                  else {
                    pstmt.setString(index, arrQueryVO[i].getValue().toString());
                  }
                }
                else if (arrQueryVO[i].getValue() instanceof Integer) {
                  pstmt.setInt(index,
                               ( (Integer) arrQueryVO[i].getValue()).intValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Double) {
                  pstmt.setDouble(index,
                                  ( (Double) arrQueryVO[i].getValue()).
                                  doubleValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Long) {
                  pstmt.setLong(index,
                                ( (Long) arrQueryVO[i].getValue()).longValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Float) {
                  pstmt.setFloat(index,
                                 ( (Float) arrQueryVO[i].getValue()).floatValue());
                }
                else {
                  pstmt.setObject(index, arrQueryVO[i].getValue());
                }
                index++;
              } // end of if(!arrQueryVO[i].isFix())
            } // end of for
          }
          arrQueryVO = null;
          arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);

          j = 0;
          if (arrQueryVO != null) {
            for (j = 0; j < arrQueryVO.length; j++) {

              if (!arrQueryVO[j].isFix()) {

                if (arrQueryVO[j].getValue() instanceof String) {
//                  stringReader = null;
//                  stringReader = new StringReader(arrQueryVO[i].getValue().toString());
//                  pstmt.setCharacterStream(index, stringReader, arrQueryVO[i].getValue().toString().length());
                  pstmt.setString(index, arrQueryVO[j].getValue().toString());
                }
                else if (arrQueryVO[j].getValue() instanceof Integer) {
                  pstmt.setInt(index,
                               ( (Integer) arrQueryVO[j].getValue()).intValue());
                }
                else if (arrQueryVO[j].getValue() instanceof Double) {
                  pstmt.setDouble(index,
                                  ( (Double) arrQueryVO[j].getValue()).
                                  doubleValue());
                }
                else if (arrQueryVO[j].getValue() instanceof Long) {
                  pstmt.setLong(index,
                                ( (Long) arrQueryVO[j].getValue()).longValue());
                }
                else if (arrQueryVO[j].getValue() instanceof Float) {
                  pstmt.setFloat(index,
                                 ( (Float) arrQueryVO[j].getValue()).floatValue());
                }
                else {
                  pstmt.setObject(index, arrQueryVO[j].getValue());
                }
                index++;
              } // end of if (!arrQueryVO[i].isFix())
            } // end of for
          }
          break;

        case 'D':
          index = 1;
          arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);
          if (arrQueryVO != null) {
            for (i = 0; i < arrQueryVO.length; i++) {
              if (!arrQueryVO[i].isFix()) {
                if (arrQueryVO[i].getValue() instanceof String) {
//                  stringReader = null;
//                  stringReader = new StringReader(arrQueryVO[i].getValue().toString());
//                  pstmt.setCharacterStream(index, stringReader, arrQueryVO[i].getValue().toString().length());
                  pstmt.setString(index, arrQueryVO[i].getValue().toString());
                }
                else if (arrQueryVO[i].getValue() instanceof Integer) {
                  pstmt.setInt(index,
                               ( (Integer) arrQueryVO[i].getValue()).intValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Double) {
                  pstmt.setDouble(index,
                                  ( (Double) arrQueryVO[i].getValue()).
                                  doubleValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Long) {
                  pstmt.setLong(index,
                                ( (Long) arrQueryVO[i].getValue()).longValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Float) {
                  pstmt.setFloat(index,
                                 ( (Float) arrQueryVO[i].getValue()).floatValue());
                }
                else {
                  pstmt.setObject(index, arrQueryVO[i].getValue());
                }
                index++;
              } // end of if (!arrQueryVO[i].isFix())
            } // end of for
          }
          break;

        case 'E':
          index = 1;
          arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);
          if (arrQueryVO != null) {
            for (i = 0; i < arrQueryVO.length; i++) {
              if (!arrQueryVO[i].isFix()) {
                if (arrQueryVO[i].getValue() instanceof String) {
//                  stringReader = null;
//                  stringReader = new StringReader(arrQueryVO[i].getValue().toString());
//                  pstmt.setCharacterStream(index, stringReader, arrQueryVO[i].getValue().toString().length());
                  pstmt.setString(index, arrQueryVO[i].getValue().toString());
                }
                else if (arrQueryVO[i].getValue() instanceof Integer) {
                  pstmt.setInt(index,
                               ( (Integer) arrQueryVO[i].getValue()).intValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Double) {
                  pstmt.setDouble(index,
                                  ( (Double) arrQueryVO[i].getValue()).
                                  doubleValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Long) {
                  pstmt.setLong(index,
                                ( (Long) arrQueryVO[i].getValue()).longValue());
                }
                else if (arrQueryVO[i].getValue() instanceof Float) {
                  pstmt.setFloat(index,
                                 ( (Float) arrQueryVO[i].getValue()).floatValue());
                }
                else {
                  pstmt.setObject(index, arrQueryVO[i].getValue());
                }
                index++;
              } // end of if (!arrQueryVO[i].isFix())
            } // end of for
          }
          break;
      }
    }
    catch (Exception ex) {
      throw ex;
    }

    return pstmt;
  }

  /**
   * �ش� insert, update, delete ���� error ǥ���մϴ�.
   * @param msg String
   */
  private void printSQLInfo(String msg) {
    if (isDebugPrintStatementTypeSQL) {
      // MCUP ������ �Ϳ� ����Ǿ�? �־����?
    	System.out.println(msg);
      //Logs.error(999, msg);
    }
  }

  /******************************************************************************
   Close Methods
   ******************************************************************************/
  /**
   * Connection, Statement close
   * @param conn Connection
   * @param stmt Statement
   */
  public void close(Connection conn, Statement stmt, boolean isConnectionCommit) {
    close(conn, null, stmt, null, null, isConnectionCommit);
  }

  /**
   * Connection, PreparedStatement close
   * @param conn Connection
   * @param pstmt PreparedStatement
   */
  public void close(Connection conn, PreparedStatement pstmt,
                    boolean isConnectionCommit) {
    close(conn, pstmt, null, null, null, isConnectionCommit);
  }

  /**
   * Connection, PreparedStatement, ResultSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rts ResultSet
   */
  public void close(Connection conn, PreparedStatement pstmt, ResultSet rts,
                    boolean isConnectionCommit) {
    close(conn, pstmt, null, rts, null, isConnectionCommit);
  }

  /**
   * Connection, Statement, ResultSet close
   * @param conn Connection
   * @param stmt Statement
   * @param rts ResultSet
   */
  public void close(Connection conn, Statement stmt, ResultSet rts,
                    boolean isConnectionCommit) {
    close(conn, null, stmt, rts, null, isConnectionCommit);
  }

  /**
   * Connection, PreparedStatement, RowSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rs RowSet
   */
  public void close(Connection conn, PreparedStatement pstmt, RowSet rs,
                    boolean isConnectionCommit) {
    close(conn, pstmt, null, null, rs, isConnectionCommit);
  }

  /**
   * Connection, Statement, RowSet close
   * @param conn Connection
   * @param stmt Statement
   * @param rs RowSet
   */
  public void close(Connection conn, Statement stmt, RowSet rs,
                    boolean isConnectionCommit) {
    close(conn, null, stmt, null, rs, isConnectionCommit);
  }

  /**
   * Statement, RowSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rs RowSet
   */
  public void close(Statement stmt, RowSet rs, boolean isConnectionCommit) {
    close(null, null, stmt, null, rs, isConnectionCommit);
  }

  /**
   * PreparedStatement, RowSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rs RowSet
   */
  public void close(PreparedStatement pstmt, RowSet rs,
                    boolean isConnectionCommit) {
    close(null, pstmt, null, null, rs, isConnectionCommit);
  }

  /**
   * Connection close
   * @param conn Connection
   * @param rs RowSet
   */
  public void close(Connection conn, RowSet rs, boolean isConnectionCommit) {
    close(conn, null, null, null, rs, isConnectionCommit);
  }

  /**
   * Connection close
   * @param conn Connection
   * @param rs ResultSet
   */
  public void close(Connection conn, ResultSet rs, boolean isConnectionCommit) {
    close(conn, null, null, rs, null, isConnectionCommit);
  }

  /**
   * Connection close
   * @param conn Connection
   */
  public void close(Connection conn, boolean isConnectionCommit) {
    close(conn, null, null, null, null, isConnectionCommit);
  }

  /**
   * PreparedStatement close
   * @param pstmt PreparedStatement
   */
  public void close(PreparedStatement pstmt, boolean isConnectionCommit) {
    close(null, pstmt, null, null, null, isConnectionCommit);
  }

  /**
   * Statement close
   * @param stmt Statement
   */
  public void close(Statement stmt, boolean isConnectionCommit) {
    close(null, null, stmt, null, null, isConnectionCommit);
  }

  /**
   * ResultSet Close method
   * @param rts ResultSet
   */
  public void close(ResultSet rts, boolean isConnectionCommit) {
    close(null, null, null, rts, null, isConnectionCommit);
  }

  /**
   * RowSet Close method
   * @param rs RowSet
   */
  public void close(RowSet rs, boolean isConnectionCommit) {
    close(null, null, null, null, rs, isConnectionCommit);
  }

  /**
   * Connection, Statement close
   * @param conn Connection
   * @param stmt Statement
   */
  public void close(Connection conn, Statement stmt) {
    close(conn, null, stmt, null, null, false);
  }

  /**
   * Connection, PreparedStatement close
   * @param conn Connection
   * @param pstmt PreparedStatement
   */
  public void close(Connection conn, PreparedStatement pstmt) {
    close(conn, pstmt, null, null, null, false);
  }

  /**
   * Connection, PreparedStatement, ResultSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rts ResultSet
   */
  public void close(Connection conn, PreparedStatement pstmt, ResultSet rts) {
    close(conn, pstmt, null, rts, null, false);
  }

  /**
   * Connection, Statement, ResultSet close
   * @param conn Connection
   * @param stmt Statement
   * @param rts ResultSet
   */
  public void close(Connection conn, Statement stmt, ResultSet rts) {
    close(conn, null, stmt, rts, null, false);
  }

  /**
   * Connection, PreparedStatement, RowSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rs RowSet
   */
  public void close(Connection conn, PreparedStatement pstmt, RowSet rs) {
    close(conn, pstmt, null, null, rs, false);
  }

  /**
   * Connection, Statement, RowSet close
   * @param conn Connection
   * @param stmt Statement
   * @param rs RowSet
   */
  public void close(Connection conn, Statement stmt, RowSet rs) {
    close(conn, null, stmt, null, rs, false);
  }

  /**
   * Statement, RowSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rs RowSet
   */
  public void close(Statement stmt, RowSet rs) {
    close(null, null, stmt, null, rs, false);
  }

  /**
   * PreparedStatement, RowSet close
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param rs RowSet
   */
  public void close(PreparedStatement pstmt, RowSet rs) {
    close(null, pstmt, null, null, rs, false);
  }

  /**
   * Connection close
   * @param conn Connection
   * @param rs RowSet
   */
  public void close(Connection conn, RowSet rs) {
    close(conn, null, null, null, rs, false);
  }

  /**
   * Connection close
   * @param conn Connection
   * @param rs ResultSet
   */
  public void close(Connection conn, ResultSet rs) {
    close(conn, null, null, rs, null, false);
  }

  /**
   * Connection close
   * @param conn Connection
   */
  public void close(Connection conn) {
    close(conn, null, null, null, null, false);
  }

  /**
   * PreparedStatement close
   * @param pstmt PreparedStatement
   */
  public void close(PreparedStatement pstmt) {
    close(null, pstmt, null, null, null, false);
  }

  /**
   * Statement close
   * @param stmt Statement
   */
  public void close(Statement stmt) {
    close(null, null, stmt, null, null, false);
  }

  /**
   * ResultSet Close method
   * @param rts ResultSet
   */
  public void close(ResultSet rts) {
    close(null, null, null, rts, null, false);
  }

  /**
   * RowSet Close method
   * @param rs RowSet
   */
  public void close(RowSet rs) {
    close(null, null, null, null, rs, false);
  }

  /**
   * SQL ���� ��ü close �޼ҵ� �Դϴ�.
   * @param conn Connection
   * @param pstmt PreparedStatement
   * @param stmt Statement
   * @param rts ResultSet
   * @param rs RowSet
   */
  public void close(Connection conn, PreparedStatement pstmt, Statement stmt,
                    ResultSet rts, RowSet rs, boolean isConnectionCommit) {

    if (rts != null) {
      try {
        rts.close();
        rts = null;
      }
      catch (Exception ex) {}
    }

    if (rs != null) {
      try {
        rs.close();
        rs = null;
      }
      catch (Exception ex) {}
    }

    if (pstmt != null) {
      try {
        pstmt.close();
        pstmt = null;
      }
      catch (Exception ex) {}
    }

    if (stmt != null) {
      try {
        stmt.close();
        stmt = null;
      }
      catch (Exception ex) {}
    }

    if (conn != null) {

      if (isConnectionCommit) {
        try {
          conn.commit();
        }
        catch (Exception ex) {}

        try {
          if (!conn.getAutoCommit()) {
            conn.setAutoCommit(true);
          }
        }

        catch (Exception ex) {}
      }

      try {
        conn.close();
      }
      catch (Exception ex) {}
    }

  }

  /**
   * Connection ��ü�� �����͸� rollback �մϴ�.
   * @param conn Connection
   */
  public void rollback(Connection conn) {
    if (conn != null) {
      try {
        conn.rollback();
      }
      catch (Exception ex) {
      }
    }
  }

  /**
   * setAutoCommitTrue
   * @param conn Connection
   */
  public void setAutoCommitTrue(Connection conn) {
    if (conn != null) {
      try {
        conn.setAutoCommit(true);
      }
      catch (Exception ex) {
      }
    }
  }

  /******************************************************************************
   Debug �κ�
   ******************************************************************************/

  /**
   * �����? ��������.. PreparedStatement -> Statement ������ ���������� ��ȯ�Ͽ� �����մϴ�.
   * @param mode char
   * @param arrQueryVO QueryVO[]
   * @return String
   */
  private String getStmtSQL(QuerySQLVO querySQLVO) {

    String stmtSQL = null;
    QueryVO[] arrQueryVO = null;
    StringBuffer sb = new StringBuffer();

    if (querySQLVO != null
        && querySQLVO.getPreparedStatementSql() != null
        && querySQLVO.getPreparedStatementSql().length() > 0) {

      if (!querySQLVO.isSetStmtSQL()) {
        stmtSQL = querySQLVO.getStatementSql();
      }
      else {
        stmtSQL = querySQLVO.getPreparedStatementSql();

        switch (querySQLVO.getMode()) {
          case 'I':
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), false);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {
                if (!arrQueryVO[i].isFix())
                  if (arrQueryVO[i].getValue() instanceof Integer
                      || arrQueryVO[i].getValue() instanceof Long
                      || arrQueryVO[i].getValue() instanceof Double
                      || arrQueryVO[i].getValue() instanceof Float
                      ) {
                    stmtSQL = replaceFirst(stmtSQL, "?",
                                           arrQueryVO[i].getValue().toString());
                  }
                  else {
                    sb.delete(0, sb.length());
                    sb.append("'");
                    sb.append(arrQueryVO[i].getValue().toString());
                    sb.append("'");
                    stmtSQL = replaceFirst(stmtSQL, "?", sb.toString());
                  }
              }
            }
            break;
          case 'U':
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), false);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {
                if (!arrQueryVO[i].isFix()) {
                  if (arrQueryVO[i].getValue() instanceof Integer
                      || arrQueryVO[i].getValue() instanceof Long
                      || arrQueryVO[i].getValue() instanceof Double
                      || arrQueryVO[i].getValue() instanceof Float
                      ) {
                    stmtSQL = replaceFirst(stmtSQL, "?",
                                           arrQueryVO[i].getValue().toString());
                  }
                  else {

                    sb.delete(0, sb.length());
                    sb.append("'");
                    sb.append(arrQueryVO[i].getValue().toString());
                    sb.append("'");

                    stmtSQL = replaceFirst(stmtSQL, "?", sb.toString());
                  }
                }
              }
            }

            arrQueryVO = null;
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {

                if (!arrQueryVO[i].isFix())
                  if (arrQueryVO[i].getValue() instanceof Integer
                      || arrQueryVO[i].getValue() instanceof Long
                      || arrQueryVO[i].getValue() instanceof Double
                      || arrQueryVO[i].getValue() instanceof Float
                      ) {
                    stmtSQL = replaceFirst(stmtSQL, "?",
                                           arrQueryVO[i].getValue().
                                           toString());
                  }
                  else {
                    sb.delete(0, sb.length());
                    sb.append("'");
                    sb.append(arrQueryVO[i].getValue().toString());
                    sb.append("'");

                    stmtSQL = replaceFirst(stmtSQL, "?", sb.toString());
                  }

              }
            }
            break;

          case 'D':
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {
                if (!arrQueryVO[i].isFix())
                  if (arrQueryVO[i].getValue() instanceof Integer
                      || arrQueryVO[i].getValue() instanceof Long
                      || arrQueryVO[i].getValue() instanceof Double
                      || arrQueryVO[i].getValue() instanceof Float
                      ) {
                    stmtSQL = replaceFirst(stmtSQL, "?",
                                           arrQueryVO[i].getValue().toString());
                  }
                  else {
                    sb.delete(0, sb.length());
                    sb.append("'");
                    sb.append(arrQueryVO[i].getValue().toString());
                    sb.append("'");

                    stmtSQL = replaceFirst(stmtSQL, "?", sb.toString());
                  }
              }
            }
            break;

          case 'E':
            arrQueryVO = getObject(querySQLVO.getArrQueryVO(), true);
            if (arrQueryVO != null) {
              for (int i = 0; i < arrQueryVO.length; i++) {
                if (!arrQueryVO[i].isFix())
                  if (arrQueryVO[i].getValue() instanceof Integer
                      || arrQueryVO[i].getValue() instanceof Long
                      || arrQueryVO[i].getValue() instanceof Double
                      || arrQueryVO[i].getValue() instanceof Float
                      ) {
                    stmtSQL = replaceFirst(stmtSQL, "?",
                                           arrQueryVO[i].getValue().toString());
                  }
                  else {
                    sb.delete(0, sb.length());
                    sb.append("'");
                    sb.append(arrQueryVO[i].getValue().toString());
                    sb.append("'");

                    stmtSQL = replaceFirst(stmtSQL, "?", sb.toString());
                  }
              }
            }
            break;
        }
      } // end of
    }

    return stmtSQL;
  }

  /**
   * �ش� �迭�� �Ϳ��� �ش� ���°��� �͸��� �����մϴ�.
   * @param arrQueryVO QueryVO[]
   * @param status boolean
   * @return QueryVO[]
   */
  private QueryVO[] getObject(QueryVO[] arrQueryVO, boolean status) {
    QueryVO[] arr = null;
    Vector vcTemp = new Vector(1, 1);

    if (arrQueryVO != null) {
      for (int i = 0; i < arrQueryVO.length; i++) {
        if (arrQueryVO[i].isStatus() == status) {
          vcTemp.add(arrQueryVO[i]);
        }
      }
    }
    arr = new QueryVO[vcTemp.size()];
    vcTemp.copyInto(arr);

    return arr;
  }

  /**
   * DBConnection �� close �Ǿ����� Ȯ��.. null, close �Ǿ������?  true ����
   * @param conn Connection
   * @return boolean
   */
  public boolean isClose(Connection conn) {
    boolean result = true;
    if (conn != null) {
      try {
        result = conn.isClosed();
      }
      catch (Exception ex) {}
    }
    return result;
  }

  /******************************************************************************
   PreparedStatement �� AddBatch �� �ƴ� ������ ��ü������ ... Vector�� �־�
   ���ο� ���� ���࿡ ���� �������? Rowcount�� �����ϴ� �Լ���...
   ******************************************************************************/


  /**
   * �ش� ���ڸ�.. Ư�� ���ڷ� ġȯ�մϴ�.
   * @param data String
   * @param word String
   * @param replaceWord String
   * @return String
   */
  private String replaceAll(String data, String word, String replaceWord) {
    StringBuffer sb = new StringBuffer();
    int e = 0;
    int s = 0;

    while ( (e = data.indexOf(word, s)) >= 0) {
      sb.append(data.substring(s, e));
      sb.append(replaceWord);
      s = e + word.length();
    }
    sb.append(data.substring(s));
    return sb.toString();
  }

  /**
   * ���ڿ��� ù��° �κи� ��ȯ�մϴ�.
   * @param data String
   * @param word String
   * @param replaceWord String
   * @return String
   */
  private String replaceFirst(String data, String word, String replaceWord) {
    StringBuffer sb = new StringBuffer();
    int e = 0;
    int s = 0;

    if ( (e = data.indexOf(word, s)) >= 0) {
      sb.append(data.substring(s, e));
      sb.append(replaceWord);
      s = e + word.length();
    }
    sb.append(data.substring(s));
    return sb.toString();
  }

  /**
   * ���? ���� SQL�� ����ϴ�? �Լ��Դϴ�. (��ġ��)
   * @param tableName String
   * @throws Exception
   */
  public void insertAddSQL(String tableName) throws Exception {

    QuerySQLVO argsQuerySQLVO = new QuerySQLVO();
    argsQuerySQLVO.setMode('I');
    argsQuerySQLVO.setTableName(tableName);

    QueryVO[] arrQueryVO = new QueryVO[vc.size()];
    vc.copyInto(arrQueryVO);

    argsQuerySQLVO.setArrQueryVO(arrQueryVO);
    vcQuerySQLVO.add(argsQuerySQLVO);
    vc.clear();
    argsQuerySQLVO = null;
  }

  /**
   * ���� ���� SQL�� ����ϴ�? �Լ��Դϴ�. (��ġ��)
   * @param tableName String
   * @throws Exception
   */
  public void updateAddSQL(String tableName) throws Exception {
    QuerySQLVO argsQuerySQLVO = new QuerySQLVO();

    argsQuerySQLVO.setMode('U');
    argsQuerySQLVO.setTableName(tableName);

    QueryVO[] arrQueryVO = new QueryVO[vc.size()];
    vc.copyInto(arrQueryVO);

    argsQuerySQLVO.setArrQueryVO(arrQueryVO);
    vcQuerySQLVO.add(argsQuerySQLVO);
    vc.clear();
    argsQuerySQLVO = null;
  }

  /**
   * ���� ���� SQL�� ����ϴ�? �Լ��Դϴ�. (��ġ��)
   * @param tableName String
   * @throws Exception
   */
  public void deleteAddSQL(String tableName) throws Exception {
    QuerySQLVO argsQuerySQLVO = new QuerySQLVO();
    argsQuerySQLVO.setMode('D');
    argsQuerySQLVO.setTableName(tableName);

    QueryVO[] arrQueryVO = new QueryVO[vc.size()];
    vc.copyInto(arrQueryVO);

    argsQuerySQLVO.setArrQueryVO(arrQueryVO);
    vcQuerySQLVO.add(argsQuerySQLVO);
    vc.clear();
    argsQuerySQLVO = null;
  }

  /**
   * ��ϵ�? �������� �ϳ��� �����Ͽ� effect row count�� �����ϴ� �Լ��Դϴ�. (�ش� �۾��� �����ϱ����� �̸� transaction ������ �ϼž� �մϴ�)
   * @param conn Connection
   * @throws Exception
   * @return int
   */
  public int executeUpdateSQLS(Connection conn) throws Exception {

    int effectRowsCount = -1;
    QuerySQLVO tmpQuerySQLVO = null;
    try {
      if (conn == null)throw new Exception(
          "[executeUpdateSQLS] Connection is null");
      if (vcQuerySQLVO == null)throw new Exception(
          "[executeUpdateSQLS] vcQuerySQLVO is null");

      for (int i = 0; i < vcQuerySQLVO.size(); i++) {
        tmpQuerySQLVO = ( (QuerySQLVO) vcQuerySQLVO.get(i));
        if (effectRowsCount == -1) {
          effectRowsCount = executeUpdate(conn, tmpQuerySQLVO);
        }
        else {
          effectRowsCount += executeUpdate(conn, tmpQuerySQLVO);
        }
      }
    }
    catch (Exception ex) {
      throw ex;
    }

    return effectRowsCount;
  }

  /**
   * executeBatch �Լ��� �����ϴ� �Լ��Դϴ�. (�ش� �۾��� �����ϱ����� �̸� transaction ������ �ϼž� �մϴ�)
   * @param conn Connection
   */
  public void executeBatch(Connection conn) throws Exception {

    HashMap hm = new HashMap();
    /**********************************************************
     1. �ش� ���� ������ ���� PreparedStatement �� AddBatch �Ѵ�.
     2. ��ϵ�? PreparedStatement ��ü�� ���? executeBatch�Ѵ�.
     **********************************************************/
    QuerySQLVO tmpQuerySQLVO = null;
    PreparedStatement tmpPstmt = null;
    Vector vcPstmt = new Vector();

    try {

      for (int i = 0; i < vcQuerySQLVO.size(); i++) {

        tmpQuerySQLVO = null;
        tmpPstmt = null;

        tmpQuerySQLVO = (QuerySQLVO) vcQuerySQLVO.get(i);
        tmpQuerySQLVO.setPreparedStatementSql(getQueryString(tmpQuerySQLVO));

        // �ش纯���� true �̸� �������� ����մϴ�?.
        if (isRunSQLView) printSQLInfo(getStmtSQL(tmpQuerySQLVO));

        tmpPstmt = (PreparedStatement) hm.get(tmpQuerySQLVO.
                                              getPreparedStatementSql());

        // �ش� PreparedStatement �� �����ϸ�
        if (tmpPstmt != null) {
          tmpPstmt = setDataValues(tmpPstmt, tmpQuerySQLVO);
          tmpPstmt.addBatch();
        }
        else {
          tmpPstmt = conn.prepareStatement(tmpQuerySQLVO.
                                           getPreparedStatementSql());
          tmpPstmt = setDataValues(tmpPstmt, tmpQuerySQLVO);
          tmpPstmt.addBatch();
          hm.put(tmpQuerySQLVO.getPreparedStatementSql(), tmpPstmt);
          vcPstmt.add(tmpPstmt);
        }
      }

      for (int i = 0; i < vcPstmt.size(); i++) {
        tmpPstmt = (PreparedStatement) vcPstmt.get(i);
        tmpPstmt.executeBatch();
        close(tmpPstmt);
      }

      hm.clear();
      vcPstmt.clear();

    }
    catch (Exception ex) {
      hm.clear();
      vcPstmt.clear();
      close(tmpPstmt);
      throw ex;

    }
    finally {
      hm.clear();
      vcPstmt.clear();
      close(tmpPstmt);
    }
  }
}
