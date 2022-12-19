package dhbw.einpro.blogengine.impl;

import java.util.Objects;

import dhbw.einpro.blogengine.interfaces.IUser;

/**
 * Klasse enthält Informationen zu einem Benutzer des Blog-Systems
 */
public class User implements Comparable<User>, IUser
{

    private String firstName;
    private String lastName;
    private String email;

    public User(String firstName, String lastName, String email){

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }


    /**
     * Vergleicht die im Parameter o übergebene Person mit der aktuellen Instanz.
     * Dabei werden die Attribute des Benutzers in der Reihenfolge Nachname, Vorname
     * und Email-Adresse verglichen
     *
     * @param o Benutzer, der mit der aktuellen Instanz verglichen werden soll
     * @return Liefert einen Integer-Wert zurück @see Interface Comparable
     */
    @Override
    public int compareTo(User o) {

        if (lastName.compareTo(o.getLastName()) == 0){
            if (firstName.compareTo(o.getFirstName()) == 0){
                if (email.compareTo(o.getEmail()) == 0){

                    return 0;

                }
                else {

                    return email.compareTo(o.getEmail());

                }
            }
            else {

                return firstName.compareTo(o.getFirstName());

            }
        }
        else {

            return lastName.compareTo(o.getLastName());

        }

    }


    /**
     * Liefert die Email eines Benutzers zurück
     *
     * @return Gibt die Email-Adresse des Benutzers
     */
    @Override
    public String getEmail() {

        return email;

    }

    /**
     * @param p_email aktualisiert die E-Mail-Adresse des Benutzers
     */
    @Override
    public void setEmail(String p_email) {

        email = p_email;

    }

    /**
     * Liefert den Vorname des Benutzers zurück
     *
     * @return Vorname des Benutzers
     */
    @Override
    public String getFirstName() {

        return firstName;

    }

    /**
     * Setzt den Vornamen des Benutzers
     *
     * @param p_firstName Vorname des Benutzers
     */
    @Override
    public void setFirstName(String p_firstName) {

        firstName = p_firstName;

    }

    /**
     * Liefert den Nachnamen des Benutzers zurück
     *
     * @return Nachname
     */
    @Override
    public String getLastName() {

        return lastName;

    }

    /**
     * Setzt den Nachnamen des Benutzers
     *
     * @param p_lastName Nachname der gesetzt werden soll
     */
    @Override
    public void setLastName(String p_lastName) {

        lastName = p_lastName;

    }
}
