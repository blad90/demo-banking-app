package com.demobanking.service;

import com.demobanking.dto.UserDTO;
import com.demobanking.entity.User;
import com.demobanking.entity.UserState;
import com.demobanking.events.Users.ValidateUserCommand;
import com.demobanking.exceptions.UserAlreadyExistsException;
import com.demobanking.exceptions.UserNotFoundException;
import com.demobanking.messaging.UserValidateProducer;
import com.demobanking.repository.IUserRepository;
import com.demobanking.utils.UserMapper;
import com.google.protobuf.Message;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService{

    private IUserRepository userRepository;
    private UserValidateProducer userValidateProducer;
    private final KafkaTemplate<String, Message> template;

    @KafkaListener(
            topics = "VALIDATE_USER_CMD")
    public void onUserValidate(ValidateUserCommand validateUserCommand)  {
        boolean validated = userRepository.findById(validateUserCommand.getUserId()).isPresent();


        User user = userRepository.findById(validateUserCommand.getUserId()).orElse(null);

        if(user != null) {
            UserDTO userDTO = UserMapper.mapToDTO(user);
            userValidateProducer.publishUserValidated(userDTO, validateUserCommand.getSagaId());
        } else{
            userValidateProducer.publishUserNotValidated(validateUserCommand.getSagaId());
        }
    }

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
    public void registerLogActivity(Long userId) {
        IO.println("### REGISTERING LOG ACTIVITY ### for USER ID: " + userId); // TODO: Example, placeholder
        //userValidateProducer.publishUserValidateEvents(new UserValidatedEvent(1L, true));

    }

    @Override
    public void reverseLogActivity(Long userId) {
        IO.println("### REVERSING LOG ACTIVITY ### for USER ID: " + userId); // TODO: Example, placeholder
        //userValidateProducer.publishUserValidateEvents(new UserValidatedEvent(1L, false));
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
