package com.test.books.business.usecase.base

import com.test.books.data.repository.RepositoryProxy
import rx.Observable
import rx.Scheduler
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.schedulers.TestScheduler
import rx.subscriptions.Subscriptions

interface BaseUseCase<K> {
    fun subscribeTest(subscriber: BaseSubscriber<K>?)
    fun subscribe(subscriber: BaseSubscriber<K>?)
}

abstract class BaseUseCaseImpl<K> : BaseUseCase<K> {

    private var subscriberScheduler: Scheduler? = null
    private var observableScheduler: Scheduler? = null
    private var subscription: Subscription? = Subscriptions.empty()

    protected lateinit var repositoryFactory: RepositoryProxy

    val isUnsubscribed: Boolean
        get() = subscription!!.isUnsubscribed

    constructor() {
        this.subscriberScheduler = Schedulers.io()
        this.observableScheduler = AndroidSchedulers.mainThread()
    }

    constructor(repositoryFactory: RepositoryProxy) {
        this.repositoryFactory = repositoryFactory
        this.subscriberScheduler = Schedulers.io()
        this.observableScheduler = AndroidSchedulers.mainThread()
    }

    abstract fun buildUseCaseObservable(): Observable<K>

    override fun subscribe(subscriber: BaseSubscriber<K>?) {
        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(subscriberScheduler)
                .observeOn(observableScheduler)
                .subscribe(subscriber)
    }

    override fun subscribeTest(subscriber: BaseSubscriber<K>?) {
        var testScheduler = TestScheduler()
        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(testScheduler)
                .observeOn(testScheduler)
                .subscribe(subscriber)
        testScheduler.triggerActions()
    }

    fun subscribe() {
        this.subscription = this.buildUseCaseObservable().subscribeOn(subscriberScheduler)
                .observeOn(observableScheduler).subscribe(object : BaseSubscriber<K>(null) {

                })
    }

    fun unsubscribe() {
        if (subscription != null && !subscription!!.isUnsubscribed) {
            subscription!!.unsubscribe()
        }
    }
}
