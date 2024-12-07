package life.liquide.trades.service;

import life.liquide.trades.entity.TradesMasterEntity;
import life.liquide.trades.exception.ResourceNotFoundException;
import life.liquide.trades.model.TradesMasterDto;
import life.liquide.trades.model.TradesRequestDto;
import life.liquide.trades.repository.TradesMasterRepository;
import life.liquide.trades.response.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradesMasterRepository tradesMasterRepository;

    @InjectMocks
    private TradeService tradeService;

    private TradesMasterEntity sampleEntity;
    private TradesMasterDto sampleDto;
    private TradesRequestDto sampleRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEntity = TradesMasterEntity.builder()
                .id(1L)
                .type("BUY")
                .userId(123L)
                .symbol("AAPL")
                .shares(10)
                .price(150.0)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        sampleDto = TradesMasterDto.builder()
                .id(1L)
                .type("BUY")
                .userId(123L)
                .symbol("AAPL")
                .shares(10)
                .price(150.0)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        sampleRequestDto = TradesRequestDto.builder()
                .type("BUY")
                .userId(123L)
                .symbol("AAPL")
                .shares(10)
                .price(150.0)
                .build();
    }

    /*** TEST CASES FOR getTrade() METHOD ***/

    @Test
    void testGetTrade_Success() {
        when(tradesMasterRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        ServiceResponse<TradesMasterDto> response = tradeService.getTrade(1L);

        assertEquals("Fetched Successfully!", response.getMessage());
        assertEquals(sampleDto.getId(), response.getData().getId());
        verify(tradesMasterRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTrade_NotFound() {
        when(tradesMasterRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeService.getTrade(1L));

        assertEquals("Trade not found for ID: 1", exception.getMessage());
        verify(tradesMasterRepository, times(1)).findById(1L);
    }

    /*** TEST CASES FOR getTrades() METHOD ***/

    @Test
    void testGetTrades_FetchAllTrades_Success() {
        when(tradesMasterRepository.findAll()).thenReturn(List.of(sampleEntity));

        ServiceResponse<List<TradesMasterDto>> response = tradeService.getTrades(null, null);

        assertEquals("Fetched all trades successfully!", response.getMessage());
        assertEquals(1, response.getData().size());
        verify(tradesMasterRepository, times(1)).findAll();
    }

    @Test
    void testGetTrades_FetchByUserId_Success() {
        when(tradesMasterRepository.findAllByUserId(123L)).thenReturn(List.of(sampleEntity));

        ServiceResponse<List<TradesMasterDto>> response = tradeService.getTrades(null, 123L);

        assertEquals("Fetched trades by user ID successfully!", response.getMessage());
        verify(tradesMasterRepository, times(1)).findAllByUserId(123L);
    }

    @Test
    void testGetTrades_FetchByType_Success() {
        when(tradesMasterRepository.findAllByType("BUY")).thenReturn(List.of(sampleEntity));

        ServiceResponse<List<TradesMasterDto>> response = tradeService.getTrades("BUY", null);

        assertEquals("Fetched trades by type successfully!", response.getMessage());
        verify(tradesMasterRepository, times(1)).findAllByType("BUY");
    }

    @Test
    void testGetTrades_FetchByTypeAndUserId_Success() {
        when(tradesMasterRepository.findAllByTypeAndUserId("BUY", 123L)).thenReturn(List.of(sampleEntity));

        ServiceResponse<List<TradesMasterDto>> response = tradeService.getTrades("BUY", 123L);

        assertEquals("Fetched trades by type and user ID successfully!", response.getMessage());
        verify(tradesMasterRepository, times(1)).findAllByTypeAndUserId("BUY", 123L);
    }

    @Test
    void testGetTrades_Failure() {
        when(tradesMasterRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        ServiceResponse<List<TradesMasterDto>> response = tradeService.getTrades(null, null);

        assertEquals(ServiceResponse.FAILED, response.getStatus());
        assertNull(response.getData());
        verify(tradesMasterRepository, times(1)).findAll();
    }

    /*** TEST CASES FOR createTrade() METHOD ***/

    @Test
    void testCreateTrade_Success() {
        when(tradesMasterRepository.save(any(TradesMasterEntity.class))).thenReturn(sampleEntity);

        ServiceResponse<TradesMasterDto> response = tradeService.createTrade(sampleRequestDto);

        assertEquals("Trade created successfully!", response.getMessage());
        assertEquals(sampleDto.getSymbol(), response.getData().getSymbol());
        verify(tradesMasterRepository, times(1)).save(any(TradesMasterEntity.class));
    }

    @Test
    void testCreateTrade_NullRequest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tradeService.createTrade(null));

        assertEquals("Trade request data cannot be null", exception.getMessage());
        verify(tradesMasterRepository, never()).save(any());
    }

    @Test
    void testCreateTrade_FailureOnSave() {
        when(tradesMasterRepository.save(any(TradesMasterEntity.class)))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tradeService.createTrade(sampleRequestDto));

        assertTrue(exception.getMessage().contains("Error creating trade: Database error"));
        verify(tradesMasterRepository, times(1)).save(any(TradesMasterEntity.class));
    }
}
