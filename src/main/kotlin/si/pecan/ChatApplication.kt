package si.pecan

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ChatApplication

fun main(args: Array<String>) {
    SpringApplication.run(ChatApplication::class.java, *args)
}
