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
     *
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
     *
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
     *
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
}
