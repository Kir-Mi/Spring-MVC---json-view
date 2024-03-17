package com.example.jsonview.service;

import com.example.jsonview.exception.NotFoundException;
import com.example.jsonview.model.Product;
import com.example.jsonview.model.User;
import com.example.jsonview.model.UserOrder;
import com.example.jsonview.repository.UserRepository;
import com.example.jsonview.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private final User user = new User();
    private final UserOrder order = new UserOrder();
    private final Product product = new Product();

    @BeforeEach
    public void setUp() {
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");

        product.setId(1L);
        product.setName("Apple");
        product.setPrice(10L);

        order.setId(1L);
        order.setAmount(10L);
        order.setStatus(Status.CREATED);
        order.setProducts(new ArrayList<>(List.of(product)));
        user.setOrders(new ArrayList<>(List.of(order)));
    }

    @Test
    public void getUserByIdTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(user, userService.getUserById(1L));

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void whenUserNotFound_thenThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllUsersTest() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepository.findAll()).thenReturn(userList);

        assertEquals(userList, userService.getAllUsers());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUserTest() {
        User user = new User();
        user.setName("John");

        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.createUser(user));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void removeUserTest() {
        doNothing().when(userRepository).deleteById(1L);
        userService.removeUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateUserTest() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John");
        existingUser.setSurname("Doe");

        User updatedUser = new User();
        updatedUser.setName("Jane");
        updatedUser.setSurname("Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getSurname(), result.getSurname());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_NotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, user));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any());
    }
}
