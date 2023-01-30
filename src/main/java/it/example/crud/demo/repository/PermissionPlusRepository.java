package it.example.crud.demo.repository;

import it.example.crud.demo.models.db.CompositeKey;
import it.example.crud.demo.models.db.PermissionPlusTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionPlusRepository extends JpaRepository<PermissionPlusTable, CompositeKey> {

    @Query(nativeQuery = true, value="SELECT * FROM operations WHERE role= ?1")
    List<PermissionPlusTable> findByRole(String role);

}
