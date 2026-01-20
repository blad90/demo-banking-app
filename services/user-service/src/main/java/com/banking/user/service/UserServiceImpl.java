package com.banking.user.service;

import com.banking.user.dto.UserDTO;
import com.banking.user.entity.User;
import com.banking.user.entity.UserState;
import com.banking.user.repository.IUserRepository;
import com.banking.user.utils.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService{

    private IUserRepository userRepository;

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = UserMapper.mapToEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public Boolean updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(null);
        User updatedUser;

        if(user != null){
            updatedUser = UserMapper.mapToEntityUpdate(userDTO, user);
            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }

    @Override
    public Boolean disableUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(null);
        User updatedUser;

        if(user != null){
            updatedUser = UserMapper.mapToEntityUpdate(userDTO, user);
            updatedUser.setUserState(UserState.USER_INACTIVE);
            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserDTO> retrieveUserById(Long id) {
        User userRetrieved = userRepository.findById(id).get();
        UserDTO userDTO = UserMapper.mapToDTO(userRetrieved);

        return Optional.of(userDTO);
    }

    @Override
    public List<UserDTO> retrieveAllUsers() {
        return userRepository.findAll().stream().map(
                user -> new UserDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAlias(),
                        user.getAddress(),
                        user.getPhoneNumber(),
                        user.getEmailAddress(),
                        user.getUserState()
                )
        ).toList();
    }
}
