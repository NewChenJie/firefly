package zool.firefly.util;

import java.time.LocalDate;
import java.time.YearMonth;

public class TestSome {
    public static void main(String[] args) {
        int i = YearMonth.parse("2018-11").getMonth().maxLength();
        System.out.println(i);
    }

}
