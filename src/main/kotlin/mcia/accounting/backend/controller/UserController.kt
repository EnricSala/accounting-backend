package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.AppUser
import mcia.accounting.backend.service.auth.UserService
import mcia.accounting.backend.service.request.UserRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UserController.PATH)
class UserController(private val userService: UserService) :
        BaseController<AppUser, UserRequest>(UserController.PATH, userService) {

    @GetMapping
    fun findAll(): Iterable<AppUser> {
        log.debug("GET {}", PATH)
        return service.findAll()
    }

    @GetMapping("/self")
    fun self(auth: Authentication): AppUser {
        log.debug("GET {}/self {}", PATH, auth.name)
        return userService.findByUsername(auth.name)
    }

    @PostMapping("/self/password")
    fun selfPasswordChange(@RequestParam password: String, auth: Authentication) {
        log.debug("POST {}/self/password {}", PATH, auth.name)
        userService.selfPasswordChange(auth.name, password)
    }

    @PostMapping("/{id}/password")
    fun forcePasswordChange(@PathVariable id: Long, @RequestParam password: String) {
        log.debug("POST {}/{}/password", PATH, id)
        userService.forcePasswordChange(id, password)
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + AppUser.RESOURCE
    }

}
