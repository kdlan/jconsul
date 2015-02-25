package com.loukou.jconsul.client.model;

import com.google.common.base.Optional;

public class JConsulResponse<T> {

    private final Optional<T> result;
    private final long lastContact;
    private final boolean knownLeader;
    private final long index;

    public JConsulResponse(Optional<T> result, long lastContact, boolean knownLeader, long index) {
        this.result = result;
        this.lastContact = lastContact;
        this.knownLeader = knownLeader;
        this.index = index;
    }

    public JConsulResponse(T result, long lastContact, boolean knownLeader, long index) {
        this(Optional.fromNullable(result), lastContact, knownLeader, index);
    }

    public JConsulResponse(long lastContact, boolean knownLeader, long index) {
        this(Optional.<T> absent(), lastContact, knownLeader, index);
    }

    public Optional<T> getResult() {
        return result;
    }

    public long getLastContact() {
        return lastContact;
    }

    public boolean isKnownLeader() {
        return knownLeader;
    }

    public long getIndex() {
        return index;
    }

}
