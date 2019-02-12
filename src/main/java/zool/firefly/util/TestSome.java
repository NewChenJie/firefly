package zool.firefly.util;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import zool.firefly.domain.vo.AliCustomerVo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
public class TestSome {
    public static void main(String[] args) {
        AliCustomerVo vo = AliCustomerVo.builder().build();
        ArrayList<AliCustomerVo> list = new ArrayList<>();
        list.add(vo);
        System.out.println(list instanceof Page);
        System.out.println(1);

    }

}
