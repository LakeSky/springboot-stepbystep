package com.example.helloworld;

import com.example.helloworld.example.A;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gang on 2021/6/3.
 */
public class MyTest {
    @Test
    public void test() {
        A a1 = new A();
        a1.setName("nihao");
        A a2 = new A();
        a2.setName("nihao");
        Map<A, String> map = new HashMap<>();
        map.put(a1,"a1");
        map.put(a2,"a2");
        
        int i1=a1.hashCode();
        int i2=a2.hashCode();

        System.out.println(i1);
        System.out.println(i2);
        System.out.println(map);
    }
}
