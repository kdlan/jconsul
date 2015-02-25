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

import com.loukou.jconsul.client.AgentRequestBuilder.RegisterStatus;
import com.loukou.jconsul.client.model.HealthCheck;
import com.loukou.jconsul.client.model.Member;
import com.loukou.jconsul.client.model.Service;

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
        RegisterStatus status=jconsul.agent().registerCheck("test").id("test").notes("aaa").ttl(10);
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
        RegisterStatus status = jconsul.agent().registerCheck("test").id(key).notes("aaa").ttl(10);
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

        RegisterStatus status = jconsul.agent().registerService(serviceId).port(1234).tags("a","b").ttl(10);

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
