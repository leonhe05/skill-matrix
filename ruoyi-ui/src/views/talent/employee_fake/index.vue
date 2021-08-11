<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
       <el-form-item label="员工号" prop="eeId">
        <el-input
          v-model="queryParams.eeId"
          placeholder="请输入员工编号"
          clearable
          size="small"
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item label="中文名" prop="chiName">
        <el-input
          v-model="queryParams.chiName"
          placeholder="请输入中文名"
          clearable
          size="small"
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item label="英文名" prop="engName">
        <el-input
          v-model="queryParams.engName"
          placeholder="请输入英文名"
          clearable
          size="small"
          style="width: 240px"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工号" align="center" prop="eeId" />
      <el-table-column label="英文名" align="center" prop="engName" width="130"/>
      <el-table-column label="中文名" align="center" prop="chiName" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="部门" align="center" prop="department" />
      <el-table-column label="等级" align="center" prop="grading" />
      <el-table-column label="性别" align="center" prop="sex" />
      <el-table-column label="位置" align="center" prop="position" />
      <el-table-column label="登录账号" align="center" prop="nwsId" />
      <el-table-column label="状态" align="center" prop="enable" >
        <template #default="scope">
          <span>{{ scope.row.enable == '1' ? '启用' : '未启用' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作"  align="center" width="100" class-name="small-padding fixed-width" >
      <template slot-scope="scope">
         <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" >修改</el-button >
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="nws账号" prop="nwsId">
          <el-input v-model="form.nwsId" type="text" placeholder="请输入nws账号"></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enable">
          <el-switch v-model="form.enable" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import { listEmployee,updateEmployee } from "@/api/talent/employee_fake";

export default {
  name: "Logininfor",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eeId: '',
      },
      // 表单参数
      form: {},
      // 弹出层标题
      title: "修改用户登录信息",
      // 是否显示弹出层
      open: false,
      // 表单校验
      rules: {

      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listEmployee(this.queryParams).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.open = true;
      this.form = JSON.parse(JSON.stringify(row));
      this.form.enable = true;
    },
    /** 提交表单 */
    submitForm() {
      this.form.enable = this.form.enable ? '1' : '0'
      updateEmployee(this.form).then(response => {
          this.msgSuccess("修改成功");
          this.open = false;
          this.getList();
      });
    },
    cancel() {
      this.open = false;
    }
  }
};
</script>



<style scoped >
.el-form--inline .el-form-item {
    display: inline-block;
    margin-right: 10px;
    vertical-align: top;
}

.el-form-item {
    margin-bottom: 6px;
}

</style>
