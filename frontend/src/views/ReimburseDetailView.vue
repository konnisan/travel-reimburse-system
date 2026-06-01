<template>
  <main class="page">
    <section class="detail-page">
      <div class="bill-shell">
          <header class="bill-header">
            <div class="bill-left">
              <el-button plain :icon="ArrowLeft" @click="handleCloseDetail">返回</el-button>
              <span>{{ detailForm.reimBillNo || '提交后生成单号' }}</span>
            </div>
            <h2>差旅报销单</h2>
            <div class="bill-date">日期：{{ detailForm.billDate || today }}</div>
          </header>

          <el-form
            ref="detailFormRef"
            :model="detailForm"
            :rules="detailRules"
            label-width="112px"
            class="bill-form"
            :disabled="isReadonly"
          >
            <section class="bill-section">
              <button class="section-title" type="button" @click="toggleSection('basic')">
                <el-icon><component :is="sectionOpen.basic ? ArrowDown : ArrowRight" /></el-icon>
                <span>基础信息</span>
                <el-tag :type="getBillStatusTag(detailForm.billStatus)" effect="light">{{ detailForm.billStatusName || '草稿' }}</el-tag>
              </button>
              <div v-show="sectionOpen.basic" class="section-body">
                <el-row :gutter="18">
                  <el-col :span="8">
                    <el-form-item label="报销标题" prop="title">
                      <el-input v-model="detailForm.title" maxlength="500" show-word-limit placeholder="请输入" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="报销人" prop="reimburserId">
                      <el-select v-model="detailForm.reimburserId" filterable placeholder="请选择">
                        <el-option v-for="employee in employeeOptions" :key="employee.id" :label="employee.name" :value="employee.id" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="报销部门" prop="reimDepartmentId">
                      <el-select v-model="detailForm.reimDepartmentId" filterable placeholder="请选择">
                        <el-option v-for="department in departmentOptions" :key="department.id" :label="department.name" :value="department.id" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="18">
                  <el-col :span="8">
                    <el-form-item label="费用归属公司" prop="reimCompanyId">
                      <el-select v-model="detailForm.reimCompanyId" filterable placeholder="请选择">
                        <el-option v-for="company in companyOptions" :key="company.id" :label="company.name" :value="company.id" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="业务类型" prop="businessTypeId">
                      <el-tree-select v-model="detailForm.businessTypeId" :data="businessTypeOptions" placeholder="请选择" check-strictly />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="单据日期" prop="billDate">
                      <el-date-picker v-model="detailForm.billDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="出差事由" prop="reason">
                  <el-input v-model="detailForm.reason" type="textarea" :rows="3" maxlength="500" show-word-limit placeholder="请输入" />
                </el-form-item>
              </div>
            </section>

            <section class="bill-section">
              <button class="section-title" type="button" @click="toggleSection('trip')">
                <el-icon><component :is="sectionOpen.trip ? ArrowDown : ArrowRight" /></el-icon>
                <span>补录行程</span>
                <el-button type="primary" plain :icon="Plus" :disabled="isReadonly" @click.stop="openTripDialog('add')">补录行程</el-button>
              </button>
              <div v-show="sectionOpen.trip" class="section-body">
                <el-table :data="tripList" border size="small" empty-text="暂无补录行程">
                  <el-table-column prop="travelerName" label="出行人员" width="120" />
                  <el-table-column label="出差日期" width="210">
                    <template #default="{ row }">{{ row.departDate }} 至 {{ row.arriveDate }}</template>
                  </el-table-column>
                  <el-table-column label="行程" min-width="180">
                    <template #default="{ row }">{{ row.departCityName }} - {{ row.arriveCityName }}</template>
                  </el-table-column>
                  <el-table-column prop="remark" label="行程说明" min-width="220" show-overflow-tooltip />
                  <el-table-column label="操作" width="168" align="center">
                    <template #default="{ row, $index }">
                      <el-button link type="primary" :icon="Edit" :disabled="isReadonly" @click="openTripDialog('edit', row, $index)">编辑</el-button>
                      <el-button link type="primary" :icon="DocumentCopy" :disabled="isReadonly" @click="openTripDialog('copy', row)">复制</el-button>
                      <el-button link type="danger" :icon="Delete" :disabled="isReadonly" @click="handleDeleteTrip($index)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </section>

            <section class="bill-section">
              <button class="section-title" type="button" @click="toggleSection('subsidy')">
                <el-icon><component :is="sectionOpen.subsidy ? ArrowDown : ArrowRight" /></el-icon>
                <span>补助信息</span>
                <el-tooltip :content="subsidyNotice" placement="top">
                  <span class="section-tip">{{ subsidyNotice }}</span>
                </el-tooltip>
              </button>
              <div v-show="sectionOpen.subsidy" class="section-body">
                <el-table :data="subsidyList" border size="small" empty-text="暂无补助信息">
                  <el-table-column prop="travelerName" label="出行人" width="120" />
                  <el-table-column label="出差日期" width="210">
                    <template #default="{ row }">{{ row.startDate }} 至 {{ row.endDate }}</template>
                  </el-table-column>
                  <el-table-column prop="subsidyDays" label="补助天数" width="96" align="right" />
                  <el-table-column prop="route" label="行程" min-width="160" />
                  <el-table-column prop="subsidyCityName" label="补助城市" width="110" />
                  <el-table-column prop="applyAmount" label="申请金额" width="110" align="right">
                    <template #default="{ row }">{{ formatMoney(row.applyAmount) }}</template>
                  </el-table-column>
                  <el-table-column prop="subsidyAmount" label="补助金额" width="110" align="right">
                    <template #default="{ row }">{{ formatMoney(row.subsidyAmount) }}</template>
                  </el-table-column>
                  <el-table-column label="操作" width="90" align="center">
                    <template #default="{ $index }">
                      <el-button link type="primary" :icon="Calendar" @click="openSubsidyDialog($index)">编辑</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </section>

            <section class="bill-section">
              <button class="section-title" type="button" @click="toggleSection('expense')">
                <el-icon><component :is="sectionOpen.expense ? ArrowDown : ArrowRight" /></el-icon>
                <span>费用合计</span>
              </button>
              <div v-show="sectionOpen.expense" class="section-body">
                <el-descriptions :column="4" border>
                  <el-descriptions-item label="补助总金额">{{ formatMoney(expenseSummary.subsidyAmount) }}</el-descriptions-item>
                  <el-descriptions-item label="餐费补助">{{ formatMoney(expenseSummary.mealAmount) }}</el-descriptions-item>
                  <el-descriptions-item label="交通补助">{{ formatMoney(expenseSummary.trafficAmount) }}</el-descriptions-item>
                  <el-descriptions-item label="通讯补助">{{ formatMoney(expenseSummary.communicationAmount) }}</el-descriptions-item>
                </el-descriptions>
              </div>
            </section>

            <section class="bill-section">
              <button class="section-title" type="button" @click="toggleSection('remark')">
                <el-icon><component :is="sectionOpen.remark ? ArrowDown : ArrowRight" /></el-icon>
                <span>备注信息</span>
                <el-button plain type="danger" :icon="Delete" :disabled="isReadonly || !detailForm.remark" @click.stop="handleClearRemark">删除备注</el-button>
              </button>
              <div v-show="sectionOpen.remark" class="section-body">
                <el-input v-model="detailForm.remark" type="textarea" :rows="4" maxlength="1000" show-word-limit placeholder="请输入备注" />
              </div>
            </section>
          </el-form>
      </div>
      <div class="page-footer">
        <el-button @click="handleCloseDetail">关闭</el-button>
        <el-button v-if="detailMode !== 'view' && can('reimburse:save')" :loading="saving" @click="handleSaveDraft">保存草稿</el-button>
        <el-button
          v-if="detailMode !== 'view' && can('reimburse:submit')"
          type="primary"
          :loading="saving"
          @click="handleSubmit"
        >
          提交
        </el-button>
      </div>
    </section>

    <el-dialog v-model="tripDialogVisible" :title="tripDialogTitle" width="760px">
      <el-alert
        class="trip-alert"
        type="info"
        show-icon
        :closable="false"
        title="仅可补录未从申请单带入或未产生费用的行程信息。跨天跨城行程：武汉-北京，1号-5号，1号至5号补助按北京匹配。"
      />
      <el-form ref="tripFormRef" :model="tripForm" :rules="tripRules" label-width="92px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出行人" prop="travelerId">
              <el-select v-model="tripForm.travelerId" filterable placeholder="请选择">
                <el-option v-for="employee in employeeOptions" :key="employee.id" :label="employee.name" :value="employee.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出发城市" prop="departCityNo">
              <el-select v-model="tripForm.departCityNo" filterable placeholder="请选择">
                <el-option v-for="city in cityOptions" :key="city.no" :label="city.name" :value="city.no" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="到达城市" prop="arriveCityNo">
              <el-select v-model="tripForm.arriveCityNo" filterable placeholder="请选择">
                <el-option v-for="city in cityOptions" :key="city.no" :label="city.name" :value="city.no" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出发日期" prop="departDate">
              <el-date-picker v-model="tripForm.departDate" type="date" value-format="YYYY-MM-DD" :disabled-date="disableFutureDate" placeholder="请选择" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="到达日期" prop="arriveDate">
              <el-date-picker v-model="tripForm.arriveDate" type="date" value-format="YYYY-MM-DD" :disabled-date="disableArrivalDate" placeholder="请选择" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="行程说明" prop="remark">
          <el-input v-model="tripForm.remark" type="textarea" maxlength="500" show-word-limit placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="tripDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTrip">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="subsidyDialogVisible" title="补助日历" width="1080px" class="subsidy-dialog">
      <template v-if="editingSubsidy">
        <div class="subsidy-calendar-layout">
          <aside class="subsidy-sidebar">
            <div class="trip-type-row">
              <span>出差类型</span>
              <strong>{{ businessTypeName || '-' }}</strong>
            </div>
            <div class="trip-range">
              <span>开始日期</span>
              <div class="range-line">
                <i />
                <span>{{ editingSubsidy.startDate }}</span>
              </div>
              <el-button type="primary" class="route-chip">{{ editingSubsidy.route }} {{ editingSubsidy.subsidyDays }}天</el-button>
              <span>结束日期</span>
              <div class="range-line">
                <i />
                <span>{{ editingSubsidy.endDate }}</span>
              </div>
            </div>
            <div class="subsidy-total-box">
              <div>
                <span>补助总额</span>
                <strong>CNY {{ formatMoney(editingSubsidy.subsidyAmount) }}</strong>
              </div>
              <div>
                <span>标准总额</span>
                <strong>CNY {{ formatMoney(editingSubsidy.applyAmount) }}</strong>
              </div>
              <div>
                <span>补助金额</span>
                <strong>CNY {{ formatMoney(editingSubsidy.subsidyAmount) }}</strong>
              </div>
            </div>
          </aside>

          <section class="calendar-panel">
            <div class="calendar-panel-head">
              <h3>补助日历</h3>
              <el-checkbox
                :model-value="isAllCalendarChecked(editingSubsidy)"
                :indeterminate="isAllCalendarIndeterminate(editingSubsidy)"
                :disabled="isReadonly"
                @change="toggleAllCalendar(Boolean($event))"
              >
                全选
              </el-checkbox>
            </div>
            <div class="subsidy-calendar-table">
              <div class="calendar-grid calendar-grid-head">
                <div>出差日期</div>
                <div>补助地点</div>
                <div>
                  <el-checkbox
                    :model-value="isCalendarColumnChecked('meal')"
                    :indeterminate="isCalendarColumnIndeterminate('meal')"
                    :disabled="isReadonly"
                    @change="toggleCalendarColumn('meal', Boolean($event))"
                  >
                    餐费补助
                  </el-checkbox>
                </div>
                <div>
                  <el-checkbox
                    :model-value="isCalendarColumnChecked('traffic')"
                    :indeterminate="isCalendarColumnIndeterminate('traffic')"
                    :disabled="isReadonly"
                    @change="toggleCalendarColumn('traffic', Boolean($event))"
                  >
                    交通补助
                  </el-checkbox>
                </div>
                <div>
                  <el-checkbox
                    :model-value="isCalendarColumnChecked('communication')"
                    :indeterminate="isCalendarColumnIndeterminate('communication')"
                    :disabled="isReadonly"
                    @change="toggleCalendarColumn('communication', Boolean($event))"
                  >
                    通讯补助
                  </el-checkbox>
                </div>
              </div>
              <div v-for="row in editingSubsidy.calendarList" :key="row.calendarId" class="calendar-grid calendar-grid-row">
                <div class="date-cell">
                  <strong>{{ row.travelDate }}</strong>
                  <span>{{ row.weekName }}</span>
                  <el-checkbox
                    :model-value="isCalendarRowChecked(row)"
                    :indeterminate="isCalendarRowIndeterminate(row)"
                    :disabled="isReadonly"
                    @change="toggleCalendarRow(row, Boolean($event))"
                  />
                </div>
                <div class="city-cell">{{ row.cityName }}</div>
                <div class="subsidy-amount-cell">
                  <span>CNY {{ formatMoney(row.mealStandardAmount) }} / 天</span>
                  <div>
                    <el-checkbox v-model="row.mealChecked" :disabled="isReadonly" @change="normalizeCalendarAmount(row, 'meal')" />
                    <el-input-number
                      v-model="row.mealAmount"
                      controls-position="right"
                      :controls="false"
                      :min="0"
                      :max="row.mealStandardAmount"
                      :precision="2"
                      :disabled="isReadonly || !row.mealChecked"
                      @change="normalizeCalendarAmount(row, 'meal')"
                    />
                  </div>
                </div>
                <div class="subsidy-amount-cell">
                  <span>CNY {{ formatMoney(row.trafficStandardAmount) }} / 天</span>
                  <div>
                    <el-checkbox v-model="row.trafficChecked" :disabled="isReadonly" @change="normalizeCalendarAmount(row, 'traffic')" />
                    <el-input-number
                      v-model="row.trafficAmount"
                      :controls="false"
                      :min="0"
                      :max="row.trafficStandardAmount"
                      :precision="2"
                      :disabled="isReadonly || !row.trafficChecked"
                      @change="normalizeCalendarAmount(row, 'traffic')"
                    />
                  </div>
                </div>
                <div class="subsidy-amount-cell">
                  <span>CNY {{ formatMoney(row.communicationStandardAmount) }} / 天</span>
                  <div>
                    <el-checkbox v-model="row.communicationChecked" :disabled="isReadonly" @change="normalizeCalendarAmount(row, 'communication')" />
                    <el-input-number
                      v-model="row.communicationAmount"
                      :controls="false"
                      :min="0"
                      :max="row.communicationStandardAmount"
                      :precision="2"
                      :disabled="isReadonly || !row.communicationChecked"
                      @change="normalizeCalendarAmount(row, 'communication')"
                    />
                  </div>
                </div>
              </div>
            </div>
          </section>
        </div>
      </template>
    </el-dialog>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowDown,
  ArrowLeft,
  ArrowRight,
  Calendar,
  Delete,
  DocumentCopy,
  Edit,
  Plus
} from '@element-plus/icons-vue'
import {
  queryTravelReimburseBaseData,
  queryTravelReimburseDetail,
  saveTravelReimburseDraft,
  submitTravelReimburse,
  type CostShare,
  type ManualTrip,
  type SubsidyCalendar,
  type SubsidyInfo,
  type TravelReimburseDetail
} from '@/api/reimburse'
import { useAuthStore } from '@/stores/auth'
import { useRoute, useRouter } from 'vue-router'

