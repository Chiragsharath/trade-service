package life.liquide.trades.model;

import life.liquide.trades.entity.TradesMasterEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class TradesMasterDto {

    private Long id;

    private String type;

    private Long userId;

    private String symbol;

    private Integer shares;

    private Double price;

    private Long createdAt;

    private Long updatedAt;

    public static TradesMasterDto convertEntityToDto(TradesMasterEntity tradesMasterEntity)
    {
        return TradesMasterDto.builder().
                id(tradesMasterEntity.getId()).
                type(tradesMasterEntity.getType()).
                userId(tradesMasterEntity.getUserId()).
                symbol(tradesMasterEntity.getSymbol()).
                shares(tradesMasterEntity.getShares()).
                price(tradesMasterEntity.getPrice()).
                createdAt(tradesMasterEntity.getCreatedAt()).
                updatedAt(tradesMasterEntity.getUpdatedAt()).build();
    }

}