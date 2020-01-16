package at.htl.dogschool.entity;

import javax.persistence.*;
@Entity
public class Dog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Person owner;

    //region Constructors
    public Dog() {
    }

    public Dog(String name, Person owner) {
        this.setName(name);
        this.setOwner(owner);
    }
    //endregion

    //region Getter and Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
    //endregion


    @Override
    public String toString() {
        return String.format("%s, (%s, %s)", owner.getLastName(), owner.getFirstName());
    }
}
