package com.example.javaspringlearn;

import java.sql.Timestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String firstname;
    private String lastname;
    private String password;
    @UpdateTimestamp
    private Timestamp dateCreate;

    public Users() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getLogin() { 
        return login; 
    }

    public void setLogin(String login) { 
        this.login = login;
    }

    public String getFirstname() { 
        return firstname; 
    }

    public void setFirstname(String firstname) { 
        this.firstname = firstname; 
    }

    public String getLastname() { 
        return lastname; 
    }

    public void setLastname(String lastname) { 
        this.lastname = lastname; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password;
    }

    public Timestamp getDateCreate() { 
        return dateCreate; 
    }

    public void setDateCreate(Timestamp dateCreate) { 
        this.dateCreate = dateCreate;
    }
}
