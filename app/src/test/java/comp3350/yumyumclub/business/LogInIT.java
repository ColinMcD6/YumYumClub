package comp3350.yumyumclub.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.business.AccessUser;
import recipebook.objects.User;
import recipebook.persistence.UserPersistence;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class LogInIT {
    private AccessUser accessUser;
    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        UserPersistence persistence = new UserPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.accessUser = new AccessUser(persistence);
    }

    @Test
    public void testLogIn() {
        // Adding a new user
        accessUser.addUser("testUser", "password123", "test@example.com");

        // Log in the new user
        boolean result = accessUser.logIn("testUser", "password123");
        assertTrue("User should be able to log in", result);

        // Verify current user
        User currentUser = accessUser.getCurrentUser();
        assertNotNull("There should be a current user logged in", currentUser);
        assertTrue("Current user should be 'testUser'", "testUser".equals(currentUser.getUserName()));
    }

    @Test
    public void testAddUserAndCheckUser() {
        // Adding a new user
        accessUser.addUser("newUser", "password123", "new@example.com");

        // Check if the new user exists
        boolean userExists = accessUser.checkUser("newUser", "password123", "new@example.com");
        assertTrue("Newly added user should exist", userExists);
    }

    @Test
    public void testUpdateUser() {
        // Adding a new user
        accessUser.addUser("updateUser", "password123", "update@example.com");

        // Log in the new user
        accessUser.logIn("updateUser", "password123");

        // Update the user's password
        accessUser.UpdateUser("newpassword");

        // Log out
        accessUser.logOut();

        // Attempt to log in with the new password
        boolean result = accessUser.logIn("updateUser", "newpassword");
        assertTrue("User should be able to log in with the new password", result);
    }

    @Test
    public void testLogOut() {
        // Adding a new user
        accessUser.addUser("logoutUser", "password123", "logout@example.com");

        // Log in the new user
        accessUser.logIn("logoutUser", "password123");

        // Log out the user
        accessUser.logOut();

        // Verify no current user is logged in
        User currentUser = accessUser.getCurrentUser();
        assertTrue("There should be no current user logged in", currentUser == null);
    }




    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }




}
