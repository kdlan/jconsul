package com.loukou.jconsul.recipes;

public interface ValueListener<T> {

    public void onChange(T value);
}
