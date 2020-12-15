package ru.vas.restcore.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vas.restcore.db.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
