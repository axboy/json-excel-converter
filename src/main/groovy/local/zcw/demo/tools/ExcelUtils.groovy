package local.zcw.demo.tools

import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * @author zcw
 * @date 2018/2/1 10:15
 * @version 1.0.0
 */
class ExcelUtils {

    static OutputStream createExcelByList(OutputStream out, List<Map> data) {
        List head = []
        List<List> content = []
        if (data.size() == 0) {
            return out
        }
        data[0].each { k, v ->
            head.add(k)
        }
        data.each { colMap ->
            List colList = []
            head.each { key ->
                colList.add(colMap[key])
            }
            content.add(colList)
        }
        return createExcel(out, head, content)
    }


    static OutputStream createExcelByMap(OutputStream out, List<String> head, Map<String, String> content) {
        List<List<String>> list = []
        content.each { String k, String v ->
            list.add([k, v])
        }
        return createExcel(out, head, list)
    }

    static OutputStream createExcel(OutputStream out, List<String> head, List<List<String>> content) {
        int count = 0
        int size = content.size()
        XSSFWorkbook workbook = new XSSFWorkbook()
        XSSFSheet sheet = workbook.createSheet()
        int rowNum = 0
        if (head != null) {
            putOneRowInExcel(sheet, head, 0)
            rowNum = 1
        }
        while (count < size) {
            putOneRowInExcel(sheet, content.get(count), rowNum);
            rowNum++
            count++
        }

        try {
            workbook.write(out);
            out.close()
            out.flush()
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out
    }

    /**
     * 插入一行数据到excel
     *
     * @param sheet
     * @param list ;每一列的数据,[xxx,xx,xxx]
     * @param rowNum ;行号，从0开始
     * @return
     */
    static XSSFSheet putOneRowInExcel(XSSFSheet sheet, List<String> list, int rowNum) {
        XSSFRow row = sheet.createRow(rowNum)
        int col = 0
        for (String it : list) {
            XSSFCell cell = row.createCell(col)
            cell.setCellValue(it)
            col++
        }
        return sheet
    }

    /**
     * 读取excel，只读1、2列
     * @param is
     * @return
     */
    static List<List<String>> readExcel(InputStream is) {
        List<List<String>> resp = []
        XSSFWorkbook wb = new XSSFWorkbook(is)
        XSSFSheet sheet = wb.getSheetAt(0)        //只处理第一个sheet
        int lastRowNum = sheet.getLastRowNum()
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow xssfRow = sheet.getRow(i)
            String row1 = getStringValue(xssfRow.getCell(0))
            String row2 = getStringValue(xssfRow.getCell(1))
            resp.add([row1, row2])
        }
        return resp
    }

    private static String getStringValue(XSSFCell cell) {
        if (!cell) {
            return ""
        }
        return cell.getStringCellValue()
    }
}
