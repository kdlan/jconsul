package com.loukou.jconsul.recipes;

import org.junit.Test;

import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.model.Value;

public class SharedMapTest {

    @Test
    public void test() throws Exception {
        JConsul jconsul=new JConsul();
        SharedMap map=new SharedMap();


        jconsul.keyValue().put("a").value("1231231231321");
        String value=map.get("a");
        System.out.println(value);
        jconsul.keyValue().put("a").value("dhvlakvaldkjba");
        Thread.sleep(1000);
        value=map.get("a");
        System.out.println(value);

        jconsul.keyValue().put("a").value("=-=-==-)(*()*&&%^$$#$%#");
        Thread.sleep(1000);
        value=map.get("a");
        System.out.println(value);

        jconsul.keyValue().delete("aq2k12k3j").execute();
        value=map.get("aq2k12k3j");
        jconsul.keyValue().put("aq2k12k3j").value("xxxxxxxxxxxxxxxxxxxx");
        Thread.sleep(10000);
        value=map.get("aq2k12k3j");
        System.out.println(value);
    }

}
