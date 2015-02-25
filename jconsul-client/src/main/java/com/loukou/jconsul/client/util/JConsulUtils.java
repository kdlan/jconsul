package com.loukou.jconsul.client.util;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.loukou.jconsul.client.JConsulResponseCallback;
import com.loukou.jconsul.client.model.JConsulResponse;

public class JConsulUtils {

    public static <V> JConsulResponse<V> transform(JConsulResponse<List<V>> input) {

        Optional<V> value = input.getResult().transform(new Function<List<V>, V>() {
            @Override
            public V apply(List<V> input) {
                return Iterables.getOnlyElement(input);
            }
        });
        long index = input.getIndex();
        long lastContact = input.getLastContact();
        boolean knownLeader = input.isKnownLeader();

        return new JConsulResponse<V>(value, lastContact, knownLeader, index);

    }

    public static <V> JConsulResponseCallback<List<V>> transform(final JConsulResponseCallback<V> callback){
       return new JConsulResponseCallback<List<V>>() {

            @Override
            public void onComplete(JConsulResponse<List<V>> consulResponse) {
                callback.onComplete(JConsulUtils.transform(consulResponse));
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

        };
    }
}
