package app.my.SpartanJCApp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactModel implements Serializable {
    private String id;
    private String firstName, lastName, address;
    private ArrayList<DataModel> phones, emails, socials, notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<DataModel> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<DataModel> phones) {
        this.phones = phones;
    }

    public ArrayList<DataModel> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<DataModel> emails) {
        this.emails = emails;
    }

    public ArrayList<DataModel> getSocials() {
        return socials;
    }

    public void setSocials(ArrayList<DataModel> socials) {
        this.socials = socials;
    }

    public ArrayList<DataModel> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<DataModel> notes) {
        this.notes = notes;
    }
}
