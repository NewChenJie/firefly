package zool.firefly.util;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.shiro.crypto.hash.SimpleHash;
import zool.firefly.domain.entity.AliNewCustomer;

public class TestSomething {

    private static final int capacity = 1000000;
    private static final int key = 999998;
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), capacity);
    static {
        for (int i = 0; i < capacity; i++) {
            bloomFilter.put(i);
        }
    }
    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";//加密方式
        Object crdentials = "123456";//密码原值
        Object salt = null;//盐值
        int hashIterations = 2;//加密2次
        Object result = new SimpleHash(hashAlgorithmName,crdentials,new MySimpleByteSource(ByteSourceUtils.serialize("guest")),hashIterations);
        System.out.println(result);

        /*返回计算机最精确的时间,单位微妙*/
        long start = System.nanoTime();

        if (bloomFilter.mightContain(key)) {
            System.out.println("成功过滤到" + key);
        }
        long end = System.nanoTime();
        System.out.println("布隆过滤器消耗时间:" + (end - start));
        int sum = 0;
        for (int i = capacity + 20000; i < capacity + 30000; i++) {
            if (bloomFilter.mightContain(i)) {
                sum = sum + 1;
            }
        }
        System.out.println("错判率为:" + sum);
    }
}
