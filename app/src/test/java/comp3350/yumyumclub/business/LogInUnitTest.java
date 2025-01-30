package comp3350.yumyumclub.business;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import recipebook.business.AccessUser;
import recipebook.objects.User;
import recipebook.persistence.UserPersistence;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class LogInUnitTest
{
    private AccessUser accessUser;
    @Mock private UserPersistence userPersistence;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accessUser = new AccessUser(userPersistence);
    }

    @Test
    public void testUser()
    {
        //test add user
        // Arrange
        doNothing().when(userPersistence).addUser(anyString(), anyString(), anyString());

        // Act
        accessUser.addUser("John", "password", "john@example.com");

        // Assert
        verify(userPersistence, times(1)).addUser("John", "password", "john@example.com");

        //test log in
        User mockUser = new User("Seyi", "123", "123@123.com");
        when(userPersistence.logIn("Seyi", "123")).thenReturn(1);
        when(userPersistence.getCurrentUser()).thenReturn(mockUser);

        // Act
        boolean loggedIn = accessUser.logIn("Seyi", "123");

        // Assert
        assertTrue(loggedIn);
        assertEquals("Seyi", accessUser.getCurrentUser().getUserName());

        //test update user
        User mockUser2 = new User("Seyi", "123", "123@123.com");
        when(userPersistence.getCurrentUser()).thenReturn(mockUser2);
        doNothing().when(userPersistence).updateUser(anyString(), anyString());
        accessUser.logIn("Seyi", "123");

        // Act
        accessUser.UpdateUser("newpassword");

        // Assert
        verify(userPersistence, times(1)).updateUser("Seyi", "newpassword");

        //testing login with the new password
        User mockUser3 = new User("Seyi","newpassword","123@123.com");
        when(userPersistence.logIn("Seyi", "newpassword")).thenReturn(1);
        when(userPersistence.getCurrentUser()).thenReturn(mockUser3);

        //testing checkUser
        when(userPersistence.checkUser("Seyi", "123@123.com")).thenReturn(true);
        when(userPersistence.checkUser("dne", "dne")).thenReturn(false);
        assert(accessUser.checkUser("Seyi","new123", "123@123.com"));
        assert(!accessUser.checkUser("dne", "dne", "dne"));
        verify(userPersistence).checkUser("Seyi", "123@123.com");
        verify(userPersistence).checkUser("dne", "dne");

        //testing checkEmpty
        assert(accessUser.checkEmpty("", "", ""));
        assert(accessUser.checkEmpty("Seyi", "123", ""));
        assert(!accessUser.checkEmpty("Seyi", "123", "123@123.com"));

        //testing checkStringLength
        String over50 = "123123123123123123123123123123123123123123123123123123123123123123123123123";
        assert(!accessUser.checkStringLength(over50,"under50", "under50"));
        assert(accessUser.checkStringLength("under50","under50", "under50"));

        //testing log out
        when(userPersistence.logOut("Seyi")).thenReturn(1);
        accessUser.logOut();
        assert(accessUser.getCurrentUser()==null);

        //verify accessUser's get instance works
        assert (AccessUser.getInstance() == accessUser);



    }

}


