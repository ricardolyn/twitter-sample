package com.ricardo.twitterpic.core.respository;

public interface Repository<T> {

    boolean isEmpty();

    T retrieve();

    void persist(T t);

    void clear();

}
