package com.example.library.helper;

import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:13
 * desc   :  rx帮助类
 * version: 1.0
 */

public class RxHelper {

    /**
     * 统一线程处理
     * <p>
     * 发布事件io线程，接收事件主线程
     */
    public static <T> MaybeTransformer<T, T> rxSchedulerHelper() {//compose处理线程
        return new MaybeTransformer<T, T>() {

            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一线程处理
     * <p>
     * 发布事件io线程，接收事件主线程
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelperIO() {//compose处理线程
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param t
     * @return Flowable
     */
    public static <T> Flowable<T> createFlowable(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 生成Observable
     *
     * @param t
     * @return Flowable
     */
    public static <T> Observable<T> createObservable(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

}
