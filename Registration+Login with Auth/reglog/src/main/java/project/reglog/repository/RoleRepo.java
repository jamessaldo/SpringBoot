package project.reglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.reglog.model.Roles;

public interface RoleRepo extends JpaRepository<Roles, Integer>{
    Roles findByName(String name);
    @Query( value="SELECT * FROM role WHERE role = ?1", nativeQuery = true)
    Roles getByName(String name);
}

