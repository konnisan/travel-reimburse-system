package com.kjd.backend.controller;

import com.kjd.backend.common.Result;
import com.kjd.backend.dto.CalculateCostShareDTO;
import com.kjd.backend.dto.CalculateSubsidyCalendarDTO;
import com.kjd.backend.dto.CalculateTripSubsidyDTO;
import com.kjd.backend.dto.CreateTravelReimburseDraftDTO;
import com.kjd.backend.dto.InvalidTravelReimburseDTO;
import com.kjd.backend.dto.QueryTravelReimburseBaseDataDTO;
import com.kjd.backend.dto.QueryTravelReimburseDetailDTO;
import com.kjd.backend.dto.QueryTravelReimbursePageListDTO;
import com.kjd.backend.dto.SaveTravelReimburseDetailDTO;
import com.kjd.backend.dto.SubmitTravelReimburseDTO;
import com.kjd.backend.service.TravelReimburseService;
import com.kjd.backend.vo.CalculateCostShareVO;
import com.kjd.backend.vo.CalculateSubsidyCalendarVO;
import com.kjd.backend.vo.CalculateTripSubsidyVO;
import com.kjd.backend.vo.CreateTravelReimburseDraftVO;
import com.kjd.backend.vo.InvalidTravelReimburseVO;
import com.kjd.backend.vo.SaveTravelReimburseDetailVO;
import com.kjd.backend.vo.SubmitTravelReimburseVO;
import com.kjd.backend.vo.TravelReimburseBaseDataVO;
import com.kjd.backend.vo.TravelReimburseDetailVO;
import com.kjd.backend.vo.TravelReimbursePageVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/b2c/reimburse")
public class TravelReimburseController {
    private final TravelReimburseService travelReimburseService;

    public TravelReimburseController(TravelReimburseService travelReimburseService) {
        this.travelReimburseService = travelReimburseService;
    }

    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("TravelReimburseController ok");
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_QueryTravelReimbursePageList")
    @PreAuthorize("hasAuthority('reimburse:list')")
    public Result<TravelReimbursePageVO> queryTravelReimbursePageList(@RequestBody QueryTravelReimbursePageListDTO dto) {
        return Result.success(travelReimburseService.queryTravelReimbursePageList(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CreateTravelReimburseDraft")
    @PreAuthorize("hasAuthority('reimburse:create')")
    public Result<CreateTravelReimburseDraftVO> createTravelReimburseDraft(@RequestBody(required = false) CreateTravelReimburseDraftDTO dto) {
        return Result.success(travelReimburseService.createTravelReimburseDraft(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_QueryTravelReimburseDetail")
    @PreAuthorize("hasAuthority('reimburse:view')")
    public Result<TravelReimburseDetailVO> queryTravelReimburseDetail(@RequestBody QueryTravelReimburseDetailDTO dto) {
        return Result.success(travelReimburseService.queryTravelReimburseDetail(dto));
    }

    @PostMapping({
            "/fcecf/fccapi/COMM_REIMBURSE_SaveTravelReimburseDraft",
            "/fcecf/fccapi/COMM_REIMBURSE_SaveTravelReimburseDetail"
    })
    @PreAuthorize("hasAuthority('reimburse:save')")
    public Result<SaveTravelReimburseDetailVO> saveTravelReimburseDraft(@RequestBody SaveTravelReimburseDetailDTO dto) {
        return Result.success(travelReimburseService.saveTravelReimburseDraft(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_SubmitTravelReimburse")
    @PreAuthorize("hasAuthority('reimburse:submit')")
    public Result<SubmitTravelReimburseVO> submitTravelReimburse(@RequestBody SubmitTravelReimburseDTO dto) {
        return Result.success(travelReimburseService.submitTravelReimburse(dto));
    }

    @PostMapping({
            "/fcecf/fccapi/COMM_REIMBURSE_CancelTravelReimburse",
            "/fcecf/fccapi/COMM_REIMBURSE_InvalidTravelReimburse"
    })
    @PreAuthorize("hasAuthority('reimburse:invalid')")
    public Result<InvalidTravelReimburseVO> cancelTravelReimburse(@RequestBody InvalidTravelReimburseDTO dto) {
        return Result.success(travelReimburseService.cancelTravelReimburse(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_QueryTravelReimburseBaseData")
    @PreAuthorize("hasAnyAuthority('reimburse:list', 'reimburse:view', 'reimburse:create', 'reimburse:edit')")
    public Result<TravelReimburseBaseDataVO> queryTravelReimburseBaseData(@RequestBody(required = false) QueryTravelReimburseBaseDataDTO dto) {
        return Result.success(travelReimburseService.queryTravelReimburseBaseData(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CalculateTripSubsidy")
    @PreAuthorize("hasAnyAuthority('reimburse:create', 'reimburse:edit', 'reimburse:save', 'reimburse:submit')")
    public Result<CalculateTripSubsidyVO> calculateTripSubsidy(@RequestBody CalculateTripSubsidyDTO dto) {
        return Result.success(travelReimburseService.calculateTripSubsidy(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CalculateSubsidyCalendar")
    @PreAuthorize("hasAnyAuthority('reimburse:create', 'reimburse:edit', 'reimburse:save', 'reimburse:submit')")
    public Result<CalculateSubsidyCalendarVO> calculateSubsidyCalendar(@RequestBody CalculateSubsidyCalendarDTO dto) {
        return Result.success(travelReimburseService.calculateSubsidyCalendar(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CalculateCostShare")
    @PreAuthorize("hasAnyAuthority('reimburse:create', 'reimburse:edit', 'reimburse:save', 'reimburse:submit')")
    public Result<CalculateCostShareVO> calculateCostShare(@RequestBody CalculateCostShareDTO dto) {
        return Result.success(travelReimburseService.calculateCostShare(dto));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleBusinessException(IllegalArgumentException ex) {
        return Result.fail(ex.getMessage());
    }
}
