package com.kjd.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjd.backend.dto.CalculateCostShareDTO;
import com.kjd.backend.dto.CalculateCostShareDataDTO;
import com.kjd.backend.dto.CalculateSubsidyCalendarDTO;
import com.kjd.backend.dto.CalculateSubsidyCalendarDataDTO;
import com.kjd.backend.dto.CalculateTripSubsidyDTO;
import com.kjd.backend.dto.CalculateTripSubsidyDataDTO;
import com.kjd.backend.dto.CostShareDTO;
import com.kjd.backend.dto.CreateTravelReimburseDraftDTO;
import com.kjd.backend.dto.CreateTravelReimburseDraftDataDTO;
import com.kjd.backend.dto.InvalidTravelReimburseDTO;
import com.kjd.backend.dto.ManualTripDTO;
import com.kjd.backend.dto.QueryTravelReimburseBaseDataDTO;
import com.kjd.backend.dto.QueryTravelReimburseDetailDTO;
import com.kjd.backend.dto.QueryTravelReimbursePageListDTO;
import com.kjd.backend.dto.QueryTravelReimbursePageListDataDTO;
import com.kjd.backend.dto.SaveTravelReimburseDetailDTO;
import com.kjd.backend.dto.SubmitTravelReimburseDTO;
import com.kjd.backend.dto.SubsidyCalendarDTO;
import com.kjd.backend.dto.SubsidyInfoDTO;
import com.kjd.backend.dto.TravelReimburseDetailSaveDTO;
import com.kjd.backend.entity.FkReimApportion;
import com.kjd.backend.entity.FkReimItinerary;
import com.kjd.backend.entity.FkReimMain;
import com.kjd.backend.entity.FkReimSubsidy;
import com.kjd.backend.entity.FkSubsidyCalendar;
import com.kjd.backend.mapper.FkReimApportionMapper;
import com.kjd.backend.mapper.FkReimItineraryMapper;
import com.kjd.backend.mapper.FkReimMainMapper;
import com.kjd.backend.mapper.FkReimSubsidyMapper;
import com.kjd.backend.mapper.FkSubsidyCalendarMapper;
import com.kjd.backend.service.RedisSupportService;
import com.kjd.backend.service.TravelReimburseService;
import com.kjd.backend.vo.CalculateCostShareVO;
import com.kjd.backend.vo.CalculateSubsidyCalendarVO;
import com.kjd.backend.vo.CalculateTripSubsidyVO;
import com.kjd.backend.vo.BusinessTypeVO;
import com.kjd.backend.vo.CityVO;
import com.kjd.backend.vo.CostShareVO;
import com.kjd.backend.vo.CreateTravelReimburseDraftVO;
import com.kjd.backend.vo.EmployeeVO;
import com.kjd.backend.vo.InvalidTravelReimburseVO;
import com.kjd.backend.vo.ManualTripVO;
import com.kjd.backend.vo.ProjectVO;
import com.kjd.backend.vo.ReimCompanyVO;
import com.kjd.backend.vo.ReimDepartmentVO;
import com.kjd.backend.vo.SaveTravelReimburseDetailVO;
import com.kjd.backend.vo.SubmitTravelReimburseVO;
import com.kjd.backend.vo.SubsidyCalendarVO;
import com.kjd.backend.vo.SubsidyInfoVO;
import com.kjd.backend.vo.TravelReimburseBaseDataVO;
import com.kjd.backend.vo.TravelReimburseDetailVO;
import com.kjd.backend.vo.TravelReimbursePageBean;
import com.kjd.backend.vo.TravelReimbursePageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TravelReimburseServiceImpl implements TravelReimburseService {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final FkReimMainMapper mainMapper;
    private final FkReimItineraryMapper itineraryMapper;
    private final FkReimSubsidyMapper subsidyMapper;
    private final FkSubsidyCalendarMapper calendarMapper;
    private final FkReimApportionMapper apportionMapper;
    private final RedisSupportService redisSupportService;

    public TravelReimburseServiceImpl(FkReimMainMapper mainMapper,
                                      FkReimItineraryMapper itineraryMapper,
                                      FkReimSubsidyMapper subsidyMapper,
                                      FkSubsidyCalendarMapper calendarMapper,
                                      FkReimApportionMapper apportionMapper,
                                      RedisSupportService redisSupportService) {
        this.mainMapper = mainMapper;
        this.itineraryMapper = itineraryMapper;
        this.subsidyMapper = subsidyMapper;
        this.calendarMapper = calendarMapper;
        this.apportionMapper = apportionMapper;
        this.redisSupportService = redisSupportService;
    }

    @Override
    public TravelReimbursePageVO queryTravelReimbursePageList(QueryTravelReimbursePageListDTO dto) {
        QueryTravelReimbursePageListDTO request = dto == null ? new QueryTravelReimbursePageListDTO() : dto;
        int current = request.getCurrent() == null || request.getCurrent() < 1 ? 1 : request.getCurrent();
        int size = request.getSize() == null || request.getSize() < 1 ? 10 : request.getSize();
        QueryTravelReimbursePageListDataDTO data = request.getData();

        LambdaQueryWrapper<FkReimMain> wrapper = new LambdaQueryWrapper<>();
        if (data != null) {
            wrapper.like(StringUtils.hasText(data.getReimBillNo()), FkReimMain::getReimBillNo, data.getReimBillNo());
            wrapper.like(StringUtils.hasText(data.getTitle()), FkReimMain::getReimbursementTitle, data.getTitle());
            wrapper.like(StringUtils.hasText(data.getReason()), FkReimMain::getBusinessTripReason, data.getReason());
            wrapper.eq(StringUtils.hasText(data.getReimCompanyId()), FkReimMain::getReimCompanyId, data.getReimCompanyId());
            wrapper.eq(StringUtils.hasText(data.getReimDepartmentId()), FkReimMain::getReimDepartmentId, data.getReimDepartmentId());
            wrapper.eq(StringUtils.hasText(data.getReimburserId()), FkReimMain::getReimburserId, data.getReimburserId());
            wrapper.eq(StringUtils.hasText(data.getBusinessTypeId()), FkReimMain::getBusinessTypeId, data.getBusinessTypeId());
            wrapper.eq(StringUtils.hasText(data.getBillStatus()), FkReimMain::getBillStatus, data.getBillStatus());
        }
        wrapper.orderByDesc(FkReimMain::getCreationTime);

        Page<FkReimMain> dbPage = mainMapper.selectPage(new Page<>(current, size), wrapper);
        TravelReimbursePageVO vo = new TravelReimbursePageVO();
        vo.setCurrent(dbPage.getCurrent());
        vo.setSize(dbPage.getSize());
        vo.setTotal(dbPage.getTotal());
        vo.setPages(dbPage.getPages());
        vo.setRecords(dbPage.getRecords().stream().map(this::toPageBean).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateTravelReimburseDraftVO createTravelReimburseDraft(CreateTravelReimburseDraftDTO dto) {
        CreateTravelReimburseDraftDataDTO data = dto == null ? null : dto.getData();
        String id = uuid();
        String billDate = data != null && StringUtils.hasText(data.getBillDate()) ? data.getBillDate() : LocalDate.now().format(DATE);

        FkReimMain main = new FkReimMain();
        main.setId(id);
        main.setBillDate(billDate);
        main.setBillStatus("0");
        main.setCreationTime(LocalDateTime.now().format(DATE_TIME));
        main.setSubsidyTotal("0.00");
        main.setMealAllowance("0.00");
        main.setTransportationAllowance("0.00");
        main.setPhoneAllowance("0.00");
        main.setVersion(0);
        mainMapper.insert(main);

        CreateTravelReimburseDraftVO vo = new CreateTravelReimburseDraftVO();
        vo.setId(id);
        vo.setVersion(0);
        vo.setReimBillNo("");
        vo.setBillDate(billDate);
        vo.setBillStatus("0");
        vo.setBillStatusName(statusName("0"));
        vo.setRedirectUrl("reimburse/detail/" + id);
        return vo;
    }

    @Override
    public TravelReimburseDetailVO queryTravelReimburseDetail(QueryTravelReimburseDetailDTO dto) {
        String id = dto == null || dto.getData() == null ? null : dto.getData().getId();
        return buildDetail(mustGetMain(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveTravelReimburseDetailVO saveTravelReimburseDraft(SaveTravelReimburseDetailDTO dto) {
        TravelReimburseDetailSaveDTO data = dto == null ? null : dto.getData();
        if (data == null || !StringUtils.hasText(data.getId())) {
            throw new IllegalArgumentException("reimburse id is required");
        }
        ensureDraftMain(data);
        saveDetail(data, "0", false);

        SaveTravelReimburseDetailVO vo = new SaveTravelReimburseDetailVO();
        vo.setValid(true);
        vo.setMessage("save success");
        vo.setDetail(buildDetail(mustGetMain(data.getId())));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubmitTravelReimburseVO submitTravelReimburse(SubmitTravelReimburseDTO dto) {
        TravelReimburseDetailSaveDTO data = dto == null ? null : dto.getData();
        if (data == null || !StringUtils.hasText(data.getId())) {
            throw new IllegalArgumentException("reimburse id is required");
        }
        FkReimMain old = mustGetMain(data.getId());
        if ("1".equals(old.getBillStatus())) {
            throw new IllegalArgumentException("completed bill cannot be submitted again");
        }
        if ("2".equals(old.getBillStatus())) {
            throw new IllegalArgumentException("cancelled bill cannot be submitted");
        }
        validateSubmit(data);
        saveDetail(data, "1", true);

        FkReimMain saved = mustGetMain(data.getId());
        SubmitTravelReimburseVO vo = new SubmitTravelReimburseVO();
        vo.setValid(true);
        vo.setMessage("submit success");
        vo.setId(saved.getId());
        vo.setVersion(defaultVersion(saved.getVersion()));
        vo.setReimBillNo(saved.getReimBillNo());
        vo.setBillStatus(saved.getBillStatus());
        vo.setBillStatusName(statusName(saved.getBillStatus()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvalidTravelReimburseVO cancelTravelReimburse(InvalidTravelReimburseDTO dto) {
        String id = dto == null || dto.getData() == null ? null : dto.getData().getId();
        Integer version = dto == null || dto.getData() == null ? null : dto.getData().getVersion();
        FkReimMain main = mustGetMain(id);
        checkOptimisticVersion(version, main);
        if ("2".equals(main.getBillStatus())) {
            throw new IllegalArgumentException("bill already cancelled");
        }
        main.setBillStatus("2");
        int updated = mainMapper.updateById(main);
        assertOptimisticUpdated(updated);

        InvalidTravelReimburseVO vo = new InvalidTravelReimburseVO();
        FkReimMain saved = mustGetMain(main.getId());
        vo.setId(saved.getId());
        vo.setVersion(defaultVersion(saved.getVersion()));
        vo.setBillStatus("2");
        vo.setBillStatusName(statusName("2"));
        vo.setMessage("cancel success");
        return vo;
    }

    @Override
    public TravelReimburseBaseDataVO queryTravelReimburseBaseData(QueryTravelReimburseBaseDataDTO dto) {
        TravelReimburseBaseDataVO cached = redisSupportService.getBaseDataCache();
        if (cached != null) {
            return cached;
        }
        TravelReimburseBaseDataVO vo = new TravelReimburseBaseDataVO();
        ReimCompanyVO company = new ReimCompanyVO();
        company.setReimCompanyId("C001");
        company.setReimCompanyNo("COMP001");
        company.setReimCompanyName("Demo Technology Co., Ltd.");

        ReimDepartmentVO department = new ReimDepartmentVO();
        department.setReimDepartmentId("D001");
        department.setReimDepartmentNo("DEP001");
        department.setReimDepartmentName("R&D Department");

        EmployeeVO employee = new EmployeeVO();
        employee.setReimburserId("E001");
        employee.setReimburserNo("10001");
        employee.setReimburserName("Zhang San");

        BusinessTypeVO rootType = new BusinessTypeVO();
        rootType.setBusinessTypeId("BT001");
        rootType.setBusinessTypeNo("BT001");
        rootType.setBusinessTypeName("Expense Reimbursement");
        rootType.setThereSubordinateNode("1");
        rootType.setSuperiorId("none");

        BusinessTypeVO travelType = new BusinessTypeVO();
        travelType.setBusinessTypeId("BT002");
        travelType.setBusinessTypeNo("BT002");
        travelType.setBusinessTypeName("Travel Reimbursement");
        travelType.setThereSubordinateNode("0");
        travelType.setSuperiorId("BT001");

        CityVO beijing = new CityVO();
        beijing.setCityNo("110000");
        beijing.setCityName("Beijing");
        beijing.setCityType("1");

        CityVO shanghai = new CityVO();
        shanghai.setCityNo("310000");
        shanghai.setCityName("Shanghai");
        shanghai.setCityType("1");

        CityVO hangzhou = new CityVO();
        hangzhou.setCityNo("330100");
        hangzhou.setCityName("Hangzhou");
        hangzhou.setCityType("2");

        ProjectVO project = new ProjectVO();
        project.setProjectId("P001");
        project.setProjectNo("PROJ001");
        project.setProjectName("Travel Project");

        vo.setReimCompanyList(Collections.singletonList(company));
        vo.setDepartmentList(Collections.singletonList(department));
        vo.setEmployeeList(Collections.singletonList(employee));
        vo.setBusinessTypeList(Arrays.asList(rootType, travelType));
        vo.setCityList(Arrays.asList(beijing, shanghai, hangzhou));
        vo.setProjectList(Collections.singletonList(project));
        redisSupportService.setBaseDataCache(vo);
        return vo;
    }

    @Override
    public CalculateTripSubsidyVO calculateTripSubsidy(CalculateTripSubsidyDTO dto) {
        CalculateTripSubsidyDataDTO data = dto == null ? null : dto.getData();
        if (data == null) {
            throw new IllegalArgumentException("trip data is required");
        }
        ManualTripDTO trip = new ManualTripDTO();
        BeanUtils.copyProperties(data, trip);
        if (!StringUtils.hasText(trip.getTripId())) {
            trip.setTripId(uuid());
        }
        SubsidyInfoDTO subsidy = buildSubsidyByTrip(trip);

        CalculateTripSubsidyVO vo = new CalculateTripSubsidyVO();
        vo.setValid(true);
        vo.setMessage("calculate success");
        vo.setTripInfo(toManualTripVO(trip));
        vo.setSubsidyInfo(toSubsidyInfoVO(subsidy));
        vo.setCalendarList(vo.getSubsidyInfo().getCalendarList());
        return vo;
    }

    @Override
    public CalculateSubsidyCalendarVO calculateSubsidyCalendar(CalculateSubsidyCalendarDTO dto) {
        CalculateSubsidyCalendarDataDTO data = dto == null ? null : dto.getData();
        List<SubsidyCalendarDTO> calendars = data == null || data.getCalendarList() == null ? new ArrayList<>() : data.getCalendarList();
        validateCalendar(calendars);
        SubsidyInfoDTO subsidy = new SubsidyInfoDTO();
        subsidy.setSubsidyId(data == null ? null : data.getSubsidyId());
        subsidy.setCalendarList(calendars);
        fillSubsidyAmounts(subsidy);

        CalculateSubsidyCalendarVO vo = new CalculateSubsidyCalendarVO();
        vo.setValid(true);
        vo.setMessage("calculate success");
        vo.setSubsidyInfo(toSubsidyInfoVO(subsidy));
        vo.setTotalSubsidyAmount(money(subsidy.getSubsidyAmount()));
        vo.setMealSubsidyAmount(money(subsidy.getMealSubsidyAmount()));
        vo.setTrafficSubsidyAmount(money(subsidy.getTrafficSubsidyAmount()));
        vo.setCommunicationSubsidyAmount(money(subsidy.getCommunicationSubsidyAmount()));
        return vo;
    }

    @Override
    public CalculateCostShareVO calculateCostShare(CalculateCostShareDTO dto) {
        CalculateCostShareDataDTO data = dto == null ? null : dto.getData();
        BigDecimal total = money(data == null ? null : data.getTotalShareAmount());
        List<CostShareDTO> shares = data == null || data.getShareList() == null ? new ArrayList<>() : new ArrayList<>(data.getShareList());
        normalizeShareRatios(shares);
        if (shares.isEmpty()) {
            shares.add(defaultShare(1, BigDecimal.ONE, total));
        }
        if ("delete".equals(data == null ? null : data.getOperateType()) && shares.size() <= 1) {
            CalculateCostShareVO vo = costShareVO(shares, total);
            vo.setValid(false);
            vo.setMessage("at least one share row is required");
            return vo;
        }
        if ("add".equals(data == null ? null : data.getOperateType())) {
            shares.add(defaultShare(shares.size() + 1, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        recalculateShare(shares, total, "average".equals(data == null ? null : data.getOperateType()));
        CalculateCostShareVO vo = costShareVO(shares, total);
        vo.setValid(true);
        vo.setMessage("calculate success");
        return vo;
    }

    private void saveDetail(TravelReimburseDetailSaveDTO data, String status, boolean generateBillNo) {
        validateCalendar(flatCalendars(data.getSubsidyList()));
        validateTripDuplicate(data.getTripList());

        FkReimMain main = mustGetMain(data.getId());
        main.setBillDate(defaultText(data.getBillDate(), main.getBillDate()));
        main.setBillStatus(status);
        if (generateBillNo && !StringUtils.hasText(main.getReimBillNo())) {
            main.setReimBillNo("CLBX" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + System.currentTimeMillis() % 10000);
        }
        main.setReimbursementTitle(data.getTitle());
        main.setBusinessTripReason(data.getReason());
        main.setReimburserId(data.getReimburserId());
        main.setReimburserNo(data.getReimburserNo());
        main.setReimburserName(data.getReimburserName());
        main.setReimDepartmentId(data.getReimDepartmentId());
        main.setReimDepartmentNo(data.getReimDepartmentNo());
        main.setReimDepartmentName(data.getReimDepartmentName());
        main.setReimCompanyId(data.getReimCompanyId());
        main.setReimCompanyNo(data.getReimCompanyNo());
        main.setReimCompanyName(data.getReimCompanyName());
        main.setBusinessTypeId(data.getBusinessTypeId());
        main.setBusinessTypeNo(data.getBusinessTypeNo());
        main.setBusinessTypeName(data.getBusinessTypeName());
        main.setRemarks(data.getRemark());

        AmountSummary amount = summarize(data.getSubsidyList());
        main.setSubsidyTotal(amount.total.toPlainString());
        main.setMealAllowance(amount.meal.toPlainString());
        main.setTransportationAllowance(amount.traffic.toPlainString());
        main.setPhoneAllowance(amount.communication.toPlainString());
        checkOptimisticVersion(data.getVersion(), main);
        int updated = mainMapper.updateById(main);
        assertOptimisticUpdated(updated);

        deleteChildren(data.getId());
        saveTrips(data.getId(), data.getTripList());
        saveSubsidies(data.getId(), data.getSubsidyList(), main);
        saveShares(data.getId(), data.getShareList(), amount.total);
    }

    private void saveTrips(String mainId, List<ManualTripDTO> trips) {
        if (trips == null) {
            return;
        }
        for (ManualTripDTO dto : trips) {
            FkReimItinerary entity = new FkReimItinerary();
            entity.setId(defaultText(dto.getTripId(), uuid()));
            entity.setMainId(mainId);
            entity.setTravelerId(dto.getTravelerId());
            entity.setTravelerNo(dto.getTravelerNo());
            entity.setTravelerName(dto.getTravelerName());
            entity.setDepartureDate(dto.getDepartureDate());
            entity.setArrivalDate(dto.getArrivalDate());
            entity.setDepartureCity(dto.getDepartureCityName());
            entity.setDepartureCityNo(dto.getDepartureCityNo());
            entity.setArrivingCity(dto.getArrivalCityName());
            entity.setArrivingCityNo(dto.getArrivalCityNo());
            entity.setItineraryInstructions(dto.getTripDesc());
            itineraryMapper.insert(entity);
        }
    }

    private void saveSubsidies(String mainId, List<SubsidyInfoDTO> subsidies, FkReimMain main) {
        if (subsidies == null) {
            return;
        }
        for (SubsidyInfoDTO dto : subsidies) {
            fillSubsidyAmounts(dto);
            FkReimSubsidy entity = new FkReimSubsidy();
            entity.setId(defaultText(dto.getSubsidyId(), uuid()));
            entity.setMainId(mainId);
            entity.setTravelerId(dto.getTravelerId());
            entity.setTravelerNo(dto.getTravelerNo());
            entity.setTravelerName(dto.getTravelerName());
            entity.setDepartureDate(dto.getStartDate());
            entity.setArrivalDate(dto.getEndDate());
            entity.setSubsidyDays(String.valueOf(defaultInt(dto.getSubsidyDays())));
            entity.setArrivingCity(dto.getSubsidyCityName());
            entity.setArrivingCityNo(dto.getSubsidyCityNo());
            entity.setApplicationAmount(money(dto.getApplyAmount()).toPlainString());
            entity.setSubsidyAmount(money(dto.getSubsidyAmount()).toPlainString());
            entity.setMealAllowance(money(dto.getMealSubsidyAmount()).toPlainString());
            entity.setTransportationAllowance(money(dto.getTrafficSubsidyAmount()).toPlainString());
            entity.setPhoneAllowance(money(dto.getCommunicationSubsidyAmount()).toPlainString());
            entity.setBusinessTypeId(main.getBusinessTypeId());
            entity.setBusinessTypeNo(main.getBusinessTypeNo());
            entity.setBusinessTypeName(main.getBusinessTypeName());
            subsidyMapper.insert(entity);
            saveCalendars(entity.getId(), dto.getCalendarList());
        }
    }

    private void saveCalendars(String subsidyId, List<SubsidyCalendarDTO> calendars) {
        if (calendars == null) {
            return;
        }
        for (SubsidyCalendarDTO dto : calendars) {
            FkSubsidyCalendar entity = new FkSubsidyCalendar();
            entity.setId(defaultText(dto.getCalendarId(), uuid()));
            entity.setMainId(subsidyId);
            entity.setTravelDate(dto.getTravelDate());
            entity.setTravelDateWeek(dto.getWeekName());
            entity.setSubsidizedCityNumber(dto.getCityNo());
            entity.setSubsidizedCities(dto.getCityName());
            entity.setCityType(dto.getCityType());
            entity.setStandardMealExpensesAmount(money(dto.getMealStandardAmount()).toPlainString());
            entity.setStandardTrafficAmount(money(dto.getTrafficStandardAmount()).toPlainString());
            entity.setStandardCommunicationAmount(money(dto.getCommunicationStandardAmount()).toPlainString());
            entity.setMealExpensesAmount(money(dto.getMealAmount()).toPlainString());
            entity.setTrafficAmount(money(dto.getTrafficAmount()).toPlainString());
            entity.setCommunicationAmount(money(dto.getCommunicationAmount()).toPlainString());
            entity.setMealChecked(boolText(dto.getMealChecked()));
            entity.setTrafficChecked(boolText(dto.getTrafficChecked()));
            entity.setCommunicationChecked(boolText(dto.getCommunicationChecked()));
            entity.setIsReimbursed("1");
            calendarMapper.insert(entity);
        }
    }

    private void saveShares(String mainId, List<CostShareDTO> shares, BigDecimal total) {
        List<CostShareDTO> data = shares == null || shares.isEmpty() ? new ArrayList<>() : new ArrayList<>(shares);
        normalizeShareRatios(data);
        if (data.isEmpty()) {
            data.add(defaultShare(1, BigDecimal.ONE, total));
        }
        recalculateShare(data, total, false);
        for (CostShareDTO dto : data) {
            FkReimApportion entity = new FkReimApportion();
            entity.setId(defaultText(dto.getShareId(), uuid()));
            entity.setMainId(mainId);
            entity.setReimCompanyId(dto.getReimCompanyId());
            entity.setReimCompanyNo(dto.getReimCompanyNo());
            entity.setReimCompanyName(dto.getReimCompanyName());
            entity.setProjectId(dto.getProjectId());
            entity.setProjectNo(dto.getProjectNo());
            entity.setProjectName(dto.getProjectName());
            entity.setApportionRatio(money4(dto.getShareRatio()).toPlainString());
            entity.setApportionAmount(money(dto.getShareAmount()).toPlainString());
            entity.setRowNo(dto.getLineNo());
            apportionMapper.insert(entity);
        }
    }

    private void deleteChildren(String mainId) {
        List<FkReimSubsidy> subsidies = subsidyMapper.selectList(new LambdaQueryWrapper<FkReimSubsidy>().eq(FkReimSubsidy::getMainId, mainId));
        for (FkReimSubsidy subsidy : subsidies) {
            calendarMapper.delete(new LambdaQueryWrapper<FkSubsidyCalendar>().eq(FkSubsidyCalendar::getMainId, subsidy.getId()));
        }
        itineraryMapper.delete(new LambdaQueryWrapper<FkReimItinerary>().eq(FkReimItinerary::getMainId, mainId));
        subsidyMapper.delete(new LambdaQueryWrapper<FkReimSubsidy>().eq(FkReimSubsidy::getMainId, mainId));
        apportionMapper.delete(new LambdaQueryWrapper<FkReimApportion>().eq(FkReimApportion::getMainId, mainId));
    }

    private TravelReimburseDetailVO buildDetail(FkReimMain main) {
        TravelReimburseDetailVO vo = new TravelReimburseDetailVO();
        vo.setId(main.getId());
        vo.setVersion(defaultVersion(main.getVersion()));
        vo.setReimBillNo(main.getReimBillNo());
        vo.setBillDate(defaultText(main.getBillDate(), datePart(main.getCreationTime())));
        vo.setBillStatus(main.getBillStatus());
        vo.setBillStatusName(statusName(main.getBillStatus()));
        vo.setTitle(main.getReimbursementTitle());
        vo.setReason(main.getBusinessTripReason());
        vo.setReimburserId(main.getReimburserId());
        vo.setReimburserNo(main.getReimburserNo());
        vo.setReimburserName(main.getReimburserName());
        vo.setReimDepartmentId(main.getReimDepartmentId());
        vo.setReimDepartmentNo(main.getReimDepartmentNo());
        vo.setReimDepartmentName(main.getReimDepartmentName());
        vo.setReimCompanyId(main.getReimCompanyId());
        vo.setReimCompanyNo(main.getReimCompanyNo());
        vo.setReimCompanyName(main.getReimCompanyName());
        vo.setBusinessTypeId(main.getBusinessTypeId());
        vo.setBusinessTypeNo(main.getBusinessTypeNo());
        vo.setBusinessTypeName(main.getBusinessTypeName());
        vo.setTotalSubsidyAmount(money(main.getSubsidyTotal()));
        vo.setMealSubsidyAmount(money(main.getMealAllowance()));
        vo.setTrafficSubsidyAmount(money(main.getTransportationAllowance()));
        vo.setCommunicationSubsidyAmount(money(main.getPhoneAllowance()));
        vo.setRemark(main.getRemarks());

        List<FkReimItinerary> trips = itineraryMapper.selectList(new LambdaQueryWrapper<FkReimItinerary>().eq(FkReimItinerary::getMainId, main.getId()));
        vo.setTripList(trips.stream().map(this::toManualTripVO).collect(Collectors.toList()));

        List<FkReimSubsidy> subsidies = subsidyMapper.selectList(new LambdaQueryWrapper<FkReimSubsidy>().eq(FkReimSubsidy::getMainId, main.getId()));
        vo.setSubsidyList(subsidies.stream().map(this::toSubsidyInfoVO).collect(Collectors.toList()));

        List<FkReimApportion> shares = apportionMapper.selectList(new LambdaQueryWrapper<FkReimApportion>().eq(FkReimApportion::getMainId, main.getId()).orderByAsc(FkReimApportion::getRowNo));
        vo.setShareList(shares.stream().map(this::toCostShareVO).collect(Collectors.toList()));
        return vo;
    }

    private TravelReimbursePageBean toPageBean(FkReimMain entity) {
        TravelReimbursePageBean bean = new TravelReimbursePageBean();
        bean.setId(entity.getId());
        bean.setVersion(defaultVersion(entity.getVersion()));
        bean.setReimBillNo(entity.getReimBillNo());
        bean.setBillStatus(entity.getBillStatus());
        bean.setBillStatusName(statusName(entity.getBillStatus()));
        bean.setReimburserId(entity.getReimburserId());
        bean.setReimburserNo(entity.getReimburserNo());
        bean.setReimburserName(entity.getReimburserName());
        bean.setReimDepartmentId(entity.getReimDepartmentId());
        bean.setReimDepartmentNo(entity.getReimDepartmentNo());
        bean.setReimDepartmentName(entity.getReimDepartmentName());
        bean.setReimCompanyId(entity.getReimCompanyId());
        bean.setReimCompanyNo(entity.getReimCompanyNo());
        bean.setReimCompanyName(entity.getReimCompanyName());
        bean.setBusinessTypeId(entity.getBusinessTypeId());
        bean.setBusinessTypeName(entity.getBusinessTypeName());
        bean.setTitle(entity.getReimbursementTitle());
        bean.setReason(entity.getBusinessTripReason());
        bean.setSubsidyAmount(money(entity.getSubsidyTotal()));
        bean.setCreateTime(parseDate(entity.getCreationTime()));
        return bean;
    }

    private ManualTripVO toManualTripVO(FkReimItinerary entity) {
        ManualTripVO vo = new ManualTripVO();
        vo.setTripId(entity.getId());
        vo.setTravelerId(entity.getTravelerId());
        vo.setTravelerNo(entity.getTravelerNo());
        vo.setTravelerName(entity.getTravelerName());
        vo.setDepartureDate(entity.getDepartureDate());
        vo.setArrivalDate(entity.getArrivalDate());
        vo.setDepartureCityNo(entity.getDepartureCityNo());
        vo.setDepartureCityName(entity.getDepartureCity());
        vo.setArrivalCityNo(entity.getArrivingCityNo());
        vo.setArrivalCityName(entity.getArrivingCity());
        vo.setTripDesc(entity.getItineraryInstructions());
        return vo;
    }

    private ManualTripVO toManualTripVO(ManualTripDTO dto) {
        ManualTripVO vo = new ManualTripVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private SubsidyInfoVO toSubsidyInfoVO(FkReimSubsidy entity) {
        SubsidyInfoVO vo = new SubsidyInfoVO();
        vo.setSubsidyId(entity.getId());
        vo.setTravelerId(entity.getTravelerId());
        vo.setTravelerNo(entity.getTravelerNo());
        vo.setTravelerName(entity.getTravelerName());
        vo.setStartDate(entity.getDepartureDate());
        vo.setEndDate(entity.getArrivalDate());
        vo.setSubsidyDays(parseInt(entity.getSubsidyDays()));
        vo.setSubsidyCityNo(entity.getArrivingCityNo());
        vo.setSubsidyCityName(entity.getArrivingCity());
        vo.setApplyAmount(money(entity.getApplicationAmount()));
        vo.setSubsidyAmount(money(entity.getSubsidyAmount()));
        vo.setMealSubsidyAmount(money(entity.getMealAllowance()));
        vo.setTrafficSubsidyAmount(money(entity.getTransportationAllowance()));
        vo.setCommunicationSubsidyAmount(money(entity.getPhoneAllowance()));
        List<FkSubsidyCalendar> calendars = calendarMapper.selectList(new LambdaQueryWrapper<FkSubsidyCalendar>().eq(FkSubsidyCalendar::getMainId, entity.getId()).orderByAsc(FkSubsidyCalendar::getTravelDate));
        vo.setCalendarList(calendars.stream().map(this::toSubsidyCalendarVO).collect(Collectors.toList()));
        return vo;
    }

    private SubsidyInfoVO toSubsidyInfoVO(SubsidyInfoDTO dto) {
        SubsidyInfoVO vo = new SubsidyInfoVO();
        BeanUtils.copyProperties(dto, vo);
        List<SubsidyCalendarDTO> calendars = dto.getCalendarList() == null ? new ArrayList<>() : dto.getCalendarList();
        vo.setCalendarList(calendars.stream().map(this::toSubsidyCalendarVO).collect(Collectors.toList()));
        return vo;
    }

    private SubsidyCalendarVO toSubsidyCalendarVO(FkSubsidyCalendar entity) {
        SubsidyCalendarVO vo = new SubsidyCalendarVO();
        vo.setCalendarId(entity.getId());
        vo.setTravelDate(entity.getTravelDate());
        vo.setWeekName(entity.getTravelDateWeek());
        vo.setCityNo(entity.getSubsidizedCityNumber());
        vo.setCityName(entity.getSubsidizedCities());
        vo.setCityType(entity.getCityType());
        vo.setMealChecked(bool(entity.getMealChecked()));
        vo.setMealStandardAmount(money(entity.getStandardMealExpensesAmount()));
        vo.setMealAmount(money(entity.getMealExpensesAmount()));
        vo.setTrafficChecked(bool(entity.getTrafficChecked()));
        vo.setTrafficStandardAmount(money(entity.getStandardTrafficAmount()));
        vo.setTrafficAmount(money(entity.getTrafficAmount()));
        vo.setCommunicationChecked(bool(entity.getCommunicationChecked()));
        vo.setCommunicationStandardAmount(money(entity.getStandardCommunicationAmount()));
        vo.setCommunicationAmount(money(entity.getCommunicationAmount()));
        return vo;
    }

    private SubsidyCalendarVO toSubsidyCalendarVO(SubsidyCalendarDTO dto) {
        SubsidyCalendarVO vo = new SubsidyCalendarVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private CostShareVO toCostShareVO(FkReimApportion entity) {
        CostShareVO vo = new CostShareVO();
        vo.setShareId(entity.getId());
        vo.setLineNo(entity.getRowNo());
        vo.setReimCompanyId(entity.getReimCompanyId());
        vo.setReimCompanyNo(entity.getReimCompanyNo());
        vo.setReimCompanyName(entity.getReimCompanyName());
        vo.setProjectId(entity.getProjectId());
        vo.setProjectNo(entity.getProjectNo());
        vo.setProjectName(entity.getProjectName());
        vo.setShareRatio(toPercent(money4(entity.getApportionRatio())));
        vo.setShareAmount(money(entity.getApportionAmount()));
        return vo;
    }

    private CostShareVO toCostShareVO(CostShareDTO dto) {
        CostShareVO vo = new CostShareVO();
        BeanUtils.copyProperties(dto, vo);
        vo.setShareRatio(toPercent(money4(dto.getShareRatio())));
        return vo;
    }

    private SubsidyInfoDTO buildSubsidyByTrip(ManualTripDTO trip) {
        LocalDate start = parseLocalDate(trip.getDepartureDate(), "departure date is required");
        LocalDate end = parseLocalDate(trip.getArrivalDate(), "arrival date is required");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("arrival date cannot be earlier than departure date");
        }
        if (start.isAfter(LocalDate.now()) || end.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("trip date cannot be later than today");
        }

        SubsidyInfoDTO subsidy = new SubsidyInfoDTO();
        subsidy.setSubsidyId(uuid());
        subsidy.setTripId(trip.getTripId());
        subsidy.setTravelerId(trip.getTravelerId());
        subsidy.setTravelerNo(trip.getTravelerNo());
        subsidy.setTravelerName(trip.getTravelerName());
        subsidy.setStartDate(trip.getDepartureDate());
        subsidy.setEndDate(trip.getArrivalDate());
        subsidy.setRoute(defaultText(trip.getDepartureCityName(), "") + "-" + defaultText(trip.getArrivalCityName(), ""));
        subsidy.setSubsidyCityNo(trip.getArrivalCityNo());
        subsidy.setSubsidyCityName(trip.getArrivalCityName());
        List<SubsidyCalendarDTO> calendars = new ArrayList<>();
        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            SubsidyCalendarDTO calendar = new SubsidyCalendarDTO();
            calendar.setCalendarId(uuid());
            calendar.setTravelDate(day.format(DATE));
            calendar.setWeekName(weekName(day.getDayOfWeek()));
            calendar.setCityNo(trip.getArrivalCityNo());
            calendar.setCityName(trip.getArrivalCityName());
            calendar.setCityType("1");
            calendar.setMealChecked(true);
            calendar.setMealStandardAmount(new BigDecimal("100.00"));
            calendar.setMealAmount(new BigDecimal("100.00"));
            calendar.setTrafficChecked(true);
            calendar.setTrafficStandardAmount(new BigDecimal("40.00"));
            calendar.setTrafficAmount(new BigDecimal("40.00"));
            calendar.setCommunicationChecked(true);
            calendar.setCommunicationStandardAmount(new BigDecimal("40.00"));
            calendar.setCommunicationAmount(new BigDecimal("40.00"));
            calendars.add(calendar);
        }
        subsidy.setCalendarList(calendars);
        fillSubsidyAmounts(subsidy);
        return subsidy;
    }

    private void fillSubsidyAmounts(SubsidyInfoDTO subsidy) {
        List<SubsidyCalendarDTO> calendars = subsidy.getCalendarList() == null ? new ArrayList<>() : subsidy.getCalendarList();
        subsidy.setSubsidyDays(calendars.size());
        BigDecimal apply = BigDecimal.ZERO;
        BigDecimal actual = BigDecimal.ZERO;
        BigDecimal meal = BigDecimal.ZERO;
        BigDecimal traffic = BigDecimal.ZERO;
        BigDecimal communication = BigDecimal.ZERO;
        for (SubsidyCalendarDTO calendar : calendars) {
            if (Boolean.TRUE.equals(calendar.getMealChecked())) {
                apply = apply.add(money(calendar.getMealStandardAmount()));
                actual = actual.add(money(calendar.getMealAmount()));
                meal = meal.add(money(calendar.getMealAmount()));
            }
            if (Boolean.TRUE.equals(calendar.getTrafficChecked())) {
                apply = apply.add(money(calendar.getTrafficStandardAmount()));
                actual = actual.add(money(calendar.getTrafficAmount()));
                traffic = traffic.add(money(calendar.getTrafficAmount()));
            }
            if (Boolean.TRUE.equals(calendar.getCommunicationChecked())) {
                apply = apply.add(money(calendar.getCommunicationStandardAmount()));
                actual = actual.add(money(calendar.getCommunicationAmount()));
                communication = communication.add(money(calendar.getCommunicationAmount()));
            }
        }
        subsidy.setApplyAmount(money(apply));
        subsidy.setSubsidyAmount(money(actual));
        subsidy.setMealSubsidyAmount(money(meal));
        subsidy.setTrafficSubsidyAmount(money(traffic));
        subsidy.setCommunicationSubsidyAmount(money(communication));
    }

    private AmountSummary summarize(List<SubsidyInfoDTO> subsidies) {
        AmountSummary summary = new AmountSummary();
        if (subsidies == null) {
            return summary;
        }
        for (SubsidyInfoDTO subsidy : subsidies) {
            fillSubsidyAmounts(subsidy);
            summary.meal = summary.meal.add(money(subsidy.getMealSubsidyAmount()));
            summary.traffic = summary.traffic.add(money(subsidy.getTrafficSubsidyAmount()));
            summary.communication = summary.communication.add(money(subsidy.getCommunicationSubsidyAmount()));
            summary.total = summary.total.add(money(subsidy.getSubsidyAmount()));
        }
        summary.normalize();
        return summary;
    }

    private void validateSubmit(TravelReimburseDetailSaveDTO data) {
        List<String> errors = new ArrayList<>();
        require(errors, data.getTitle(), "title is required");
        require(errors, data.getReason(), "reason is required");
        require(errors, data.getReimburserId(), "reimburser is required");
        require(errors, data.getReimDepartmentId(), "department is required");
        require(errors, data.getReimCompanyId(), "company is required");
        require(errors, data.getBusinessTypeId(), "business type is required");
        if (data.getTripList() == null || data.getTripList().isEmpty()) {
            errors.add("trip list is required");
        }
        if (StringUtils.hasText(data.getTitle()) && data.getTitle().length() > 500) {
            errors.add("title max length is 500");
        }
        if (StringUtils.hasText(data.getReason()) && data.getReason().length() > 500) {
            errors.add("reason max length is 500");
        }
        if (StringUtils.hasText(data.getRemark()) && data.getRemark().length() > 1000) {
            errors.add("remark max length is 1000");
        }
        validateTripDuplicate(data.getTripList());
        validateCalendar(flatCalendars(data.getSubsidyList()));
        fillShareDefaults(data);
        normalizeShareRatios(data.getShareList());
        validateShare(data.getShareList(), summarize(data.getSubsidyList()).total);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }

    private void validateTripDuplicate(List<ManualTripDTO> trips) {
        if (trips == null) {
            return;
        }
        for (ManualTripDTO trip : trips) {
            LocalDate start = parseLocalDate(trip.getDepartureDate(), "departure date is required");
            LocalDate end = parseLocalDate(trip.getArrivalDate(), "arrival date is required");
            if (end.isBefore(start)) {
                throw new IllegalArgumentException("arrival date cannot be earlier than departure date");
            }
            if (start.isAfter(LocalDate.now()) || end.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("trip date cannot be later than today");
            }
        }
        for (int i = 0; i < trips.size(); i++) {
            for (int j = i + 1; j < trips.size(); j++) {
                ManualTripDTO a = trips.get(i);
                ManualTripDTO b = trips.get(j);
                if (!StringUtils.hasText(a.getTravelerId()) || !a.getTravelerId().equals(b.getTravelerId())) {
                    continue;
                }
                LocalDate aStart = parseLocalDate(a.getDepartureDate(), "departure date is required");
                LocalDate aEnd = parseLocalDate(a.getArrivalDate(), "arrival date is required");
                LocalDate bStart = parseLocalDate(b.getDepartureDate(), "departure date is required");
                LocalDate bEnd = parseLocalDate(b.getArrivalDate(), "arrival date is required");
                if (!aStart.isAfter(bEnd) && !aEnd.isBefore(bStart)) {
                    throw new IllegalArgumentException("same traveler trip dates cannot overlap");
                }
            }
        }
    }

    private void validateCalendar(List<SubsidyCalendarDTO> calendars) {
        for (SubsidyCalendarDTO calendar : calendars) {
            if (money(calendar.getMealAmount()).compareTo(BigDecimal.ZERO) < 0
                    || money(calendar.getTrafficAmount()).compareTo(BigDecimal.ZERO) < 0
                    || money(calendar.getCommunicationAmount()).compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("subsidy amount cannot be negative");
            }
            if (money(calendar.getMealAmount()).compareTo(money(calendar.getMealStandardAmount())) > 0) {
                throw new IllegalArgumentException("meal amount cannot exceed standard amount");
            }
            if (money(calendar.getTrafficAmount()).compareTo(money(calendar.getTrafficStandardAmount())) > 0) {
                throw new IllegalArgumentException("traffic amount cannot exceed standard amount");
            }
            if (money(calendar.getCommunicationAmount()).compareTo(money(calendar.getCommunicationStandardAmount())) > 0) {
                throw new IllegalArgumentException("communication amount cannot exceed standard amount");
            }
        }
    }

    private void validateShare(List<CostShareDTO> shares, BigDecimal total) {
        if (shares == null || shares.isEmpty()) {
            throw new IllegalArgumentException("at least one share row is required");
        }
        BigDecimal ratioSum = BigDecimal.ZERO;
        BigDecimal amountSum = BigDecimal.ZERO;
        for (CostShareDTO share : shares) {
            requireShare(share.getReimCompanyId(), "share company is required");
            requireShare(share.getProjectId(), "project is required");
            ratioSum = ratioSum.add(money4(share.getShareRatio()));
            amountSum = amountSum.add(money(share.getShareAmount()));
        }
        if (ratioSum.compareTo(BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP)) != 0) {
            throw new IllegalArgumentException("share ratio total must be 100%");
        }
        if (amountSum.compareTo(money(total)) != 0) {
            throw new IllegalArgumentException("share amount total must equal subsidy total");
        }
    }

    private void recalculateShare(List<CostShareDTO> rows, BigDecimal total, boolean average) {
        if (rows == null || rows.isEmpty()) {
            return;
        }
        if (average) {
            BigDecimal ratio = BigDecimal.ONE.divide(BigDecimal.valueOf(rows.size()), 4, RoundingMode.DOWN);
            BigDecimal usedRatio = BigDecimal.ZERO;
            BigDecimal usedAmount = BigDecimal.ZERO;
            for (int i = 1; i < rows.size(); i++) {
                rows.get(i).setShareRatio(ratio);
                rows.get(i).setShareAmount(money(total.multiply(ratio)));
                usedRatio = usedRatio.add(ratio);
                usedAmount = usedAmount.add(rows.get(i).getShareAmount());
            }
            rows.get(0).setShareRatio(BigDecimal.ONE.subtract(usedRatio).setScale(4, RoundingMode.HALF_UP));
            rows.get(0).setShareAmount(money(total.subtract(usedAmount)));
        } else {
            BigDecimal usedRatio = BigDecimal.ZERO;
            BigDecimal usedAmount = BigDecimal.ZERO;
            for (int i = 1; i < rows.size(); i++) {
                CostShareDTO row = rows.get(i);
                row.setShareRatio(money4(row.getShareRatio()));
                row.setShareAmount(money(total.multiply(row.getShareRatio())));
                usedRatio = usedRatio.add(row.getShareRatio());
                usedAmount = usedAmount.add(row.getShareAmount());
            }
            if (usedRatio.compareTo(BigDecimal.ONE) > 0) {
                throw new IllegalArgumentException("share ratio after first row cannot exceed 100%");
            }
            rows.get(0).setShareRatio(BigDecimal.ONE.subtract(usedRatio).setScale(4, RoundingMode.HALF_UP));
            rows.get(0).setShareAmount(money(total.subtract(usedAmount)));
        }
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).setLineNo(i + 1);
        }
    }

    private void normalizeShareRatios(List<CostShareDTO> shares) {
        if (shares == null) {
            return;
        }
        for (CostShareDTO share : shares) {
            if (share.getShareRatio() != null && share.getShareRatio().compareTo(BigDecimal.ONE) > 0) {
                share.setShareRatio(share.getShareRatio().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            }
        }
    }

    private void fillShareDefaults(TravelReimburseDetailSaveDTO data) {
        if (data == null || data.getShareList() == null) {
            return;
        }
        for (CostShareDTO share : data.getShareList()) {
            if (!StringUtils.hasText(share.getReimCompanyId())) {
                share.setReimCompanyId(data.getReimCompanyId());
                share.setReimCompanyNo(data.getReimCompanyNo());
                share.setReimCompanyName(data.getReimCompanyName());
            }
        }
    }

    private BigDecimal toPercent(BigDecimal ratio) {
        return money4(ratio).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    private CalculateCostShareVO costShareVO(List<CostShareDTO> shares, BigDecimal total) {
        CalculateCostShareVO vo = new CalculateCostShareVO();
        vo.setShareList(shares.stream().map(this::toCostShareVO).collect(Collectors.toList()));
        vo.setTotalShareRatio(toPercent(shares.stream().map(CostShareDTO::getShareRatio).map(this::money4).reduce(BigDecimal.ZERO, BigDecimal::add)));
        vo.setTotalShareAmount(shares.stream().map(CostShareDTO::getShareAmount).map(this::money).reduce(BigDecimal.ZERO, BigDecimal::add));
        if (vo.getTotalShareAmount().compareTo(BigDecimal.ZERO) == 0 && total.compareTo(BigDecimal.ZERO) > 0) {
            vo.setTotalShareAmount(total);
        }
        return vo;
    }

    private List<SubsidyCalendarDTO> flatCalendars(List<SubsidyInfoDTO> subsidies) {
        if (subsidies == null) {
            return new ArrayList<>();
        }
        return subsidies.stream()
                .filter(item -> item.getCalendarList() != null)
                .flatMap(item -> item.getCalendarList().stream())
                .collect(Collectors.toList());
    }

    private FkReimMain mustGetMain(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("reimburse id is required");
        }
        FkReimMain main = mainMapper.selectById(id);
        if (main == null) {
            throw new IllegalArgumentException("reimburse bill not found");
        }
        return main;
    }

    private void ensureDraftMain(TravelReimburseDetailSaveDTO data) {
        if (mainMapper.selectById(data.getId()) != null) {
            return;
        }
        FkReimMain main = new FkReimMain();
        main.setId(data.getId());
        main.setBillDate(defaultText(data.getBillDate(), LocalDate.now().format(DATE)));
        main.setBillStatus("0");
        main.setCreationTime(LocalDateTime.now().format(DATE_TIME));
        main.setSubsidyTotal("0.00");
        main.setMealAllowance("0.00");
        main.setTransportationAllowance("0.00");
        main.setPhoneAllowance("0.00");
        main.setVersion(0);
        mainMapper.insert(main);
        data.setVersion(0);
    }

    private void checkOptimisticVersion(Integer requestVersion, FkReimMain main) {
        if (requestVersion == null) {
            throw new IllegalArgumentException("version is required, please refresh the bill and retry");
        }
        if (!requestVersion.equals(defaultVersion(main.getVersion()))) {
            throw new IllegalArgumentException("bill has been modified by another user, please refresh and retry");
        }
        main.setVersion(requestVersion);
    }

    private void assertOptimisticUpdated(int updated) {
        if (updated <= 0) {
            throw new IllegalArgumentException("bill has been modified by another user, please refresh and retry");
        }
    }

    private CostShareDTO defaultShare(int lineNo, BigDecimal ratio, BigDecimal amount) {
        CostShareDTO dto = new CostShareDTO();
        dto.setShareId(uuid());
        dto.setLineNo(lineNo);
        dto.setReimCompanyId("C001");
        dto.setReimCompanyNo("COMP001");
        dto.setReimCompanyName("Demo Technology Co., Ltd.");
        dto.setProjectId("P001");
        dto.setProjectNo("PROJ001");
        dto.setProjectName("Travel Project");
        dto.setShareRatio(money4(ratio));
        dto.setShareAmount(money(amount));
        return dto;
    }

    private BigDecimal money(String value) {
        if (!StringUtils.hasText(value)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        try {
            return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal money4(String value) {
        if (!StringUtils.hasText(value)) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }
        try {
            return new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal money4(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(4, RoundingMode.HALF_UP);
    }

    private Date parseDate(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        for (String pattern : Arrays.asList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd")) {
            try {
                return new SimpleDateFormat(pattern).parse(value);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    private LocalDate parseLocalDate(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return LocalDate.parse(value, DATE);
    }

    private String statusName(String status) {
        if ("1".equals(status)) {
            return "Completed";
        }
        if ("2".equals(status)) {
            return "Cancelled";
        }
        return "Draft";
    }

    private String weekName(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.MONDAY) {
            return "Monday";
        }
        if (dayOfWeek == DayOfWeek.TUESDAY) {
            return "Tuesday";
        }
        if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            return "Wednesday";
        }
        if (dayOfWeek == DayOfWeek.THURSDAY) {
            return "Thursday";
        }
        if (dayOfWeek == DayOfWeek.FRIDAY) {
            return "Friday";
        }
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return "Saturday";
        }
        return "Sunday";
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String defaultText(String value, String defaultValue) {
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    private String datePart(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.length() >= 10 ? value.substring(0, 10) : value;
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private Integer defaultVersion(Integer value) {
        return value == null ? 0 : value;
    }

    private int parseInt(String value) {
        if (!StringUtils.hasText(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean bool(String value) {
        return "1".equals(value) || "true".equalsIgnoreCase(value);
    }

    private String boolText(Boolean value) {
        return Boolean.TRUE.equals(value) ? "1" : "0";
    }

    private void require(List<String> errors, String value, String message) {
        if (!StringUtils.hasText(value)) {
            errors.add(message);
        }
    }

    private void requireShare(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    private static class AmountSummary {
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal meal = BigDecimal.ZERO;
        private BigDecimal traffic = BigDecimal.ZERO;
        private BigDecimal communication = BigDecimal.ZERO;

        private void normalize() {
            total = total.setScale(2, RoundingMode.HALF_UP);
            meal = meal.setScale(2, RoundingMode.HALF_UP);
            traffic = traffic.setScale(2, RoundingMode.HALF_UP);
            communication = communication.setScale(2, RoundingMode.HALF_UP);
        }
    }
}
