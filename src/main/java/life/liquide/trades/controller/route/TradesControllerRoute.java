package life.liquide.trades.controller.route;


import io.swagger.v3.oas.annotations.tags.Tag;
import life.liquide.trades.model.TradesMasterDto;
import life.liquide.trades.model.TradesRequestDto;
import life.liquide.trades.response.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@Tag(name = "Trades")
@RequestMapping("/v1/trades")
public interface TradesControllerRoute {

  @GetMapping("/{tradeId}")
  ResponseEntity<ServiceResponse<TradesMasterDto>> getTrade(@PathVariable Long tradeId);

  @GetMapping("")
  ResponseEntity<ServiceResponse<List<TradesMasterDto>>> getTrades(@RequestParam(required = false)
                                                                           String type,

                                                                   @RequestParam(required = false)
                                                                           Long userId);

  @PostMapping("")
  ResponseEntity<ServiceResponse<TradesMasterDto>> createTrade(@RequestBody TradesRequestDto tradesRequestDto);

}

