package dhbw.einpro.blogengine.impl;

/**
 * Klasse repräsentiert ein Post.
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import dhbw.einpro.blogengine.exceptions.IllegalOperationException;
import dhbw.einpro.blogengine.exceptions.UserNotFoundException;
import dhbw.einpro.blogengine.interfaces.IBlogEngine;
import dhbw.einpro.blogengine.interfaces.IComment;
import dhbw.einpro.blogengine.interfaces.IPost;
import dhbw.einpro.blogengine.interfaces.IUser;

/**
 * Die Klasse implementiert einen Post im Blog-System.
 */
public class Post implements IPost
{

    private IUser user;
    private IBlogEngine blogEngine;
    private int postId;
    private List<IComment> comments;
    private List<IUser> likedUsers;
    private List<IUser> dislikedUsers;
    private String postTitle;
    private String postContent;
    private IUser postAuthor;

    public Post (String title, String content, IUser user, IBlogEngine blogEngine){

        this.postTitle = title;
        this.postContent = content;
        this.user = user;
        this.blogEngine = blogEngine;


    }


    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * Liefert die Id des Post.
     *
     * @return Id des Posts
     */
    @Override
    public int getId() {

        return postId;

    }

    /**
     * Setzt die Id des Posts
     *
     * @param p_id neue Id für den Post
     */
    @Override
    public void setId(int p_id) {

        postId = p_id;

    }

    /**
     * Liefert die Liste aller Kommentare zum Post zurück
     *
     * @return Liste von Kommentaren zum Post
     */
    @Override
    public List<IComment> getComments() {

        return comments;

    }

    /**
     * Fügt den angegebenen Kommentar zum Post hinzu
     *
     * @param p_comment Kommentar, der zum Post hinzugefügt werden soll
     * @throws IllegalOperationException wird ausgelöst, falls der Autor des
     *                                   Kommentars gleichzeitig Autor des Posts
     *                                   ist.
     *                                   Diese Exception wird ebenfalls ausgelöst,
     *                                   falls der Inhalt (content) des Kommentars mehr als 256 Zeichen umfasst.
     * @throws UserNotFoundException     wird ausgelöst, falls der als Author angegebene Benutzer
     *                                   nicht in der Blog Engine registriert ist
     */
    @Override
    public void addComment(IComment p_comment) throws IllegalOperationException, UserNotFoundException {

        User myUserA = (User) postAuthor;
        User myUserB = (User) p_comment.getAuthor();

        if (myUserA.compareTo(myUserB) == 0 || p_comment.getContent().length() <= 256){

            throw new IllegalOperationException();

        }

        if (blogEngine.containsUser(p_comment.getAuthor()) == false){

            throw new UserNotFoundException();

        }

        comments.add(p_comment);

    }

    /**
     * Entfernt einen Comment für einen User.
     *
     * @param p_user    Benutzer, der die Methode aufruft
     * @param p_comment Kommentar, der entfernt werden soll
     * @throws IllegalOperationException wird ausgelöst, falls der Benutzer, der die Methode aufruft nicht gleichzeitig Autor des Kommentars ist.
     */
    @Override
    public void removeComment(IUser p_user, IComment p_comment) throws IllegalOperationException {

        User myUserA = (User) postAuthor;
        User myUserB = (User) p_user;

        if(myUserA.compareTo(myUserB) != 0){

            throw new IllegalOperationException();

        }

        comments.remove(p_comment);

    }

    /**
     * Fügt den Benutzer zur Liste der Benutzer hinzu, die den Post gut finden.
     *
     * @param p_person Benutzer, der den Post gut findet. Der Benutzer wird in die
     *                 Liste der Benutzer aufgenommen, die den Post gut finden (Siehe
     *                 auch {@link #getLikes()}). Falls der gleiche Benutzer diese
     *                 Methode mehrmals aufruft, so wird er nur das erste Mal in die
     *                 Liste aufgenommen.
     *                 Falls der Benutzer bereits in der Liste der Benutzer, die den
     *                 Post nicht gut finden ist, wird dieser daraus entfernt, bevor
     *                 er in die Liste der Benutzer, die den Post gut finden auf-
     *                 genommen wird.
     * @throws IllegalOperationException wird ausgelöst, falls der User zugleich
     *                                   Autor des Posts ist.
     * @throws UserNotFoundException     wird ausgelöst, falls der angegebene Benutzer
     *                                   nicht im Blog-System registriert ist
     */
    @Override
    public void like(IUser p_person) throws IllegalOperationException, UserNotFoundException {

        User myUserA = (User) postAuthor;
        User myUserB = (User) p_person;

        if(myUserA.compareTo(myUserB) == 0){

            throw new IllegalOperationException();

        }

        if(!blogEngine.containsUser(p_person)){

            throw new UserNotFoundException();

        }

        if(!likedUsers.contains(p_person)){
            if (!dislikedUsers.contains(p_person))         {

                likedUsers.add(p_person);

            }
            else {

                dislikedUsers.remove(p_person);
                likedUsers.add(p_person);

            }
        }

    }

