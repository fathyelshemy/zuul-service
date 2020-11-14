package com.fathy.gatewayservice.repositories;

import com.fathy.gatewayservice.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(final String email);
    User findByEmail(final String email);
}
