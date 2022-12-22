package dhbw.einpro.blogengine;

import dhbw.einpro.blogengine.exceptions.DuplicateEmailException;
import dhbw.einpro.blogengine.exceptions.DuplicateUserException;
import dhbw.einpro.blogengine.exceptions.IllegalOperationException;
import dhbw.einpro.blogengine.exceptions.PostNotFoundException;
import dhbw.einpro.blogengine.exceptions.UserNotFoundException;
import dhbw.einpro.blogengine.interfaces.IBlogEngine;
import dhbw.einpro.blogengine.interfaces.IPost;
import dhbw.einpro.blogengine.interfaces.IUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import dhbw.einpro.blogengine.*;

public class BlogEngineTest {
    private IBlogEngine blogEngineMain;
    private IUser user_one;
    private IUser user_two;
    private IUser user_three;
    private IPost post_one;
    private IPost post_two;
    private IPost post_three;

    @BeforeEach
    public void createBlogEngineForTests() throws DuplicateEmailException, DuplicateUserException, UserNotFoundException {
        this.blogEngineMain = new BlogEngine();

        this.user_one = new User("Goldi", "holdi", "goldiholdi@gmail.com");
        this.user_two = new User("Max", "Musti", "maximusti@arcor.de");
        this.user_three = new User("Ju", "Goldi", "jGoldi@gmx.de");

        post_one = new Post("This is title one", "This is content of title one", user_one, blogEngineMain);
        post_two = new Post("Good title", "This is good content", user_two, blogEngineMain);
        post_three = new Post("Bad title", "This is bad content", user_three, blogEngineMain);

        this.blogEngineMain.addUser(user_one);
        this.blogEngineMain.addUser(user_two);
        this.blogEngineMain.addUser(user_three);

        this.blogEngineMain.addPost(post_one);
        this.blogEngineMain.addPost(post_two);
        this.blogEngineMain.addPost(post_three);
    }

    @Test
    public void sizeTest() {
        Assertions.assertEquals(3, blogEngineMain.size());
    }

    @Test
    public void addUserTest() throws DuplicateEmailException, DuplicateUserException {
        IUser user_four = new User("Pia", "Muelli", "pimue@outlook.de");
        Assertions.assertTrue(this.blogEngineMain.addUser(user_four));
        Assertions.assertTrue(this.blogEngineMain.containsUser(user_four));
    }

    @Test
    public void addUserDuplicateEmailExceptionTest() {
        IUser user_four = new User("jana", "Goldi", "jGoldi@gmx.de");
        Assertions.assertThrows(DuplicateEmailException.class, () -> {
            this.blogEngineMain.addUser(user_four);
        });
    }

    @Test
    public void addUserDuplicateUserExceptionTest() throws DuplicateEmailException, DuplicateUserException {
        IUser user_four = new User("Pia", "Muelli", "pimue@outlook.de");
        this.blogEngineMain.addUser(user_four);
        Assertions.assertThrows(DuplicateUserException.class, () -> {
            this.blogEngineMain.addUser(user_four);
        });
    }

    @Test
    public void removeUserTest() {
        Assertions.assertTrue(this.blogEngineMain.removeUser(user_one));
        Assertions.assertFalse(this.blogEngineMain.removeUser(user_one));
    }

    @Test
    public void addPostTest() throws UserNotFoundException, DuplicateEmailException, DuplicateUserException, PostNotFoundException, IllegalOperationException {
        IUser user_four = new User("My", "Name", "isJohn@cena.de");
        IPost post_four = new Post("Great", "Usefull Content inside this post", user_four, this.blogEngineMain);
        this.blogEngineMain.addUser(user_four);

        Assertions.assertEquals(4, this.blogEngineMain.addPost(post_four));

        this.blogEngineMain.removePost(user_two, 2);
        Assertions.assertEquals(5, this.blogEngineMain.addPost(post_two));
    }

