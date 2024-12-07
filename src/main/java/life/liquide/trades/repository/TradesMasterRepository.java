package life.liquide.trades.repository;

import life.liquide.trades.entity.TradesMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradesMasterRepository extends
    JpaRepository<TradesMasterEntity, Long> {

    List<TradesMasterEntity> findAllByUserId(Long userId);

    List<TradesMasterEntity> findAllByType(String type);

    List<TradesMasterEntity> findAllByTypeAndUserId(String type, Long userId);




}