<template>
  <div class="app-container">
    <!-- 搜索表单部分保持不变 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="检测员姓名" prop="inspectorName">
        <el-input
          v-model="queryParams.inspectorName"
          placeholder="请输入检测员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="AQI级别" prop="aqiLevel">
        <el-select
          v-model="queryParams.aqiLevel"
          placeholder="请选择AQI级别"
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="level in aqiLevelOptions"
            :key="level.aqiLevel"
            :label="level.aqiLevel"
            :value="level.aqiLevel"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="检测日期" prop="checkDate">
        <el-date-picker clearable
                        v-model="queryParams.checkDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择检测日期"
        />
      </el-form-item>
      <el-form-item label="检测地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入检测地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:record:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格部分 -->
    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录编号" align="center" prop="recordId" width="80" />
      <el-table-column label="检测员" align="center" prop="inspectorName" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.inspectorName || '未知检测员' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检测员ID" align="center" prop="inspectorId" width="100" />
      <el-table-column label="AQI级别" align="center" prop="aqiLevel" width="120">
        <template slot-scope="scope">
          <el-tag
            :type="getAqiLevelTagType(scope.row.aqiLevel)"
            effect="plain"
          >
            {{ scope.row.aqiLevel || '未分级' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="检测日期" align="center" prop="checkDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检测时间" align="center" prop="checkTime" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.checkTime || '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检测地点" align="center" prop="location" width="200" />
      <el-table-column label="附加数据" align="center" prop="additionalData" width="250" show-overflow-tooltip />
      <!-- 派遣状态列 -->
      <el-table-column label="派遣状态" align="center" width="150">
        <template slot-scope="scope">
          <el-tag
            :type="isDispatched(scope.row.recordId) ? 'success' : 'info'"
            effect="plain"
          >
            {{ isDispatched(scope.row.recordId) ? '已派遣' : '待处理' }}
          </el-tag>
          <div v-if="isDispatched(scope.row.recordId)" style="font-size: 12px; color: #666;">
            网格员: {{ getAssignedInspector(scope.row.recordId) ? getAssignedInspector(scope.row.recordId).name : '' }}
            <span v-if="getAssignedInspector(scope.row.recordId) && getAssignedInspector(scope.row.recordId).area">
              ({{ getAssignedInspector(scope.row.recordId) ? getAssignedInspector(scope.row.recordId).area : '' }})
            </span>
            <div style="font-size: 11px; color: #999; margin-top: 2px;">
              派遣时间: {{ getAssignedInspector(scope.row.recordId) ? getAssignedInspector(scope.row.recordId).dispatchTime : '' }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" align="center" prop="submitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['system:record:query']"
          >查看</el-button>
          <!-- 派遣按钮（表格列中） -->
          <el-button
            size="mini"
            type="text"
            @click="openDispatchModal(scope.row.recordId)"
          style="color: #1890ff;"
          >
          派遣
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:record:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看检查记录对话框 -->
    <el-dialog :title="'查看检查记录 - ' + (form.recordId || '')" :visible.sync="viewOpen" width="500px" append-to-body>
      <el-form ref="viewForm" :model="form" label-width="100px" :disabled="true">
        <el-form-item label="记录编号">
          <el-input v-model="form.recordId" disabled />
        </el-form-item>
        <el-form-item label="检测员">
          <el-input v-model="form.inspectorName" disabled />
        </el-form-item>
        <el-form-item label="检测员ID">
          <el-input v-model="form.inspectorId" disabled />
        </el-form-item>
        <el-form-item label="AQI级别">
          <el-input v-model="form.aqiLevel" disabled />
        </el-form-item>
        <el-form-item label="检测日期">
          <el-input v-model="form.checkDate" disabled />
        </el-form-item>
        <el-form-item label="检测时间">
          <el-input v-model="form.checkTime" disabled />
        </el-form-item>
        <el-form-item label="检测地点">
          <el-input v-model="form.location" disabled />
        </el-form-item>
        <el-form-item label="附加数据">
          <el-input v-model="form.additionalData" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="提交时间">
          <el-input v-model="form.submitTime" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改检查记录对话框 -->
    <el-dialog :title="title" :visible.sync="editOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="记录编号" prop="recordId">
          <el-input v-model="form.recordId" disabled />
        </el-form-item>
        <el-form-item label="检测员" prop="inspectorName">
          <el-input v-model="form.inspectorName" disabled />
        </el-form-item>
        <el-form-item label="AQI级别" prop="aqiLevelId">
          <el-select
            v-model="form.aqiLevelId"
            placeholder="请选择AQI级别"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="level in aqiLevelOptions"
              :key="level.aqiLevelId"
              :label="`${level.aqiLevel} (${level.description || '无描述'})`"
              :value="level.aqiLevelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检测日期" prop="checkDate">
          <el-date-picker clearable
                          v-model="form.checkDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择检测日期"
                          style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="检测时间" prop="checkTime">
          <el-time-picker clearable
                          v-model="form.checkTime"
                          placeholder="请选择检测时间"
                          value-format="HH:mm:ss"
                          style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="检测地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入检测地点" />
        </el-form-item>
        <el-form-item label="附加数据" prop="additionalData">
          <el-input v-model="form.additionalData" type="textarea" placeholder="请输入附加数据" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 派遣对话框 -->
    <el-dialog
      title="派遣网格员"
      :visible.sync="dispatchDialogVisible"
      width="400px"
      append-to-body
    >
      <div style="padding: 20px;">
        <!-- 网格员选择 -->
        <div style="margin-bottom: 20px;">
          <div style="margin-bottom: 8px; font-weight: bold; color: #333;">
            选择网格员
            <span style="color: #f56c6c; margin-left: 4px;">*</span>
          </div>
          <el-select
            v-model="dispatchForm.inspectorId"
            placeholder="请选择"
            style="width: 100%"
            :loading="wgLoading"
            clearable
          >
            <el-option
              v-for="item in tableList"
              :key="item.id || item.wgId"
              :label="item.wgName || item.name"
              :value="item.id || item.wgId"
            />
          </el-select>
        </div>
      </div>

      <div slot="footer" style="text-align: right; padding: 10px 20px; border-top: 1px solid #eee;">
        <el-button @click="dispatchDialogVisible = false" :disabled="dispatchLoading">取消</el-button>
        <el-button
          type="primary"
          @click="confirmDispatch"
          :loading="dispatchLoading"
          :disabled="!dispatchForm.inspectorId"
        >
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 引入原有的API
import { listRecord, getRecord, delRecord, addRecord, updateRecord,dispatchInspector} from "@/api/system/record"
import { getAqiLevelOptions } from "@/api/system/bz"
// 引入网格员API
import { listTable } from "@/api/system/table"

export default {
  name: "Record",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 检查记录提交表格数据
      recordList: [],
      // AQI级别下拉选项
      aqiLevelOptions: [],
      // 查看弹出层标题
      viewOpen: false,
      // 编辑弹出层标题
      editOpen: false,
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inspectorName: null,
        aqiLevel: null,
        checkDate: null,
        location: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        aqiLevelId: [
          {required: true, message: "请选择AQI级别", trigger: "change"}
        ],
        checkDate: [
          {required: true, message: "请选择检测日期", trigger: "change"}
        ],
        checkTime: [
          {required: true, message: "请选择检测时间", trigger: "change"}
        ],
        location: [
          {required: true, message: "请输入检测地点", trigger: "blur"}
        ]
      },

      // 派遣弹窗是否显示
      dispatchDialogVisible: false,
      // 当前被操作的记录ID
      currentRecordId: null,
      // 派遣表单
      dispatchForm: {
        inspectorId: null,
        remark: ''
      },
      // 网格员列表
      tableList: [],
      // 网格员加载状态
      wgLoading: false,
      // 派遣加载状态
      dispatchLoading: false,
      // 派遣关系映射表
      dispatchMap: {}
    }
  },
  computed: {
    // 计算属性：获取当前选中的网格员信息
    selectedInspector() {
      if (!this.dispatchForm.inspectorId || this.tableList.length === 0) {
        return null
      }
      return this.tableList.find(table => table.id === this.dispatchForm.inspectorId) || null
    }
  },
  created() {
    this.getList()
    this.getAqiLevelOptions()
  },
  methods: {
    /** 查询检查记录列表 */
    getList() {
      this.loading = true
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /** 获取AQI级别下拉选项 */


    /** 加载网格员列表 */
    loadTableList() {
      if (this.tableList.length > 0 || this.wgLoading) {
        return
      }

      this.wgLoading = true
      const params = {
        pageNum: 1,
        pageSize: 100
      }

      listTable(params).then(response => {
        if (response.code === 200) {
          this.tableList = response.rows || []
          if (this.tableList.length === 0) {
            this.$message.warning('暂无可用网格员，请先添加网格员')
          }
        } else {
          this.$modal.msgError("获取网格员列表失败: " + response.msg)
          this.tableList = []
        }
        this.wgLoading = false
      }).catch(error => {
        console.error("获取网格员列表失败:", error)
        this.wgLoading = false
        this.$modal.msgError("获取网格员列表失败，请检查网络连接")
        this.tableList = []
      })
    },

    /** 跳转到网格员管理页面 */
    gotoTableManagement() {
      this.$router.push('/system/table')
      this.dispatchDialogVisible = false
    },

    /** 根据AQI级别返回标签类型 */
    getAqiLevelTagType(aqiLevel) {
      if (!aqiLevel) return 'info'
      const levelMap = {
        '优': 'success',
        '良': 'primary',
        '轻度污染': 'warning',
        '中度污染': 'warning',
        '重度污染': 'danger',
        '严重污染': 'danger'
      }
      return levelMap[aqiLevel] || 'info'
    },

    /** 判断记录是否已被派遣 */
    isDispatched(recordId) {
      return !!this.dispatchMap[recordId]
    },

    /** 获取为指定记录派遣的网格员信息 */
    getAssignedInspector(recordId) {
      return this.dispatchMap[recordId] || null
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.reset()
      const recordId = row.recordId || this.ids[0]
      getRecord(recordId).then(response => {
        this.form = response.data
        this.viewOpen = true
      })
    },

    // 取消按钮
    cancel() {
      this.editOpen = false
      this.viewOpen = false
      this.reset()
    },

    // 表单重置
    reset() {
      this.form = {
        recordId: null,
        inspectorId: null,
        inspectorName: null,
        aqiLevelId: null,
        aqiLevel: null,
        checkDate: null,
        checkTime: null,
        location: null,
        additionalData: null,
        submitTime: null
      }
      this.resetForm("form")
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const recordId = row.recordId || this.ids[0]
      getRecord(recordId).then(response => {
        this.form = response.data
        this.editOpen = true
        this.title = "修改检查记录"
      })
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.editOpen = false
              this.getList()
            })
          } else {
            this.$modal.msgError("管理员不能新增记录，记录由检测员提交")
          }
        }
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const recordIds = row.recordId || this.ids
      this.$modal.confirm('是否确认删除检查记录编号为"' + recordIds + '"的数据项？').then(() => {
        return delRecord(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },

    /** 导出按钮操作 */
    handleExport() {
      const params = {
        ...this.queryParams
      }
      this.download('system/record/export', params, `检查记录_${new Date().getTime()}.xlsx`)
    },

    /** 打开派遣对话框 */
    openDispatchModal(recordId) {
      console.log('打开派遣，记录ID:', recordId)
      this.currentRecordId = recordId
      this.dispatchForm = {inspectorId: null}
      this.dispatchDialogVisible = true

      // 直接加载网格员
      this.loadSimpleTableList()
    },

    /** 确认派遣 */
    confirmDispatch() {

      if (!this.dispatchForm.inspectorId) {
        this.$message.warning('请选择网格员')
        return
      }

      this.dispatchLoading = true

      const requestData = {
        recordId: this.currentRecordId,
        inspectorId: this.dispatchForm.inspectorId
      }
      console.log('派遣请求数据：', requestData)  // 添加调试日志
      console.log('派遣接口路径：', '/system/record/dispatch')  // 根据实际修改


      dispatchInspector(requestData).then(response => {
        console.log('派遣接口响应：', response)
        if (response.code === 200) {
          this.$message.success('派遣成功')
          this.dispatchDialogVisible = false
          this.getList() // 刷新表格
        } else {
          this.$message.error(response.msg || '派遣失败')
        }
      }).catch(() => {
        this.$message.error('网络错误')
      }).finally(() => {
        this.dispatchLoading = false
      })
    },
    /** 加载网格员 - 简化版 */
    /** 加载网格员 - 简化版 */
    loadSimpleTableList() {
      this.wgLoading = true
      this.tableList = []

      listTable({ pageNum: 1, pageSize: 100 }).then(response => {
        console.log('网格员API响应：', response)  // 添加调试日志
        if (response.code === 200 && response.rows) {
          console.log('网格员数据结构：', response.rows[0])  // 查看第一个元素

          this.tableList = response.rows
          console.log('加载的网格员数量：', this.tableList.length)
        } else {
          this.$message.warning('无网格员数据')
        }
      }).catch(error => {
        console.error('加载网格员失败：', error)
        this.$message.error('加载网格员列表失败')
      }).finally(() => {
        this.wgLoading = false
      })
    }

  }
}
</script>

<style scoped>
.el-tag {
  margin: 2px;
}
</style>
