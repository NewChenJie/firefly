package zool.firefly.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zool.firefly.domain.form.PageForm;
import zool.firefly.service.ExcelService;
import zool.firefly.util.ExcelUtil;
import zool.firefly.util.KeyValue;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelServiceImpl;

    @GetMapping("/export")
    public KeyValue excelExport(@Validated  PageForm form, HttpServletResponse response, BindingResult bindingResult)throws IOException {
        String fileName = new String("excel导出_".getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename= "+fileName+ LocalDate.now().toString()+".xls");
        Workbook workbook = ExcelExportUtil.exportExcel(excelServiceImpl.getExcelExport(form), ExcelType.HSSF);
        workbook.setSheetName(0, "sheet名字");
        return ExcelUtil.getExcelStream(response, workbook);
    }

}
