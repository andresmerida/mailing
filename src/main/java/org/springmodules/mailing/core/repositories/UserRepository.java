package org.springmodules.mailing.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springmodules.mailing.core.entity.User;

/**
 * Created by andresmerida on 3/3/2016.
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
