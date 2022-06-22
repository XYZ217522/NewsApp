package com.example.news

import io.reactivex.Flowable
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleRxTest {

    @Test
    fun rxDeferTest() {
        // 如果需要每個觀察者被訂閱的時候都重新創建被觀察者（一對一），則可以使用defer操作符：
        val flowable = Flowable.defer {
            Flowable.just("A", "B", "C")
        }.cache()

        // 訂閱第一個觀察者
        flowable.subscribe { str: String? ->
            println("rxDeferTest first flowable : $str")
        }

        // 訂閱第二個觀察者
        flowable.subscribe { str: String? ->
            println("rxDeferTest second flowable : $str")
        }
    }

    @Test
    fun rxConcatArrayTest() {
        Flowable.concatArray(
            Flowable.just(1),
            Flowable.just(2),
            Flowable.just(3),
            Flowable.just(4),
            Flowable.just(5),
            Flowable.just(6),
            Flowable.just(7)
        ).subscribe { ele: Int -> println("rxConcatArrayTest : $ele") }
    }

    @Test
    fun rxZipTest() {
        Flowable.zip(
            Flowable.just(1, 2, 3),
            Flowable.just(4, 5)
        ) { int1: Int, int2: Int -> int1 + int2 }
            .subscribe { ele: Int -> println("rxZipTest : $ele") }
    }
}