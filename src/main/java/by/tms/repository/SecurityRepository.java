package by.tms.repository;

import by.tms.model.Security;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Integer> {
    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM security WHERE role = :roleParam")
    List<Security> customFindByRole(String roleParam);

    Optional<Security> getByUsername(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE security SET role = 'ADMIN' WHERE user_id = ::userId")
    int setAdminRoleByUserId(Integer userId);
}
