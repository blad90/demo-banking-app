package com.banking.user.service;

import com.banking.user.dto.UserDTO;
import com.banking.user.entity.User;
import com.banking.user.entity.UserState;
import com.banking.user.exceptions.UserAlreadyExistsException;
import com.banking.user.exceptions.UserNotFoundException;
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
        boolean userExistsWithNationalId = userRepository
                .findUserByNationalId(userDTO.getNationalId()).isPresent();

        if(userExistsWithNationalId) throw new UserAlreadyExistsException(userDTO.getNationalId());

        User user = UserMapper.mapToEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        User updatedUser;

        updatedUser = UserMapper.mapToEntityUpdate(userDTO, user);
        userRepository.save(updatedUser);
    }

    @Override
    public void changeUserState(Long id, UserDTO userDTO, UserState userState) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User updatedUser;

        updatedUser = UserMapper.mapToEntityUpdate(userDTO, user);
        updatedUser.setUserState(userState);
        userRepository.save(updatedUser);
    }

    @Override
    public void enableUser(Long id, UserDTO userDTO) {
        changeUserState(id, userDTO, UserState.USER_ACTIVE);
    }

    @Override
    public void suspendUser(Long id, UserDTO userDTO) {
        changeUserState(id, userDTO, UserState.USER_SUSPENDED);
    }

    @Override
    public void disableUser(Long id, UserDTO userDTO) {
        changeUserState(id, userDTO, UserState.USER_INACTIVE);
    }

    @Override
    public Optional<UserDTO> retrieveUserById(Long id) {
        User userRetrieved = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
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
                        user.getNationalId(),
                        user.getAddress(),
                        user.getPhoneNumber(),
                        user.getEmailAddress(),
                        user.getUserState()
                )
        ).toList();
    }
}
