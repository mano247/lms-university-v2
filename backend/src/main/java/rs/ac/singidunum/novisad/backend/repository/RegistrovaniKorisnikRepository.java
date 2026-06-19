package rs.ac.singidunum.novisad.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.user.RegisteredUser;

@Repository
public interface RegistrovaniKorisnikRepository extends JpaRepository<RegisteredUser, Long>{

	Optional<RegisteredUser> findByEmail(String email);

	Boolean existsRegistrovaniKorisnikByEmail(String email);
}
