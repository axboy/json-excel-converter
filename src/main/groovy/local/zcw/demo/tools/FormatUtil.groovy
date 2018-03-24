package local.zcw.demo.tools

import groovy.json.JsonParserType
import groovy.json.JsonSlurper

/**
 * @author zcw
 * @date 2018/2/1 10:16
 * @version 1.0.0
 */
class FormatUtil {

    /**
     * 将json字符串转为map对象
     * @param jsonStr
     * @return
     */
    static Map json2Map(String jsonStr) {
        def parser = new JsonSlurper().setType(JsonParserType.LAX)
        def map = parser.parseText(jsonStr) as Map
        return map
    }

    /**
     * 将json字符串转为List<Map>对象
     * @param jsonStr
     * @return
     */
    static List<Map> json2ListMap(String jsonStr) {
        def parser = new JsonSlurper().setType(JsonParserType.LAX)
        parser.setMaxSizeForInMemory(Integer.MAX_VALUE)
        def result = parser.parseText(jsonStr) as List<Map>
        return result
    }

    /**
     * 把单层map转换为对象型
     * @param data
     * @return
     */
    static Map mergeMap(List<List<String>> data) {
        Map resp = [:]
        data.each { List<String> it ->
            String key = it.get(0)
            String value = it.get(1)
            String[] arr = key.split("\\.")
            int index = 0

            def tmp = resp
            while (index < arr.length) {
                String k = arr[index++]

                //最后一层
                if (index == arr.length) {
                    tmp.put(k, value)
                    break
                }

                //非最后一层
                def v = tmp.get(k)
                if (!v) {
                    tmp.put(k, [:])
                }
                tmp = tmp.get(k)
            }
        }
        return resp
    }

    /**
     * 把多层级map转为单层的
     * @param map
     * @return
     */
    static Map<String, String> expandsMap(Map map) {
        Map<String, String> resp = [:]
        realExpandsMap(map, null, resp)
        return resp
    }

    private static void realExpandsMap(Map data, String preKey, Map<String, String> resp) {
        data.each { key, value ->
            String newKey = preKey ? "${preKey}.${key}" : key
            if (value instanceof Map) {
                realExpandsMap(value, newKey, resp)
            }
            if (value instanceof String) {
                resp.put(newKey, value)
            }
        }
    }
}
