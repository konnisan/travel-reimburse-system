package com.kjd.demoday3.service;

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

public interface TravelReimburseService {
    TravelReimbursePageVO queryTravelReimbursePageList(QueryTravelReimbursePageListDTO dto);

    CreateTravelReimburseDraftVO createTravelReimburseDraft(CreateTravelReimburseDraftDTO dto);

    TravelReimburseDetailVO queryTravelReimburseDetail(QueryTravelReimburseDetailDTO dto);

    SaveTravelReimburseDetailVO saveTravelReimburseDraft(SaveTravelReimburseDetailDTO dto);

    SubmitTravelReimburseVO submitTravelReimburse(SubmitTravelReimburseDTO dto);

    InvalidTravelReimburseVO cancelTravelReimburse(InvalidTravelReimburseDTO dto);

    TravelReimburseBaseDataVO queryTravelReimburseBaseData(QueryTravelReimburseBaseDataDTO dto);

    CalculateTripSubsidyVO calculateTripSubsidy(CalculateTripSubsidyDTO dto);

    CalculateSubsidyCalendarVO calculateSubsidyCalendar(CalculateSubsidyCalendarDTO dto);

    CalculateCostShareVO calculateCostShare(CalculateCostShareDTO dto);
}
