package life.liquide.trades.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import life.liquide.trades.entity.TradesMasterEntity;
import life.liquide.trades.model.TradesMasterDto;
import life.liquide.trades.model.TradesRequestDto;
import life.liquide.trades.repository.TradesMasterRepository;
import life.liquide.trades.response.ServiceResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class TradeService {


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TradesMasterRepository tradesMasterRepository;

    public ServiceResponse<TradesMasterDto> getTrade(Long tradeId) {
        Optional<TradesMasterEntity> tradesMasterEntity = tradesMasterRepository.findById(tradeId);


        return tradesMasterEntity.map(masterEntity -> new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                TradesMasterDto.convertEntityToDto(masterEntity))).orElse(null);
    }

    public ServiceResponse<List<TradesMasterDto>> getTrades(String type, Long userId) {

        if (areBothNull(type, userId)) {
            return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                    fetchAllTrades());
        } else if (isOnlyUserIdPresent(type, userId)) {
            return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                    fetchTradesByUserId(userId));
        } else if (isOnlyTypePresent(type, userId)) {
            return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                    fetchTradesByType(type));
        } else {
            return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                    fetchTradesByTypeAndUserId(type, userId));
        }
    }

    private boolean areBothNull(String type, Long userId) {
        return Objects.isNull(type) && Objects.isNull(userId);
    }

    private boolean isOnlyUserIdPresent(String type, Long userId) {
        return Objects.isNull(type) && Objects.nonNull(userId);
    }

    private boolean isOnlyTypePresent(String type, Long userId) {
        return Objects.nonNull(type) && Objects.isNull(userId);
    }

    private List<TradesMasterDto> fetchAllTrades() {

        List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
        List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAll();
        tradesMasterEntities.forEach(tradesMasterEntity -> {
            tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
        });

        return tradesMasterDtoList;
    }

    private List<TradesMasterDto> fetchTradesByUserId(Long userId) {

        List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
        List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAllByUserId(userId);
        tradesMasterEntities.forEach(tradesMasterEntity -> {
            tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
        });
        return tradesMasterDtoList;
    }

    private List<TradesMasterDto> fetchTradesByType(String type) {
        List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
        List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAllByType(type);
        tradesMasterEntities.forEach(tradesMasterEntity -> {
            tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
        });
        return tradesMasterDtoList;
    }

    private List<TradesMasterDto> fetchTradesByTypeAndUserId(String type, Long userId) {
        List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
        List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAllByTypeAndUserId(type,userId);
        tradesMasterEntities.forEach(tradesMasterEntity -> {
            tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
        });
        return tradesMasterDtoList;
    }


    public ServiceResponse<TradesMasterDto> createTrade(@NonNull TradesRequestDto tradesMasterDto) {

        TradesMasterEntity tradesMasterEntity = TradesMasterEntity.convertDtoToEntity(tradesMasterDto);

        TradesMasterEntity save = tradesMasterRepository.save(tradesMasterEntity);
        if (Objects.nonNull(save))
        {
            return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                   TradesMasterDto.convertEntityToDto(save));
        }

        return null;
    }
}
