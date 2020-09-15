package project.reglog.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Setter
@Getter
@EqualsAndHashCode(exclude = "roles")
@Entity(name="User") @Table(name="user")
public class Users implements Serializable {
	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="username",unique=true)
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="address")
	private String address;
	@Column(name="email",unique=true)
	private String email;
	@Column(name="join_date")
	private LocalDateTime join_date;
	
	//many-to-many relation to Roles table
	@JsonManagedReference
	@ManyToMany()
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Roles> roles;
	
	public Users(){
	}
	public Users(String username, String Email, String password, String address, 
				LocalDateTime date, Roles...roles) {
		this.setUsername(username);
		this.setEmail(Email);
		this.setPassword(password);
		this.setAddress(address);
		this.setJoin_date(date);
		this.setRoles(roles);
	}

	public Set<Roles> getRoles() {
        return roles;
    }
    public void setRoles(Roles...roles) {
		this.roles = Stream.of(roles).collect(Collectors.toSet());
	}
}	
