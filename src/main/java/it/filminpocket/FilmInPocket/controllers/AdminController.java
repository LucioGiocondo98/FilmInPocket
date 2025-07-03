package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.AdminUserViewDto;
import it.filminpocket.FilmInPocket.servicies.AdminViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private AdminViewerService adminViewerService;

    @GetMapping("/users")
    public Page<AdminUserViewDto> getAllUsers(Pageable pageable) {
        return adminViewerService.getAllUsers(pageable);
    }

    @GetMapping("/users/{id}")
    public AdminUserViewDto getUserById(@PathVariable int id) {
        return adminViewerService.getUserById(id);
    }

}
