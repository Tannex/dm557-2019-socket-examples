package dk.imada.dm557

import groovy.util.logging.Slf4j
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post

@Slf4j
@Controller("/")
class HomeController {

    @Get("/")
    String index() {
        log.debug("GET /")
        "Hello Network and Security\n"
    }

    @Post("/")
    String listen(String input) {
        log.debug("POST / (input=$input)")
        "You posted '$input'\n"
    }
}