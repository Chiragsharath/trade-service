package life.liquide.trades.controller;


import life.liquide.trades.controller.route.TradesControllerRoute;
import life.liquide.trades.exception.CustomException;
import life.liquide.trades.exception.ResourceNotFoundException;
import life.liquide.trades.model.TradesMasterDto;
import life.liquide.trades.model.TradesRequestDto;
import life.liquide.trades.response.ServiceResponse;
import life.liquide.trades.service.TradeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/trades")
@Validated
public class TradesController implements TradesControllerRoute {


  @Autowired
  TradeService tradeService;

  @Override
  public ResponseEntity<ServiceResponse<TradesMasterDto>> getTrade(Long tradeId) {
    try {
      ServiceResponse<TradesMasterDto> response = tradeService.getTrade(tradeId);
      if (response == null) {
        throw new ResourceNotFoundException("Trade not found for ID: " + tradeId);
      }
      return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>(ServiceResponse.FAILED, ex.getMessage(), null));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse<>(ServiceResponse.FAILED, "An unexpected error occurred: " + ex.getMessage(), null));
    }
  }

  @Override
  public ResponseEntity<ServiceResponse<List<TradesMasterDto>>> getTrades(String type, Long userId) {
    try {
      ServiceResponse<List<TradesMasterDto>> response = tradeService.getTrades(type, userId);
      return ResponseEntity.ok(response);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse<>(ServiceResponse.FAILED, "An unexpected error occurred: " + ex.getMessage(), null));
    }
  }

  @Override
  public ResponseEntity<ServiceResponse<TradesMasterDto>> createTrade(@NonNull TradesRequestDto tradesRequestDto) {
    try {
      ServiceResponse<TradesMasterDto> response = tradeService.createTrade(tradesRequestDto);
      if (response == null) {
        throw new CustomException("Failed to create trade.");
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CustomException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse<>(ServiceResponse.FAILED, ex.getMessage(), null));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse<>(ServiceResponse.FAILED, "An unexpected error occurred: " + ex.getMessage(), null));
    }
  }

  // Global exception handler (can also use @ControllerAdvice instead)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ServiceResponse<String>> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse<>(ServiceResponse.FAILED, "Error: " + ex.getMessage(), null));
  }


}
