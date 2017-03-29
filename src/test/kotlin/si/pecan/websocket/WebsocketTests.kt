package si.pecan.websocket

import com.winterbe.expekt.should
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.scheduling.TaskScheduler
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import si.pecan.ChatApplication
import si.pecan.ChatRoomRepository
import si.pecan.InstantMessageRepository
import si.pecan.UserRepository
import si.pecan.controller.ControllerTestBase
import si.pecan.dto.ChatRoom
import si.pecan.dto.Message
import si.pecan.dto.PostMessageRequest
import si.pecan.services.ChatService
import java.lang.reflect.Type
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ChatApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
open class WebsocketTests :ControllerTestBase() {

    companion object {
        val WEBSOCKET_URI = "ws://localhost:{port}/messages"
        val WEBSOCKET_TOPIC = "/topic"
    }

    private fun getUri() = WEBSOCKET_URI.replace("{port}", port.toString())


    @LocalServerPort
    var port: Long  = 0

    @Autowired
    lateinit var chatService: ChatService

    @Autowired
    lateinit var chatRepository: ChatRoomRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var instantMessageRepository: InstantMessageRepository

    @Autowired
    lateinit var scheduler: TaskScheduler
    lateinit var queue: BlockingQueue<String>
    lateinit var client: WebSocketStompClient
    lateinit var handler: StompFrameHandler

    @Before
    fun setup() {
        queue = LinkedBlockingDeque()
        client = WebSocketStompClient(SockJsClient(listOf(WebSocketTransport(StandardWebSocketClient()))))
        client.taskScheduler = scheduler
        handler = DefaultStompFrameHandler(queue)
        client.start()
    }
    protected fun createChatRoom(): Pair<String, ChatRoom> {
        val initiator = "user"
        val target = "target"

        val users = arrayOf(initiator, target).map(userService::getOrCreate)

        val chatRoom = chatService.getOrCreateChat(initiator, target)
        return Pair(initiator, chatRoom)
    }
    @Test
    fun shouldReceiveMessageFromServer() {
        val (initiator, chatRoom) = createChatRoom()
        val session = client.connect(getUri(), object: StompSessionHandlerAdapter(){}).get(1, TimeUnit.SECONDS)
        session.setAutoReceipt(true)
        session.subscribe("/topic/post/${chatRoom.id}", handler)

        val message = "Test"
        session.send("/app/post/${chatRoom.id}", mapper.writeValueAsBytes(PostMessageRequest(initiator, chatRoom.id, message)))

        val value = queue.poll(8, TimeUnit.SECONDS)
        value.should.not.be.`null`
        val (poster, content, created, id, chatId) = mapper.readValue(value, Message::class.java)
        poster.should.equal(initiator)
        content.should.equal(message)
        created.should.not.be.`null`
        id.should.not.be.`null`
        chatId.should.equal(chatRoom.id)
    }

    @After
    fun tearDown() {
        instantMessageRepository.deleteAll()
        chatRepository.deleteAll()
        userRepository.deleteAll()
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