    @Test
    public void addPostUserNotFoundExceptionTest() {
        IUser user_four = new User("My", "Name", "isJohn@cena.de");
        IPost post_four = new Post("Great", "Usefull Content inside this post", user_four, this.blogEngineMain);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.blogEngineMain.addPost(post_four);
        });
    }

    @Test
    public void addPostIllegalOperationException() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.blogEngineMain.removePost(user_two, 1);
        });
    }

    @Test
    public void getPostsTest() {
        List<IPost> postList = this.blogEngineMain.getPosts();

        Assertions.assertEquals(3, postList.size());
        Assertions.assertEquals(post_one, postList.get(0));
        Assertions.assertEquals(post_two, postList.get(1));
        Assertions.assertEquals(post_three, postList.get(2));
        Assertions.assertEquals(1, postList.get(0).getId());
        Assertions.assertEquals(2, postList.get(1).getId());
        Assertions.assertEquals(3, postList.get(2).getId());
    }

    @Test
    public void findPostsByAuthorTest() throws UserNotFoundException {
        List<IPost> postList = this.blogEngineMain.findPostsByAuthor(this.user_two);
        Assertions.assertEquals(this.post_two, postList.get(0));

        IPost post_four = new Post("Great", "Usefull Content inside this post", this.user_two, this.blogEngineMain);
        this.blogEngineMain.addPost(post_four);

        postList = this.blogEngineMain.findPostsByAuthor(this.user_two);

        Assertions.assertEquals(this.post_two, postList.get(0));
        Assertions.assertEquals(post_four, postList.get(1));
        Assertions.assertEquals(2, postList.size());
    }

    @Test
    public void findPostByIdTest() {
        IPost post_1 = this.blogEngineMain.findPostById(1);
        IPost post_2 = this.blogEngineMain.findPostById(2);
        IPost post_3 = this.blogEngineMain.findPostById(3);
        IPost post_4 = this.blogEngineMain.findPostById(4);

        Assertions.assertEquals(post_one, post_1);
        Assertions.assertEquals(post_two, post_2);
        Assertions.assertEquals(post_three, post_3);
        Assertions.assertEquals(null, post_4);
    }

    @Test
    public void containsPostTest() {
        Assertions.assertTrue(this.blogEngineMain.containsPost(1));
        Assertions.assertTrue(this.blogEngineMain.containsPost(2));
        Assertions.assertTrue(this.blogEngineMain.containsPost(3));
        Assertions.assertFalse(this.blogEngineMain.containsPost(4));
    }

    @Test
    public void containsUserTest() {
        IUser user_four = new User("My", "Name", "isJohn@cena.de");
        Assertions.assertTrue(this.blogEngineMain.containsUser(user_one));
        Assertions.assertTrue(this.blogEngineMain.containsUser(user_two));
        Assertions.assertTrue(this.blogEngineMain.containsUser(user_three));
        Assertions.assertFalse(this.blogEngineMain.containsUser(user_four));
    }

    @Test
    public void findUserByEmailTest() throws UserNotFoundException {
        IUser user_four = this.blogEngineMain.findUserByEmail("goldiholdi@gmail.com");

        Assertions.assertEquals(user_one, user_four);
    }

    @Test
    public void findUserByEmailUserNotFoundExceptionTest() {
        Assertions.assertThrows(UserNotFoundException.class, () -> {
           this.blogEngineMain.findUserByEmail("johncena@alleskoenner.de");
        });
    }

    @Test
    public void sortPostsByTitleTest() throws UserNotFoundException {
        IPost post_four = new Post("CesarTitle", "Wild content of history stories", user_two, blogEngineMain);
        this.blogEngineMain.addPost(post_four);

        List<IPost> sortedPosts = new ArrayList<>();
        sortedPosts.add(this.post_three);
        sortedPosts.add(post_four);
        sortedPosts.add(this.post_two);
        sortedPosts.add(this.post_one);

        Assertions.assertEquals(sortedPosts, blogEngineMain.sortPostsByTitle());
    }

    @Test
    public void findPostsByTitleTest() throws UserNotFoundException {
        IPost post_four = new Post("This is title one", "Wild content of history stories", user_two, blogEngineMain);
        this.blogEngineMain.addPost(post_four);

        List<IPost> posts = this.blogEngineMain.findPostsByTitle("This is title one");

        Assertions.assertEquals(this.post_one, posts.get(0));
        Assertions.assertEquals(post_four, posts.get(1));
    }
}
