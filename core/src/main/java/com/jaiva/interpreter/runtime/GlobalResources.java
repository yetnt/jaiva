package com.jaiva.interpreter.runtime;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalResources  {

    public record ResValue(
            Thread thread,
            Resources resources
    ) {}

    private final ConcurrentHashMap<Long, ResValue> threadResourcesMap = new ConcurrentHashMap<>();

    public GlobalResources() {
        // each IConfig gets a new GlobalResources for the current thread
        threadResourcesMap.put(
                Thread.currentThread().threadId(),
                new ResValue(Thread.currentThread(), new Resources())
        );
    }

    public Resources registerNewThead(Thread thread) {
        Resources resources = new Resources();
        threadResourcesMap.put(thread.threadId(),  new ResValue(thread, resources));
        return resources;
    }

    public void releaseCurrent() throws IOException {
        // releases this own thread's resources.
        Resources resValue = ofCurrentThread();
        resValue.release();
        threadResourcesMap.remove(Thread.currentThread().threadId());
    }

    protected void interruptAll() {
        // Interrupts all the threads as the runtime is ending usually due to an error.
        threadResourcesMap.values().forEach(s -> {
            // if its this current thread, skip.
            if (s.thread.threadId() != Thread.currentThread().threadId()) {
                s.thread.interrupt();
                // the thread is repsonsible for cleaning its resources and removing itself.
            }
        });
    }

    public Resources ofCurrentThread() {
        ResValue resValue = threadResourcesMap.get(Thread.currentThread().threadId());
        if (resValue == null) {
            Resources res = new Resources();
            threadResourcesMap.put(Thread.currentThread().threadId(), new ResValue(
                    Thread.currentThread(),
                    res
            ));
            return res;
        }
        return resValue.resources;
    }

    public ResValue getValue(Long threadId) {
        return threadResourcesMap.get(threadId);
    }

}
