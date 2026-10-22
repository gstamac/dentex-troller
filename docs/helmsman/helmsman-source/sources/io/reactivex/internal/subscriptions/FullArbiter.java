package io.reactivex.internal.subscriptions;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* JADX INFO: loaded from: classes2.dex */
public final class FullArbiter<T> extends FullArbiterPad2 implements Subscription {
    static final Subscription INITIAL = new InitialSubscription();
    static final Object REQUEST = new Object();
    final Subscriber<? super T> actual;
    volatile boolean cancelled;
    final SpscLinkedArrayQueue<Object> queue;
    long requested;
    Disposable resource;
    volatile Subscription s = INITIAL;

    public FullArbiter(Subscriber<? super T> subscriber, Disposable disposable, int i) {
        this.actual = subscriber;
        this.resource = disposable;
        this.queue = new SpscLinkedArrayQueue<>(i);
    }

    @Override // org.reactivestreams.Subscription
    public void request(long j) {
        if (SubscriptionHelper.validate(j)) {
            BackpressureHelper.add(this.missedRequested, j);
            SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
            Object obj = REQUEST;
            spscLinkedArrayQueue.offer(obj, obj);
            drain();
        }
    }

    @Override // org.reactivestreams.Subscription
    public void cancel() {
        if (this.cancelled) {
            return;
        }
        this.cancelled = true;
        dispose();
    }

    void dispose() {
        Disposable disposable = this.resource;
        this.resource = null;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public boolean setSubscription(Subscription subscription) {
        if (this.cancelled) {
            if (subscription == null) {
                return false;
            }
            subscription.cancel();
            return false;
        }
        ObjectHelper.requireNonNull(subscription, "s is null");
        this.queue.offer(this.s, NotificationLite.subscription(subscription));
        drain();
        return true;
    }

    public boolean onNext(T t, Subscription subscription) {
        if (this.cancelled) {
            return false;
        }
        this.queue.offer(subscription, NotificationLite.next(t));
        drain();
        return true;
    }

    public void onError(Throwable th, Subscription subscription) {
        if (this.cancelled) {
            RxJavaPlugins.onError(th);
        } else {
            this.queue.offer(subscription, NotificationLite.error(th));
            drain();
        }
    }

    public void onComplete(Subscription subscription) {
        this.queue.offer(subscription, NotificationLite.complete());
        drain();
    }

    void drain() {
        if (this.wip.getAndIncrement() != 0) {
            return;
        }
        SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
        Subscriber<? super T> subscriber = this.actual;
        int iAddAndGet = 1;
        while (true) {
            Object objPoll = spscLinkedArrayQueue.poll();
            if (objPoll != null) {
                Object objPoll2 = spscLinkedArrayQueue.poll();
                if (objPoll == REQUEST) {
                    long andSet = this.missedRequested.getAndSet(0L);
                    if (andSet != 0) {
                        this.requested = BackpressureHelper.addCap(this.requested, andSet);
                        this.s.request(andSet);
                    }
                } else if (objPoll == this.s) {
                    if (NotificationLite.isSubscription(objPoll2)) {
                        Subscription subscription = NotificationLite.getSubscription(objPoll2);
                        if (!this.cancelled) {
                            this.s = subscription;
                            long j = this.requested;
                            if (j != 0) {
                                subscription.request(j);
                            }
                        } else {
                            subscription.cancel();
                        }
                    } else if (NotificationLite.isError(objPoll2)) {
                        spscLinkedArrayQueue.clear();
                        dispose();
                        Throwable error = NotificationLite.getError(objPoll2);
                        if (!this.cancelled) {
                            this.cancelled = true;
                            subscriber.onError(error);
                        } else {
                            RxJavaPlugins.onError(error);
                        }
                    } else if (NotificationLite.isComplete(objPoll2)) {
                        spscLinkedArrayQueue.clear();
                        dispose();
                        if (!this.cancelled) {
                            this.cancelled = true;
                            subscriber.onComplete();
                        }
                    } else {
                        long j2 = this.requested;
                        if (j2 != 0) {
                            subscriber.onNext((Object) NotificationLite.getValue(objPoll2));
                            this.requested = j2 - 1;
                        }
                    }
                }
            } else {
                iAddAndGet = this.wip.addAndGet(-iAddAndGet);
                if (iAddAndGet == 0) {
                    return;
                }
            }
        }
    }

    static final class InitialSubscription implements Subscription {
        @Override // org.reactivestreams.Subscription
        public void cancel() {
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
        }

        InitialSubscription() {
        }
    }
}
