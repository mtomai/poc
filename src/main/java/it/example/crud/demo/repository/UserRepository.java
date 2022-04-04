package it.example.crud.demo.repository;

import it.example.crud.demo.models.db.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Long> {

	@Async
	CompletableFuture<UserTable> findOneByName(String name);

	@Query("SELECT u FROM users u WHERE u.age = 43")
	Collection<UserTable> findAllActiveUsers();
}