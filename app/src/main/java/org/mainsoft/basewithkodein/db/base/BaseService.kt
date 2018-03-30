package org.mainsoft.basewithkodein.db.base

import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.Property
import io.objectbox.query.QueryBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable

abstract class BaseService<T>(db: BoxStore) {

    private val subscriptions = CompositeDisposable()

    private val notesBox: Box<T> = db.boxFor<T>(getType())
    protected var builder: QueryBuilder<T> = notesBox.query()

    protected fun close() {
        builder.close()
    }

    private fun reInit() {
        close()
        builder = notesBox.query()
    }

    fun readAll(consumer: Consumer<MutableList<T>>) {
        addSubscribe(initDisposable({ builder.build().find() }, consumer))
    }

    private fun readSortAll(property: Property, consumer: Consumer<MutableList<T>>, isAsc: Boolean) {
        addSubscribe(initDisposable({
            if (isAsc) {
                builder.order(property).build().find()
            } else {
                builder.orderDesc(property).build().find()
            }
        }, consumer))
    }

    fun readSortAllAsc(property: Property, consumer: Consumer<MutableList<T>>) {
        readSortAll(property, consumer, true)
    }

    fun readSortAllDesc(property: Property, consumer: Consumer<MutableList<T>>) {
        readSortAll(property, consumer, false)
    }

    fun readSortAll(propertyWhere: Property, value: Long,
                    propertyOrder: Property,
                    consumer: Consumer<MutableList<T>>) {
        addSubscribe(initDisposable({
            builder.equal(propertyWhere, value)
                    .order(propertyOrder)
                    .build()
                    .find()
        }, consumer))
    }

    fun readLessSortAll(property: Property,
                        value: Long,
                        propertyOrder: Property,
                        consumer: Consumer<MutableList<T>>) {
        addSubscribe(initDisposable({
            builder
                    .less(property, value)
                    .order(propertyOrder)
                    .build()
                    .find()
        }, consumer))
    }

    fun readGreaterSortAll(value: Long,
                           property: Property,
                           propertyOrder: Property,
                           consumer: Consumer<MutableList<T>>) {
        addSubscribe(initDisposable({
            builder.greater(property, value)
                    .order(propertyOrder)
                    .build()
                    .find()
        }, consumer))
    }

    fun readBetweenSortAll(value: Long, valueSecond: Long,
                           property: Property,
                           propertyOrder: Property,
                           consumer: Consumer<MutableList<T>>) {
        addSubscribe(initDisposable({
            builder.between(property, value, valueSecond)
                    .order(propertyOrder)
                    .build()
                    .find()
        }, consumer))
    }

    fun readById(id: Long, consumer: Consumer<T>) {
        addSubscribe(initItemDisposable({ notesBox.get(id) }, consumer))
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

    fun reSave(id: Long, item: T, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.remove(id)
            notesBox.put(item)
            true
        }, consumer))
    }

    fun save(item: T, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.put(item)
            true
        }, consumer))
    }

    fun saveAndLoad(item: T, consumer: Consumer<MutableList<T>>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.put(item)
            true
        }, Consumer { readAll(consumer) }))
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

    fun remove(items: List<T>) {
        remove(items, Consumer {
            //LISTENER SAVE
        })
    }

    fun remove(items: List<T>, consumer: Consumer<Boolean>) {
        addSubscribe(initBolDisposable(Callable {
            notesBox.remove(items)
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

    protected open fun initBolDisposable(callable: Callable<Boolean>, consumer: Consumer<Boolean>): Disposable {
        return Observable.fromCallable(callable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { false }
                .subscribe { t ->
                    consumer.accept(t)
                    reInit()
                    onStop()
                }
    }

    ////////////////////////////////////////////////////////////////////////////

    protected open fun initDisposable(callable: () -> MutableList<T>, consumer: Consumer<MutableList<T>>): Disposable {
        return Observable.fromCallable(callable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { ArrayList() }
                .subscribe { t ->
                    consumer.accept(t)
                    reInit()
                    onStop()
                }
    }

    ////////////////////////////////////////////////////////////////////////////

    protected open fun initItemDisposable(callable: () -> T, consumer: Consumer<T>): Disposable {
        return Observable.fromCallable(callable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    consumer.accept(t)
                    reInit()
                    onStop()
                }
    }

    ////////////////////////////////////////////////////////////////////////////

    protected open fun addSubscribe(subscription: Disposable) {
        onStop()
        subscriptions.add(subscription)
    }

    ////////////////////////////////////////////////////////////////////////////

    protected open fun onStop() {
        subscriptions.clear()
    }

    ////////////////////////////////////////////////////////////////////////////

    protected abstract fun getType(): Class<T>


    ////////////////////////////////////////////////////////////////////////////


    protected fun getEndOfDay(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    protected fun getStartOfDay(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}