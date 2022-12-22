package dhbw.einpro.blogengine.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import dhbw.einpro.blogengine.exceptions.DuplicateEmailException;
import dhbw.einpro.blogengine.exceptions.DuplicateUserException;
import dhbw.einpro.blogengine.exceptions.IllegalOperationException;
import dhbw.einpro.blogengine.exceptions.PostNotFoundException;
import dhbw.einpro.blogengine.exceptions.UserNotFoundException;
import dhbw.einpro.blogengine.interfaces.IBlogEngine;
import dhbw.einpro.blogengine.interfaces.IComment;
import dhbw.einpro.blogengine.interfaces.IPost;
import dhbw.einpro.blogengine.interfaces.IUser;

/**
 * Klasse implementiert die Funktionalität einer Blog Engine.
 */
public class BlogEngine implements IBlogEngine
{

    private List<IUser> blogEngineUsers = new ArrayList<IUser>();
    private List<IPost> blogEnginePosts = new ArrayList<IPost>();
    private int postId = 0;

    /**
     * Liefert die Anzahl der Benutzer im Blog-System.
     *
     * @return Anzahl der Benutzer im Blog-System.
     */
    @Override
    public int size() {

        return blogEngineUsers.size();

    }

    /**
     * Fügt einen neune Benutzer zum Blog-Sysstem
     *
     * @param p_user Benutzer der zum Blog-System hinzugefügt werden soll.
     * @return Liefert true falls der Benutzer hinzugefügt werden konnte. Sonst
     * false.
     * @throws DuplicateEmailException wird ausgelöst, falls bereits ein User mit
     *                                 der gleichen Email-Adresse im System
     *                                 vorhanden ist.
     * @throws DuplicateUserException  wird ausgelöst, falls der gleiche User erneut
     *                                 in das Blog-System hinzugefügt werden soll.
     */
    @Override
    public boolean addUser(IUser p_user) throws DuplicateEmailException, DuplicateUserException {

            for (IUser blogEngineUser : blogEngineUsers){

                if (p_user.getEmail().equals(blogEngineUser.getEmail())){

                    throw new DuplicateEmailException();

                }
                if(p_user.equals(blogEngineUser)){

                    throw new DuplicateUserException();

                }

            }

        return blogEngineUsers.add(p_user);

    }

    /**
     * Entfernt einen Benutzer aus dem Blog-System
     *
     * @param p_user Benutzer, der aus dem Blog-System entfernt werden soll
     * @return liefert true, fall der Benutzer aus dem Blog-System entfernt werden
     * konnte. Sonst false.
     */
    @Override
    public boolean removeUser(IUser p_user) {

        return blogEngineUsers.remove(p_user);

    }

    /**
     * Fügt ein Post zum Blog-System hinzu.
     *
     * @param p_post Post, der zum Blog-System hinzugefügt werden soll. Posts werden
     *               aufsteigend ab der Zahl 1 nummeriert. Beispiel: Wenn Sie einen
     *               ersten Post zum Blog-System hinzufügen, dann bekommt der Post die
     *               Id 1. Der zweite Post bekommt die Id 2 und alle weiteren Posts
     *               bekommen aufsteigende Nummern.
     * @return Id des neu hinzugefügten Posts.
     * @throws UserNotFoundException wird ausgelöst, falls der im Parameter
     *                               author angegebene Benutzer noch nicht als Benutzer registriert ist.     *
     */
    @Override
    public int addPost(IPost p_post) throws UserNotFoundException {

        if(!containsUser(p_post.getAuthor())){

            throw new UserNotFoundException();

        }

        postId += 1;
        p_post.setId(postId);
        blogEnginePosts.add(p_post);


        return postId;

    }

