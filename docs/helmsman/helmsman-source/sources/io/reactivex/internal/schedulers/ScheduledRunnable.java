package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;

/* JADX INFO: loaded from: classes2.dex */
public final class ScheduledRunnable extends AtomicReferenceArray<Object> implements Runnable, Callable<Object>, Disposable {
    static final Object DISPOSED = new Object();
    static final Object DONE = new Object();
    static final int FUTURE_INDEX = 1;
    static final int PARENT_INDEX = 0;
    static final int THREAD_INDEX = 2;
    private static final long serialVersionUID = -6120223772001106981L;
    final Runnable actual;

    public ScheduledRunnable(Runnable runnable, DisposableContainer disposableContainer) {
        super(3);
        this.actual = runnable;
        lazySet(0, disposableContainer);
    }

    @Override // java.util.concurrent.Callable
    public Object call() {
        run();
        return null;
    }

    @Override // java.lang.Runnable
    public void run() {
        Object obj;
        Object obj2;
        lazySet(2, Thread.currentThread());
        try {
            this.actual.run();
        } catch (Throwable th) {
            try {
                RxJavaPlugins.onError(th);
            } finally {
                lazySet(2, null);
                Object obj3 = get(0);
                if (obj3 != DISPOSED && obj3 != null && compareAndSet(0, obj3, DONE)) {
                    ((DisposableContainer) obj3).delete(this);
                }
                do {
                    obj = get(1);
                    if (obj == DISPOSED) {
                        break;
                    }
                } while (!compareAndSet(1, obj, DONE));
            }
        }
        lazySet(2, null);
        Object obj4 = get(0);
        if (obj4 != DISPOSED && obj4 != null && compareAndSet(0, obj4, DONE)) {
            ((DisposableContainer) obj4).delete(this);
        }
        do {
            if (r0 == obj2) {
                return;
            }
        } while (!compareAndSet(1, get(i), DONE));
    }

    public void setFuture(Future<?> future) {
        Object obj;
        do {
            obj = get(1);
            if (obj == DONE) {
                return;
            }
            if (obj == DISPOSED) {
                future.cancel(get(2) != Thread.currentThread());
                return;
            }
        } while (!compareAndSet(1, obj, future));
    }

    @Override // io.reactivex.disposables.Disposable
    public void dispose() {
        Object obj;
        Object obj2;
        Object obj3;
        while (true) {
            Object obj4 = get(1);
            if (obj4 == DONE || obj4 == (obj3 = DISPOSED)) {
                break;
            }
            if (compareAndSet(1, obj4, obj3)) {
                if (obj4 == null) {
                    break;
                }
                ((Future) obj4).cancel(get(2) != Thread.currentThread());
                break;
            }
        }
        do {
            obj = get(0);
            if (obj == DONE || obj == (obj2 = DISPOSED) || obj == null) {
                return;
            }
        } while (!compareAndSet(0, obj, obj2));
        ((DisposableContainer) obj).delete(this);
    }

    @Override // io.reactivex.disposables.Disposable
    public boolean isDisposed() {
        Object obj = get(1);
        return obj == DISPOSED || obj == DONE;
    }
}
