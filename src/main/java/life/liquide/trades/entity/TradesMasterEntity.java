package life.liquide.trades.entity;

import jakarta.persistence.*;
import life.liquide.trades.model.TradesRequestDto;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trades_master")
public class TradesMasterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "type" , nullable = false)
  private String type;

  @NotNull
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "symbol", nullable = false)
  private String symbol;

  @Column(name = "shares", nullable = false)
  private Integer shares;

  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "created_at", updatable = false)
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @PrePersist
  public void prePersist() {
    long currentTime = System.currentTimeMillis();
    this.createdAt = currentTime;
    this.updatedAt = currentTime;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = System.currentTimeMillis();
  }

  public static TradesMasterEntity convertDtoToEntity(TradesRequestDto tradesRequestDto)
  {
    return TradesMasterEntity.builder().
            type(tradesRequestDto.getType()).
            userId(tradesRequestDto.getUserId()).
            symbol(tradesRequestDto.getSymbol()).
            shares(tradesRequestDto.getShares()).
            price(tradesRequestDto.getPrice()).
            createdAt(tradesRequestDto.getCreatedAt()).
            updatedAt(tradesRequestDto.getUpdatedAt()).build();
  }


}