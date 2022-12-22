package dhbw.einpro.blogengine;

import dhbw.einpro.blogengine.exceptions.DuplicateEmailException;
import dhbw.einpro.blogengine.exceptions.DuplicateUserException;
import dhbw.einpro.blogengine.exceptions.IllegalOperationException;
import dhbw.einpro.blogengine.exceptions.UserNotFoundException;
import dhbw.einpro.blogengine.interfaces.IBlogEngine;
import dhbw.einpro.blogengine.interfaces.IComment;
import dhbw.einpro.blogengine.interfaces.IPost;
import dhbw.einpro.blogengine.interfaces.IUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentTest {

    private IBlogEngine blogEngineMain;
    private IUser user_one;
    private IUser user_two;
    private IUser user_three;
    private IPost post_one;
    private IPost post_two;
    private IPost post_three;
    private IComment comment_one;
    private IComment comment_two;
    private IComment comment_three;

    @BeforeEach
    public void initializeComment() throws DuplicateEmailException, DuplicateUserException, UserNotFoundException, IllegalOperationException {
        blogEngineMain = new BlogEngine();
        this.user_one = new User("Goldi", "holdi", "goldiholdi@gmail.com");
        this.user_two = new User("Max", "Musti", "maximusti@arcor.de");
        this.user_three = new User("Ju", "Goss", "jGoss@gmx.de");
        this.post_one = new Post("This is title one", "This is content of title one", this.user_one, this.blogEngineMain);
        this.post_two = new Post("Good title", "This is good content", this.user_two, this.blogEngineMain);
        this.post_three = new Post("Bad title", "This is bad content", this.user_three, this.blogEngineMain);
        this.comment_one = new Comment("This is comment one", this.user_one);
        this.comment_two = new Comment("This is comment two from user two", this.user_two);
        this.comment_three = new Comment("This is comment threeeee", this.user_three);

        this.blogEngineMain.addUser(this.user_one);
        this.blogEngineMain.addPost(this.post_one);

        this.blogEngineMain.addUser(this.user_two);
        this.blogEngineMain.addPost(this.post_two);

        this.blogEngineMain.addUser(this.user_three);
        this.blogEngineMain.addPost(this.post_three);

        this.post_one.addComment(this.comment_two);
        this.post_two.addComment(this.comment_three);
        this.post_three.addComment(this.comment_one);
    }

    @Test
    public void getContentTest() {
        Assertions.assertEquals("This is comment one", this.comment_one.getContent());
    }

    @Test
    public void setContentTest() throws IllegalOperationException {
        this.comment_one.setContent("Hi");
        Assertions.assertEquals("Hi", this.comment_one.getContent());
    }

    @Test
    public void setContentIllegalOperationException() throws IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.comment_one.setContent("0123456789012345678901234567890123456789012345678901234" +
                "567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "01234567890123456789012345678901234567891234567");
        });
        this.comment_one.setContent("0123456789012345678901234567890123456789012345678901234" +
            "567890123456789012345678901234567890123456789012345678901234567890123456789" +
            "01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
            "0123456789012345678901234567890123456789123456");
    }

    @Test
    public void getAuthorTest() {
        Assertions.assertEquals(this.user_one, this.comment_one.getAuthor());
    }

    @Test
    public void setAuthorTest() {
        this.comment_one.setAuthor(this.user_two);

        Assertions.assertEquals(this.user_two, this.comment_one.getAuthor());
    }

    /*
    @Test
    public void setPostTest() {
        Assertions.assertEquals(post_three, this.comment_one.getPost());
    }
     */
}
