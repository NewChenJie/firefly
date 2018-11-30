package zool.firefly.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageForm {

    @NotNull(message = "不为空")
    private Integer pageIndex;

    @NotNull
    private Integer pageSize;

}
