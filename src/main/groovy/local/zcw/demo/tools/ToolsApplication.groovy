package local.zcw.demo.tools

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/")
@SpringBootApplication
class ToolsApplication {

    static void main(String[] args) {
        SpringApplication.run ToolsApplication, args
    }

    /**
     * json转excel，多层结构
     * @param file
     */
    @RequestMapping(value = "/json2excel", method = RequestMethod.POST)
    ResponseEntity<?> json2excel(@RequestBody Map map, HttpServletResponse response) {
        response.setHeader("Content-disposition", "attachment;filename=file-" + System.currentTimeMillis() + ".xlsx")
        Map<String, String> expandedMap = FormatUtil.expandsMap(map)
        ExcelUtils.createExcelByMap(response.getOutputStream(), null, expandedMap)
        return ResponseEntity.ok().build()
    }

    /**
     * json转excel，多层结构
     * @param file
     */
    @RequestMapping(value = "/json2excelByFile", method = RequestMethod.POST)
    ResponseEntity<?> json2excelByFile(MultipartFile file, HttpServletResponse response) {
        def fileName = file.getName()
        byte[] data = file.getBytes()
        String jsonStr = new String(data, "UTF-8")
        Map<String, String> expandedMap = FormatUtil.expandsMap(FormatUtil.json2Map(jsonStr))
        response.setHeader("Content-disposition", "attachment;filename=${fileName}-" + System.currentTimeMillis() + ".xlsx")
        ExcelUtils.createExcelByMap(response.getOutputStream(), null, expandedMap)
        return ResponseEntity.ok().build()
    }

    /**
     * excel转json，多层结构
     * @param file
     */
    @RequestMapping(value = "/excel2json", method = RequestMethod.POST)
    def excel2json(MultipartFile file, HttpServletResponse response) {
        def fileName = file.getName()
        List<List<String>> data = ExcelUtils.readExcel(file.getInputStream())
        Map resp = FormatUtil.mergeMap(data)
        //response.setHeader("Content-disposition", "attachment;filename=${fileName}-" + System.currentTimeMillis() + ".json")
        return resp
    }

    /**
     * 单层，多列json，转excel
     * @param map
     * @param response
     * @return
     */
    @RequestMapping(value = "/multiRowJson2Excel", method = RequestMethod.POST)
    ResponseEntity<?> multiRowJson2Excel(@RequestBody List<Map> data, HttpServletResponse response) {
        response.setHeader("Content-disposition", "attachment;filename=file-" + System.currentTimeMillis() + ".xlsx")
        ExcelUtils.createExcelByList(response.getOutputStream(), data)
        return ResponseEntity.ok().build()
    }

    /**
     * 单层，多列json，转excel
     * @param map
     * @param response
     * @return
     */
    @RequestMapping(value = "/multiRowJson2ExcelByFile", method = RequestMethod.POST)
    ResponseEntity<?> multiRowJson2ExcelByFile(MultipartFile file, HttpServletResponse response) {

        def fileName = file.getName()
        byte[] bytes = file.getBytes()
        String jsonStr = new String(bytes, "UTF-8")
        List<Map> data = FormatUtil.json2ListMap(jsonStr)
        response.setHeader("Content-disposition", "attachment;filename=${fileName}-" + System.currentTimeMillis() + ".xlsx")
        ExcelUtils.createExcelByList(response.getOutputStream(), data)
        return ResponseEntity.ok().build()
    }
}
