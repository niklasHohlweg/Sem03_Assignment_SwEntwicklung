package dhbw.einpro.blogengine;

import dhbw.einpro.blogengine.exceptions.DuplicateEmailException;
import dhbw.einpro.blogengine.exceptions.DuplicateUserException;
import dhbw.einpro.blogengine.interfaces.IBlogEngine;
import dhbw.einpro.blogengine.interfaces.IUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
    private IUser user_one;
    private IUser user_two;
    private IUser user_three;
    private IBlogEngine blogEngineMain;

    @BeforeEach
    public void initalizeUser() throws DuplicateEmailException, DuplicateUserException {
        this.blogEngineMain = new BlogEngine();

        this.user_one = new User("Goldi", "holdi", "goldiholdi@gmail.com");
        this.user_two = new User("Max", "Musti", "maximusti@arcor.de");
        this.user_three = new User("Ju", "Goldi", "jGoldi@gmx.de");

        this.blogEngineMain.addUser(this.user_one);
        this.blogEngineMain.addUser(this.user_two);
        this.blogEngineMain.addUser(this.user_three);
    }

    @Test
    public void getEmailTest() {
        Assertions.assertEquals("goldiholdi@gmail.com", this.user_one.getEmail());
        Assertions.assertEquals("maximusti@arcor.de", this.user_two.getEmail());
        Assertions.assertEquals("jGoldi@gmx.de", this.user_three.getEmail());
    }

    @Test
    public void setEmailTest() {
        this.user_three.setEmail("neueEmail@email.de");
        Assertions.assertEquals("neueEmail@email.de", this.user_three.getEmail());
    }

    @Test
    public void getFirstNameTest() {
        Assertions.assertEquals("Goldi", this.user_one.getFirstName());
        Assertions.assertEquals("Max", this.user_two.getFirstName());
        Assertions.assertEquals("Ju", this.user_three.getFirstName());
    }

    @Test
    public void setFirstNameTest() {
        this.user_three.setFirstName("newName");
        Assertions.assertEquals("newName", this.user_three.getFirstName());
    }

    @Test
    public void getLastNameTest() {
        Assertions.assertEquals("holdi", this.user_one.getLastName());
        Assertions.assertEquals("Musti", this.user_two.getLastName());
        Assertions.assertEquals("Goldi", this.user_three.getLastName());
    }

    @Test
    public void setLastNameTest() {
        this.user_three.setLastName("newName");
        Assertions.assertEquals("newName", this.user_three.getLastName());
    }
}
