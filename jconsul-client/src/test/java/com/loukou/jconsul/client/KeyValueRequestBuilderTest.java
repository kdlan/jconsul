package com.loukou.jconsul.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.JConsulResponseCallback;
import com.loukou.jconsul.client.model.ConsistencyMode;
import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.model.kv.Value;
import com.google.common.base.Optional;

public class KeyValueRequestBuilderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyValueRequestBuilderTest.class);
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
    public void testGet() {
        JConsulResponse<Value> response = jconsul.keyValue().get("axxxxxxxxxxxxxxxx").poll(1, 1)
                .consistency(ConsistencyMode.STALE).response();

        assertFalse(response.getResult().isPresent());

        jconsul.keyValue().put("a").flags(123123131l).value("xxxxxx");
        response = jconsul.keyValue().get("a").response();

        assertTrue(response.getResult().isPresent());


        long start = System.currentTimeMillis();
        Optional<Value> value = jconsul.keyValue().get("a").poll(2, response.getIndex())
                .consistency(ConsistencyMode.STALE).value();
        assertTrue(value.isPresent());
        long end = System.currentTimeMillis();

        assertTrue(end - start + 1000 > 2000);

    }

    @Test
    public void testGetKeys() {
        JConsulResponse<List<String>> response = jconsul.keyValue().keys("a")
                .consistency(ConsistencyMode.STALE).response();
        assertTrue(response.getResult().isPresent());
        List<String> result = response.getResult().get();

        assertTrue(result.size() > 0);
    }

    @Test
    public void testPut() {
        boolean success=jconsul.keyValue().put("/test/abc").flags(123123131l).value("xxxxxx");
        assertTrue(success);

        Optional<Value> result = jconsul.keyValue().get("test/abc").value();
        assertTrue(result.isPresent());
        Value value = result.get();
        assertEquals("xxxxxx", value.getRawValue());
        assertEquals(123123131l, value.getFlags());

    }

    @Test
    public void testCallback() throws Exception {
        final int expect = 10;
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean success = new AtomicBoolean(true);
        final String key = "test/callback";

        jconsul.keyValue().put(key).value("0");

        final int pollseconds=10;

        JConsulResponse<Value> result = jconsul.keyValue().get(key).response();

        final JConsulResponseCallback<Value> callback = new JConsulResponseCallback<Value>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.warn(throwable.getMessage(),throwable);
                success.set(false);
            }

            @Override
            public void onComplete(JConsulResponse<Value> consulResponse) {
                int value = Integer.parseInt(consulResponse.getResult().get().getRawValue());
                LOGGER.info("response number is {}",value);
                if(value<count.get()){
                    jconsul.keyValue().get(key).poll(pollseconds, consulResponse.getIndex()).async(this);
                    return;
                }
                assertEquals(count.get(), value);

                if (count.incrementAndGet() < expect) {
                    jconsul.keyValue().get(key).poll(pollseconds, consulResponse.getIndex()).async(this);
                }
            }
        };

        jconsul.keyValue().get(key).poll(pollseconds, result.getIndex()).async(callback);

        for(int i=0;i<expect;i++){
            LOGGER.info("put number is {}",i);
            jconsul.keyValue().put(key).value(String.valueOf(i));
            Thread.sleep(2000);
        }

        while(count.get()<expect){

        }

        assertTrue(success.get());
    }

}
