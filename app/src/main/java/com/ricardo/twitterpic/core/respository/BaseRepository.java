package com.ricardo.twitterpic.core.respository;

class BaseRepository<T> implements Repository<T> {

    private T t;

    @Override
    public boolean isEmpty() {
        return t == null;
    }

    @Override
    public synchronized T retrieve() {
        return t;
    }

    @Override
    public synchronized void persist(T t) {
        this.t = t;
    }

    @Override
    public synchronized void clear() {
        t = null;
    }
}
