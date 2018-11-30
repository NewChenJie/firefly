package zool.firefly.service.Impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import zool.firefly.domain.form.PageForm;
import zool.firefly.domain.vo.ExcelExportVo;
import zool.firefly.service.ExcelService;
import zool.firefly.util.ExcelUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public List<Map<String, Object>> getExcelExport(PageForm form) {
        ExportParams exportParams = new ExportParams();
        List<Map<String, Object>> excelList = new ArrayList<>();
        ExcelExportVo demo1 = ExcelExportVo.builder().name("测试1").date(new Date()).sex(1).build();
        ExcelExportVo demo2 = ExcelExportVo.builder().name("测试2").date(new Date()).sex(2).build();
        ExcelUtil.addExcelList(excelList, exportParams, ExcelExportVo.class, Lists.newArrayList(demo1,demo2));
        return excelList;
    }
}