    /**
     * Entfernt Post aus dem Blog-System
     *
     * @param p_author Autor des Posts, der entfernt werden soll
     * @param p_postId Id des gesuchten Posts
     * @throws PostNotFoundException     wird ausgelöst, falls der Post nicht
     *                                   gefunden wurde
     * @throws IllegalOperationException wird ausgelöst, falls der im Parameter
     *                                   author angegebene Benutzer nicht der
     *                                   tatsächliche Autor des zu löschenden Posts
     *                                   ist
     */
    @Override
    public void removePost(IUser p_author, int p_postId) throws PostNotFoundException, IllegalOperationException {

        IPost myPost = findPostById(p_postId);

        if (myPost == null){

            throw new PostNotFoundException();

        }

        User myUserA = (User) p_author;
        User myUserB = (User) myPost.getAuthor();

        if(myUserA.compareTo(myUserB) != 0){

            throw new IllegalOperationException();

        }

        blogEnginePosts.remove(myPost);

    }

    /**
     * Liefert eine Liste aller Posts, die im Blog-System erstellt wurden
     *
     * @return Liste aller Posts im Blog-System
     */
    @Override
    public List<IPost> getPosts() {

        return blogEnginePosts;

    }

    /**
     * Liefert eine Liste aller Posts des angegebenen Autors zurück
     *
     * @param p_author Benutzer dessen Posts gesucht werden sollen
     * @return Liste aller Posts des angegebenen Benutzers
     */
    @Override
    public List<IPost> findPostsByAuthor(IUser p_author) {

        List<IPost> foundPosts = null;

        for (IPost blogEnginePost: blogEnginePosts) {

            User myUserA = (User) p_author;
            User myUserB = (User) blogEnginePost.getAuthor();

            if(myUserA.compareTo(myUserB) == 0){

                foundPosts.add(blogEnginePost);

            }

        }

        return foundPosts;

    }

    /**
     * Liefert einen Post zur angegebenen Id zurück
     *
     * @param p_postId Id des gesuchten Posts
     * @return Post mit der spezifizierten Id
     */
    @Override
    public IPost findPostById(int p_postId) {

        for(int i = 0; i < blogEnginePosts.size(); i++){

            IPost myPost = blogEnginePosts.get(i);

            if(myPost.getId() == p_postId){

                return myPost;

            }

        }

        return null;

    }

    /**
     * Prüft, ob ein Post mit der Id im Blog-System existiert
     *
     * @param p_postId Id des gesuchten Post
     * @return true falls der Post gefunden wurde
     */
    @Override
    public boolean containsPost(int p_postId) {

        for (IPost blogEnginePost: blogEnginePosts) {

            if (blogEnginePost.getId() == postId){

                return true;

            }

        }

        return false;

    }

    /**
     * Prüft, ob ein bestimmter Benutzer im Blog-System existiert.
     *
     * @param user der gesuchte Benutzer
     * @return true falls der Benutzer gefunden wurde
     */
    @Override
    public boolean containsUser(IUser user) {

        for (IUser blogEngineUser: blogEngineUsers) {

            User myUserA = (User) blogEngineUser;
            User myUserB = (User) user;

            if (myUserA.compareTo(myUserB) == 0){

                return true;

            }

        }

        return false;

    }

    /**
     * Liefert den entsprechenden Benutzer des Blog-Systems mit der angegebenen
     * Email-Adresse
     *
     * @param p_email Email-Adresse des Benutzers, der gesucht werden soll
     * @return Instanz der Klasse User
     * @throws UserNotFoundException wird ausgelöst, falls der Benutzer nicht
     *                               gefunden wurde
     */
    @Override
    public IUser findUserByEmail(String p_email) throws UserNotFoundException {

        for (IUser blogEngineUser: blogEngineUsers) {

            if (blogEngineUser.getEmail().equals(p_email)){

                return blogEngineUser;

            }

        }

        throw new UserNotFoundException();

    }

    /**
     * Sortiert die Posts anhand des Titels
     *
     * @return Liste aller Posts im Blog-System sortiert nach dem Titel
     */
    @Override
    public List<IPost> sortPostsByTitle() {



        return null;
    }

    /**
     * Liefert all Posts zu einem gegebenen Titel.
     *
     * @param title Titel des zu suchenden Posts
     * @return Liste aller Posts
     */
    @Override
    public List<IPost> findPostsByTitle(String title) {

        List<IPost> foundPosts = null;

        for (IPost blogEnginePost: blogEnginePosts) {

            if (blogEnginePost.getTitle().equals(title)){

                foundPosts.add(blogEnginePost);

            }

        }

        return foundPosts;

    }
}