type DetailMode = 'create' | 'edit' | 'view'
type SectionKey = 'basic' | 'trip' | 'subsidy' | 'expense' | 'remark'
type SubsidyKind = 'meal' | 'traffic' | 'communication'
type TripDialogMode = 'add' | 'edit' | 'copy'

interface OptionItem {
  id: string
  no?: string
  name: string
}

interface CityOption {
  no: string
  name: string
  type: string
}

interface TreeOption {
  value: string
  label: string
  no?: string
  children?: TreeOption[]
}

interface TripRow {
  tripId: string
  travelerId: string
  travelerNo?: string
  travelerName: string
  departCityNo: string
  departCityName: string
  arriveCityNo: string
  arriveCityName: string
  departDate: string
  arriveDate: string
  remark: string
}

interface CalendarRow {
  calendarId: string
  travelDate: string
  weekName: string
  cityNo: string
  cityName: string
  cityType: string
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

interface SubsidyRow {
  subsidyId: string
  tripId: string
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
  calendarList: CalendarRow[]
}

interface DetailForm {
  id: string
  version: number
  reimBillNo: string
  billDate: string
  billStatus: string
  billStatusName: string
  reimburserId: string
  reimDepartmentId: string
  reimCompanyId: string
  businessTypeId: string
  title: string
  reason: string
  remark: string
}

const auth = useAuthStore()
const route = useRoute()
const router = useRouter()
const detailFormRef = ref<FormInstance>()
const tripFormRef = ref<FormInstance>()
const saving = ref(false)
const tripDialogVisible = ref(false)
const subsidyDialogVisible = ref(false)
const detailMode = ref<DetailMode>('view')
const hasUnsavedNewDraft = ref(false)
const tripDialogMode = ref<TripDialogMode>('add')
const editingTripIndex = ref(-1)
const editingSubsidyIndex = ref(-1)
const today = new Date().toISOString().slice(0, 10)
const subsidyNotice = '1、请根据实际出差日期选择补助 2、出差期间当日有用餐安排的请自行核减当日餐补 3、出差期间当日有用车的，请自行核减当日交补'

const detailForm = reactive<DetailForm>({
  id: '',
  version: 0,
  reimBillNo: '',
  billDate: '',
  billStatus: '0',
  billStatusName: '草稿',
  reimburserId: '',
  reimDepartmentId: '',
  reimCompanyId: '',
  businessTypeId: '',
  title: '',
  reason: '',
  remark: ''
})

const sectionOpen = reactive<Record<SectionKey, boolean>>({
  basic: true,
  trip: true,
  subsidy: true,
  expense: true,
  remark: true
})

const tripForm = reactive({
  travelerId: '',
  departCityNo: '',
  arriveCityNo: '',
  departDate: '',
  arriveDate: '',
  remark: ''
})

const detailRules: FormRules<DetailForm> = {
  billDate: [{ required: true, message: '请选择单据日期', trigger: 'change' }],
  reimburserId: [{ required: true, message: '请选择报销人', trigger: 'change' }],
  reimDepartmentId: [{ required: true, message: '请选择报销部门', trigger: 'change' }],
  reimCompanyId: [{ required: true, message: '请选择费用归属公司', trigger: 'change' }],
  businessTypeId: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入报销标题', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入出差事由', trigger: 'blur' }]
}

