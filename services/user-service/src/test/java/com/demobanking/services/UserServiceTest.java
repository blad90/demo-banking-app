package com.demobanking.services;


import com.demobanking.dto.UserDTO;
import com.demobanking.dto.UserSessionDTO;
import com.demobanking.entity.User;
import com.demobanking.entity.UserState;
import com.demobanking.repository.IUserRepository;
import com.demobanking.service.UserServiceImpl;
import com.demobanking.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                99L, "Jeffey", "Perez", "Jeff",
                "34875-57-33", "Avenue K",
                "212-333-4444", "j@email.com",
                UserState.USER_CREATED, "22"
        );
        userDTO.setUserSessionDTO(new UserSessionDTO("test_user","test_password"));
    }

    @Test
    @DisplayName("Test Case: Verifying if user can be registered")
    public void testUserServiceCreateUser(){
        User testUser = UserMapper.mapToEntity(userDTO, "TEST_SESSION_ID");

//        when(userRepository.save(Mockito.any(User.class)))
//                .thenReturn(testUser);

        //String userId = userService.createUser(userDTO);

        //Assertions.assertNotNull(userId);
        //verify(userRepository).findById(99L);
    }

    @Test
    @DisplayName("Test Case: Verifying if user exists by specific ID")
    public void testUserServiceUserExistsById(){
        User testUser = UserMapper.mapToEntity(userDTO, "TEST_SESSION_ID");

        when(userRepository.findById(99L))
                .thenReturn(Optional.of(testUser));

        Optional<UserDTO> result = userService.retrieveUserById(99L);

        assertEquals(99L, result.get().getId());
        verify(userRepository).findById(99L);
    }

    @Test
    @DisplayName("Test Case: Verifying if user National ID already exists")
    public void testUserNationalIdAlreadyExists(){
        User testUser = UserMapper.mapToEntity(userDTO, "TEST_SESSION_ID");

        when(userRepository.findUserByNationalId("001-736453-9"))
                .thenReturn(Optional.of(testUser));

        Optional<UserDTO> result = userService.findUserByNationalId("001-736453-9");
        assertNotEquals("001-736453-9", result.get().getNationalId());
    }
}
