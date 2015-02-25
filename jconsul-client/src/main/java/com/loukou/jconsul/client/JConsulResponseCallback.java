package com.loukou.jconsul.client;

import com.loukou.jconsul.client.model.JConsulResponse;

/**
 * For API calls that support long-polling, this callback is used to handle
 * the result on success or failure for an async HTTP call.
 *
 * @param <T> The Response type.
 */
public interface JConsulResponseCallback<T> {

    /**
     * Callback for a successful {@link com.loukou.jconsul.client.model.JConsulResponse}.
     *
     * @param consulResponse The Consul response.
     */
    public void onComplete(JConsulResponse<T> consulResponse);

    /**
     * Callback for an unsuccessful request.
     *
     * @param throwable The exception thrown.
     */
    public void onFailure(Throwable throwable);

}
