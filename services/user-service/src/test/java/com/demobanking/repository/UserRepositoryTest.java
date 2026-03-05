package com.demobanking.repository;

import com.demobanking.entity.User;
import com.demobanking.entity.UserState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    @DisplayName("Test Case: Verifying if user can be created and then retrieved")
    public void testUserRepoSaveAndReturnUser(){
        User testUser = User.builder()
                .firstName("Maria")
                .lastName("Stacy")
                .alias("Mary")
                .nationalId("274-886-9686")
                .address("Avenue Testing K")
                .emailAddress("ms@email.com")
                .phoneNumber("305-565-7777")
                .userState(UserState.USER_CREATED)
                .build();
        User savedUser = userRepository.save(testUser);
        Assertions.assertNotNull(savedUser, "User exists");
        Assertions.assertTrue(savedUser.getId() > 0);
    }

    @Test
    @DisplayName("Test Case: Verifying whether multiple users can be registered")
    public void testUserRepoSaveAllAndReturnAllSavedUsers(){
        User testUser1 = User.builder()
                .firstName("John")
                .lastName("Marcos")
                .alias("Johnny")
                .nationalId("111-333-5555")
                .address("Grand Concourse")
                .emailAddress("jm@email.com")
                .phoneNumber("222-565-7777")
                .userState(UserState.USER_CREATED)
                .build();
        User testUser2 = User.builder()
                .firstName("Stephanie")
                .lastName("Stacy")
                .alias("Steph")
                .nationalId("212-886-7777")
                .address("Avenue Testing M")
                .emailAddress("sss@email.com")
                .phoneNumber("305-565-7777")
                .userState(UserState.USER_CREATED)
                .build();
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        List<User> users = userRepository.findAll();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size(), "There are two users saved.");
    }

    @Test
    @DisplayName("Test Case: Verifying whether an user/customer can be retrieved by the National ID")
    public void testUserRepoFindByNationalId(){
        User testUser1 = User.builder()
                .firstName("Mathews")
                .lastName("Jordan")
                .alias("Matt")
                .nationalId("111-222-4321")
                .address("La Correa Street")
                .emailAddress("jord@email.com")
                .phoneNumber("111-222-4444")
                .userState(UserState.USER_CREATED)
                .build();

        userRepository.save(testUser1);

        User userRetrievedByNationalId = userRepository.findUserByNationalId("111-222-4321").get();

        Assertions.assertNotNull(userRetrievedByNationalId);
        Assertions.assertEquals(testUser1.getNationalId(), userRetrievedByNationalId.getNationalId());
    }
}
