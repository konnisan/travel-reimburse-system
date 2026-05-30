package com.kjd.backend.service;

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
