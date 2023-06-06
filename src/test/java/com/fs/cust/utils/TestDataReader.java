package com.fs.cust.utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class TestDataReader {

    public Map getxlData(String testcaseName, String sheetName, String inp_type) throws IOException{
        Map a = new Hashtable();
        FileInputStream fis = new FileInputStream(System.getProperty("user,dir")+"//src//test//resources//testdata//CustomerTestData.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        int sheets= workbook.getNumberOfSheets();

        for(int i=0;i<sheets;i++){
            if(workbook.getSheetName(i).equalsIgnoreCase(sheetName)){
                HSSFSheet sheet = workbook.getSheetAt(i);

                //Identify Testcases coloumn by scanning the entire 1st row
                Iterator<Row> rows = sheet.iterator();
                Row firstrow = rows.next();
                Iterator<Cell> ce = firstrow.cellIterator();

                int k=0;
                int column=0;
                while (ce.hasNext()){
                    Cell value = ce.next();
                    if(value.getStringCellValue().equalsIgnoreCase("TCNAME")){
                        column=k;
                        break;
                    }k++;
                }

                while(rows.hasNext()){
                    Row r = rows.next();

                    if(r.getCell(column).getStringCellValue().equalsIgnoreCase(testcaseName)){
                        Iterator<Cell> cv = r.cellIterator();
                        while(cv.hasNext()){
                            Cell c = cv.next();
                            CellValue cvl = evaluator.evaluate(c);
                            if(firstrow.getCell(c.getColumnIndex()).getStringCellValue().contains(inp_type)){
                                if(c.getCellType() == c.CELL_TYPE_STRING && cvl.getStringValue()!=""){
                                    a.put(firstrow.getCell(c.getColumnIndex()).getStringCellValue().replace(inp_type+"_",""),cvl.getStringValue());
                                }else if(c.getCellType() == c.CELL_TYPE_NUMERIC && cvl.getNumberValue()!=0){
                                    a.put(firstrow.getCell(c.getColumnIndex()).getStringCellValue().replace(inp_type+"_",""), NumberToTextConverter.toText(cvl.getNumberValue()));
                                }else if((c.getCellType() == c.CELL_TYPE_FORMULA && cvl.getStringValue()!="")){
                                    a.put(firstrow.getCell(c.getColumnIndex()).getStringCellValue().replace(inp_type+"_",""),cvl.getStringValue());
                                }
                                }
                        }
                    }
                }
            }

        } return a;

    }

    public void putxlData(String testcaseName, String sheetName, Map actlreslt) throws IOException{
        Map a = new Hashtable();
        FileInputStream fis = new FileInputStream(System.getProperty("user,dir")+"//src//test//resources//testdata//CustomerTestData.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        int sheets= workbook.getNumberOfSheets();

        for(int i=0;i<sheets;i++){
            if(workbook.getSheetName(i).equalsIgnoreCase(sheetName)){
                HSSFSheet sheet = workbook.getSheetAt(i);

                //Identify Testcases coloumn by scanning the entire 1st row
                Iterator<Row> rows = sheet.iterator();
                Row firstrow = rows.next();
                Iterator<Cell> ce = firstrow.cellIterator();

                int k=0,l=0;
                int column=0;
                int idclmn=0;
                while (ce.hasNext()){
                    Cell value = ce.next();
                    if(value.getStringCellValue().equalsIgnoreCase("TCNAME")){
                        column=k;
                        break;
                    }k++;
                }

                while (ce.hasNext()){
                    Cell value = ce.next();
                    if(value.getStringCellValue().equalsIgnoreCase("ACTL_CUST_ID")){
                        idclmn=k;
                        break;
                    }l++;
                }


                while(rows.hasNext()){
                    Row r = rows.next();

                    if(r.getCell(column).getStringCellValue().equalsIgnoreCase(testcaseName)){
                        r.createCell(idclmn).setCellValue(actlreslt.get("CUST_ID").toString());
                            break;
                        }
                    }
                }
            }

        FileOutputStream out = new FileOutputStream(new File(System.getProperty("user,dir")+"//src//test//resources//testdata//CustomerTestData.xls"));
        workbook.write(out);
        out.close();

        }

    public Map getqparam(String tcnm, String apinm) throws IOException{
       Map qparam_data = getxlData(tcnm,apinm,"qurey") ;
       return qparam_data;
    }

    public Map getpparam(String tcnm, String apinm) throws IOException{
        Map pparam_data = getxlData(tcnm,apinm,"path") ;
        return pparam_data;
    }

    public Map getrq_body(String tcnm, String apinm) throws IOException{
        Map reqbody_data = getxlData(tcnm,apinm,"reqbody") ;
        return reqbody_data;
    }

    public Map getparam(String tcnm, String apinm) throws IOException{
        Map param_data = getxlData(tcnm,apinm,"param") ;
        return param_data;
    }

    public Map getexp_response(String tcnm, String apinm) throws IOException{
        Map expres_data = getxlData(tcnm,apinm,"exp_res") ;
        return expres_data;
    }

}
