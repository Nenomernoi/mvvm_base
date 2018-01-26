package org.mainsoft.basewithkodein.db.base

import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.query.QueryBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.concurrent.Callable

abstract class BaseService<T>(db: BoxStore) {

    private val subscriptions = CompositeDisposable()

    val notesBox: Box<T> = db.boxFor<T>(getType())
    val builder: QueryBuilder<T> = notesBox.query()

    fun readAll(consumer: Consumer<MutableList<T>>) {
        addSubscribe(initDisposabe({ builder.build().find() }, consumer))
    }

    fun reSave(items: List<T>) {
        reSave(items, Consumer {
            //LISTENER SAVE
        })
    }

    fun reSave(items: List<T>, consumer: Consumer<Boolean>) {

        addSubscribe(initBolDisposable(Callable {
            notesBox.removeAll()
            notesBox.put(items)
            true
        }, consumer))
    }

    fun save(item: T) {
        save(item, Consumer {
            //LISTENER SAVE
        })
    }

    fun save(item: T, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.put(item)
            true
        }, consumer))
    }

    fun save(items: List<T>) {
        save(items, Consumer {
            //LISTENER SAVE
        })
    }

    fun save(items: List<T>, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.put(items)
            true
        }, consumer))
    }

    fun remove(item: T) {
        remove(item, Consumer {
            //LISTENER SAVE
        })
    }

    fun remove(item: T, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.remove(item)
            true
        }, consumer))
    }

    fun removeById(id: Long) {
        removeById(id, Consumer {
            //LISTENER SAVE
        })
    }

    fun removeById(id: Long, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.remove(id)
            true
        }, consumer))
    }

    fun removeById(id: List<Long>) {
        removeById(id, Consumer {
            //LISTENER SAVE
        })
    }

    fun removeById(id: List<Long>, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.removeByKeys(id)
            true
        }, consumer))
    }

    fun removeAll() {
        removeAll(Consumer {
            //LISTENER SAVE
        })
    }

    fun removeAll(consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.removeAll()
            true
        }, consumer))
    }

    ////////////////////////////////////////////////////////////////////////////

    protected fun initBolDisposable(callable: Callable<Boolean>, consumer: Consumer<Boolean>): Disposable {

        return Observable.fromCallable(callable).subscribeOn(Schedulers.computation()).observeOn(
                AndroidSchedulers.mainThread()).onErrorReturn { throwable -> false }.subscribe(consumer)
    }

    ////////////////////////////////////////////////////////////////////////////

    protected fun initDisposabe(callable: () -> MutableList<T>, consumer: Consumer<MutableList<T>>): Disposable {

        return Observable.fromCallable(callable).subscribeOn(Schedulers.computation()).observeOn(
                AndroidSchedulers.mainThread()).onErrorReturn { throwable -> ArrayList() }.subscribe(consumer)
    }

    ////////////////////////////////////////////////////////////////////////////

    protected fun addSubscribe(subscription: Disposable) {
        subscriptions.add(subscription)
    }

    ////////////////////////////////////////////////////////////////////////////

    fun onStop() {
        subscriptions.clear()
    }

    ////////////////////////////////////////////////////////////////////////////

    protected abstract fun getType(): Class<T>
}