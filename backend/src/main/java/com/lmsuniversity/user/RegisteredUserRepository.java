package com.lmsuniversity.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long>{

	Optional<RegisteredUser> findByEmail(String email);

	Boolean existsRegisteredUserByEmail(String email);
}
