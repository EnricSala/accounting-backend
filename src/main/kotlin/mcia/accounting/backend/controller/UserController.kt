package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.AppUser
import mcia.accounting.backend.service.auth.UserService
import mcia.accounting.backend.service.request.UserRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UserController.PATH)
class UserController(private val userService: UserService) {

    @GetMapping
    fun findAll(): Iterable<AppUser> {
        log.debug("GET {}", PATH)
        return userService.findAll()
    }

    @GetMapping("/self")
    fun self(auth: Authentication): AppUser {
        log.debug("GET {}/self {}", PATH, auth.name)
        return userService.findByUsername(auth.name)
    }

    @PostMapping("/self/password")
    fun selfPasswordChange(@RequestParam password: String, auth: Authentication) {
        log.debug("POST {}/self/password {}", PATH, auth.name)
        userService.selfPasswordChange(auth, password)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): AppUser {
        log.debug("GET {}/{}", PATH, id)
        return userService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody request: UserRequest): AppUser {
        log.debug("POST {} {}", PATH, request)
        return userService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UserRequest): AppUser {
        log.debug("PUT {}/{} {}", PATH, id, request)
        return userService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return userService.deleteById(id)
    }

    @PutMapping("/{id}/password")
    fun otherPasswordChange(@PathVariable id: Long, @RequestParam password: String, auth: Authentication) {
        log.debug("PUT {}/{}/password {}", PATH, id, auth.name)
        userService.forcePasswordChange(id, auth, password)
    }

    companion object {
        private val log = loggerOf(UserController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + AppUser.RESOURCE
    }

}
