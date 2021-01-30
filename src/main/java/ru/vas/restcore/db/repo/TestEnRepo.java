package ru.vas.restcore.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vas.restcore.db.domain.TestEn;

@Repository
public interface TestEnRepo extends JpaRepository<TestEn, Long> {
}
