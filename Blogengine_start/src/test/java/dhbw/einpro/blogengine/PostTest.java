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

import java.util.ArrayList;
import java.util.List;

public class PostTest {
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
    public void initializePost() throws DuplicateEmailException, DuplicateUserException, UserNotFoundException {
        blogEngineMain = new BlogEngine();
        this.user_one = new User("Goldi", "holdi", "goldiholdi@gmail.com");
        this.user_two = new User("Max", "Musti", "maximusti@arcor.de");
        this.user_three = new User("Ju", "Goldi", "jGoldi@gmx.de");
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
    }

    @Test
    public void getIdTest() {
        Assertions.assertEquals(1, this.post_one.getId());
        Assertions.assertEquals(2, this.post_two.getId());
        Assertions.assertEquals(3, this.post_three.getId());
    }

    @Test
    public void setIdTest() {
        this.post_one.setId(4);
        Assertions.assertEquals(4, this.post_one.getId());
    }

    @Test
    public void getCommentsTest() throws UserNotFoundException, IllegalOperationException {
        this.post_one.addComment(this.comment_two);
        this.post_one.addComment(this.comment_three);

        Assertions.assertEquals(this.comment_two, this.post_one.getComments().get(0));
        Assertions.assertEquals(this.comment_three, this.post_one.getComments().get(1));
        Assertions.assertEquals(2, this.post_one.getComments().size());
    }

    @Test
    public void addCommentTest() throws UserNotFoundException, IllegalOperationException {
        this.post_three.addComment(this.comment_one);
        List<IComment> comments = new ArrayList<>();
        comments.add(this.comment_one);

        Assertions.assertEquals(comments, this.post_three.getComments());
    }

