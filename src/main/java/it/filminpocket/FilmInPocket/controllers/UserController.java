package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.UpdateUserDto;
import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.mappers.UserMapper;
import it.filminpocket.FilmInPocket.servicies.ImageUploadService;
import it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService;
import it.filminpocket.FilmInPocket.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserCardAcquisitionService userCardAcquisitionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private UserMapper userMapper;


    @GetMapping("/me/tickets")
    public UserDto getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userCardAcquisitionService.rechargeFilmTicketsForUser(user);

        return userMapper.toDto(user);
    }


    @PutMapping("/me")
    public UserDto updateUser(
            @RequestBody UpdateUserDto dto,
            @AuthenticationPrincipal User user) {
        return userService.updateUser(user, dto);
    }


    @PutMapping("/me/image")
    public UserDto updateProfileImage(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal User user) throws IOException {
        String imageUrl = imageUploadService.uploadImage(image);
        return userService.updateProfileImage(user, imageUrl);
    }
}
