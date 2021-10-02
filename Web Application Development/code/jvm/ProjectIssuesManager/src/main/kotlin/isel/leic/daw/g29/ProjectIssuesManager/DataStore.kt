package isel.leic.daw.g29.ProjectIssuesManager

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

@Component
class DataStore() {
    private val mapCounter = ConcurrentHashMap<String, Int>()
    private val totalTime = AtomicLong(0)
    private val tot = AtomicInteger(0)

    fun add(time: Long) {
        totalTime.getAndAdd(time)
    }

    fun inc(path: String) {
        val value = mapCounter.get(path)
        if (value == null) {
            mapCounter.put(path, 1)
            tot.getAndAdd(1)
        } else {
            mapCounter.put(path, value + 1)
        }
    }

    fun getCounter(path: String): Int {
        return mapCounter.get(path)!!
    }

    fun getAverage() : Long? {
        return totalTime.get().div(tot.get())
    }
}