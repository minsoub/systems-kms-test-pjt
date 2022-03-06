package com.bithumbsystems.repository;

import com.bithumbsystems.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
