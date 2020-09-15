package project.reglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import project.reglog.model.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    Users findByEmail(String email);
    @Modifying
    @Query( value="DELETE FROM user WHERE id = ?1", nativeQuery = true)
    void deleteByIdVoid(int id);
    @Modifying
    @Query( value="DELETE FROM user_roles WHERE user_id = ?1", nativeQuery = true)
    void deleteRelationRole(int id);
}
