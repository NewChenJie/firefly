package zool.firefly.util;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static void addExcelList(List<Map<String, Object>> excelList, ExportParams exportParams, Class<?> cls, List<?> list) {
        HashMap<String, Object> reportMap = new HashMap<>(16);
        reportMap.put("title", exportParams);
        reportMap.put("entity", cls);
        reportMap.put("data", list);
        excelList.add(reportMap);
    }

    public static KeyValue getExcelStream(HttpServletResponse response, Workbook workbook) throws IOException {
        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
            return KeyValue.ok("导出成功");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
}
