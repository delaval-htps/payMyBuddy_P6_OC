package com.paymybuddy.repository;

import com.paymybuddy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository extends {@link JpaRepository} to acces to {@link Role}
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String roleName);

}
