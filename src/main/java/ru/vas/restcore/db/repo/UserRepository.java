package ru.vas.restcore.db.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vas.restcore.db.domain.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {"roles", "subscriptions"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
