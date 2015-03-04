package com.loukou.jconsul.spring;

import static org.junit.Assert.*;

import java.net.ConnectException;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.loukou.jconsul.client.JConsul;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jconsulPropertyPlaceholderConfigurerContext.xml")
public class JConsulPropertyPlaceholderConfigurerTest {

    static String value = "localfile_value_1231231";
    static String desc = "localfile_desc_121242141";

    @BeforeClass
    public static void beforeClass() {
        try {
            JConsul jconsul = new JConsul();
            jconsul.agent().self();
            value = "test_value_123";
            jconsul.keyValue().put("__unittest/com.loukou.jconsul.spring.PlaceholderTestBean/value")
                    .value("test_value_123");
            desc = "test_desc_xxx";
            jconsul.keyValue().put("__unittest/com.loukou.jconsul.spring.PlaceholderTestBean/desc")
                    .value("test_desc_xxx");
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof ConnectException) {
                // ignore
            } else {
                throw e;
            }
        }
    }

    @Resource
    PlaceholderTestBean testBean;

    @Test
    public void test() {
        assertEquals(value, testBean.getValue());
        assertEquals(desc, testBean.getDesc());
    }

}
