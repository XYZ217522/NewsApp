package com.example.news.util

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object SwitchSchedulers {

    fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> {
        return MaybeTransformer { observable: Maybe<T> ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { observable: Single<T> ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
        return FlowableTransformer { observable: Flowable<T> ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}