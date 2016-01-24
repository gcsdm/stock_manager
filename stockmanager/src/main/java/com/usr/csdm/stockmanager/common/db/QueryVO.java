package com.usr.csdm.stockmanager.common.db;

/**
 * ���������� ������ �ִ� Ŭ���� �Դϴ�.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class QueryVO {

  private boolean status = false; // �����÷��� (true : where ��, false:��������)
  private String filed = null; // table filed name
  private Object value = ""; // data value
  private boolean fix = false; // �����÷��� (true : value �� sql���� �����Ҷ�, false : java���� �������϶�)
  private String valueSQL = ""; // value sql : "to_char(?,'yyyymmddhh24miss') �̷����� SQL���̸鼭 ��ȯ�� �����Ͱ� �ް� �� ���?)

  /**
   * ������
   */
  public QueryVO() {

  }

  /**
   * �⺻������ �ʱ�ȭ �մϴ�.
   */
  public void clear(){
    this.status = false;
    this.filed = null;
    this.value = null;
    this.fix = false;
    this.valueSQL = null;
  }

  /**
   * �ʵ����? �����մϴ�.
   * @return String
   */
  public String getFiled() {
    return filed;
  }

  /**
   * ���°��� �����մϴ�
   * @return boolean
   */
  public boolean isStatus() {
    return status;
  }

  /**
   * �ʵ尪�� �����մϴ�.
   * @param filed String
   */
  public void setFiled(String filed) {
    this.filed = filed;
  }

  /**
   * ���°��� �����մϴ�.
   * @param status boolean
   */
  public void setStatus(boolean status) {
    this.status = status;
  }

  /**
   * �����͸� �����մϴ�.
   * @return Object
   */
  public Object getValue() {
    return value;
  }

  /**
   * object �����͸� �����մϴ�.
   * @param value Object
   */
  public void setValue(Object value) {
    if(value != null){
      this.value = value;
    }else{
      this.value = "";
    }
  }

  /**
   * int �����͸� �����մϴ�.
   * @param value int
   */
  public void setValue(int value) {
    this.value = new Integer(value);
  }

  /**
   * long �����͸� �����մϴ�.
   * @param value long
   */
  public void setValue(long value) {
    this.value = new Long(value);
  }

  /**
   * double ���� �����մϴ�.
   * @param value double
   */
  public void setValue(double value) {
    this.value = new Double(value);
  }

  /**
   * float �����͸� �����մϴ�.
   * @param value float
   */
  public void setValue(float value) {
    this.value = new Float(value);
  }

  /**
   * ������ ������ ���� �÷��׸� �����մϴ�.
   * @return boolean
   */
  public boolean isFix() {
    return fix;
  }

  /**
   * �����Ͱ� sql���� value ����, java���� ������ Ÿ�������� ���� ������ �����մϴ�.  (true:sql, false:java)
   * @param fix boolean
   */
  public void setFix(boolean fix) {
    this.fix = fix;
  }

  /**
   * SQL Value�� �����մϴ�.
   * @return String
   */
  public String getValueSQL() {
    return valueSQL;
  }

  /**
   * SQL Value�� �����մϴ�.
   * @param valueSQL String
   */
  public void setValueSQL(String valueSQL) {
    this.valueSQL = valueSQL;
  }

}
