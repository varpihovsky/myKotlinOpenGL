package multithreading

object ThreadPool {
    var checkTime = 8000L

    private val pool: MutableList<Thread> = mutableListOf()

    init {
        addThread(
            Thread {
                while (!Thread.currentThread().isInterrupted) {
                    try {
                        Thread.sleep(checkTime)
                    } catch (e: InterruptedException) {
                        break
                    }

                    pool.forEachIndexed { i: Int, thread: Thread ->
                        if (thread.isInterrupted) {
                            pool.removeAt(i)
                            return@forEachIndexed
                        }
                    }
                }
            }
        )
    }

    @Synchronized
    internal fun addThread(thread: Thread): Thread {
        pool.add(thread)
        return thread
    }

    @Synchronized
    internal fun interruptAll() {
        pool.forEach { it.interrupt() }
    }
}