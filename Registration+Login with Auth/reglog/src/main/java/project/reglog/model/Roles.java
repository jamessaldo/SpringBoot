package project.reglog.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
@Table(name = "role")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="role")
    private String name;
    
    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "roles")
    private Set<Users> users = new HashSet<>();
    
    public Roles(String name){
        this.name = name;
    }
    public Roles(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