    @Test
    public void addCommentIllegalOperationExceptionTest() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.post_three.addComment(this.comment_three);
        });
    }

    @Test
    public void addCommentUserNotFoundException() {
        IUser user_four = new User("JAndi", "Bandi", "jandibandi@gmail.com");
        IComment comment_four = new Comment("Comment user not found", user_four);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.post_three.addComment(comment_four);
        });
    }

    @Test
    public void removeCommentTest() throws UserNotFoundException, IllegalOperationException {
        this.post_one.addComment( this.comment_two);
        this.post_one.addComment( this.comment_three);
        Assertions.assertEquals( this.comment_two,  this.post_one.getComments().get(0));
        Assertions.assertEquals( this.comment_three,  this.post_one.getComments().get(1));

        this.post_one.removeComment( this.user_two,  this.comment_two);

        Assertions.assertEquals( this.comment_three,  this.post_one.getComments().get(0));

        this.post_one.removeComment( this.user_three,  this.comment_three);

        List<IComment> comments = new ArrayList<>();
        Assertions.assertEquals(comments,  this.post_one.getComments());
    }

    @Test
    public void removeCommentIllegalOperationExceptionTest() throws IllegalOperationException, UserNotFoundException {
        this.post_one.addComment( this.comment_two);
        this.post_one.addComment( this.comment_three);
        Assertions.assertEquals( this.comment_two,  this.post_one.getComments().get(0));
        Assertions.assertEquals( this.comment_three,  this.post_one.getComments().get(1));

        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.post_one.removeComment( this.user_three,  this.comment_two);
        });
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.post_one.removeComment( this.user_one,  this.comment_two);
        });
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.post_one.removeComment( this.user_two,  this.comment_three);
        });
    }

    @Test
    public void likeTest() throws UserNotFoundException, IllegalOperationException {
        this.post_one.like( this.user_three);

        Assertions.assertEquals( this.user_three,  this.post_one.getLikes().get(0));

        this.post_one.like( this.user_two);
        this.post_one.like( this.user_three);
        this.post_one.like( this.user_two);

        Assertions.assertEquals( this.user_two,  this.post_one.getLikes().get(1));
        Assertions.assertEquals(2,  this.post_one.getLikes().size());
    }

    @Test
    public void likeIllegalOperationException() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.post_one.like( this.user_one);
        });
    }

    @Test
    public void likeUserNotFoundException() {
        IUser user_four = new User("JAndi", "Bandi", "jandibandi@gmail.com");

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.post_one.like(user_four);
        });
    }

   @Test
    public void dislikeTest() throws UserNotFoundException, IllegalOperationException {
       this.post_one.disLike( this.user_three);

       Assertions.assertEquals( this.user_three,  this.post_one.getDisLikes().get(0));

       this.post_one.disLike( this.user_two);
       this.post_one.disLike( this.user_three);
       this.post_one.disLike( this.user_two);

       Assertions.assertEquals( this.user_two,  this.post_one.getDisLikes().get(1));
       Assertions.assertEquals(2,  this.post_one.getDisLikes().size());
   }

   @Test
    public void dislikeIllegalOperationExceptionTest() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            this.post_one.disLike( this.user_one);
        });
   }

   @Test
    public void dislikeUserNotFoundException() {
       IUser user_four = new User("JAndi", "Bandi", "jandibandi@gmail.com");

       Assertions.assertThrows(UserNotFoundException.class, () -> {
           this.post_one.disLike(user_four);
       });
   }

   @Test
    public void likedislikeTest() throws UserNotFoundException, IllegalOperationException {
       this.post_one.like( this.user_two);
       this.post_one.like( this.user_two);
       this.post_one.like( this.user_three);

       Assertions.assertEquals(2,  this.post_one.getLikes().size());
       Assertions.assertEquals( this.user_two,  this.post_one.getLikes().get(0));
       Assertions.assertEquals( this.user_three,  this.post_one.getLikes().get(1));

       this.post_one.disLike( this.user_two);

       Assertions.assertEquals(1,  this.post_one.getLikes().size());
       Assertions.assertEquals( this.user_three,  this.post_one.getLikes().get(0));
       Assertions.assertEquals(1,  this.post_one.getDisLikes().size());
       Assertions.assertEquals( this.user_two,  this.post_one.getDisLikes().get(0));

       this.post_one.disLike( this.user_three);

       Assertions.assertEquals(0,  this.post_one.getLikes().size());
       Assertions.assertEquals(2,  this.post_one.getDisLikes().size());
       Assertions.assertEquals( this.user_two,  this.post_one.getDisLikes().get(0));
       Assertions.assertEquals( this.user_three,  this.post_one.getDisLikes().get(1));
   }

   @Test
    public void getScoreTest() throws UserNotFoundException, IllegalOperationException, DuplicateEmailException, DuplicateUserException {
       IUser user_four = new User("first", "last", "mail@gmail.com");
       IUser user_five = new User("hi", "du", "hidu@arcor.de");
       IUser user_six  = new User("thatis", "aname", "thatisaname@gmx.de");

       this.blogEngineMain.addUser(user_four);
       this.blogEngineMain.addUser(user_five);
       this.blogEngineMain.addUser(user_six);

       this.post_one.like( this.user_two);
       this.post_one.like( this.user_three);
       this.post_one.like(user_four);
       this.post_one.like(user_five);
       this.post_one.disLike(user_six);

       Assertions.assertEquals(30, this.post_one.getScore());

       this.post_one.disLike(user_five);

       Assertions.assertEquals(10,  this.post_one.getScore());

       this.post_one.disLike(user_four);

       Assertions.assertEquals(-10,  this.post_one.getScore());

       this.post_one.disLike( this.user_three);

       Assertions.assertEquals(-30, post_one.getScore());

       this.post_one.disLike(this.user_two);

       Assertions.assertEquals(-50, this.post_one.getScore());
   }

   @Test
    public void getTitleTest() {
        Assertions.assertEquals("This is title one", this.post_one.getTitle());
   }

   @Test
    public void setTitleTest() {
        this.post_one.setTitle("This is edited title one");
        Assertions.assertEquals("This is edited title one", this.post_one.getTitle());
   }

   @Test
    public void getContentTest() {
        Assertions.assertEquals("This is content of title one", this.post_one.getContent());
   }

   @Test
    public void setContentTest() {
        this.post_one.setContent("This is edited content");
        Assertions.assertEquals("This is edited content", this.post_one.getContent());
   }

   @Test
    public void getAuthorTest() {
        Assertions.assertEquals(this.user_one, this.post_one.getAuthor());
   }

   @Test
    public void setAuthorTest() {
        this.post_one.setAuthor(this.user_two);
        Assertions.assertEquals(this.user_two, this.post_one.getAuthor());
   }

   @Test
    public void getLikesTest() throws DuplicateEmailException, DuplicateUserException, UserNotFoundException, IllegalOperationException {
       IUser user_four = new User("first", "last", "mail@gmail.com");
       IUser user_five = new User("hi", "du", "hidu@arcor.de");
       IUser user_six  = new User("thatis", "aname", "thatisaname@gmx.de");

       this.blogEngineMain.addUser(user_four);
       this.blogEngineMain.addUser(user_five);
       this.blogEngineMain.addUser(user_six);

       this.post_one.like( this.user_two);
       this.post_one.like( this.user_three);
       this.post_one.like(user_four);
       this.post_one.like(user_five);
       this.post_one.like(user_six);

       List<IUser> likeUser = new ArrayList<>();
       likeUser.add(this.user_two);
       likeUser.add(this.user_three);
       likeUser.add(user_four);
       likeUser.add(user_five);
       likeUser.add(user_six);

       Assertions.assertEquals(5, this.post_one.getLikes().size());
       Assertions.assertEquals(likeUser, this.post_one.getLikes());
   }

    @Test
    public void getDisLikesTest() throws DuplicateEmailException, DuplicateUserException, UserNotFoundException, IllegalOperationException {
        IUser user_four = new User("first", "last", "mail@gmail.com");
        IUser user_five = new User("hi", "du", "hidu@arcor.de");
        IUser user_six  = new User("thatis", "aname", "thatisaname@gmx.de");

        this.blogEngineMain.addUser(user_four);
        this.blogEngineMain.addUser(user_five);
        this.blogEngineMain.addUser(user_six);

        this.post_one.disLike( this.user_two);
        this.post_one.disLike( this.user_three);
        this.post_one.disLike(user_four);
        this.post_one.disLike(user_five);
        this.post_one.disLike(user_six);

        List<IUser> dislikeUser = new ArrayList<>();
        dislikeUser.add(this.user_two);
        dislikeUser.add(this.user_three);
        dislikeUser.add(user_four);
        dislikeUser.add(user_five);
        dislikeUser.add(user_six);

        Assertions.assertEquals(5, this.post_one.getDisLikes().size());
        Assertions.assertEquals(dislikeUser, this.post_one.getDisLikes());
    }
}
