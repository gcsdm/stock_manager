package com.usr.csdm.stockmanager;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

import com.usr.csdm.stockmanager.common.StringUtil;
import com.usr.csdm.stockmanager.common.db.DBUtil;
import com.usr.csdm.stockmanager.info.CompanyDTO;

public class LoadExcelData {

	public static void main(String[] args){
		
		System.out.println("------------------------");
//		long sTime = System.currentTimeMillis();
//		System.out.println("START !!!");
//		ArrayList<CompanyDTO> arrList = loadCompanyExcelFile("C:/Users/ncsdm/git/stock_manager/stockmanager/data/data.xls");
//		insertCompanyList(arrList);
//		System.out.println("STOP !!! ["+ (System.currentTimeMillis() - sTime) +"ms]");
	}
	
	private static void insertCompanyList(ArrayList<CompanyDTO> arrList){
		
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try{
        	conn = DBUtil.getMySQLConnection("stock_manager", "stockmanager", "managerstock");
        	pstmt = conn.prepareStatement("insert into tbl_company"
        			+ "(code,name,category,stock_count,stock_money,face_value,unit_type,phone,address) "
        			+ "values(?,?,?,?,?,?,?,?,?)");
        	
        	for(int i = 0; i < arrList.size(); i++){
        		int idx = 1;
        		pstmt.clearParameters();
        		
        		pstmt.setString(idx++, arrList.get(i).getCode());	
        		pstmt.setString(idx++, arrList.get(i).getName());
        		pstmt.setString(idx++, arrList.get(i).getCategory());
        		pstmt.setLong(idx++, arrList.get(i).getStockCount());
        		pstmt.setLong(idx++, arrList.get(i).getStockMoney());
        		pstmt.setFloat(idx++, arrList.get(i).getFaceValue());
        		pstmt.setString(idx++, arrList.get(i).getUnitType());
        		pstmt.setString(idx++, arrList.get(i).getPhone());
        		pstmt.setString(idx++, arrList.get(i).getAddress());
        		
        		pstmt.addBatch();
        		
        	}
        	
        	pstmt.executeBatch();
        	
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	DBUtil.close(pstmt);
        	DBUtil.close(conn);
        }

	}
	
	private static ArrayList<CompanyDTO> loadCompanyExcelFile(String excelFilePath){
		
		ArrayList<CompanyDTO> arr = new ArrayList<CompanyDTO>();
		System.out.println("ExcelFile ["+excelFilePath+"]");
		
		try{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelFilePath));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			
			HSSFSheet sheet = wb.getSheetAt(0); // 시트 가져오기
			
			for(int i = 0 ; i < sheet.getPhysicalNumberOfRows(); i++ ){
				Row r = sheet.getRow(i);
				if(r != null){
					
					String code = StringUtil.null2Trim( r.getCell(1).getStringCellValue() );					
					if("종목코드".equals(code)) continue;
					
					CompanyDTO dto = new CompanyDTO();
					
					dto.setCode( code );
					dto.setName( StringUtil.null2Trim(r.getCell(2).getStringCellValue()) );
					dto.setCategory( StringUtil.null2Trim(r.getCell(3).getStringCellValue()) );
					dto.setCategoryName( StringUtil.null2Trim(r.getCell(4).getStringCellValue()) );
					dto.setStockCount( StringUtil.string2Long(r.getCell(5).getStringCellValue()) );
					dto.setStockMoney( StringUtil.string2Long(r.getCell(6).getStringCellValue()) );
					dto.setFaceValue( StringUtil.string2Float(r.getCell(7).getStringCellValue()) );
					dto.setUnitType( StringUtil.null2Trim(r.getCell(8).getStringCellValue()) );
					dto.setPhone( StringUtil.null2Trim(r.getCell(9).getStringCellValue()) );
					dto.setAddress( StringUtil.null2Trim(r.getCell(10).getStringCellValue()) );
					
					arr.add(dto);
					
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}

		System.out.println(arr.size());
		
		return arr;
	}
}
