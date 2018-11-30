package zool.firefly.service;

import zool.firefly.domain.form.PageForm;

import java.util.List;
import java.util.Map;

public interface ExcelService {
    List<Map<String, Object>> getExcelExport(PageForm form);
}
