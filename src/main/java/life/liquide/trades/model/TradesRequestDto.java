package life.liquide.trades.model;

import life.liquide.trades.entity.TradesMasterEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class TradesRequestDto {

    private String type;

    private Long userId;

    private String symbol;

    private Integer shares;

    private Double price;

    private Long createdAt;

    private Long updatedAt;

    public static TradesRequestDto convertEntityToDto(TradesMasterEntity tradesMasterEntity)
    {
        return TradesRequestDto.builder().
                type(tradesMasterEntity.getType()).
                userId(tradesMasterEntity.getUserId()).
                symbol(tradesMasterEntity.getSymbol()).
                shares(tradesMasterEntity.getShares()).
                price(tradesMasterEntity.getPrice()).
                createdAt(tradesMasterEntity.getCreatedAt()).
                updatedAt(tradesMasterEntity.getUpdatedAt()).build();
    }

}