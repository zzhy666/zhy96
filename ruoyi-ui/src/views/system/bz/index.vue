<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="空气质量指数级别" prop="aqiLevel">
        <el-input
          v-model="queryParams.aqiLevel"
          placeholder="请输入空气质量指数级别"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空气质量描述" prop="description">
        <el-input
          v-model="queryParams.description"
          placeholder="请输入空气质量描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="颜色名称" prop="colorName">
        <el-input
          v-model="queryParams.colorName"
          placeholder="请输入颜色名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="颜色代码" prop="colorCode">
        <el-input
          v-model="queryParams.colorCode"
          placeholder="请输入颜色代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间" prop="updatedAt">
        <el-date-picker clearable
          v-model="queryParams.updatedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择更新时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:bz:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:bz:edit']"
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
          v-hasPermi="['system:bz:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:bz:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bzList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="级别编号" align="center" prop="levelId" />
      <el-table-column label="空气质量指数级别" align="center" prop="aqiLevel" />
      <el-table-column label="空气质量描述" align="center" prop="description" />
      <el-table-column label="颜色名称" align="center" prop="colorName" />
      <el-table-column label="颜色代码" align="center" prop="colorCode" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:bz:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:bz:remove']"
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

    <!-- 添加或修改空气质量指数(AQI)级别标准对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="空气质量指数级别" prop="aqiLevel">
              <el-input v-model="form.aqiLevel" placeholder="请输入空气质量指数级别" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="空气质量描述" prop="description">
              <el-input v-model="form.description" placeholder="请输入空气质量描述" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="颜色名称" prop="colorName">
              <el-input v-model="form.colorName" placeholder="请输入颜色名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="颜色代码" prop="colorCode">
              <el-input v-model="form.colorCode" placeholder="请输入颜色代码" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="创建时间" prop="createdAt">
              <el-date-picker clearable
                v-model="form.createdAt"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择创建时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="更新时间" prop="updatedAt">
              <el-date-picker clearable
                v-model="form.updatedAt"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择更新时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBz, getBz, delBz, addBz, updateBz } from "@/api/system/bz"

export default {
  name: "Bz",
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
      // 空气质量指数(AQI)级别标准表格数据
      bzList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        aqiLevel: null,
        description: null,
        colorName: null,
        colorCode: null,
        createdAt: null,
        updatedAt: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        aqiLevel: [
          { required: true, message: "空气质量指数级别不能为空", trigger: "blur" }
        ],
        description: [
          { required: true, message: "空气质量描述不能为空", trigger: "blur" }
        ],
        colorName: [
          { required: true, message: "颜色名称不能为空", trigger: "blur" }
        ],
        colorCode: [
          { required: true, message: "颜色代码不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询空气质量指数(AQI)级别标准列表 */
    getList() {
      this.loading = true
      listBz(this.queryParams).then(response => {
        this.bzList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        levelId: null,
        aqiLevel: null,
        description: null,
        colorName: null,
        colorCode: null,
        createdAt: null,
        updatedAt: null
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
      this.ids = selection.map(item => item.levelId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加空气质量指数(AQI)级别标准"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const levelId = row.levelId || this.ids
      getBz(levelId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改空气质量指数(AQI)级别标准"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.levelId != null) {
            updateBz(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBz(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const levelIds = row.levelId || this.ids
      this.$modal.confirm('是否确认删除空气质量指数(AQI)级别标准编号为"' + levelIds + '"的数据项？').then(function() {
        return delBz(levelIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/bz/export', {
        ...this.queryParams
      }, `bz_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
