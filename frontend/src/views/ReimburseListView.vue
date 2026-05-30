<template>
  <main class="page">
    <section class="page-header">
      <div>
        <h1>差旅报销单</h1>
        <p>列表页 reimburse/list；详情页 reimburse/detail/{id}</p>
      </div>
      <div class="header-actions">
        <div class="user-summary">
          <el-avatar :size="30">{{ userInitial }}</el-avatar>
          <div>
            <strong>{{ auth.user?.displayName }}</strong>
            <span>{{ auth.roles.join(' / ') }}</span>
          </div>
        </div>
        <el-button v-if="can('user:manage')" :icon="UserFilled" @click="userManageVisible = true">
          用户管理
        </el-button>
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
              <el-option label="已完成" value="1" />
              <el-option label="已作废" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="18">
        <el-col :span="6">
          <el-form-item label="费用归属公司" prop="reimCompanyId">
            <el-select v-model="queryForm.reimCompanyId" placeholder="请选择" clearable filterable>
              <el-option
                v-for="company in companyOptions"
                :key="company.id"
                :label="company.name"
                :value="company.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="报销部门" prop="reimDepartmentId">
            <el-select v-model="queryForm.reimDepartmentId" placeholder="请选择" clearable filterable>
              <el-option
                v-for="department in departmentOptions"
                :key="department.id"
                :label="department.name"
                :value="department.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="报销人" prop="reimburserId">
            <el-select v-model="queryForm.reimburserId" placeholder="请选择" clearable filterable>
              <el-option
                v-for="employee in employeeOptions"
                :key="employee.id"
                :label="employee.name"
                :value="employee.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="业务类型" prop="businessTypeId">
            <el-tree-select
              v-model="queryForm.businessTypeId"
              :data="businessTypeOptions"
              placeholder="请选择"
              clearable
              check-strictly
            />
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
        <el-table-column label="操作" width="132" fixed="left" align="center">
          <template #default="{ row }">
            <el-tooltip content="查看详情">
              <el-button
                v-if="can('reimburse:view')"
                link
                type="primary"
                :icon="View"
                @click="handleOpenDetail(row, 'view')"
              />
            </el-tooltip>
            <el-tooltip content="编辑">
              <el-button
                v-if="can('reimburse:edit')"
                link
                type="primary"
                :icon="Edit"
                @click="handleOpenDetail(row, 'edit')"
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
            <el-link
              type="primary"
              :underline="false"
              :disabled="!can('reimburse:view')"
              @click="handleOpenDetail(row, 'view')"
            >
              {{ row.reimBillNo || '-' }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="报销标题" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-link
              type="primary"
              :underline="false"
              :disabled="!can('reimburse:view')"
              @click="handleOpenDetail(row, 'view')"
            >
              {{ row.title }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="billStatusName" label="状态" width="96" align="center">
          <template #default="{ row }">
            <el-tag :type="getBillStatusTag(row.billStatus)" effect="light">
              {{ row.billStatusName }}
            </el-tag>
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

    <el-drawer
      v-model="detailVisible"
      :title="detailTitle"
      size="86%"
      destroy-on-close
      class="detail-drawer"
      :before-close="handleBeforeCloseDetail"
    >
      <template #default>
        <el-form
          ref="detailFormRef"
          :model="detailForm"
          :rules="detailRules"
          label-width="112px"
          :disabled="detailMode === 'view'"
        >
          <el-card shadow="never" class="section-card">
            <template #header>
              <div class="section-header">
                <span>主单基本信息</span>
                <el-tag :type="getBillStatusTag(detailForm.billStatus)" effect="light">
                  {{ detailForm.billStatusName || '草稿' }}
                </el-tag>
              </div>
            </template>
            <el-row :gutter="18">
              <el-col :span="6">
                <el-form-item label="报销单号">
                  <el-input v-model="detailForm.reimBillNo" disabled placeholder="提交后生成" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="单据日期" prop="billDate">
                  <el-date-picker
                    v-model="detailForm.billDate"
                    type="date"
                    value-format="YYYY-MM-DD"
                    placeholder="请选择"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="报销人" prop="reimburserId">
                  <el-select v-model="detailForm.reimburserId" filterable placeholder="请选择">
                    <el-option
                      v-for="employee in employeeOptions"
                      :key="employee.id"
                      :label="employee.name"
                      :value="employee.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="报销部门" prop="reimDepartmentId">
                  <el-select v-model="detailForm.reimDepartmentId" filterable placeholder="请选择">
                    <el-option
                      v-for="department in departmentOptions"
                      :key="department.id"
                      :label="department.name"
                      :value="department.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="18">
              <el-col :span="6">
                <el-form-item label="费用归属公司" prop="reimCompanyId">
                  <el-select v-model="detailForm.reimCompanyId" filterable placeholder="请选择">
                    <el-option
                      v-for="company in companyOptions"
                      :key="company.id"
                      :label="company.name"
                      :value="company.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="业务类型" prop="businessTypeId">
                  <el-tree-select
                    v-model="detailForm.businessTypeId"
                    :data="businessTypeOptions"
                    placeholder="请选择"
                    check-strictly
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报销标题" prop="title">
                  <el-input v-model="detailForm.title" maxlength="500" show-word-limit />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="出差事由" prop="reason">
              <el-input
                v-model="detailForm.reason"
                type="textarea"
                :rows="3"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-card>

          <el-card shadow="never" class="section-card">
            <template #header>
              <div class="section-header">
                <span>补录行程</span>
                <el-button type="primary" plain :icon="Plus" @click="tripDialogVisible = true">
                  补录行程
                </el-button>
              </div>
            </template>
            <el-table :data="tripList" border size="small">
              <el-table-column prop="travelerName" label="出行人" width="110" />
              <el-table-column prop="departCityName" label="出发城市" width="120" />
              <el-table-column prop="arriveCityName" label="到达城市" width="120" />
              <el-table-column prop="departDate" label="出发日期" width="120" />
              <el-table-column prop="arriveDate" label="到达日期" width="120" />
              <el-table-column prop="projectName" label="项目" min-width="160" show-overflow-tooltip />
              <el-table-column prop="remark" label="行程说明" min-width="200" show-overflow-tooltip />
            </el-table>
          </el-card>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-card shadow="never" class="section-card">
                <template #header>
                  <div class="section-header">
                    <span>补助日历</span>
                    <el-button plain :icon="Calendar" @click="subsidyDialogVisible = true">
                      编辑补助
                    </el-button>
                  </div>
                </template>
                <el-table :data="subsidyCalendarList" border size="small" height="260">
                  <el-table-column prop="subsidyDate" label="日期" width="116" />
                  <el-table-column prop="cityName" label="城市" width="100" />
                  <el-table-column prop="standardAmount" label="标准金额" align="right" width="100">
                    <template #default="{ row }">{{ formatMoney(row.standardAmount) }}</template>
                  </el-table-column>
                  <el-table-column prop="actualAmount" label="实际金额" align="right" width="100">
                    <template #default="{ row }">{{ formatMoney(row.actualAmount) }}</template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card shadow="never" class="section-card">
                <template #header>
                  <div class="section-header">
                    <span>费用合计</span>
                  </div>
                </template>
                <el-descriptions :column="2" border>
                  <el-descriptions-item label="补助总金额">
                    {{ formatMoney(expenseSummary.subsidyAmount) }}
                  </el-descriptions-item>
                  <el-descriptions-item label="分摊总金额">
                    {{ formatMoney(allocationTotalAmount) }}
                  </el-descriptions-item>
                  <el-descriptions-item label="分摊比例合计">
                    {{ allocationTotalRatio }}%
                  </el-descriptions-item>
                  <el-descriptions-item label="校验状态">
                    <el-tag :type="allocationValid ? 'success' : 'warning'">
                      {{ allocationValid ? '已平衡' : '待调整' }}
                    </el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-col>
          </el-row>

          <el-card shadow="never" class="section-card">
            <template #header>
              <div class="section-header">
                <span>费用归属及分摊</span>
                <el-button plain :icon="Share" @click="allocationDialogVisible = true">
                  费用分摊
                </el-button>
              </div>
            </template>
            <el-table :data="allocationList" border size="small">
              <el-table-column prop="reimCompanyName" label="费用归属公司" min-width="160" />
              <el-table-column prop="reimDepartmentName" label="报销部门" min-width="140" />
              <el-table-column prop="projectName" label="项目" min-width="160" />
              <el-table-column prop="ratio" label="分摊比例" width="110" align="right">
                <template #default="{ row }">{{ row.ratio }}%</template>
              </el-table-column>
              <el-table-column prop="amount" label="分摊金额" width="120" align="right">
                <template #default="{ row }">{{ formatMoney(row.amount) }}</template>
              </el-table-column>
            </el-table>
          </el-card>

          <el-card shadow="never" class="section-card">
            <template #header>
              <div class="section-header">
                <span>备注信息</span>
                <small>最多1000字</small>
              </div>
            </template>
            <el-input
              v-model="detailForm.remark"
              type="textarea"
              :rows="4"
              maxlength="1000"
              show-word-limit
            />
          </el-card>
        </el-form>
      </template>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="handleCloseDetail">关闭</el-button>
          <el-button
            v-if="detailMode !== 'view' && can('reimburse:save')"
            :loading="saving"
            @click="handleSaveDraft"
          >
            保存草稿
          </el-button>
          <el-button
            v-if="detailMode !== 'view' && can('reimburse:submit')"
            type="primary"
            :loading="saving"
            @click="handleSubmit"
          >
            提交
          </el-button>
        </div>
      </template>
    </el-drawer>

    <el-dialog v-model="userManageVisible" title="用户管理" width="860px">
      <el-table :data="userRows" border size="small">
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="displayName" label="姓名" width="140" />
        <el-table-column prop="roles" label="角色" min-width="180">
          <template #default="{ row }">
            <el-tag v-for="role in row.roles" :key="role" effect="light" class="role-tag">
              {{ role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '启用' ? 'success' : 'info'" effect="light">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190" align="center">
          <template #default>
            <el-button link type="primary">分配角色</el-button>
            <el-button link>重置密码</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="72" align="center">
        </el-table-column>
      </el-table>
      <template #footer>
        <el-text type="info">后端用户管理接口接入后，这里可替换为真实增删改查。</el-text>
        <el-button @click="userManageVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="tripDialogVisible" title="补录行程" width="720px">
      <el-form :model="tripForm" label-width="92px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出行人">
              <el-select v-model="tripForm.travelerId" placeholder="请选择">
                <el-option
                  v-for="employee in employeeOptions"
                  :key="employee.id"
                  :label="employee.name"
                  :value="employee.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目">
              <el-select v-model="tripForm.projectId" placeholder="请选择">
                <el-option
                  v-for="project in projectOptions"
                  :key="project.id"
                  :label="project.name"
                  :value="project.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出发城市">
              <el-select v-model="tripForm.departCityNo" placeholder="请选择">
                <el-option
                  v-for="city in cityOptions"
                  :key="city.no"
                  :label="city.name"
                  :value="city.no"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到达城市">
              <el-select v-model="tripForm.arriveCityNo" placeholder="请选择">
                <el-option
                  v-for="city in cityOptions"
                  :key="city.no"
                  :label="city.name"
                  :value="city.no"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出发日期">
              <el-date-picker v-model="tripForm.departDate" type="date" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到达日期">
              <el-date-picker v-model="tripForm.arriveDate" type="date" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="行程说明">
          <el-input v-model="tripForm.remark" type="textarea" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="tripDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddTrip">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="subsidyDialogVisible" title="补助日历" width="920px">
      <div class="dialog-toolbar">
        <el-button
          type="primary"
          plain
          :icon="Plus"
          :disabled="detailMode === 'view'"
          @click="handleAddSubsidyDate"
        >
          新增补助日期
        </el-button>
      </div>
      <el-table :data="subsidyCalendarList" border size="small">
        <el-table-column label="日期" width="160">
          <template #default="{ row }">
            <el-date-picker
              v-model="row.subsidyDate"
              type="date"
              value-format="YYYY-MM-DD"
              :disabled="detailMode === 'view'"
              @change="handleSubsidyCalendarChange"
            />
          </template>
        </el-table-column>
        <el-table-column label="城市" width="150">
          <template #default="{ row }">
            <el-select
              v-model="row.cityNo"
              :disabled="detailMode === 'view'"
              @change="handleSubsidyCityChange(row)"
            >
              <el-option
                v-for="city in cityOptions"
                :key="city.no"
                :label="city.name"
                :value="city.no"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="启用" width="80">
          <template #default="{ row }">
            <el-switch
              v-model="row.enabled"
              :disabled="detailMode === 'view'"
              @change="handleSubsidyCalendarChange"
            />
          </template>
        </el-table-column>
        <el-table-column prop="standardAmount" label="标准金额" width="120" align="right" />
        <el-table-column label="实际金额" width="160">
          <template #default="{ row }">
            <el-input-number
              v-model="row.actualAmount"
              :min="0"
              :max="row.standardAmount"
              :disabled="!row.enabled || detailMode === 'view'"
              :precision="2"
              @change="handleSubsidyCalendarChange"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="72" align="center">
          <template #default="{ $index }">
            <el-button
              link
              type="danger"
              :disabled="detailMode === 'view'"
              @click="handleRemoveSubsidyDate($index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="subsidyDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleRecalculate">重新计算</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="allocationDialogVisible" title="费用分摊" width="820px">
      <div class="dialog-toolbar">
        <el-button plain :icon="Plus" @click="handleAddAllocation">新增分摊</el-button>
        <el-button plain :icon="Refresh" @click="handleAverageAllocation">平均分摊</el-button>
      </div>
      <el-table :data="allocationList" border size="small">
        <el-table-column type="index" label="序号" width="56" />
        <el-table-column prop="reimCompanyName" label="费用归属公司" min-width="150" />
        <el-table-column prop="reimDepartmentName" label="报销部门" min-width="130" />
        <el-table-column prop="projectName" label="项目" min-width="150" />
        <el-table-column label="分摊比例" width="150">
          <template #default="{ row, $index }">
            <el-input-number
              v-model="row.ratio"
              :min="0"
              :max="100"
              :precision="2"
              :disabled="$index === 0"
              @change="handleAllocationChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="分摊金额" width="150">
          <template #default="{ row, $index }">
            <el-input-number
              v-model="row.amount"
              :min="0"
              :precision="2"
              :disabled="$index === 0"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="72" align="center">
          <template #default="{ $index }">
            <el-button
              link
              type="danger"
              :icon="CircleClose"
              :disabled="allocationList.length <= 1"
              @click="handleRemoveAllocation($index)"
            />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-text :type="allocationValid ? 'success' : 'warning'" class="allocation-status">
          比例合计 {{ allocationTotalRatio }}%，金额合计 {{ formatMoney(allocationTotalAmount) }}
        </el-text>
        <el-button @click="allocationDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleConfirmAllocation">确认</el-button>
      </template>
    </el-dialog>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Calendar,
  CircleClose,
  Edit,
  Plus,
  Refresh,
  Search,
  Share,
  SwitchButton,
  UserFilled,
  View
} from '@element-plus/icons-vue'
import {
  calculateCostShare,
  invalidTravelReimburse,
  queryTravelReimburseDetail,
  queryTravelReimbursePageList,
  saveTravelReimburseDraft,
  submitTravelReimburse,
  type CostShare,
  type ManualTrip,
  type QueryTravelReimbursePageListData,
  type SubsidyCalendar,
  type SubsidyInfo,
  type TravelReimburseDetail,
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
  projectId: string
  projectName: string
  remark: string
}

interface SubsidyCalendarRow {
  calendarId: string
  subsidyDate: string
  cityNo: string
  cityName: string
  cityType: string
  enabled: boolean
  standardAmount: number
  actualAmount: number
}

interface AllocationRow {
  shareId: string
  reimCompanyId: string
  reimCompanyNo?: string
  reimCompanyName: string
  reimDepartmentName?: string
  projectId: string
  projectNo?: string
  projectName: string
  ratio: number
  amount: number
}

interface DetailForm {
  id: string
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

const queryFormRef = ref<FormInstance>()
const detailFormRef = ref<FormInstance>()
const auth = useAuthStore()
const router = useRouter()
const loading = ref(false)
const saving = ref(false)
const userManageVisible = ref(false)
const detailVisible = ref(false)
const tripDialogVisible = ref(false)
const subsidyDialogVisible = ref(false)
const allocationDialogVisible = ref(false)
const detailMode = ref<DetailMode>('view')
const hasUnsavedNewDraft = ref(false)
const tableData = ref<TravelReimbursePageRow[]>([])
const selectedRows = ref<TravelReimbursePageRow[]>([])
const localDetailMap = ref<Record<string, TravelReimburseDetail>>({})

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

const detailForm = reactive<DetailForm>({
  id: '',
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

const tripForm = reactive({
  travelerId: '',
  departCityNo: '',
  arriveCityNo: '',
  departDate: '',
  arriveDate: '',
  projectId: '',
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

const projectOptions: OptionItem[] = [
  { id: '12BC248B25083001', no: 'nonProjectRelated', name: '非项目类费用归集' },
  { id: '1C811ABF96195000', no: 'centralChina', name: '华中客户定制化项目' },
  { id: '1C5931735AC4A000', no: 'southChina', name: '华南客户定制化项目' },
  { id: '1771EC45F2443000', no: 'northChina', name: '华北客户定制化项目' },
  { id: '1762792DB4E9A002', no: 'eastChina', name: '华东客户定制化项目' },
  { id: '17071065FC29A002', no: 'southWest', name: '西南客户定制化项目' },
  { id: '162664EBE9ABE001', no: 'northWest', name: '西北客户定制化项目' },
  { id: '162664B8526BE002', no: 'northEast', name: '东北客户定制化项目' }
]

const cityOptions = [
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
const subsidyCalendarList = ref<SubsidyCalendarRow[]>([])
const allocationList = ref<AllocationRow[]>([])
const expenseSummary = reactive({
  subsidyAmount: 0
})

const detailTitle = computed(() => {
  const modeLabel = detailMode.value === 'view' ? '查看' : detailMode.value === 'edit' ? '编辑' : '新增'
  return `${modeLabel}差旅报销单`
})

const allocationTotalRatio = computed(() => {
  return Number(allocationList.value.reduce((sum, item) => sum + Number(item.ratio || 0), 0).toFixed(2))
})

const allocationTotalAmount = computed(() => {
  return Number(allocationList.value.reduce((sum, item) => sum + Number(item.amount || 0), 0).toFixed(2))
})

const allocationValid = computed(() => {
  return allocationTotalRatio.value === 100 && allocationTotalAmount.value === expenseSummary.subsidyAmount
})

const userInitial = computed(() => auth.user?.displayName?.slice(0, 1) || auth.user?.username?.slice(0, 1) || 'U')

const can = (permission: string) => auth.hasPermission(permission)

const findOption = (options: OptionItem[], id: string) => options.find((item) => item.id === id)

const flattenBusinessTypes = (options: TreeOption[]): TreeOption[] => {
  return options.flatMap((item) => [item, ...flattenBusinessTypes(item.children ?? [])])
}

const businessTypeFlatOptions = flattenBusinessTypes(businessTypeOptions)

const findBusinessType = (id: string) => businessTypeFlatOptions.find((item) => item.value === id)

const uid = (prefix: string) => `${prefix}_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`

const getCityStandardAmount = (cityType?: string) => {
  if (cityType === '1') return 300
  if (cityType === '2') return 220
  return 160
}

const makeSubsidyCalendarRow = (date: string, city = cityOptions[0]): SubsidyCalendarRow => {
  const standardAmount = getCityStandardAmount(city?.type)
  return {
    calendarId: uid('calendar'),
    subsidyDate: date,
    cityNo: city?.no ?? '',
    cityName: city?.name ?? '',
    cityType: city?.type ?? '',
    enabled: true,
    standardAmount,
    actualAmount: standardAmount
  }
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

const mockRows: TravelReimbursePageRow[] = [
  {
    id: '25',
    reimBillNo: 'CLBX202605220001',
    billStatus: '0',
    billStatusName: '草稿',
    reimburserId: '13AB3A3F72409002',
    reimburserNo: '74541',
    reimburserName: '徐年年',
    reimDepartmentId: '13AB8D7B52A9B002',
    reimDepartmentNo: '072001',
    reimDepartmentName: '客户成功事业部',
    reimCompanyId: '19218A262C976000',
    reimCompanyNo: '0408',
    reimCompanyName: '胜意科技上海分公司',
    businessTypeId: '1B5FEB7DD4396000',
    businessTypeName: '项目出差',
    title: '上海客户拜访差旅报销',
    reason: '拜访重点客户并完成合同评审',
    subsidyAmount: 860,
    createTime: '2026-05-22 09:20:00'
  },
  {
    id: '26',
    reimBillNo: 'CLBX202605220002',
    billStatus: '1',
    billStatusName: '已完成',
    reimburserId: '13AB498CC6409002',
    reimburserNo: '74008',
    reimburserName: '郑雨雪',
    reimDepartmentId: '14515BB4BFB92003',
    reimDepartmentNo: '072003',
    reimDepartmentName: '企业费控事业部',
    reimCompanyId: '1C54557F1782E000',
    reimCompanyNo: '0407',
    reimCompanyName: '胜意科技北京分公司',
    businessTypeId: '13AB3A418F808001',
    businessTypeName: '个人团队培训',
    title: '北京产品方案会议差旅',
    reason: '参与客户产品方案讨论',
    subsidyAmount: 1260,
    createTime: '2026-05-21 16:12:00'
  }
]

const buildRowFromDetail = (detail: TravelReimburseDetail): TravelReimbursePageRow => {
  const employee = findOption(employeeOptions, detail.reimburserId)
  const department = findOption(departmentOptions, detail.reimDepartmentId)
  const company = findOption(companyOptions, detail.reimCompanyId)
  const businessType = findBusinessType(detail.businessTypeId)
  return {
    id: detail.id,
    reimBillNo: detail.reimBillNo ?? '',
    billStatus: detail.billStatus ?? '0',
    billStatusName: detail.billStatusName ?? '草稿',
    reimburserId: detail.reimburserId,
    reimburserNo: detail.reimburserNo ?? employee?.no ?? '',
    reimburserName: detail.reimburserName ?? employee?.name ?? '',
    reimDepartmentId: detail.reimDepartmentId,
    reimDepartmentNo: detail.reimDepartmentNo ?? department?.no ?? '',
    reimDepartmentName: detail.reimDepartmentName ?? department?.name ?? '',
    reimCompanyId: detail.reimCompanyId,
    reimCompanyNo: detail.reimCompanyNo ?? company?.no ?? '',
    reimCompanyName: detail.reimCompanyName ?? company?.name ?? '',
    businessTypeId: detail.businessTypeId,
    businessTypeName: detail.businessTypeName ?? businessType?.label ?? '',
    title: detail.title,
    reason: detail.reason,
    subsidyAmount: Number(detail.totalSubsidyAmount ?? expenseSummary.subsidyAmount),
    createTime: `${detail.billDate || new Date().toISOString().slice(0, 10)} 00:00:00`
  }
}

const upsertLocalRow = (row: TravelReimbursePageRow) => {
  const tableIndex = tableData.value.findIndex((item) => item.id === row.id)
  if (tableIndex >= 0) tableData.value.splice(tableIndex, 1, row)
  else tableData.value.unshift(row)

  const mockIndex = mockRows.findIndex((item) => item.id === row.id)
  if (mockIndex >= 0) mockRows.splice(mockIndex, 1, row)
  else mockRows.unshift(row)

  pagination.total = tableData.value.length
  pagination.pages = Math.max(1, Math.ceil(tableData.value.length / pagination.size))
}

const buildDetailPayload = (): TravelReimburseDetail => {
  const employee = findOption(employeeOptions, detailForm.reimburserId)
  const department = findOption(departmentOptions, detailForm.reimDepartmentId)
  const company = findOption(companyOptions, detailForm.reimCompanyId)
  const businessType = findBusinessType(detailForm.businessTypeId)
  const primaryTrip = tripList.value[0]
  const calendars = subsidyCalendarList.value.map(toApiCalendar)
  const subsidyTotal = calendars.reduce((sum, item) => {
    return sum + (item.mealChecked ? item.mealAmount : 0) + (item.trafficChecked ? item.trafficAmount : 0) + (item.communicationChecked ? item.communicationAmount : 0)
  }, 0)

  return {
    id: detailForm.id,
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
    totalSubsidyAmount: expenseSummary.subsidyAmount,
    remark: detailForm.remark,
    tripList: tripList.value.map(toApiTrip),
    subsidyList: calendars.length
      ? [
          {
            subsidyId: uid('subsidy'),
            tripId: primaryTrip?.tripId,
            travelerId: primaryTrip?.travelerId || detailForm.reimburserId,
            travelerNo: primaryTrip?.travelerNo || employee?.no || '',
            travelerName: primaryTrip?.travelerName || employee?.name || '',
            startDate: calendars[0].travelDate,
            endDate: calendars[calendars.length - 1].travelDate,
            subsidyDays: calendars.filter((item) => item.mealChecked || item.trafficChecked || item.communicationChecked).length,
            route: primaryTrip ? `${primaryTrip.departCityName}-${primaryTrip.arriveCityName}` : '',
            subsidyCityNo: calendars[calendars.length - 1].cityNo,
            subsidyCityName: calendars[calendars.length - 1].cityName,
            applyAmount: subsidyTotal,
            subsidyAmount: subsidyTotal,
            mealSubsidyAmount: calendars.reduce((sum, item) => sum + (item.mealChecked ? item.mealAmount : 0), 0),
            trafficSubsidyAmount: calendars.reduce((sum, item) => sum + (item.trafficChecked ? item.trafficAmount : 0), 0),
            communicationSubsidyAmount: calendars.reduce((sum, item) => sum + (item.communicationChecked ? item.communicationAmount : 0), 0),
            calendarList: calendars
          }
        ]
      : [],
    shareList: allocationList.value.map(toApiShare)
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

const toApiCalendar = (row: SubsidyCalendarRow): SubsidyCalendar => {
  const amount = row.enabled ? Number(row.actualAmount || 0) : 0
  return {
    calendarId: row.calendarId,
    travelDate: row.subsidyDate,
    cityNo: row.cityNo,
    cityName: row.cityName,
    cityType: row.cityType,
    mealChecked: row.enabled,
    mealStandardAmount: row.standardAmount,
    mealAmount: amount,
    trafficChecked: false,
    trafficStandardAmount: 0,
    trafficAmount: 0,
    communicationChecked: false,
    communicationStandardAmount: 0,
    communicationAmount: 0
  }
}

const toApiShare = (row: AllocationRow): CostShare => {
  const company = findOption(companyOptions, row.reimCompanyId) ?? findOption(companyOptions, detailForm.reimCompanyId) ?? companyOptions[0]
  const project = findOption(projectOptions, row.projectId) ?? projectOptions[0]
  return {
    shareId: row.shareId,
    reimCompanyId: row.reimCompanyId || company?.id || detailForm.reimCompanyId,
    reimCompanyNo: row.reimCompanyNo || company?.no,
    reimCompanyName: row.reimCompanyName || company?.name || '',
    projectId: row.projectId || project?.id || '',
    projectNo: row.projectNo || project?.no,
    projectName: row.projectName || project?.name || '',
    shareRatio: row.ratio,
    shareAmount: row.amount
  }
}

const toAllocationRow = (item: CostShare): AllocationRow => {
  const company = findOption(companyOptions, item.reimCompanyId ?? '') ?? findOption(companyOptions, detailForm.reimCompanyId) ?? companyOptions[0]
  const project = findOption(projectOptions, item.projectId ?? '') ?? projectOptions[0]
  return {
    shareId: item.shareId ?? uid('share'),
    reimCompanyId: item.reimCompanyId || company?.id || '',
    reimCompanyNo: item.reimCompanyNo || company?.no,
    reimCompanyName: item.reimCompanyName || company?.name || '',
    reimDepartmentName: findOption(departmentOptions, detailForm.reimDepartmentId)?.name ?? '',
    projectId: item.projectId || project?.id || '',
    projectNo: item.projectNo || project?.no,
    projectName: item.projectName || project?.name || '',
    ratio: Number(item.shareRatio || 0),
    amount: Number(item.shareAmount || 0)
  }
}

const makeAllocationRow = (): AllocationRow => {
  const company = findOption(companyOptions, detailForm.reimCompanyId) ?? companyOptions[0]
  const department = findOption(departmentOptions, detailForm.reimDepartmentId)
  const usedProjects = new Set(allocationList.value.map((item) => item.projectId))
  const project = projectOptions.find((item) => !usedProjects.has(item.id)) ?? projectOptions[0]
  return {
    shareId: uid('share'),
    reimCompanyId: company?.id ?? '',
    reimCompanyNo: company?.no,
    reimCompanyName: company?.name ?? '',
    reimDepartmentName: department?.name ?? '',
    projectId: project?.id ?? '',
    projectNo: project?.no,
    projectName: project?.name ?? '',
    ratio: 0,
    amount: 0
  }
}

const applyDetailToForm = (detail: TravelReimburseDetail) => {
  Object.assign(detailForm, {
    id: detail.id,
    reimBillNo: detail.reimBillNo ?? '',
    billDate: detail.billDate,
    billStatus: detail.billStatus ?? '0',
    billStatusName: detail.billStatusName ?? '草稿',
    reimburserId: detail.reimburserId,
    reimDepartmentId: detail.reimDepartmentId,
    reimCompanyId: detail.reimCompanyId,
    businessTypeId: detail.businessTypeId,
    title: detail.title,
    reason: detail.reason,
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
    projectId: '',
    projectName: '',
    remark: item.tripDesc ?? ''
  }))
  subsidyCalendarList.value = (detail.subsidyList ?? [])
    .flatMap((item) => item.calendarList ?? [])
    .map((item) => {
      const amount = Number(item.mealAmount || 0) + Number(item.trafficAmount || 0) + Number(item.communicationAmount || 0)
      return {
        calendarId: item.calendarId ?? uid('calendar'),
        subsidyDate: item.travelDate,
        cityNo: item.cityNo,
        cityName: item.cityName,
        cityType: item.cityType ?? '',
        enabled: Boolean(item.mealChecked || item.trafficChecked || item.communicationChecked),
        standardAmount: Number(item.mealStandardAmount || 0) + Number(item.trafficStandardAmount || 0) + Number(item.communicationStandardAmount || 0),
        actualAmount: amount
      }
    })
  allocationList.value = (detail.shareList ?? []).map((item) => ({
    shareId: item.shareId ?? uid('share'),
    reimCompanyId: item.reimCompanyId ?? detail.reimCompanyId,
    reimCompanyNo: item.reimCompanyNo ?? detail.reimCompanyNo,
    reimCompanyName: item.reimCompanyName,
    reimDepartmentName: detail.reimDepartmentName,
    projectId: item.projectId ?? '',
    projectNo: item.projectNo,
    projectName: item.projectName,
    ratio: Number(item.shareRatio || 0),
    amount: Number(item.shareAmount || 0)
  }))
  expenseSummary.subsidyAmount = Number(detail.totalSubsidyAmount ?? 0)
}

const fillMockList = () => {
  tableData.value = mockRows
  pagination.total = mockRows.length
  pagination.pages = 1
}

const handleSearch = async () => {
  loading.value = true
  try {
    const result = await queryTravelReimbursePageList(
      pagination.current,
      pagination.size,
      normalizeQueryData()
    )
    tableData.value = result.records
    pagination.total = result.total
    pagination.pages = result.pages
  } catch (error) {
    console.warn(error)
    fillMockList()
    ElMessage.warning('接口未连接，已显示模板示例数据')
  } finally {
    loading.value = false
  }
}

const normalizeQueryData = () => {
  return Object.fromEntries(
    Object.entries(queryForm).filter(([, value]) => value !== undefined && value !== '')
  ) as QueryTravelReimbursePageListData
}

const handleReset = () => {
  queryFormRef.value?.resetFields()
  pagination.current = 1
  handleSearch()
}

const handleLogout = async () => {
  if (!(await confirmDiscardDraft())) {
    return
  }
  await auth.signOut()
  detailVisible.value = false
  userManageVisible.value = false
  ElMessage.success('已退出登录')
  router.replace('/login')
}

const handleAuthExpired = () => {
  auth.clearSession()
  detailVisible.value = false
  userManageVisible.value = false
  router.replace('/login')
}

const handleCreateDraft = () => {
  if (!can('reimburse:create')) {
    ElMessage.warning('没有新增权限')
    return
  }
  const localId = uid('draft')
  router.push({ path: `/reimburse/detail/${localId}`, query: { mode: 'create' } })
}

const openDraftDetail = (id: string, redirectUrl: string) => {
  detailMode.value = 'create'
  hasUnsavedNewDraft.value = true
  resetDetailForm()
  detailForm.id = id
  detailForm.billDate = new Date().toISOString().slice(0, 10)
  detailVisible.value = true
  ElMessage.info('已打开新草稿，点击保存草稿或提交后才会入库：' + redirectUrl)
}

const shouldConfirmUnsavedDraft = () => detailMode.value === 'create' && hasUnsavedNewDraft.value

const confirmDiscardDraft = async () => {
  if (!shouldConfirmUnsavedDraft()) {
    return true
  }
  try {
    await ElMessageBox.confirm(
      '当前新建草稿还没有保存或提交，关闭后不会入库。确定放弃吗？',
      '未保存草稿',
      {
        confirmButtonText: '放弃草稿',
        cancelButtonText: '继续编辑',
        type: 'warning'
      }
    )
    hasUnsavedNewDraft.value = false
    return true
  } catch {
    return false
  }
}

const handleCloseDetail = async () => {
  if (!(await confirmDiscardDraft())) {
    return
  }
  detailVisible.value = false
}
const handleBeforeCloseDetail = (done: () => void) => {
  confirmDiscardDraft().then((confirmed) => {
    if (confirmed) {
      done()
    }
  })
}

const handleOpenDetail = (row: TravelReimbursePageRow, mode: DetailMode) => {
  if (mode === 'view' && !can('reimburse:view')) {
    ElMessage.warning('没有查看权限')
    return
  }
  if (mode === 'edit' && !can('reimburse:edit')) {
    ElMessage.warning('没有编辑权限')
    return
  }
  router.push({ path: `/reimburse/detail/${row.id}`, query: { mode } })
}

const hydrateDetailSections = (row: TravelReimbursePageRow) => {
  tripList.value = [
    {
      travelerId: row.reimburserId,
      tripId: uid('trip'),
      travelerNo: row.reimburserNo,
      travelerName: row.reimburserName,
      departCityNo: '10119',
      departCityName: '北京',
      arriveCityNo: '10621',
      arriveCityName: '上海',
      departDate: row.createTime.slice(0, 10),
      arriveDate: row.createTime.slice(0, 10),
      projectId: '1762792DB4E9A002',
      projectName: '华东客户定制化项目',
      remark: '客户现场沟通'
    }
  ]
  subsidyCalendarList.value = [
    {
      calendarId: uid('calendar'),
      subsidyDate: row.createTime.slice(0, 10),
      cityNo: '10621',
      cityName: '上海',
      cityType: '1',
      enabled: true,
      standardAmount: 300,
      actualAmount: 300
    },
    {
      calendarId: uid('calendar'),
      subsidyDate: row.createTime.slice(0, 10),
      cityNo: '10621',
      cityName: '上海',
      cityType: '1',
      enabled: true,
      standardAmount: 280,
      actualAmount: 280
    }
  ]
  allocationList.value = [
    {
      shareId: uid('share'),
      reimCompanyId: row.reimCompanyId,
      reimCompanyNo: row.reimCompanyNo,
      reimCompanyName: row.reimCompanyName,
      reimDepartmentName: row.reimDepartmentName,
      projectId: '1762792DB4E9A002',
      projectNo: 'eastChina',
      projectName: '华东客户定制化项目',
      ratio: 100,
      amount: row.subsidyAmount
    }
  ]
  expenseSummary.subsidyAmount = row.subsidyAmount
}

const resetDetailForm = () => {
  Object.assign(detailForm, {
    id: '',
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
  tripList.value = []
  subsidyCalendarList.value = []
  allocationList.value = []
  expenseSummary.subsidyAmount = 0
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
  try {
    await invalidTravelReimburse(row.id, value)
    ElMessage.success('作废成功')
    handleSearch()
  } catch (error) {
    console.warn(error)
    row.billStatus = '2'
    row.billStatusName = '已作废'
    ElMessage.warning('接口未连接，已在本地模拟作废')
  }
}

const handleSelectionChange = (rows: TravelReimbursePageRow[]) => {
  selectedRows.value = rows
}

const handleAddTrip = () => {
  const traveler = employeeOptions.find((item) => item.id === tripForm.travelerId)
  const departCity = cityOptions.find((item) => item.no === tripForm.departCityNo)
  const arriveCity = cityOptions.find((item) => item.no === tripForm.arriveCityNo)
  const project = projectOptions.find((item) => item.id === tripForm.projectId)
  if (!traveler || !departCity || !arriveCity || !tripForm.departDate || !tripForm.arriveDate) {
    ElMessage.warning('请补全行程信息')
    return
  }
  if (tripForm.arriveDate < tripForm.departDate) {
    ElMessage.warning('到达日期不可早于出发日期')
    return
  }
  tripList.value.push({
    tripId: uid('trip'),
    travelerId: traveler.id,
    travelerNo: traveler.no,
    travelerName: traveler.name,
    departCityNo: departCity.no,
    departCityName: departCity.name,
    arriveCityNo: arriveCity.no,
    arriveCityName: arriveCity.name,
    departDate: tripForm.departDate,
    arriveDate: tripForm.arriveDate,
    projectId: project?.id ?? '',
    projectName: project?.name ?? '',
    remark: tripForm.remark
  })
  const nextCalendars = getDateRange(tripForm.departDate, tripForm.arriveDate).map((date) =>
    makeSubsidyCalendarRow(date, arriveCity)
  )
  subsidyCalendarList.value.push(...nextCalendars)
  if (!allocationList.value.length && project) {
    const company = findOption(companyOptions, detailForm.reimCompanyId)
    const department = findOption(departmentOptions, detailForm.reimDepartmentId)
    allocationList.value = [
      {
        shareId: uid('share'),
        reimCompanyId: company?.id ?? '',
        reimCompanyNo: company?.no,
        reimCompanyName: company?.name ?? '',
        reimDepartmentName: department?.name ?? '',
        projectId: project.id,
        projectNo: project.no,
        projectName: project.name,
        ratio: 100,
        amount: expenseSummary.subsidyAmount
      }
    ]
  }
  handleRecalculate(false)
  Object.assign(tripForm, {
    travelerId: '',
    departCityNo: '',
    arriveCityNo: '',
    departDate: '',
    arriveDate: '',
    projectId: '',
    remark: ''
  })
  tripDialogVisible.value = false
}

const handleSubsidyCalendarChange = () => {
  handleRecalculate(false)
}

const handleSubsidyCityChange = (row: SubsidyCalendarRow) => {
  const city = cityOptions.find((item) => item.no === row.cityNo)
  if (!city) return
  row.cityName = city.name
  row.cityType = city.type
  row.standardAmount = getCityStandardAmount(city.type)
  row.actualAmount = Math.min(row.actualAmount || row.standardAmount, row.standardAmount)
  handleRecalculate(false)
}

const handleAddSubsidyDate = () => {
  const lastCalendar = subsidyCalendarList.value[subsidyCalendarList.value.length - 1]
  const lastTrip = tripList.value[tripList.value.length - 1]
  const defaultDate = lastCalendar?.subsidyDate || detailForm.billDate || new Date().toISOString().slice(0, 10)
  const defaultCityNo = lastCalendar?.cityNo || lastTrip?.arriveCityNo
  const city = cityOptions.find((item) => item.no === defaultCityNo) ?? cityOptions[0]
  subsidyCalendarList.value.push(makeSubsidyCalendarRow(defaultDate, city))
  handleRecalculate(false)
}

const handleRemoveSubsidyDate = (index: number) => {
  subsidyCalendarList.value.splice(index, 1)
  handleRecalculate(false)
}

const handleRecalculate = (showMessage = true) => {
  expenseSummary.subsidyAmount = Number(
    subsidyCalendarList.value
      .filter((item) => item.enabled)
      .reduce((sum, item) => sum + Number(item.actualAmount || 0), 0)
      .toFixed(2)
  )
  if (allocationList.value[0]) {
    allocationList.value[0].amount = expenseSummary.subsidyAmount
  }
  if (showMessage) {
    ElMessage.success('已重新计算补助金额')
  }
}

const handleAllocationChange = (row: AllocationRow) => {
  row.amount = Number(((expenseSummary.subsidyAmount * row.ratio) / 100).toFixed(2))
  recalculateAllocation('recalculate')
}

const handleAllocationCompanyChange = (row: AllocationRow) => {
  const company = findOption(companyOptions, row.reimCompanyId)
  row.reimCompanyNo = company?.no
  row.reimCompanyName = company?.name ?? ''
}

const handleAllocationProjectChange = (row: AllocationRow) => {
  const project = findOption(projectOptions, row.projectId)
  row.projectNo = project?.no
  row.projectName = project?.name ?? ''
}

const recalculateAllocation = async (operateType: 'add' | 'delete' | 'average' | 'recalculate') => {
  try {
    const result = await calculateCostShare({
      reimburseId: detailForm.id,
      operateType,
      totalShareAmount: expenseSummary.subsidyAmount,
      shareList: allocationList.value.map(toApiShare)
    })
    allocationList.value = result.shareList.map(toAllocationRow)
  } catch (error) {
    console.warn(error)
  }
}

const handleAddAllocation = async () => {
  allocationList.value.push(makeAllocationRow())
  await recalculateAllocation('average')
}

const handleRemoveAllocation = async (index: number) => {
  if (allocationList.value.length <= 1) {
    ElMessage.warning('至少保留一条分摊')
    return
  }
  allocationList.value.splice(index, 1)
  await recalculateAllocation('recalculate')
}

const handleAverageAllocation = async () => {
  if (!allocationList.value.length) {
    allocationList.value.push(makeAllocationRow())
  }
  await recalculateAllocation('average')
}

const handleConfirmAllocation = () => {
  if (!allocationValid.value) {
    ElMessage.warning('分摊比例或金额合计未平衡')
    return
  }
  allocationDialogVisible.value = false
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
    localDetailMap.value[savedDetail.id] = savedDetail
    upsertLocalRow(buildRowFromDetail(savedDetail))
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
  const valid = await detailFormRef.value?.validate().catch(() => false)
  if (!valid) return
  if (!allocationValid.value) {
    ElMessage.warning('请先调整费用分摊，使比例和金额合计平衡')
    return
  }
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
      reimBillNo: result.reimBillNo,
      billStatus: result.billStatus,
      billStatusName: result.billStatusName
    }
    localDetailMap.value[submittedDetail.id] = submittedDetail
    upsertLocalRow(buildRowFromDetail(submittedDetail))
    applyDetailToForm(submittedDetail)
    hasUnsavedNewDraft.value = false
    detailMode.value = 'view'
    ElMessage.success('提交成功')
  } catch (error) {
    console.warn(error)
    const message = typeof error === 'object' && error && 'msg' in error
      ? String((error as { msg?: string }).msg)
      : '提交失败'
    ElMessage.warning(message)
  } finally {
    saving.value = false
  }
}

const getBillStatusTag = (status: string) => {
  if (status === '1') return 'success'
  if (status === '2') return 'info'
  return 'warning'
}

const formatMoney = (value: number) => {
  return Number(value || 0).toFixed(2)
}

onMounted(() => {
  window.addEventListener('auth:expired', handleAuthExpired)
  if (auth.isAuthenticated) {
    auth.hydrateUser()
    handleSearch()
  }
})

onUnmounted(() => {
  window.removeEventListener('auth:expired', handleAuthExpired)
})

defineExpose({
  handleSearch,
  handleReset,
  handleCreateDraft,
  handleOpenDetail,
  handleInvalid,
  handleSaveDraft,
  handleSubmit,
  handleAddTrip,
  handleAddSubsidyDate,
  handleRemoveSubsidyDate,
  handleRecalculate,
  handleConfirmAllocation
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-summary {
  display: flex;
  align-items: center;
  gap: 8px;
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
.query-panel :deep(.el-tree-select),
.query-panel :deep(.el-date-editor) {
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

.section-card {
  margin-bottom: 14px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.detail-drawer :deep(.el-drawer__body) {
  padding: 14px 18px 0;
  background: #f5f7fa;
}

.detail-drawer :deep(.el-select),
.detail-drawer :deep(.el-tree-select),
.detail-drawer :deep(.el-date-editor) {
  width: 100%;
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.dialog-toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.role-tag {
  margin-right: 6px;
}

.allocation-status {
  margin-right: auto;
}

@media (max-width: 900px) {
  .page-header,
  .header-actions {
    align-items: stretch;
    flex-direction: column;
  }
}
</style>
