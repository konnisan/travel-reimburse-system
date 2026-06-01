import request from '@/request'

export interface QueryTravelReimbursePageListData {
  reimBillNo?: string
  title?: string
  reason?: string
  reimCompanyId?: string
  reimDepartmentId?: string
  reimburserId?: string
  businessTypeId?: string
  billStatus?: string
}

export interface TravelReimbursePageRow {
  id: string
  version: number
  reimBillNo: string
  billStatus: string
  billStatusName: string
  reimburserId: string
  reimburserNo: string
  reimburserName: string
  reimDepartmentId: string
  reimDepartmentNo: string
  reimDepartmentName: string
  reimCompanyId: string
  reimCompanyNo: string
  reimCompanyName: string
  businessTypeId: string
  businessTypeName: string
  title: string
  reason: string
  subsidyAmount: number
  createTime: string
}

export interface ManualTrip {
  tripId?: string
  travelerId: string
  travelerNo?: string
  travelerName: string
  departureCityNo: string
  departureCityName: string
  arrivalCityNo: string
  arrivalCityName: string
  departureDate: string
  arrivalDate: string
  tripDesc?: string
}

export interface SubsidyCalendar {
  calendarId?: string
  travelDate: string
  weekName?: string
  cityNo: string
  cityName: string
  cityType?: string
  mealChecked: boolean
  mealStandardAmount: number
  mealAmount: number
  trafficChecked: boolean
  trafficStandardAmount: number
  trafficAmount: number
  communicationChecked: boolean
  communicationStandardAmount: number
  communicationAmount: number
}

export interface SubsidyInfo {
  subsidyId?: string
  tripId?: string
  travelerId: string
  travelerNo?: string
  travelerName: string
  startDate: string
  endDate: string
  subsidyDays: number
  route: string
  subsidyCityNo: string
  subsidyCityName: string
  applyAmount: number
  subsidyAmount: number
  mealSubsidyAmount: number
  trafficSubsidyAmount: number
  communicationSubsidyAmount: number
  calendarList: SubsidyCalendar[]
}

export interface CostShare {
  shareId?: string
  lineNo?: number
  reimCompanyId?: string
  reimCompanyNo?: string
  reimCompanyName: string
  projectId?: string
  projectNo?: string
  projectName: string
  shareRatio: number
  shareAmount: number
}

export interface CalculateCostShareData {
  reimburseId?: string
  operateType: 'add' | 'delete' | 'average' | 'recalculate'
  totalShareAmount: number
  shareList: CostShare[]
}

export interface CalculateCostShareResult {
  valid: boolean
  message: string
  shareList: CostShare[]
  totalShareRatio: number
  totalShareAmount: number
}

export interface TravelReimburseDetail {
  id: string
  version?: number
  reimBillNo?: string
  billDate: string
  billStatus?: string
  billStatusName?: string
  title: string
  reason: string
  reimburserId: string
  reimburserNo?: string
  reimburserName?: string
  reimDepartmentId: string
  reimDepartmentNo?: string
  reimDepartmentName?: string
  reimCompanyId: string
  reimCompanyNo?: string
  reimCompanyName?: string
  businessTypeId: string
  businessTypeNo?: string
  businessTypeName?: string
  totalSubsidyAmount?: number
  remark?: string
  tripList: ManualTrip[]
  subsidyList: SubsidyInfo[]
  shareList: CostShare[]
}

export interface PageResult<T> {
  total: number
  pages: number
  current: number
  size: number
  records: T[]
}

export interface CreateTravelReimburseDraftResult {
  id: string
  version: number
  reimBillNo: string
  billDate: string
  billStatus: string
  billStatusName: string
  redirectUrl: string
}

export interface InvalidTravelReimburseResult {
  id: string
  version: number
  billStatus: string
  billStatusName: string
  message: string
}

export interface SaveTravelReimburseDetailResult {
  valid: boolean
  message: string
  detail: TravelReimburseDetail
}

export interface SubmitTravelReimburseResult {
  valid: boolean
  message: string
  id: string
  version: number
  reimBillNo: string
  billStatus: string
  billStatusName: string
}

export interface TravelReimburseBaseData {
  reimCompanyList: unknown[]
  departmentList: unknown[]
  employeeList: unknown[]
  businessTypeList: unknown[]
  cityList: unknown[]
  projectList: unknown[]
}

const basePath = '/b2c/reimburse/fcecf/fccapi'

export function queryTravelReimbursePageList(
  current: number,
  size: number,
  data: QueryTravelReimbursePageListData
) {
  return request.post<PageResult<TravelReimbursePageRow>>(
    `${basePath}/COMM_REIMBURSE_QueryTravelReimbursePageList`,
    { current, size, data },
    { loading: true }
  )
}

export function createTravelReimburseDraft(billDate?: string) {
  return request.post<CreateTravelReimburseDraftResult>(
    `${basePath}/COMM_REIMBURSE_CreateTravelReimburseDraft`,
    { data: billDate ? { billDate } : undefined },
    { loading: true }
  )
}

export function queryTravelReimburseDetail(id: string) {
  return request.post<TravelReimburseDetail>(
    `${basePath}/COMM_REIMBURSE_QueryTravelReimburseDetail`,
    { data: { id } },
    { loading: true }
  )
}

export function queryTravelReimburseBaseData() {
  return request.post<TravelReimburseBaseData>(
    `${basePath}/COMM_REIMBURSE_QueryTravelReimburseBaseData`,
    {},
    { loading: false }
  )
}

export function saveTravelReimburseDraft(data: TravelReimburseDetail) {
  return request.post<SaveTravelReimburseDetailResult>(
    `${basePath}/COMM_REIMBURSE_SaveTravelReimburseDraft`,
    { data },
    { loading: true }
  )
}

export function submitTravelReimburse(data: TravelReimburseDetail) {
  return request.post<SubmitTravelReimburseResult>(
    `${basePath}/COMM_REIMBURSE_SubmitTravelReimburse`,
    { data },
    { loading: true }
  )
}

export function invalidTravelReimburse(id: string, version?: number, invalidReason?: string) {
  return request.post<InvalidTravelReimburseResult>(
    `${basePath}/COMM_REIMBURSE_InvalidTravelReimburse`,
    { data: { id, version, invalidReason } },
    { loading: true }
  )
}

export function calculateCostShare(data: CalculateCostShareData) {
  return request.post<CalculateCostShareResult>(
    `${basePath}/COMM_REIMBURSE_CalculateCostShare`,
    { data },
    { loading: true }
  )
}
