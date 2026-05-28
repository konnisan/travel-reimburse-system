package com.kjd.demoday3.controller;

import com.kjd.demoday3.common.Result;
import com.kjd.demoday3.dto.CalculateCostShareDTO;
import com.kjd.demoday3.dto.CalculateSubsidyCalendarDTO;
import com.kjd.demoday3.dto.CalculateTripSubsidyDTO;
import com.kjd.demoday3.dto.CreateTravelReimburseDraftDTO;
import com.kjd.demoday3.dto.InvalidTravelReimburseDTO;
import com.kjd.demoday3.dto.QueryTravelReimburseBaseDataDTO;
import com.kjd.demoday3.dto.QueryTravelReimburseDetailDTO;
import com.kjd.demoday3.dto.QueryTravelReimbursePageListDTO;
import com.kjd.demoday3.dto.SaveTravelReimburseDetailDTO;
import com.kjd.demoday3.dto.SubmitTravelReimburseDTO;
import com.kjd.demoday3.service.TravelReimburseService;
import com.kjd.demoday3.vo.CalculateCostShareVO;
import com.kjd.demoday3.vo.CalculateSubsidyCalendarVO;
import com.kjd.demoday3.vo.CalculateTripSubsidyVO;
import com.kjd.demoday3.vo.CreateTravelReimburseDraftVO;
import com.kjd.demoday3.vo.InvalidTravelReimburseVO;
import com.kjd.demoday3.vo.SaveTravelReimburseDetailVO;
import com.kjd.demoday3.vo.SubmitTravelReimburseVO;
import com.kjd.demoday3.vo.TravelReimburseBaseDataVO;
import com.kjd.demoday3.vo.TravelReimburseDetailVO;
import com.kjd.demoday3.vo.TravelReimbursePageVO;
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
    public Result<TravelReimbursePageVO> queryTravelReimbursePageList(@RequestBody QueryTravelReimbursePageListDTO dto) {
        return Result.success(travelReimburseService.queryTravelReimbursePageList(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CreateTravelReimburseDraft")
    public Result<CreateTravelReimburseDraftVO> createTravelReimburseDraft(@RequestBody(required = false) CreateTravelReimburseDraftDTO dto) {
        return Result.success(travelReimburseService.createTravelReimburseDraft(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_QueryTravelReimburseDetail")
    public Result<TravelReimburseDetailVO> queryTravelReimburseDetail(@RequestBody QueryTravelReimburseDetailDTO dto) {
        return Result.success(travelReimburseService.queryTravelReimburseDetail(dto));
    }

    @PostMapping({
            "/fcecf/fccapi/COMM_REIMBURSE_SaveTravelReimburseDraft",
            "/fcecf/fccapi/COMM_REIMBURSE_SaveTravelReimburseDetail"
    })
    public Result<SaveTravelReimburseDetailVO> saveTravelReimburseDraft(@RequestBody SaveTravelReimburseDetailDTO dto) {
        return Result.success(travelReimburseService.saveTravelReimburseDraft(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_SubmitTravelReimburse")
    public Result<SubmitTravelReimburseVO> submitTravelReimburse(@RequestBody SubmitTravelReimburseDTO dto) {
        return Result.success(travelReimburseService.submitTravelReimburse(dto));
    }

    @PostMapping({
            "/fcecf/fccapi/COMM_REIMBURSE_CancelTravelReimburse",
            "/fcecf/fccapi/COMM_REIMBURSE_InvalidTravelReimburse"
    })
    public Result<InvalidTravelReimburseVO> cancelTravelReimburse(@RequestBody InvalidTravelReimburseDTO dto) {
        return Result.success(travelReimburseService.cancelTravelReimburse(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_QueryTravelReimburseBaseData")
    public Result<TravelReimburseBaseDataVO> queryTravelReimburseBaseData(@RequestBody(required = false) QueryTravelReimburseBaseDataDTO dto) {
        return Result.success(travelReimburseService.queryTravelReimburseBaseData(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CalculateTripSubsidy")
    public Result<CalculateTripSubsidyVO> calculateTripSubsidy(@RequestBody CalculateTripSubsidyDTO dto) {
        return Result.success(travelReimburseService.calculateTripSubsidy(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CalculateSubsidyCalendar")
    public Result<CalculateSubsidyCalendarVO> calculateSubsidyCalendar(@RequestBody CalculateSubsidyCalendarDTO dto) {
        return Result.success(travelReimburseService.calculateSubsidyCalendar(dto));
    }

    @PostMapping("/fcecf/fccapi/COMM_REIMBURSE_CalculateCostShare")
    public Result<CalculateCostShareVO> calculateCostShare(@RequestBody CalculateCostShareDTO dto) {
        return Result.success(travelReimburseService.calculateCostShare(dto));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleBusinessException(IllegalArgumentException ex) {
        return Result.fail(ex.getMessage());
    }
}
