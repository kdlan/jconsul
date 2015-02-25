package com.loukou.jconsul.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.AgentRequestBuilder.RegisterStatus;
import com.loukou.jconsul.client.model.agent.Check;
import com.loukou.jconsul.client.model.agent.Member;
import com.loukou.jconsul.client.model.health.HealthCheck;
import com.loukou.jconsul.client.model.health.Service;

public class AgentRequestBuilderTest {

    JConsul jconsul;

    @Before
    public void before() {
        jconsul = new JConsul();
    }

    @After
    public void after() {
        jconsul.close();
    }

    @Test
    public void testSelf() {
        assertNotNull(jconsul.agent().self());
    }

    @Test
    public void testChecks() {
        Check check = new Check();
        check.setId("test");
        check.setName("test");
        check.setNotes("aaa");
        check.setTtl("10s");
        RegisterStatus status=jconsul.agent().registerCheck(check);
        Map<String, HealthCheck> map = jconsul.agent().checks();
        assertFalse(map.isEmpty());
        HealthCheck health = map.get("test");
        assertNotNull(health);

        assertEquals("test", health.getCheckId());
        status.deregister();
    }

    @Test
    public void testServices() {
        Map<String, Service> map = jconsul.agent().services();

        Service service = map.get("consul");

        assertNotNull(service);
        assertEquals("consul", service.getId());
    }

    @Test
    public void testMembers() {
        List<Member> list = jconsul.agent().members();

        assertFalse(list.isEmpty());
    }

    @Test
    public void testCheckActions() {
        String key = "test";
        Check check = new Check();
        check.setId(key);
        check.setName("test");
        check.setNotes("aaa");
        check.setTtl("10s");
        RegisterStatus status = jconsul.agent().registerCheck(check);
        assertNotNull(status);

        String note = "zxvzdafsdfafa";
        status.pass(note);

        Map<String, HealthCheck> map = jconsul.agent().checks();

        HealthCheck health = map.get(key);

        assertNotNull(health);

        assertEquals(key, health.getCheckId());
        assertEquals(note, health.getOutput());
        assertEquals("passing", health.getStatus());

        note = "zxvzdafsaaaadfafa";
        status.warn(note);

        map = jconsul.agent().checks();

        health = map.get(key);

        assertNotNull(health);

        assertEquals(key, health.getCheckId());
        assertEquals(note, health.getOutput());
        assertEquals("warning", health.getStatus());

        note = "zxvzdafsa1123131aaadfafa";
        status.fail(note);

        map = jconsul.agent().checks();

        health = map.get(key);

        assertNotNull(health);

        assertEquals(key, health.getCheckId());
        assertEquals(note, health.getOutput());
        assertEquals("critical", health.getStatus());

        status.deregister();



        map = jconsul.agent().checks();
        health = map.get(key);
        assertNull(health);
    }

    @Test
    public void testServiceAction() {
        String serviceId = "test";
        String checkId = "service:" + serviceId;

        RegisterStatus status = jconsul.agent().registerService(serviceId,serviceId,1234,10,"a","b");

        Map<String, Service> map;
        Map<String, HealthCheck> checkMap;
        Service service;
        HealthCheck health;

        map = jconsul.agent().services();
        service = map.get(serviceId);
        assertNotNull(service);
        assertEquals(serviceId, service.getId());
        assertEquals(serviceId, service.getService());

        checkMap = jconsul.agent().checks();
        health = checkMap.get(checkId);
        assertNotNull(health);
        assertEquals(checkId, health.getCheckId());


        String note;
        note="woejfowafjxa";
        status.pass(note);

        checkMap = jconsul.agent().checks();
        health = checkMap.get(checkId);
        assertNotNull(health);
        assertEquals(checkId, health.getCheckId());
        assertEquals(note,health.getOutput());
        assertEquals("passing", health.getStatus());


        status.deregister();
        map = jconsul.agent().services();
        service = map.get(serviceId);
        assertNull(service);

    }
}
