import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import java.io.InputStream
import java.nio.charset.StandardCharsets

fun getImmediateScheduler() = object : Scheduler() {
    override fun createWorker(): Worker {
        return ExecutorScheduler.ExecutorWorker({ it.run() }, false)
    }
}

fun <T> Class<T>.enqueueResponse(mockServer: MockWebServer, fileName: String) {
    val inputStream: InputStream = this.classLoader
        .getResourceAsStream("api-response/$fileName")
        ?: kotlin.run { println("can't get inputStream");return }

    val source = Okio.buffer(Okio.source(inputStream))
    mockServer.enqueue(
        MockResponse().apply {
            setBody(source.readString(StandardCharsets.UTF_8))
        }
    )
}