const tripRules = {
  travelerId: [{ required: true, message: '请选择出行人', trigger: 'change' }],
  departCityNo: [{ required: true, message: '请选择出发城市', trigger: 'change' }],
  arriveCityNo: [{ required: true, message: '请选择到达城市', trigger: 'change' }],
  departDate: [{ required: true, message: '请选择出发日期', trigger: 'change' }],
  arriveDate: [{ required: true, message: '请选择到达日期', trigger: 'change' }],
  remark: [{ required: true, message: '请输入行程说明', trigger: 'blur' }]
}

const companyOptions: OptionItem[] = [
  { id: '1C54557F1782E000', no: '0407', name: '胜意科技北京分公司' },
  { id: '19218A262C976000', no: '0408', name: '胜意科技上海分公司' },
  { id: '1C61686865DA8000', no: '0409', name: '胜意科技武汉分公司' },
  { id: '1717271D1DA15000', no: '0410', name: '胜意科技杭州分公司' },
  { id: '16AE93CC7EF92002', no: '0411', name: '胜意科技荆州分公司' }
]

const departmentOptions: OptionItem[] = [
  { id: '13AB8D7B52A9B002', no: '072001', name: '客户成功事业部' },
  { id: '13BFD31C6029A002', no: '072002', name: '企业消费事业部' },
  { id: '14515BB4BFB92003', no: '072003', name: '企业费控事业部' },
  { id: '19206611C47A6000', no: '072004', name: '集采事业部' },
  { id: '19D32F9FE9647000', no: '072005', name: '航旅事业部' },
  { id: '13C7E2BAE0393001', no: '072006', name: '运营事业部' },
  { id: '14055D22BB808001', no: '072007', name: '营销事业部' }
]

