package it.example.crud.demo.repository;

import it.example.crud.demo.models.db.PermissionTable;
import it.example.crud.demo.models.db.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionTable, Long> {

}
