package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.UpdateUserDto;
import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.mappers.UserMapper;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserDto updateUser(User user, UpdateUserDto dto) {
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public UserDto updateProfileImage(User user, String imageUrl) {
        user.setImageUrl(imageUrl);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}
