package zool.firefly.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportVo {

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "性别",replace = {"男_1","女_2"})
    private Integer sex;

    @Excel(name = "日期", format = "yyyy-MM-dd")
    private Date date;
}