const employeeOptions: OptionItem[] = [
  { id: '13AB3A3F72409002', no: '74541', name: '徐年年' },
  { id: '13AB498CC6409002', no: '74008', name: '郑雨雪' },
  { id: '13AB4A56BB009002', no: '21552', name: '邹薇' },
  { id: '13AB591FE8009002', no: '80681', name: '王成军' },
  { id: '13AB77281A408001', no: '89899', name: '潘展飞' },
  { id: '13AB7925EB808001', no: '10503', name: '姜林' }
]

const projectOptions: OptionItem[] = [{ id: '12BC248B25083001', no: 'nonProjectRelated', name: '非项目类费用归集' }]

const cityOptions: CityOption[] = [
  { no: '10119', name: '北京', type: '1' },
  { no: '10621', name: '上海', type: '1' },
  { no: '10458', name: '武汉', type: '2' },
  { no: '10216', name: '杭州', type: '2' },
  { no: '10455', name: '荆州', type: '3' }
]

const businessTypeOptions: TreeOption[] = [
  {
    value: '18F0916A8C2C4000',
    no: '1001001',
    label: '员工差旅活动',
    children: [
      {
        value: '18F091913EEC4000',
        no: '100100101',
        label: '境内出差',
        children: [
          { value: '1B5FEB7DD4396000', no: '10010010101', label: '项目出差' },
          { value: '1A92E43082EFC000', no: '10010010102', label: '市场拓展出差' }
        ]
      },
      {
        value: '13AB3A4138008001',
        no: '100100102',
        label: '境外出差',
        children: [
          { value: '13AB3A4248008002', no: '10010010201', label: '国外考察' },
          { value: '13AB3A4154008001', no: '10010010202', label: '售后维护出差' }
        ]
      }
    ]
  },
  {
    value: '13AB3A4172008001',
    no: '1001002',
    label: '人力资源',
    children: [
      { value: '13AB3A418F808001', no: '100100201', label: '个人团队培训' },
      { value: '13AB3A41AC408001', no: '100100202', label: '招聘会' }
    ]
  },
  {
    value: '13AB3A41CD808002',
    no: '1001003',
    label: '员工福利',
    children: [
      { value: '13AB3A41ED408002', no: '100100301', label: '员工旅游' },
      { value: '13AB3A420CC08002', no: '100100302', label: '员工团建' },
      { value: '13AB3A422A808001', no: '100100303', label: '员工体检' }
    ]
  }
]

const tripList = ref<TripRow[]>([])
const subsidyList = ref<SubsidyRow[]>([])

const isReadonly = computed(() => detailMode.value === 'view')
const tripDialogTitle = computed(() => (tripDialogMode.value === 'edit' ? '编辑补录行程' : tripDialogMode.value === 'copy' ? '复制补录行程' : '补录行程'))
const businessTypeFlatOptions = computed(() => flattenBusinessTypes(businessTypeOptions))
const businessTypeName = computed(() => findBusinessType(detailForm.businessTypeId)?.label ?? '')
const editingSubsidy = computed(() => subsidyList.value[editingSubsidyIndex.value])

const expenseSummary = computed(() => ({
  subsidyAmount: sum(subsidyList.value.map((item) => item.subsidyAmount)),
  mealAmount: sum(subsidyList.value.map((item) => item.mealSubsidyAmount)),
  trafficAmount: sum(subsidyList.value.map((item) => item.trafficSubsidyAmount)),
  communicationAmount: sum(subsidyList.value.map((item) => item.communicationSubsidyAmount))
}))

const can = (permission: string) => auth.hasPermission(permission)
const uid = (prefix: string) => `${prefix}_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`
const findOption = (options: OptionItem[], id: string) => options.find((item) => item.id === id)
const findCity = (no: string) => cityOptions.find((item) => item.no === no)
const flattenBusinessTypes = (options: TreeOption[]): TreeOption[] => options.flatMap((item) => [item, ...flattenBusinessTypes(item.children ?? [])])
const findBusinessType = (id: string) => businessTypeFlatOptions.value.find((item) => item.value === id)
const sum = (values: number[]) => Number(values.reduce((total, value) => total + Number(value || 0), 0).toFixed(2))
const formatMoney = (value: number) => Number(value || 0).toFixed(2)

const mealStandardByCity = (cityType?: string) => {
  if (cityType === '1') return 100
  if (cityType === '2') return 80
  return 50
}

const getDateRange = (start: string, end: string) => {
  const dates: string[] = []
  const current = new Date(`${start}T00:00:00`)
  const last = new Date(`${end}T00:00:00`)
  while (current <= last) {
    dates.push(current.toISOString().slice(0, 10))
    current.setDate(current.getDate() + 1)
  }
  return dates
}

const weekName = (date: string) => ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][new Date(`${date}T00:00:00`).getDay()]

const makeCalendarRow = (date: string, city: CityOption): CalendarRow => {
  const mealStandard = mealStandardByCity(city.type)
  return {
    calendarId: uid('calendar'),
    travelDate: date,
    weekName: weekName(date),
    cityNo: city.no,
    cityName: city.name,
    cityType: city.type,
    mealChecked: true,
    mealStandardAmount: mealStandard,
    mealAmount: mealStandard,
    trafficChecked: true,
    trafficStandardAmount: 40,
    trafficAmount: 40,
    communicationChecked: true,
    communicationStandardAmount: 40,
    communicationAmount: 40
  }
}

const refreshSubsidyAmounts = (subsidy: SubsidyRow) => {
  subsidy.subsidyDays = subsidy.calendarList.length
  subsidy.applyAmount = sum(subsidy.calendarList.map((item) => (
    (item.mealChecked ? item.mealStandardAmount : 0) +
    (item.trafficChecked ? item.trafficStandardAmount : 0) +
    (item.communicationChecked ? item.communicationStandardAmount : 0)
  )))
  subsidy.mealSubsidyAmount = sum(subsidy.calendarList.map((item) => (item.mealChecked ? item.mealAmount : 0)))
  subsidy.trafficSubsidyAmount = sum(subsidy.calendarList.map((item) => (item.trafficChecked ? item.trafficAmount : 0)))
  subsidy.communicationSubsidyAmount = sum(subsidy.calendarList.map((item) => (item.communicationChecked ? item.communicationAmount : 0)))
  subsidy.subsidyAmount = sum([subsidy.mealSubsidyAmount, subsidy.trafficSubsidyAmount, subsidy.communicationSubsidyAmount])
}