    /**
     * Fügt den Benutzer zur Liste der Benutzer hinzu, die den Post nicht gut
     * finden.
     *
     * @param p_person User, der den Post nicht gut findet
     *                 Falls der Benutzer bereits in der Liste der Benutzer, die den
     *                 Post gut finden ist, wird dieser daraus entfernt, bevor
     *                 er in die Liste der Benutzer, die den Post nicht gut finden auf-
     *                 genommen wird.
     * @throws IllegalOperationException wird ausgelöst, falls der User zugleich
     *                                   Autor des Posts ist.
     * @throws UserNotFoundException     wird ausgelöst, falls der angegebene Benutzer
     *                                   nicht in der Blog Engine registriert ist
     */
    @Override
    public void disLike(IUser p_person) throws IllegalOperationException, UserNotFoundException {

        if (p_person == postAuthor){

            throw new IllegalOperationException();

        }

        if (!blogEngine.containsUser(p_person)){

            throw new UserNotFoundException();

        }

        if(!dislikedUsers.contains(p_person)){
            if (!likedUsers.contains(p_person))         {

                dislikedUsers.add(p_person);

            }
            else {

                likedUsers.remove(p_person);
                dislikedUsers.add(p_person);

            }
        }

    }

    /**
     * Berechnet die Resonanz eines Posts. Zur Berechnung des Scores werden die
     * Anzahl der likes und dislikes verwendet. Beispiel: Ein Post wird von 3
     * Benutzern bewertet. Die ersten 2 Benutzer finden den Post gut (like). Der
     * dritte Benutzer findet den Post nicht gut (dislike). Der Score des Posts wird
     * wie folgt berechnet: (2-1)*10 = 10
     *
     * @return Score des Posts
     */
    @Override
    public int getScore() {

        int score = (likedUsers.size() - dislikedUsers.size())*10;

        return score;

    }

    /**
     * Liefert den Titel des Posts zurück
     *
     * @return Titel des Posts
     */
    @Override
    public String getTitle() {

        return postTitle;

    }

    /**
     * Setzt oder aktualisiert den Titel des Posts
     *
     * @param p_title Der neue Titel des Posts
     */
    @Override
    public void setTitle(String p_title) {

        postTitle = p_title;

    }

    /**
     * Liefert den Inhalt des Posts zurück
     *
     * @return Inhalt des Posts
     */
    @Override
    public String getContent() {

        return postContent;

    }

    /**
     * Setzt bzw. aktualisiert den Inhalt des Posts
     *
     * @param p_content Der neue Inhalt des Posts
     */
    @Override
    public void setContent(String p_content) {

        postContent = p_content;

    }

    /**
     * Liefert den Autor des Posts zurück
     *
     * @return Autor des Posts
     */
    @Override
    public IUser getAuthor() {

        return postAuthor;

    }

    /**
     * Setzt oder aktualisiert den Autor des Posts
     *
     * @param p_author Autor, der gesetzt werden soll
     */
    @Override
    public void setAuthor(IUser p_author) {

        postAuthor = p_author;

    }

    /**
     * Liefert eine Liste von Personen zurück, die den Post gut finden
     *
     * @return Liste von Personen
     */
    @Override
    public List<IUser> getLikes() {

        return likedUsers;

    }

    /**
     * Liefert eine Liste von Personen zurück, die den Post nicht gut finden
     *
     * @return Liste von Personen
     */
    @Override
    public List<IUser> getDisLikes() {

        return dislikedUsers;

    }
}
