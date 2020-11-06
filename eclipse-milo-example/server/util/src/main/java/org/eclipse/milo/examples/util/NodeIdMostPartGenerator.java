package org.eclipse.milo.examples.util;

import java.util.concurrent.atomic.AtomicLong;

public class NodeIdMostPartGenerator {

    static private NodeIdMostPartGenerator instance;

    static public NodeIdMostPartGenerator getInstance() {
        synchronized (NodeIdMostPartGenerator.class) {
            if (instance == null) {
                instance = new NodeIdMostPartGenerator();
            }
        }
        return instance;
    }

    final private AtomicLong counter;

    private NodeIdMostPartGenerator() {
        counter = new AtomicLong(0);
    }

    public long getNext() {
        return counter.getAndIncrement();
    }

}
