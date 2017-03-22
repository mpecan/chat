package si.pecan

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.text.SimpleDateFormat

@SpringBootApplication
open class ChatApplication {
    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder
            = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
            .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .dateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).serializationInclusion(JsonInclude.Include.NON_NULL)
}

fun main(args: Array<String>) {
    SpringApplication.run(ChatApplication::class.java, *args)
}
