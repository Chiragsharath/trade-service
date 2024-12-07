package life.liquide.trades.repository;

import life.liquide.trades.entity.TradesMasterEntity;
import life.liquide.trades.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends
    JpaRepository<User, Long> {

        Optional<User> findByEmail(String email);

}