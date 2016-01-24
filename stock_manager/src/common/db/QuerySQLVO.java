package common.db;

/**
 * QueryVO Array ��ü�� ������ �ִ� Ŭ���� �Դϴ�. (�ϳ��� SQL�� �����մϴ�)
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class QuerySQLVO {

  private QueryVO[] arrQueryVO = null; // QueryVO Array ��ü
  private char mode = 'X'; // ���? (I:Insert, U:Update, D:Delete, E:�ܺ��Է�SQL, X:�⺻��)
  private String tableName = null; // �۾��� Table Name
  private String preparedStatementSql = null; // �ش� PreparedStatement Sql
  private String statementSql = null; // Statement SQL (������)
  private String errorDescription = ""; // ���� �߻��� Excetion ���п� �߰��� ������ �����ϴ� ���� �Դϴ�.

  /**
   * �ߺ��� StmtSQL �� PreparedStatementSql ������ �����ϱ� ���� ���� �ð� ������
   */
  private long createPstmtSQLTime = 0; // PreparedStatementSql ������ �ð�
  private long createStmtSQLTime = 0; // StatementSql ������ �ð�
  private long createQueryVOTime = 0; // arrQueryVO ������ �ð�

  /**
   * �⺻������ �ʱ�ȭ �մϴ�.
   */
  public void clear(){
    this.arrQueryVO = null;
    this.mode = 'X'; // �⺻���� �ϰ͵� ���� �Դϴ�.
    this.tableName = null;
    this.preparedStatementSql = null;
    this.statementSql = null;
  }

  public QuerySQLVO() {
  }

  /**
   * PreparedStatementSql �����͸� ���� �����ؾ� �ϴ��� ���� ����
   * @return boolean true : �����ؾ���, false : ���ž��ص� ��
   */
  public boolean isSetPstmtSQL(){
    if(this.preparedStatementSql == null || this.preparedStatementSql.equals("")) return true;
    else return (this.createQueryVOTime > this.createPstmtSQLTime) ? true : false;
  }

  /**
   * StatementSql �����͸� ���� �����ؾ� �ϴ��� ���� ����
   * @return boolean true : �����ؾ���, false : ���ž��ص� ��
   */
  public boolean isSetStmtSQL(){
    if(this.statementSql == null || this.statementSql.equals("")) return true;
    else return (this.createQueryVOTime > this.createStmtSQLTime) ? true : false;
  }

  public QueryVO[] getArrQueryVO() {
    return arrQueryVO;
  }

  public char getMode() {
    return mode;
  }

  public String getTableName() {
    return tableName;
  }
  public void setArrQueryVO(QueryVO[] arrQueryVO) {
    this.setCreateQueryVOTime(System.currentTimeMillis());
    this.arrQueryVO = arrQueryVO;
  }
  public void setMode(char mode) {
    if(this.mode != mode){
      this.setCreatePstmtSQLTime(0);
      this.setCreateStmtSQLTime(0);
      this.mode = mode;
    }
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getPreparedStatementSql() {
    return preparedStatementSql;
  }

  public String getStatementSql() {
    return statementSql;
  }

  public void setPreparedStatementSql(String PreparedStatementSql) {
    this.setCreatePstmtSQLTime(System.currentTimeMillis());
    this.preparedStatementSql = PreparedStatementSql;
  }

  public void setStatementSql(String StatementSql) {
    this.setCreateStmtSQLTime(System.currentTimeMillis());
    this.statementSql = StatementSql;
  }

  private long getCreateStmtSQLTime() {
    return createStmtSQLTime;
  }
  private long getCreateQueryVOTime() {
    return createQueryVOTime;
  }
  private long getCreatePstmtSQLTime() {
    return createPstmtSQLTime;
  }
  private void setCreatePstmtSQLTime(long createPstmtSQLTime) {
    this.createPstmtSQLTime = createPstmtSQLTime;
  }
  private void setCreateQueryVOTime(long createQueryVOTime) {
    this.createQueryVOTime = createQueryVOTime;
  }
  private void setCreateStmtSQLTime(long createStmtSQLTime) {
    this.createStmtSQLTime = createStmtSQLTime;
  }
  public String getErrorDescription() {
    return errorDescription;
  }
  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }


}
