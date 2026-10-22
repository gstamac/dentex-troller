package io.reactivex.internal.operators.flowable;

import androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.FullArbiterSubscriber;
import io.reactivex.internal.subscriptions.FullArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* JADX INFO: loaded from: classes2.dex */
public final class FlowableTimeout<T, U, V> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<U> firstTimeoutIndicator;
    final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
    final Publisher<? extends T> other;

    interface OnTimeout {
        void onError(Throwable th);

        void timeout(long j);
    }

    public FlowableTimeout(Flowable<T> flowable, Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function, Publisher<? extends T> publisher2) {
        super(flowable);
        this.firstTimeoutIndicator = publisher;
        this.itemTimeoutIndicator = function;
        this.other = publisher2;
    }

    @Override // io.reactivex.Flowable
    protected void subscribeActual(Subscriber<? super T> subscriber) {
        if (this.other == null) {
            this.source.subscribe((FlowableSubscriber) new TimeoutSubscriber(new SerializedSubscriber(subscriber), this.firstTimeoutIndicator, this.itemTimeoutIndicator));
        } else {
            this.source.subscribe((FlowableSubscriber) new TimeoutOtherSubscriber(subscriber, this.firstTimeoutIndicator, this.itemTimeoutIndicator, this.other));
        }
    }

    static final class TimeoutSubscriber<T, U, V> implements FlowableSubscriber<T>, Subscription, OnTimeout {
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final Publisher<U> firstTimeoutIndicator;
        volatile long index;
        final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
        Subscription s;
        final AtomicReference<Disposable> timeout = new AtomicReference<>();

        TimeoutSubscriber(Subscriber<? super T> subscriber, Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function) {
            this.actual = subscriber;
            this.firstTimeoutIndicator = publisher;
            this.itemTimeoutIndicator = function;
        }

        @Override // io.reactivex.FlowableSubscriber, org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (this.cancelled) {
                    return;
                }
                Subscriber<? super T> subscriber = this.actual;
                Publisher<U> publisher = this.firstTimeoutIndicator;
                if (publisher != null) {
                    TimeoutInnerSubscriber timeoutInnerSubscriber = new TimeoutInnerSubscriber(this, 0L);
                    if (LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(this.timeout, null, timeoutInnerSubscriber)) {
                        subscriber.onSubscribe(this);
                        publisher.subscribe(timeoutInnerSubscriber);
                        return;
                    }
                    return;
                }
                subscriber.onSubscribe(this);
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            long j = this.index + 1;
            this.index = j;
            this.actual.onNext(t);
            Disposable disposable = this.timeout.get();
            if (disposable != null) {
                disposable.dispose();
            }
            try {
                Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The publisher returned is null");
                TimeoutInnerSubscriber timeoutInnerSubscriber = new TimeoutInnerSubscriber(this, j);
                if (LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(this.timeout, disposable, timeoutInnerSubscriber)) {
                    publisher.subscribe(timeoutInnerSubscriber);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                this.actual.onError(th);
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            cancel();
            this.actual.onError(th);
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            cancel();
            this.actual.onComplete();
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            this.s.request(j);
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
            DisposableHelper.dispose(this.timeout);
        }

        @Override // io.reactivex.internal.operators.flowable.FlowableTimeout.OnTimeout
        public void timeout(long j) {
            if (j == this.index) {
                cancel();
                this.actual.onError(new TimeoutException());
            }
        }
    }

    static final class TimeoutInnerSubscriber<T, U, V> extends DisposableSubscriber<Object> {
        boolean done;
        final long index;
        final OnTimeout parent;

        TimeoutInnerSubscriber(OnTimeout onTimeout, long j) {
            this.parent = onTimeout;
            this.index = j;
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(Object obj) {
            if (this.done) {
                return;
            }
            this.done = true;
            cancel();
            this.parent.timeout(this.index);
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.done = true;
                this.parent.onError(th);
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            if (this.done) {
                return;
            }
            this.done = true;
            this.parent.timeout(this.index);
        }
    }

    static final class TimeoutOtherSubscriber<T, U, V> implements FlowableSubscriber<T>, Disposable, OnTimeout {
        final Subscriber<? super T> actual;
        final FullArbiter<T> arbiter;
        volatile boolean cancelled;
        boolean done;
        final Publisher<U> firstTimeoutIndicator;
        volatile long index;
        final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
        final Publisher<? extends T> other;
        Subscription s;
        final AtomicReference<Disposable> timeout = new AtomicReference<>();

        TimeoutOtherSubscriber(Subscriber<? super T> subscriber, Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function, Publisher<? extends T> publisher2) {
            this.actual = subscriber;
            this.firstTimeoutIndicator = publisher;
            this.itemTimeoutIndicator = function;
            this.other = publisher2;
            this.arbiter = new FullArbiter<>(subscriber, this, 8);
        }

        @Override // io.reactivex.FlowableSubscriber, org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (this.arbiter.setSubscription(subscription)) {
                    Subscriber<? super T> subscriber = this.actual;
                    Publisher<U> publisher = this.firstTimeoutIndicator;
                    if (publisher != null) {
                        TimeoutInnerSubscriber timeoutInnerSubscriber = new TimeoutInnerSubscriber(this, 0L);
                        if (LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(this.timeout, null, timeoutInnerSubscriber)) {
                            subscriber.onSubscribe(this.arbiter);
                            publisher.subscribe(timeoutInnerSubscriber);
                            return;
                        }
                        return;
                    }
                    subscriber.onSubscribe(this.arbiter);
                }
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            if (this.done) {
                return;
            }
            long j = this.index + 1;
            this.index = j;
            if (this.arbiter.onNext(t, this.s)) {
                Disposable disposable = this.timeout.get();
                if (disposable != null) {
                    disposable.dispose();
                }
                try {
                    Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The publisher returned is null");
                    TimeoutInnerSubscriber timeoutInnerSubscriber = new TimeoutInnerSubscriber(this, j);
                    if (LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(this.timeout, disposable, timeoutInnerSubscriber)) {
                        publisher.subscribe(timeoutInnerSubscriber);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.actual.onError(th);
                }
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            dispose();
            this.arbiter.onError(th, this.s);
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            if (this.done) {
                return;
            }
            this.done = true;
            dispose();
            this.arbiter.onComplete(this.s);
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            this.cancelled = true;
            this.s.cancel();
            DisposableHelper.dispose(this.timeout);
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return this.cancelled;
        }

        @Override // io.reactivex.internal.operators.flowable.FlowableTimeout.OnTimeout
        public void timeout(long j) {
            if (j == this.index) {
                dispose();
                this.other.subscribe(new FullArbiterSubscriber(this.arbiter));
            }
        }
    }
}
