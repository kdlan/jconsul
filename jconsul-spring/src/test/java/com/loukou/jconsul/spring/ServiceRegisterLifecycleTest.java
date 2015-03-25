package com.loukou.jconsul.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Optional;
import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.model.CatalogService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:serviceRegisterLifecycleContext.xml")
public class ServiceRegisterLifecycleTest {

    @Test
    public void test() {
        JConsul jconsul = new JConsul();
        Optional<List<CatalogService>> serviceOpt = jconsul.catalog().service("jconsul-spring-lifecycle-unittest").value();
        assertNotNull(serviceOpt.isPresent());
        List<CatalogService> serviceList = serviceOpt.get();
        assertEquals(1, serviceList.size());
        CatalogService service=serviceList.get(0);


        assertEquals(Arrays.asList("tag1", "tag2"), service.getServiceTags());
    }

}
