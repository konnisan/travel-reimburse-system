<template>
  <main class="page">
    <section class="page-header">
      <div>
        <h1>差旅报销单</h1>
        <p>列表页 reimburse/list；详情页 reimburse/detail/{id}</p>
      </div>
      <div class="header-actions">
        <el-popover
            placement="bottom-end"
            width="520"
            trigger="click"
            popper-class="user-popover"
        >
          <template #reference>
            <div class="user-summary user-summary-clickable">
              <el-avatar :size="30">{{ userInitial }}</el-avatar>
              <div>
                <strong>{{ auth.user?.displayName }}</strong>
                <span>{{ auth.roles.join(' / ') }}</span>
              </div>
            </div>
          </template>

          <div class="user-popover-content">
            <div class="user-popover-title">
              <strong>系统用户信息</strong>
              <span>当前可用演示账号</span>
            </div>

            <el-table :data="userRows" border size="small">
              <el-table-column prop="username" label="用户名" width="100" />
              <el-table-column prop="displayName" label="显示名称" width="120" />

              <el-table-column label="角色">
                <template #default="{ row }">
                  <el-tag
                      v-for="role in row.roles"
                      :key="role"
                      size="small"
                      type="primary"
                      effect="light"
                      class="role-tag"
                  >
                    {{ role }}
                  </el-tag>
                </template>
              </el-table-column>

              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag type="success" size="small" effect="light">
                    {{ row.status }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-popover>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button
          v-if="can('reimburse:list')"
          type="primary"
          plain
          :icon="Search"
          :loading="loading"
          @click="handleSearch"
        >
          查询
        </el-button>
        <el-button v-if="can('reimburse:create')" type="primary" :icon="Plus" @click="handleCreateDraft">
          新增
        </el-button>
        <el-button :icon="SwitchButton" @click="handleLogout">退出</el-button>
      </div>
    </section>

    <el-form ref="queryFormRef" class="query-panel" :model="queryForm" label-width="108px">
      <el-row :gutter="18">
        <el-col :span="6">
          <el-form-item label="报销单号" prop="reimBillNo">
            <el-input v-model.trim="queryForm.reimBillNo" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="报销标题" prop="title">
            <el-input v-model.trim="queryForm.title" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="报销事由" prop="reason">
            <el-input v-model.trim="queryForm.reason" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="单据状态" prop="billStatus">
            <el-select v-model="queryForm.billStatus" placeholder="请选择" clearable>
              <el-option label="草稿" value="0" />
              <el-option label="待审核" value="1" />
              <el-option label="已作废" value="2" />
              <el-option label="已完成" value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="18">
        <el-col :span="6">
          <el-form-item label="费用归属公司" prop="reimCompanyId">
            <el-select v-model="queryForm.reimCompanyId" placeholder="请选择" clearable filterable>
              <el-option v-for="company in companyOptions" :key="company.id" :label="company.name" :value="company.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="报销部门" prop="reimDepartmentId">
            <el-select v-model="queryForm.reimDepartmentId" placeholder="请选择" clearable filterable>
              <el-option v-for="department in departmentOptions" :key="department.id" :label="department.name" :value="department.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="报销人" prop="reimburserId">
            <el-select v-model="queryForm.reimburserId" placeholder="请选择" clearable filterable>
              <el-option v-for="employee in employeeOptions" :key="employee.id" :label="employee.name" :value="employee.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="业务类型" prop="businessTypeId">
            <el-tree-select v-model="queryForm.businessTypeId" :data="businessTypeOptions" placeholder="请选择" clearable check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <section class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        height="486"
        size="small"
        empty-text="暂无数据"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="42" align="center" />
        <el-table-column type="index" label="序号" width="56" align="center" />
        <el-table-column label="操作" width="156" fixed="left" align="center">
          <template #default="{ row }">
            <el-tooltip content="查看详情">
              <el-button v-if="can('reimburse:view')" link type="primary" :icon="View" @click="handleOpenDetail(row, 'view')" />
            </el-tooltip>
            <el-tooltip :content="getEditTooltip(row)">
              <el-button v-if="canEditRow(row)" link type="primary" :icon="Edit" @click="handleOpenDetail(row, 'edit')" />
            </el-tooltip>
            <el-tooltip content="审核">
              <el-button
                v-if="canApproveRow(row)"
                link
                type="success"
                :icon="Check"
                @click="handleApprove(row)"
              />
            </el-tooltip>
            <el-tooltip content="作废">
              <el-button
                v-if="can('reimburse:invalid')"
                link
                type="danger"
                :icon="CircleClose"
                :disabled="row.billStatus === '2'"
                @click="handleInvalid(row)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="reimBillNo" label="报销单号" min-width="150" show-overflow-tooltip>
          <template #default="{ row }">
            <el-link type="primary" :underline="false" :disabled="!can('reimburse:view')" @click="handleOpenDetail(row, 'view')">
              {{ row.reimBillNo || '-' }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="报销标题" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-link type="primary" :underline="false" :disabled="!can('reimburse:view')" @click="handleOpenDetail(row, 'view')">
              {{ row.title }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="billStatusName" label="状态" width="96" align="center">
          <template #default="{ row }">
            <el-tag :type="getBillStatusTag(row.billStatus)" effect="light">{{ row.billStatusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reimburserName" label="报销人" min-width="112" />
        <el-table-column prop="reimDepartmentName" label="报销部门" min-width="140" show-overflow-tooltip />
        <el-table-column prop="reimCompanyName" label="费用归属公司" min-width="160" show-overflow-tooltip />
        <el-table-column prop="businessTypeName" label="业务类型" min-width="130" show-overflow-tooltip />
        <el-table-column prop="reason" label="报销事由" min-width="220" show-overflow-tooltip />
        <el-table-column prop="subsidyAmount" label="补助金额" width="120" align="right">
          <template #default="{ row }">{{ formatMoney(row.subsidyAmount) }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="168" />
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, reactive, ref } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check, CircleClose, Edit, Plus, Refresh, Search, SwitchButton, View } from '@element-plus/icons-vue'
import {
  approveTravelReimburse,
  invalidTravelReimburse,
  queryTravelReimburseBaseData,
  queryTravelReimbursePageList,
  type QueryTravelReimbursePageListData,
  type TravelReimbursePageRow
} from '@/api/reimburse'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

type DetailMode = 'create' | 'edit' | 'view'

interface OptionItem {
  id: string
  no?: string
  name: string
}

interface TreeOption {
  value: string
  label: string
  no?: string
  disabled?: boolean
  children?: TreeOption[]
}

const auth = useAuthStore()
const router = useRouter()
const queryFormRef = ref<FormInstance>()
const loading = ref(false)
const selectedRows = ref<TravelReimbursePageRow[]>([])
const tableData = ref<TravelReimbursePageRow[]>([])

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0,
  pages: 0
})

const queryForm = reactive<QueryTravelReimbursePageListData>({
  reimBillNo: '',
  title: '',
  reason: '',
  reimCompanyId: '',
  reimDepartmentId: '',
  reimburserId: '',
  businessTypeId: '',
  billStatus: ''
})

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

function disableNonLeafOptions(options: TreeOption[]): TreeOption[] {
  return options.map((item) => {
    const children = item.children ? disableNonLeafOptions(item.children) : undefined
    return {
      ...item,
      children,
      disabled: Boolean(children?.length)
    }
  })
}

const businessTypeOptions: TreeOption[] = disableNonLeafOptions([
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
])

const userRows = ref([
  {
    username: 'admin',
    displayName: '系统管理员',
    roles: ['ADMIN'],
    status: '启用'
  },
  {
    username: 'finance',
    displayName: '财务专员',
    roles: ['FINANCE'],
    status: '启用'
  },
  {
    username: 'employee',
    displayName: '普通员工',
    roles: ['EMPLOYEE'],
    status: '启用'
  }
])

const userInitial = computed(() => auth.user?.displayName?.slice(0, 1) || auth.user?.username?.slice(0, 1) || 'U')

const can = (permission: string) => auth.hasPermission(permission)
const isAdmin = () => auth.hasRole('ADMIN')
const canEditRow = (row: TravelReimbursePageRow) => can('reimburse:edit') && (row.billStatus === '0' || isAdmin())
const canApproveRow = (row: TravelReimbursePageRow) => can('reimburse:approve') && row.billStatus === '1'
const getEditTooltip = (row: TravelReimbursePageRow) => (row.billStatus === '0' ? '编辑' : '编辑并生成新草稿')

const normalizeQueryData = () => {
  return Object.fromEntries(
    Object.entries(queryForm).filter(([, value]) => value !== undefined && value !== '')
  ) as QueryTravelReimbursePageListData
}

const handleSearch = async () => {
  loading.value = true
  try {
    const result = await queryTravelReimbursePageList(pagination.current, pagination.size, normalizeQueryData())
    tableData.value = result.records
    pagination.total = result.total
    pagination.pages = result.pages
  } catch (error) {
    console.warn(error)
    tableData.value = []
    pagination.total = 0
    pagination.pages = 0
    if (isAuthExpiredError(error)) return
    ElMessage.error('查询报销单失败')
  } finally {
    loading.value = false
  }
}


const isAuthExpiredError = (error: unknown) => {
  if (!error || typeof error !== 'object') return false
  const response = (error as { response?: { status?: number } }).response
  return response?.status === 401
}

const handleReset = () => {
  queryFormRef.value?.resetFields()
  pagination.current = 1
  handleSearch()
}

const handleCreateDraft = () => {
  if (!can('reimburse:create')) {
    ElMessage.warning('没有新增权限')
    return
  }
  router.push({ path: `/reimburse/detail/draft_${Date.now()}`, query: { mode: 'create' } })
}

const handleOpenDetail = (row: TravelReimbursePageRow, mode: DetailMode) => {
  if (mode === 'view' && !can('reimburse:view')) {
    ElMessage.warning('没有查看权限')
    return
  }
  if (mode === 'edit' && !canEditRow(row)) {
    ElMessage.warning('没有编辑权限')
    return
  }
  router.push({ path: `/reimburse/detail/${row.id}`, query: { mode } })
}

const handleApprove = async (row: TravelReimbursePageRow) => {
  if (!canApproveRow(row)) {
    ElMessage.warning('没有审核权限')
    return
  }
  await ElMessageBox.confirm('确定审核通过当前报销单吗？', '审核确认', {
    confirmButtonText: '审核通过',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await approveTravelReimburse(row.id, row.version)
  ElMessage.success('审核成功')
  handleSearch()
}

const handleInvalid = async (row: TravelReimbursePageRow) => {
  if (!can('reimburse:invalid')) {
    ElMessage.warning('没有作废权限')
    return
  }
  const { value } = await ElMessageBox.prompt('请输入作废原因', '作废差旅报销单', {
    confirmButtonText: '确认作废',
    cancelButtonText: '取消',
    inputType: 'textarea'
  })
  await invalidTravelReimburse(row.id, row.version, value)
  ElMessage.success('作废成功')
  handleSearch()
}

const handleSelectionChange = (rows: TravelReimbursePageRow[]) => {
  selectedRows.value = rows
}

const handleLogout = async () => {
  await auth.signOut()
  ElMessage.success('已退出登录')
  router.replace('/login')
}

const handleAuthExpired = () => {
  auth.clearSession()
  router.replace('/login')
}

const getBillStatusTag = (status: string) => {
  if (status === '1') return 'primary'
  if (status === '2') return 'info'
  if (status === '3') return 'success'
  return 'warning'
}

const formatMoney = (value: number) => Number(value || 0).toFixed(2)

onMounted(() => {
  window.addEventListener('auth:expired', handleAuthExpired)
  if (auth.isAuthenticated) {
    auth.hydrateUser()
    queryTravelReimburseBaseData().catch((error) => console.warn(error))
    handleSearch()
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
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 12px;
  padding: 14px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  background: #fff;
}

.page-header h1 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.page-header p {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 12px;
}

.header-actions,
.user-summary {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-summary {
  padding: 4px 8px;
  border: 1px solid #dbe3ef;
  border-radius: 6px;
  background: #fff;
}

.user-summary div {
  display: grid;
  gap: 2px;
  min-width: 86px;
}

.user-summary strong {
  font-size: 13px;
  line-height: 1.1;
}

.user-summary span {
  color: #64748b;
  font-size: 12px;
  line-height: 1.1;
}

.user-summary-clickable {
  cursor: pointer;
  transition: all 0.2s ease;
}

.user-summary-clickable:hover {
  border-color: #409eff;
  background: #f0f7ff;
}

.user-popover-content {
  display: grid;
  gap: 12px;
}

.user-popover-title {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-popover-title strong {
  font-size: 15px;
  color: #1f2937;
}

.user-popover-title span {
  font-size: 12px;
  color: #64748b;
}

.role-tag {
  margin-right: 6px;
}

.query-panel,
.table-card {
  margin-bottom: 12px;
  padding: 16px 16px 2px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  background: #fff;
}

.query-panel :deep(.el-form-item) {
  margin-bottom: 14px;
}

.query-panel :deep(.el-select),
.query-panel :deep(.el-tree-select) {
  width: 100%;
}

.table-card {
  padding-bottom: 14px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  padding-top: 14px;
}

@media (max-width: 900px) {
  .page-header,
  .header-actions {
    align-items: stretch;
    flex-direction: column;
  }
}
</style>
