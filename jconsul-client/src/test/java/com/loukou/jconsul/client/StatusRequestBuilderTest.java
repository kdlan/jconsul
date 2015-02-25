package com.loukou.jconsul.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.loukou.jconsul.client.JConsul;

public class StatusRequestBuilderTest {
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
    public void testLeader() {
        String leader=jconsul.status().leader();
        System.out.println(leader);
        assertNotNull(leader);
        assertTrue(leader.length()>0);
    }

    @Test
    public void testPeers() {
        List<String> peers=jconsul.status().peers();
        System.out.println(peers);
        assertNotNull(peers);
        assertTrue(peers.size()>0);
    }

}
