package com.loukou.jconsul.client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.loukou.jconsul.client.SessionRequestBuilder.SessionStatus;
import com.loukou.jconsul.client.model.Session;

public class SessionRequestBuilderTest {

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
    public void test() {
        Optional<Session> sessionOp = jconsul.session().info("1212313").value();
        assertFalse(sessionOp.isPresent());

        SessionStatus session = jconsul.session().create().name("afaefa").behavior("delete").lockDelay(1000).ttl(20)
                .node(nodename).execute();

        sessionOp = jconsul.session().info(session.getId()).value();
        assertTrue(sessionOp.isPresent());
        assertEquals("afaefa", sessionOp.get().getName());
        assertEquals("delete", sessionOp.get().getBehavior());
        assertEquals(1000000000000l, sessionOp.get().getLockDelay());
        assertEquals("20s", sessionOp.get().getTtl());

        jconsul.session().list().value();
        jconsul.session().node(nodename).value();


        session.destroy();
    }

}
