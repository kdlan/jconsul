package com.loukou.jconsul.client;

import java.util.concurrent.Future;

import org.junit.Test;

import com.loukou.jconsul.client.JConsul;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class JConsulTest {

    @Test
    public void test() throws Exception{
        new JConsul();

        AsyncHttpClient client=new AsyncHttpClient();

        Future<String> f=client.prepareGet("http://127.0.0.1:8500/kv/v1/a").execute(new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {
                throw new NullPointerException();
            }

            @Override
            public void onThrowable(Throwable t) {
                System.out.println("error");
                System.out.println(t);
                super.onThrowable(t);
            }
        });
        Thread.sleep(1000);
//        f.get();
    }

}