const makeSubsidyByTrip = (trip: TripRow): SubsidyRow => {
  const city = findCity(trip.arriveCityNo) ?? cityOptions[0]
  const subsidy: SubsidyRow = {
    subsidyId: uid('subsidy'),
    tripId: trip.tripId,
    travelerId: trip.travelerId,
    travelerNo: trip.travelerNo,
    travelerName: trip.travelerName,
    startDate: trip.departDate,
    endDate: trip.arriveDate,
    subsidyDays: 0,
    route: `${trip.departCityName}-${trip.arriveCityName}`,
    subsidyCityNo: trip.arriveCityNo,
    subsidyCityName: trip.arriveCityName,
    applyAmount: 0,
    subsidyAmount: 0,
    mealSubsidyAmount: 0,
    trafficSubsidyAmount: 0,
    communicationSubsidyAmount: 0,
    calendarList: getDateRange(trip.departDate, trip.arriveDate).map((date) => makeCalendarRow(date, city))
  }
  refreshSubsidyAmounts(subsidy)
  return subsidy
}

const validateTripOverlap = (row: TripRow, ignoreIndex = -1) => {
  const start = new Date(`${row.departDate}T00:00:00`)
  const end = new Date(`${row.arriveDate}T00:00:00`)
  return tripList.value.some((item, index) => {
    if (index === ignoreIndex || item.travelerId !== row.travelerId) return false
    const itemStart = new Date(`${item.departDate}T00:00:00`)
    const itemEnd = new Date(`${item.arriveDate}T00:00:00`)
    return start <= itemEnd && end >= itemStart
  })
}

const validateAllTrips = () => {
  for (let index = 0; index < tripList.value.length; index += 1) {
    if (validateTripOverlap(tripList.value[index], index)) {
      ElMessage.warning('补录行程中存在相同人员日期重叠')
      return false
    }
  }
  return true
}

const buildTripFromForm = (): TripRow | null => {
  const traveler = findOption(employeeOptions, tripForm.travelerId)
  const departCity = findCity(tripForm.departCityNo)
  const arriveCity = findCity(tripForm.arriveCityNo)
  if (!traveler || !departCity || !arriveCity) return null
  return {
    tripId: tripDialogMode.value === 'edit' && editingTripIndex.value >= 0 ? tripList.value[editingTripIndex.value].tripId : uid('trip'),
    travelerId: traveler.id,
    travelerNo: traveler.no,
    travelerName: traveler.name,
    departCityNo: departCity.no,
    departCityName: departCity.name,
    arriveCityNo: arriveCity.no,
    arriveCityName: arriveCity.name,
    departDate: tripForm.departDate,
    arriveDate: tripForm.arriveDate,
    remark: tripForm.remark
  }
}

const openTripDialog = (mode: TripDialogMode, row?: TripRow, index = -1) => {
  tripDialogMode.value = mode
  editingTripIndex.value = index
  Object.assign(tripForm, {
    travelerId: row?.travelerId ?? '',
    departCityNo: row?.departCityNo ?? '',
    arriveCityNo: row?.arriveCityNo ?? '',
    departDate: row?.departDate ?? '',
    arriveDate: row?.arriveDate ?? '',
    remark: row?.remark ?? ''
  })
  tripDialogVisible.value = true
}

const handleSaveTrip = async () => {
  const valid = await tripFormRef.value?.validate().catch(() => false)
  if (!valid) return
  if (tripForm.arriveDate < tripForm.departDate) {
    ElMessage.warning('到达日期不可早于出发日期')
    return
  }
  if (tripForm.arriveDate > today || tripForm.departDate > today) {
    ElMessage.warning('出发/到达日期不可晚于当前日期')
    return
  }
  const row = buildTripFromForm()
  if (!row) {
    ElMessage.warning('请补全行程信息')
    return
  }
  const ignoreIndex = tripDialogMode.value === 'edit' ? editingTripIndex.value : -1
  if (validateTripOverlap(row, ignoreIndex)) {
    ElMessage.warning('同一出行人的行程日期不可重复')
    return
  }
  const subsidy = makeSubsidyByTrip(row)
  if (tripDialogMode.value === 'edit' && editingTripIndex.value >= 0) {
    tripList.value.splice(editingTripIndex.value, 1, row)
    const subsidyIndex = subsidyList.value.findIndex((item) => item.tripId === row.tripId)
    if (subsidyIndex >= 0) subsidyList.value.splice(subsidyIndex, 1, subsidy)
    else subsidyList.value.push(subsidy)
  } else {
    tripList.value.push(row)
    subsidyList.value.push(subsidy)
  }
  tripDialogVisible.value = false
}

