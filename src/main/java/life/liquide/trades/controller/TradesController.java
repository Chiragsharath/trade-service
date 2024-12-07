package life.liquide.trades.controller;


import life.liquide.trades.controller.route.TradesControllerRoute;
import life.liquide.trades.model.TradesMasterDto;
import life.liquide.trades.model.TradesRequestDto;
import life.liquide.trades.response.ServiceResponse;
import life.liquide.trades.service.TradeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    return ResponseEntity.ok(tradeService.getTrade(tradeId));
  }

  @Override
  public ResponseEntity<ServiceResponse<List<TradesMasterDto>>> getTrades(String type, Long userId) {
    return ResponseEntity.ok(tradeService.getTrades(type,userId));
  }


  @Override
  public ResponseEntity<ServiceResponse<TradesMasterDto>> createTrade(@NonNull TradesRequestDto tradesRequestDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(tradeService.createTrade(tradesRequestDto));
  }


}
