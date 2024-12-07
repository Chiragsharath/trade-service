package life.liquide.trades.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import life.liquide.trades.entity.TradesMasterEntity;
import life.liquide.trades.model.TradesMasterDto;
import life.liquide.trades.model.TradesRequestDto;
import life.liquide.trades.repository.TradesMasterRepository;
import life.liquide.trades.response.ServiceResponse;
import life.liquide.trades.exception.ResourceNotFoundException;
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


        if (tradesMasterEntity.isEmpty()) {
            log.error("Trade with ID {} not found", tradeId);
            throw new ResourceNotFoundException("Trade not found for ID: " + tradeId);
        }

        return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched Successfully!",
                TradesMasterDto.convertEntityToDto(tradesMasterEntity.get()));
    }

    public ServiceResponse<List<TradesMasterDto>> getTrades(String type, Long userId) {

        try {
            if (areBothNull(type, userId)) {
                return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched all trades successfully!",
                        fetchAllTrades());
            } else if (isOnlyUserIdPresent(type, userId)) {
                return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched trades by user ID successfully!",
                        fetchTradesByUserId(userId));
            } else if (isOnlyTypePresent(type, userId)) {
                return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched trades by type successfully!",
                        fetchTradesByType(type));
            } else {
                return new ServiceResponse<>(ServiceResponse.SUCCESS, "Fetched trades by type and user ID successfully!",
                        fetchTradesByTypeAndUserId(type, userId));
            }
        } catch (Exception e) {
            log.error("Error fetching trades: {}", e.getMessage());
            return new ServiceResponse<>(ServiceResponse.FAILED, "An error occurred while fetching trades: ",null);
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
        try {
            List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
            List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAll();
            if (tradesMasterEntities.isEmpty()) {
                log.warn("No trades found");
                throw new ResourceNotFoundException("No trades found in the system.");
            }
            tradesMasterEntities.forEach(tradesMasterEntity -> {
                tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
            });
            return tradesMasterDtoList;
        } catch (Exception e) {
            log.error("Error fetching all trades: {}", e.getMessage());
            throw new RuntimeException("Error fetching all trades: " + e.getMessage());
        }
    }

    private List<TradesMasterDto> fetchTradesByUserId(Long userId) {
        try {
            List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
            List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAllByUserId(userId);
            if (tradesMasterEntities.isEmpty()) {
                log.warn("No trades found for user ID {}", userId);
                throw new ResourceNotFoundException("No trades found for user ID: " + userId);
            }
            tradesMasterEntities.forEach(tradesMasterEntity -> {
                tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
            });
            return tradesMasterDtoList;
        } catch (Exception e) {
            log.error("Error fetching trades by user ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error fetching trades by user ID: " + e.getMessage());
        }
    }

    private List<TradesMasterDto> fetchTradesByType(String type) {
        try {
            List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
            List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAllByType(type);
            if (tradesMasterEntities.isEmpty()) {
                log.warn("No trades found for type {}", type);
                throw new ResourceNotFoundException("No trades found for type: " + type);
            }
            tradesMasterEntities.forEach(tradesMasterEntity -> {
                tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
            });
            return tradesMasterDtoList;
        } catch (Exception e) {
            log.error("Error fetching trades by type {}: {}", type, e.getMessage());
            throw new RuntimeException("Error fetching trades by type: " + e.getMessage());
        }
    }

    private List<TradesMasterDto> fetchTradesByTypeAndUserId(String type, Long userId) {
        try {
            List<TradesMasterDto> tradesMasterDtoList = new ArrayList<>();
            List<TradesMasterEntity> tradesMasterEntities = tradesMasterRepository.findAllByTypeAndUserId(type, userId);
            if (tradesMasterEntities.isEmpty()) {
                log.warn("No trades found for type {} and user ID {}", type, userId);
                throw new ResourceNotFoundException("No trades found for type and user ID.");
            }
            tradesMasterEntities.forEach(tradesMasterEntity -> {
                tradesMasterDtoList.add(TradesMasterDto.convertEntityToDto(tradesMasterEntity));
            });
            return tradesMasterDtoList;
        } catch (Exception e) {
            log.error("Error fetching trades by type {} and user ID {}: {}", type, userId, e.getMessage());
            throw new RuntimeException("Error fetching trades by type and user ID: " + e.getMessage());
        }
    }

    public ServiceResponse<TradesMasterDto> createTrade(TradesRequestDto tradesRequestDto) {
        try {
            if (tradesRequestDto == null) {
                log.error("Invalid trade request data: {}", tradesRequestDto);
                throw new IllegalArgumentException("Trade request data cannot be null");
            }

            TradesMasterEntity tradesMasterEntity = TradesMasterEntity.convertDtoToEntity(tradesRequestDto);
            TradesMasterEntity savedEntity = tradesMasterRepository.save(tradesMasterEntity);

            if (Objects.isNull(savedEntity)) {
                log.error("Failed to save trade: {}", tradesRequestDto);
                throw new RuntimeException("Failed to create trade.");
            }

            return new ServiceResponse<>(ServiceResponse.SUCCESS, "Trade created successfully!",
                    TradesMasterDto.convertEntityToDto(savedEntity));
        } catch (IllegalArgumentException e) {
            // Re-throw IllegalArgumentException to avoid wrapping it into RuntimeException
            throw e;
        } catch (Exception e) {
            log.error("Error creating trade: {}", e.getMessage());
            throw new RuntimeException("Error creating trade: " + e.getMessage());
        }
    }
}