const handleDeleteTrip = async (index: number) => {
  try {
    await ElMessageBox.confirm('确定删除当前补录行程吗？删除后将同步删除关联补助信息。', '确认删除提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const [removed] = tripList.value.splice(index, 1)
    subsidyList.value = subsidyList.value.filter((item) => item.tripId !== removed.tripId)
  } catch {
    undefined
  }
}

const openSubsidyDialog = (index: number) => {
  editingSubsidyIndex.value = index
  subsidyDialogVisible.value = true
}

const calendarRows = () => editingSubsidy.value?.calendarList ?? []
const allKinds: SubsidyKind[] = ['meal', 'traffic', 'communication']
const checkedKey = (kind: SubsidyKind) => `${kind}Checked` as const
const amountKey = (kind: SubsidyKind) => `${kind}Amount` as const
const standardKey = (kind: SubsidyKind) => `${kind}StandardAmount` as const

const isCalendarRowChecked = (row: CalendarRow) => allKinds.every((kind) => row[checkedKey(kind)])
const isCalendarRowIndeterminate = (row: CalendarRow) => {
  const count = allKinds.filter((kind) => row[checkedKey(kind)]).length
  return count > 0 && count < allKinds.length
}
const isCalendarColumnChecked = (kind: SubsidyKind) => calendarRows().length > 0 && calendarRows().every((row) => row[checkedKey(kind)])
const isCalendarColumnIndeterminate = (kind: SubsidyKind) => {
  const rows = calendarRows()
  const count = rows.filter((row) => row[checkedKey(kind)]).length
  return count > 0 && count < rows.length
}
const isAllCalendarChecked = (subsidy: SubsidyRow) => subsidy.calendarList.length > 0 && subsidy.calendarList.every(isCalendarRowChecked)
const isAllCalendarIndeterminate = (subsidy: SubsidyRow) => {
  const total = subsidy.calendarList.length * allKinds.length
  const checked = subsidy.calendarList.reduce((count, row) => count + allKinds.filter((kind) => row[checkedKey(kind)]).length, 0)
  return checked > 0 && checked < total
}

const setCalendarKind = (row: CalendarRow, kind: SubsidyKind, checked: boolean) => {
  row[checkedKey(kind)] = checked
  row[amountKey(kind)] = checked ? Number(row[standardKey(kind)]) : 0
}

const toggleCalendarRow = (row: CalendarRow, checked: boolean) => {
  allKinds.forEach((kind) => setCalendarKind(row, kind, checked))
  confirmSubsidyCalendar(false)
}

const toggleCalendarColumn = (kind: SubsidyKind, checked: boolean) => {
  calendarRows().forEach((row) => setCalendarKind(row, kind, checked))
  confirmSubsidyCalendar(false)
}

const toggleAllCalendar = (checked: boolean) => {
  calendarRows().forEach((row) => allKinds.forEach((kind) => setCalendarKind(row, kind, checked)))
  confirmSubsidyCalendar(false)
}

const normalizeCalendarAmount = (row: CalendarRow, kind: SubsidyKind) => {
  if (!row[checkedKey(kind)]) {
    row[amountKey(kind)] = 0
  } else {
    row[amountKey(kind)] = Math.min(Math.max(Number(row[amountKey(kind)] || 0), 0), Number(row[standardKey(kind)]))
  }
  confirmSubsidyCalendar(false)
}

const confirmSubsidyCalendar = (close = true) => {
  if (editingSubsidy.value) {
    refreshSubsidyAmounts(editingSubsidy.value)
  }
  if (close) subsidyDialogVisible.value = false
}

const disableFutureDate = (date: Date) => date.getTime() > new Date(`${today}T23:59:59`).getTime()
const disableArrivalDate = (date: Date) => disableFutureDate(date) || Boolean(tripForm.departDate && date < new Date(`${tripForm.departDate}T00:00:00`))

const toggleSection = (key: SectionKey) => {
  sectionOpen[key] = !sectionOpen[key]
}

const handleClearRemark = async () => {
  try {
    await ElMessageBox.confirm('确定删除备注信息吗？', '确认删除提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    detailForm.remark = ''
  } catch {
    undefined
  }
}

const toApiTrip = (row: TripRow): ManualTrip => ({
  tripId: row.tripId,
  travelerId: row.travelerId,
  travelerNo: row.travelerNo,
  travelerName: row.travelerName,
  departureCityNo: row.departCityNo,
  departureCityName: row.departCityName,
  arrivalCityNo: row.arriveCityNo,
  arrivalCityName: row.arriveCityName,
  departureDate: row.departDate,
  arrivalDate: row.arriveDate,
  tripDesc: row.remark
})

const toApiCalendar = (row: CalendarRow): SubsidyCalendar => ({
  calendarId: row.calendarId,
  travelDate: row.travelDate,
  weekName: row.weekName,
  cityNo: row.cityNo,
  cityName: row.cityName,
  cityType: row.cityType,
  mealChecked: row.mealChecked,
  mealStandardAmount: row.mealStandardAmount,
  mealAmount: row.mealAmount,
  trafficChecked: row.trafficChecked,
  trafficStandardAmount: row.trafficStandardAmount,
  trafficAmount: row.trafficAmount,
  communicationChecked: row.communicationChecked,
  communicationStandardAmount: row.communicationStandardAmount,
  communicationAmount: row.communicationAmount
})

const toApiSubsidy = (row: SubsidyRow): SubsidyInfo => ({
  subsidyId: row.subsidyId,
  tripId: row.tripId,
  travelerId: row.travelerId,
  travelerNo: row.travelerNo,
  travelerName: row.travelerName,
  startDate: row.startDate,
  endDate: row.endDate,
  subsidyDays: row.subsidyDays,
  route: row.route,
  subsidyCityNo: row.subsidyCityNo,
  subsidyCityName: row.subsidyCityName,
  applyAmount: row.applyAmount,
  subsidyAmount: row.subsidyAmount,
  mealSubsidyAmount: row.mealSubsidyAmount,
  trafficSubsidyAmount: row.trafficSubsidyAmount,
  communicationSubsidyAmount: row.communicationSubsidyAmount,
  calendarList: row.calendarList.map(toApiCalendar)
})

const buildShareList = (): CostShare[] => {
  const company = findOption(companyOptions, detailForm.reimCompanyId) ?? companyOptions[0]
  const project = projectOptions[0]
  return [
    {
      shareId: uid('share'),
      reimCompanyId: company.id,
      reimCompanyNo: company.no,
      reimCompanyName: company.name,
      projectId: project.id,
      projectNo: project.no,
      projectName: project.name,
      shareRatio: 100,
      shareAmount: expenseSummary.value.subsidyAmount
    }
  ]
}

const buildDetailPayload = (): TravelReimburseDetail => {
  const employee = findOption(employeeOptions, detailForm.reimburserId)
  const department = findOption(departmentOptions, detailForm.reimDepartmentId)
  const company = findOption(companyOptions, detailForm.reimCompanyId)
  const businessType = findBusinessType(detailForm.businessTypeId)
  return {
    id: detailForm.id,
    version: detailForm.version,
    reimBillNo: detailForm.reimBillNo,
    billDate: detailForm.billDate,
    billStatus: detailForm.billStatus,
    billStatusName: detailForm.billStatusName,
    title: detailForm.title,
    reason: detailForm.reason,
    reimburserId: detailForm.reimburserId,
    reimburserNo: employee?.no ?? '',
    reimburserName: employee?.name ?? '',
    reimDepartmentId: detailForm.reimDepartmentId,
    reimDepartmentNo: department?.no ?? '',
    reimDepartmentName: department?.name ?? '',
    reimCompanyId: detailForm.reimCompanyId,
    reimCompanyNo: company?.no ?? '',
    reimCompanyName: company?.name ?? '',
    businessTypeId: detailForm.businessTypeId,
    businessTypeNo: businessType?.no ?? '',
    businessTypeName: businessType?.label ?? '',
    totalSubsidyAmount: expenseSummary.value.subsidyAmount,
    remark: detailForm.remark,
    tripList: tripList.value.map(toApiTrip),
    subsidyList: subsidyList.value.map(toApiSubsidy),
    shareList: buildShareList()
  }
}

const applyDetailToForm = (detail: TravelReimburseDetail) => {
  Object.assign(detailForm, {
    id: detail.id,
    version: detail.version ?? 0,
    reimBillNo: detail.reimBillNo ?? '',
    billDate: detail.billDate || today,
    billStatus: detail.billStatus ?? '0',
    billStatusName: detail.billStatusName ?? '草稿',
    reimburserId: detail.reimburserId ?? '',
    reimDepartmentId: detail.reimDepartmentId ?? '',
    reimCompanyId: detail.reimCompanyId ?? '',
    businessTypeId: detail.businessTypeId ?? '',
    title: detail.title ?? '',
    reason: detail.reason ?? '',
    remark: detail.remark ?? ''
  })
  tripList.value = (detail.tripList ?? []).map((item) => ({
    tripId: item.tripId ?? uid('trip'),
    travelerId: item.travelerId,
    travelerNo: item.travelerNo,
    travelerName: item.travelerName,
    departCityNo: item.departureCityNo,
    departCityName: item.departureCityName,
    arriveCityNo: item.arrivalCityNo,
    arriveCityName: item.arrivalCityName,
    departDate: item.departureDate,
    arriveDate: item.arrivalDate,
    remark: item.tripDesc ?? ''
  }))
  subsidyList.value = (detail.subsidyList ?? []).map((item) => {
    const subsidy: SubsidyRow = {
      subsidyId: item.subsidyId ?? uid('subsidy'),
      tripId: item.tripId ?? uid('trip'),
      travelerId: item.travelerId,
      travelerNo: item.travelerNo,
      travelerName: item.travelerName,
      startDate: item.startDate,
      endDate: item.endDate,
      subsidyDays: item.subsidyDays,
      route: item.route,
      subsidyCityNo: item.subsidyCityNo,
      subsidyCityName: item.subsidyCityName,
      applyAmount: item.applyAmount,
      subsidyAmount: item.subsidyAmount,
      mealSubsidyAmount: item.mealSubsidyAmount,
      trafficSubsidyAmount: item.trafficSubsidyAmount,
      communicationSubsidyAmount: item.communicationSubsidyAmount,
      calendarList: (item.calendarList ?? []).map((calendar) => ({
        calendarId: calendar.calendarId ?? uid('calendar'),
        travelDate: calendar.travelDate,
        weekName: calendar.weekName || weekName(calendar.travelDate),
        cityNo: calendar.cityNo,
        cityName: calendar.cityName,
        cityType: calendar.cityType ?? findCity(calendar.cityNo)?.type ?? '3',
        mealChecked: calendar.mealChecked,
        mealStandardAmount: Number(calendar.mealStandardAmount || 0),
        mealAmount: Number(calendar.mealAmount || 0),
        trafficChecked: calendar.trafficChecked,
        trafficStandardAmount: Number(calendar.trafficStandardAmount || 0),
        trafficAmount: Number(calendar.trafficAmount || 0),
        communicationChecked: calendar.communicationChecked,
        communicationStandardAmount: Number(calendar.communicationStandardAmount || 0),
        communicationAmount: Number(calendar.communicationAmount || 0)
      }))
    }
    refreshSubsidyAmounts(subsidy)
    return subsidy
  })
}

const resetDetailForm = () => {
  Object.assign(detailForm, {
    id: '',
    version: 0,
    reimBillNo: '',
    billDate: today,
    billStatus: '0',
    billStatusName: '草稿',
    reimburserId: '',
    reimDepartmentId: '',
    reimCompanyId: '',
    businessTypeId: '',
    title: '',
    reason: '',
    remark: ''
  })
  tripList.value = []
  subsidyList.value = []
}

const openDraftDetail = (id: string, redirectUrl: string) => {
  detailMode.value = 'create'
  hasUnsavedNewDraft.value = true
  resetDetailForm()
  detailForm.id = id
  ElMessage.info('已打开新草稿，点击保存草稿或提交后才会入库：' + redirectUrl)
}

const shouldConfirmUnsavedDraft = () => detailMode.value === 'create' && hasUnsavedNewDraft.value

const confirmDiscardDraft = async () => {
  if (!shouldConfirmUnsavedDraft()) return true
  try {
    await ElMessageBox.confirm('当前新建草稿还没有保存或提交，关闭后不会入库。确定放弃吗？', '未保存草稿', {
      confirmButtonText: '放弃草稿',
      cancelButtonText: '继续编辑',
      type: 'warning'
    })
    hasUnsavedNewDraft.value = false
    return true
  } catch {
    return false
  }
}

const handleCloseDetail = async () => {
  try {
    await ElMessageBox.confirm('确定关闭当前页面吗？', '确认关闭提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  if (!(await confirmDiscardDraft())) return
  router.push('/reimburse/list')
}

const openRouteDetail = () => {
  const id = String(route.params.id || '')
  if (!id) return
  if (route.query.mode === 'create' || id.startsWith('draft_')) {
    openDraftDetail(id, `reimburse/detail/${id}`)
    return
  }
  detailMode.value = route.query.mode === 'edit' ? 'edit' : 'view'
  hasUnsavedNewDraft.value = false
  resetDetailForm()
  detailForm.id = id
  queryTravelReimburseDetail(id).then(applyDetailToForm).catch(() => ElMessage.error('详情加载失败'))
}

const validateBeforeSubmit = async () => {
  const valid = await detailFormRef.value?.validate().catch(() => false)
  if (!valid) return false
  if (!tripList.value.length) {
    ElMessage.warning('请至少补录一条行程')
    return false
  }
  return validateAllTrips()
}

const handleSaveDraft = async () => {
  if (!can('reimburse:save')) {
    ElMessage.warning('没有保存草稿权限')
    return
  }
  saving.value = true
  const detail = buildDetailPayload()
  try {
    const result = await saveTravelReimburseDraft(detail)
    const savedDetail = result.detail ?? detail
    applyDetailToForm(savedDetail)
    hasUnsavedNewDraft.value = false
    detailMode.value = 'edit'
    ElMessage.success('保存草稿成功')
  } catch (error) {
    console.warn(error)
    ElMessage.error('保存草稿失败，当前草稿尚未入库')
  } finally {
    saving.value = false
  }
}

const handleSubmit = async () => {
  if (!can('reimburse:submit')) {
    ElMessage.warning('没有提交权限')
    return
  }
  if (!(await validateBeforeSubmit())) return
  saving.value = true
  let detail = buildDetailPayload()
  try {
    if (shouldConfirmUnsavedDraft()) {
      const saved = await saveTravelReimburseDraft(detail)
      detail = saved.detail ?? detail
      applyDetailToForm(detail)
      hasUnsavedNewDraft.value = false
    }
    const result = await submitTravelReimburse(detail)
    const submittedDetail: TravelReimburseDetail = {
      ...detail,
      version: result.version ?? detail.version,
      reimBillNo: result.reimBillNo,
      billStatus: result.billStatus,
      billStatusName: result.billStatusName
    }
    applyDetailToForm(submittedDetail)
    hasUnsavedNewDraft.value = false
    detailMode.value = 'view'
    await ElMessageBox.alert('提交成功', '提示', { confirmButtonText: '确定', type: 'success' })
    router.push('/reimburse/list')
  } catch (error) {
    console.warn(error)
    const message = typeof error === 'object' && error && 'msg' in error ? String((error as { msg?: string }).msg) : '提交失败'
    ElMessage.warning(message)
  } finally {
    saving.value = false
  }
}

const handleAuthExpired = () => {
  auth.clearSession()
  router.replace('/login')
}

const getBillStatusTag = (status: string) => {
  if (status === '1') return 'success'
  if (status === '2') return 'info'
  return 'warning'
}

onMounted(() => {
  window.addEventListener('auth:expired', handleAuthExpired)
  if (auth.isAuthenticated) {
    auth.hydrateUser()
    queryTravelReimburseBaseData().catch((error) => console.warn(error))
    openRouteDetail()
  }
})

onUnmounted(() => {
  window.removeEventListener('auth:expired', handleAuthExpired)
})
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 18px 22px 30px;
  background: #f5f7fa;
  color: #1f2937;
  font-size: 14px;
}

.section-title {
  display: flex;
  align-items: center;
}

.bill-form :deep(.el-select),
.bill-form :deep(.el-tree-select),
.bill-form :deep(.el-date-editor),
.trip-alert {
  width: 100%;
}

.detail-page {
  margin: -18px -22px -30px;
  padding: 0 22px 30px;
  background: #eef2f6;
}

.bill-shell {
  width: min(1200px, calc(100vw - 32px));
  margin: 0 auto;
  padding-bottom: 24px;
}

.bill-header {
  position: sticky;
  top: 0;
  z-index: 4;
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  height: 58px;
  border-bottom: 1px solid #d8dee8;
  background: #fff;
}

.bill-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
}

.bill-left {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 16px;
  color: #64748b;
}

.bill-date {
  padding-right: 16px;
  text-align: right;
}

.bill-form {
  padding-top: 12px;
}

.bill-section {
  margin-bottom: 12px;
  border: 1px solid #dde4ee;
  background: #fff;
}

.section-title {
  width: 100%;
  height: 36px;
  gap: 8px;
  padding: 0 12px;
  border: 0;
  border-bottom: 1px solid #e8edf3;
  background: #f8fafc;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  text-align: left;
  cursor: pointer;
}

.section-title > span:first-of-type {
  flex: 0 0 auto;
}

.section-title .el-button,
.section-title .el-tag,
.section-tip {
  margin-left: auto;
}

.section-tip {
  overflow: hidden;
  max-width: 760px;
  color: #64748b;
  font-size: 13px;
  font-weight: 400;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.section-body {
  padding: 14px 14px 16px;
}

.page-footer {
  position: sticky;
  bottom: 0;
  z-index: 4;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  width: min(1200px, calc(100vw - 32px));
  margin: 0 auto;
  padding: 12px 0;
  border-top: 1px solid #d8dee8;
  background: #eef2f6;
}

.trip-alert {
  margin-bottom: 12px;
}

.subsidy-dialog :deep(.el-dialog__body) {
  padding: 8px 0 0;
}

.subsidy-calendar-layout {
  display: grid;
  grid-template-columns: 230px minmax(0, 1fr);
  min-height: 390px;
  border-top: 1px solid #eef2f6;
}

.subsidy-sidebar {
  padding: 16px 18px;
  border-right: 1px solid #e5e7eb;
  background: #fff;
}

.trip-type-row {
  display: flex;
  gap: 18px;
  align-items: center;
  margin-bottom: 18px;
  font-size: 12px;
}

.trip-type-row span,
.trip-range > span,
.subsidy-total-box span {
  color: #4b5563;
}

.trip-type-row strong {
  color: #ff6b00;
  font-weight: 600;
}

.trip-range {
  display: grid;
  grid-template-columns: 58px 1fr;
  row-gap: 8px;
  align-items: center;
  margin-bottom: 32px;
  font-size: 12px;
}

.range-line {
  display: flex;
  align-items: center;
  gap: 10px;
}

.range-line i {
  width: 8px;
  height: 8px;
  border: 2px solid #168fdc;
  border-radius: 50%;
  background: #fff;
}

.route-chip {
  grid-column: 1 / -1;
  height: 30px;
  margin: 0 0 2px;
  border-radius: 0;
}

.subsidy-total-box {
  display: grid;
  gap: 14px;
  padding-top: 18px;
  border-top: 1px solid #edf1f5;
  font-size: 12px;
}

.subsidy-total-box div {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.subsidy-total-box strong {
  color: #ff4d00;
  font-weight: 600;
}

.calendar-panel {
  min-width: 0;
  padding: 16px;
}

.calendar-panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.calendar-panel-head h3 {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
}

.subsidy-calendar-table {
  border: 1px solid #e5e7eb;
  overflow-x: auto;
}

.calendar-grid {
  display: grid;
  grid-template-columns: 150px 112px repeat(3, minmax(150px, 1fr));
  min-width: 760px;
}

.calendar-grid > div {
  min-height: 42px;
  padding: 7px 10px;
  border-right: 1px solid #eef2f6;
  border-bottom: 1px solid #eef2f6;
  background: #fff;
}

.calendar-grid > div:last-child {
  border-right: 0;
}

.calendar-grid-head > div {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 34px;
  background: #fafafa;
  color: #374151;
  font-size: 12px;
  font-weight: 600;
}

.calendar-grid-row:last-child > div {
  border-bottom: 0;
}

.date-cell {
  display: grid;
  grid-template-columns: 1fr auto;
  column-gap: 8px;
  align-items: center;
}

.date-cell strong {
  font-size: 12px;
  font-weight: 600;
}

.date-cell span {
  color: #4b5563;
  font-size: 12px;
}

.date-cell .el-checkbox {
  grid-row: 1 / span 2;
  grid-column: 2;
}

.city-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #374151;
  font-size: 12px;
}

.subsidy-amount-cell {
  display: grid;
  gap: 4px;
  justify-items: center;
}

.subsidy-amount-cell > span {
  color: #ff5a00;
  font-size: 12px;
}

.subsidy-amount-cell > div {
  display: flex;
  align-items: center;
  gap: 8px;
}

.subsidy-amount-cell :deep(.el-input-number) {
  width: 72px;
}

.subsidy-amount-cell :deep(.el-input__wrapper) {
  min-height: 22px;
  padding: 0 5px;
  border-radius: 2px;
}

.subsidy-amount-cell :deep(.el-input__inner) {
  height: 22px;
  color: #6b7280;
  font-size: 12px;
}

.subsidy-dialog :deep(.el-checkbox__label) {
  font-size: 12px;
}

.subsidy-dialog :deep(.el-checkbox) {
  height: 18px;
}

.subsidy-dialog :deep(.el-checkbox__inner) {
  width: 12px;
  height: 12px;
}

.subsidy-dialog :deep(.el-checkbox__inner::after) {
  left: 3px;
  top: 0;
}

.subsidy-dialog :deep(.el-dialog__header) {
  margin-right: 0;
  padding: 14px 16px 10px;
  border-bottom: 1px solid #eef2f6;
}

.subsidy-dialog :deep(.el-dialog__title) {
  font-size: 14px;
  font-weight: 600;
}

@media (max-width: 900px) {
  .bill-header {
    grid-template-columns: 1fr;
    height: auto;
    gap: 4px;
    padding: 8px 0;
    text-align: center;
  }

  .bill-no,
  .bill-date {
    padding: 0;
    text-align: center;
  }
}
</style>
