package com.loukou.jconsul.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.model.catalog.CatalogNode;
import com.loukou.jconsul.client.model.health.Node;
import com.loukou.jconsul.client.model.health.Service;
import com.google.common.base.Optional;

public class CatalogRequestBuilderTest {

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
    public void testRegister() {
        jconsul.catalog().register("ssssss", "127.0.0.1").service("123", "aaaa").port(8080).check("service:123", "aaa")
                .forService("ssssss", "123").status("passing").execute();
        Optional<CatalogNode> nodeOp = jconsul.catalog().node("ssssss").value();
        assertNotNull(nodeOp);
        CatalogNode node = nodeOp.get();
        System.out.println(node.getServices());

        Service service = node.getServices().get("123");
        assertNotNull(service);
        assertEquals("123", service.getId());
        assertEquals("aaaa", service.getService());

        // TODO add test for checks

        jconsul.catalog().deregister("ssssss").service("123").execute();

        nodeOp = jconsul.catalog().node("ssssss").value();
        assertTrue(nodeOp.isPresent());
        assertNull(nodeOp.get().getServices().get("123"));

        jconsul.catalog().deregister("ssssss").execute();

        nodeOp = jconsul.catalog().node("ssssss").value();
        assertFalse(nodeOp.isPresent());
    }

    @Test
    public void testNodes() {
        JConsulResponse<List<Node>> response = jconsul.catalog().nodes().response();

        assertTrue(response.getIndex() > 0);
        assertTrue(response.isKnownLeader());

        List<Node> list = response.getResult().get();
        assertTrue(list.size() > 0);
    }

    @Test
    public void testService() {
        jconsul.catalog().register("ssssss", "127.0.0.1").service("xxxxx123", "asssaaa").port(8080).tags("aaa", "bbb").execute();

        JConsulResponse<Map<String, List<String>>> response = jconsul.catalog().serivces().response();
        assertTrue(response.getIndex() > 0);
        assertTrue(response.isKnownLeader());

        Map<String, List<String>> map=response.getResult().get();
        System.out.println(map);
        assertFalse(map.isEmpty());
        assertNotNull(map.get("asssaaa"));
        assertNotNull(map.get("consul"));
        assertEquals(Arrays.asList("aaa","bbb"),map.get("asssaaa"));
    }
}
