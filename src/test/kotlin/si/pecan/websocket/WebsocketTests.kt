package si.pecan.websocket

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import si.pecan.ChatApplication
import java.lang.reflect.Type
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

/**
 * Created by matjaz on 3/14/17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ChatApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebsocketTests {

    companion object {
        val WEBSOCKET_URI = "ws://localhost:{port}/websocket"
        val WEBSOCKET_TOPIC = "/topic"
    }

    private fun getUri() = WEBSOCKET_URI.replace("{port}", port.toString())


    @LocalServerPort
    var port: Long  = 0

    lateinit var queue: BlockingQueue<String>
    lateinit var client: WebSocketStompClient
    lateinit var handler: StompFrameHandler
    @Before
    fun setup() {
        queue = LinkedBlockingDeque()
        client = WebSocketStompClient(SockJsClient(listOf(WebSocketTransport(StandardWebSocketClient()))))
        handler = DefaultStompFrameHandler(queue)
    }

    @Test
    fun shouldReceiveMessageFromServer() {
        val session = client.connect(getUri(), object: StompSessionHandlerAdapter(){}).get(1, TimeUnit.SECONDS)
        session.subscribe(WEBSOCKET_TOPIC, handler)

        val message = "Test"
        session.send(WEBSOCKET_TOPIC, message.toByteArray())
        Assert.assertEquals(message, queue.poll(1, TimeUnit.SECONDS))
    }


}

class DefaultStompFrameHandler(val queue: BlockingQueue<String>) : StompFrameHandler {

    override fun handleFrame(headers: StompHeaders?, payload: Any?) {
         queue.offer(String(payload as ByteArray))
    }

    override fun getPayloadType(headers: StompHeaders?): Type {
        return byteArrayOf().javaClass
    }

}