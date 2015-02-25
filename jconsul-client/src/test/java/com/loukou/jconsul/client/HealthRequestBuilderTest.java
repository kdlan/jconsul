package com.loukou.jconsul.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.loukou.jconsul.client.AgentRequestBuilder.RegisterStatus;
import com.loukou.jconsul.client.model.HealthCheck;

public class HealthRequestBuilderTest {

    JConsul jconsul;
    String nodename;

    @Before
    public void before() {
        jconsul = new JConsul();
        nodename = jconsul.agent().self().getConfig().getNodeName();
    }

    @After
    public void after() {
        jconsul.close();
    }

    @Test
    public void testNode() {
        RegisterStatus check1 = jconsul.agent().registerCheck("healthUnitTestCheck1").id("healthUnitTestCheck1").ttl(10);
        RegisterStatus check2 = jconsul.agent().registerCheck("healthUnitTestCheck2").ttl(10);

        check1.pass("hello");
        check2.warn("world");

        List<HealthCheck> list = jconsul.health().node(nodename).value().get();
        assertFalse(list.isEmpty());

        boolean check1Asserted = false;
        boolean check2Asserted = false;
        for (HealthCheck check : list) {
            if ("healthUnitTestCheck1".equals(check.getCheckId())) {
                check1Asserted = true;
                assertEquals("hello", check.getOutput());
                assertEquals("passing", check.getStatus());
            } else if ("healthUnitTestCheck2".equals(check.getCheckId())) {
                check2Asserted = true;
                assertEquals("world", check.getOutput());
                assertEquals("warning", check.getStatus());
            }
        }
        assertTrue(check1Asserted);
        assertTrue(check2Asserted);

        check1.deregister();
        check2.deregister();

    }

    @Test
    public void testChecks() {
        RegisterStatus service = jconsul.agent().registerService("healthUnitTestChecks").id("healthUnitTestChecks:9999").port(9999).execute();

        RegisterStatus check1 = jconsul.agent().registerCheck("check1").id("service:healthUnitTestChecks:9999:1").service("healthUnitTestChecks:9999").ttl(10);
        RegisterStatus check2 = jconsul.agent().registerCheck("check2").id("service:healthUnitTestChecks:9999:2").service("healthUnitTestChecks:9999").ttl(10);


//        check1.deregister();
//        check2.deregister();
//
//        service.deregister();
    }

    @Test
    public void testService() {
        fail("Not yet implemented");
    }

    @Test
    public void testState() {
        fail("Not yet implemented");
    }

}
