package com.jaiva.interpreter.libs.threads;

import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.runtime.Resources;
import com.yetnt.utils.functional.consumer.ThrowableBiConsumer;
import com.yetnt.utils.functional.generic.ThrowableRunnable;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ThreadedInterp extends Thread {

    public enum CleanupCode {
        SUCCESS,
        INTERNAL_ERR,
        ERR
    }

    private final ThrowableRunnable<Exception> runnable;
    private final ThrowableBiConsumer<CleanupCode, Exception, Exception> cleanup;
    private final Resources resources;
    private final IConfig<Object> config;

    public ThreadedInterp(
            IConfig<Object> config,
            ThrowableRunnable<Exception> runnable,
            ThrowableBiConsumer<CleanupCode, Exception, Exception> cleanup
    ) {
        this.runnable = runnable;
        this.cleanup = cleanup;
        this.config = config;

        resources = config.getGlobalResources().registerNewThead(this);
    }

    @Override
    public void run() {
        try {
            runnable.run();
            finish(CleanupCode.SUCCESS, null);
        } catch (JaivaException ex) {
            finish(CleanupCode.ERR, ex);
        } catch (Exception ex) {
            finish(CleanupCode.INTERNAL_ERR, ex);
        }
    }

    private void finish(CleanupCode code, Exception ex) {
        try {
            cleanup.apply(code, ex);
            resources.release();
            config.getGlobalResources().releaseCurrent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}