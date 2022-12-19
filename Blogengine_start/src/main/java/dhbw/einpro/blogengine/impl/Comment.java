package dhbw.einpro.blogengine.impl;

import java.time.LocalDateTime;

import dhbw.einpro.blogengine.exceptions.IllegalOperationException;
import dhbw.einpro.blogengine.interfaces.IComment;
import dhbw.einpro.blogengine.interfaces.IPost;
import dhbw.einpro.blogengine.interfaces.IUser;

/**
 * Klasse implementiert einen Kommentar zu einem Post.
 */
public class Comment implements IComment
{

    private String content;
    private IUser author;

    public Comment (String comment, IUser author){

        this.content = comment;
        this.author = author;

    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * Liefert den Inhalt eines Kommentars.
     *
     * @return content
     */
    @Override
    public String getContent() {

        return content;

    }

    /**
     * Setzt den Inhalt des Kommentars Der Inhalt eines Kommentars darf maximal 256
     * Zeichen haben.
     *
     * @param p_content Inhalt des Kommentars
     * @throws IllegalOperationException wird ausgelöst, falls der Inhalt mehr als
     *                                   256 Zeichen hat.
     */
    @Override
    public void setContent(String p_content) throws IllegalOperationException {

        if(p_content.length() > 256){

            throw new IllegalOperationException();

        }

        content = p_content;

    }

    /**
     * Liefert den Autor des Kommentars zurück.
     *
     * @return User
     */
    @Override
    public IUser getAuthor() {

        return author;

    }

    /**
     * Aktualisiert den Autor des Kommentars
     *
     * @param p_author Autor des Kommentars
     */
    @Override
    public void setAuthor(IUser p_author) {

        author = p_author;

    }

    /**
     * Verknüpft den aktuellen Kommentar mit dem dazugehörigen Post
     *
     * @param p_post Post zu dem der Kommentar gehört
     */
    @Override
    public void setPost(IPost p_post) {



    }
}
