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
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工号" align="center" prop="eeId" />
      <el-table-column label="中文名" align="center" prop="chiName" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="英文名" align="center" prop="engName" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="等级" align="center" prop="grade" />
      <el-table-column label="性别" align="center" prop="gender" />
      <el-table-column label="入职时间"  width="155" align="center" prop="joinDate" />
      <el-table-column label="部门" align="center" prop="department" />
      <el-table-column label="组别" width="155" align="center" prop="section" />
      <el-table-column label="子组别" width="155" align="center" prop="subsection" />
      <el-table-column label="小公司" align="center" prop="miniCompany" />
      <el-table-column label="职位" align="center" prop="position" />
      <el-table-column label="技能等级" align="center" prop="skillGrade" />
      <el-table-column label="在职" align="center" prop="onJob" >
        <template #default="scope">
          <span>{{ scope.row.onJob == '1' ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录账号" align="center" prop="nwsId" />
      <el-table-column label="状态" align="center" prop="enable" >
        <template #default="scope">
          <span>{{ scope.row.enableLogin == '1' ? '启用' : '未启用' }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作"  align="center" width="120" class-name="small-padding fixed-width" >
        <template slot-scope="scope">
           <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" >修改</el-button >
           <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAddRole(scope.row)" >角色</el-button >
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
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">

        <el-form-item label="nws账号" prop="nwsId">
          <el-input v-model="form.nwsId" type="text" placeholder="请输入nws账号"></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enable">
          <el-switch v-model="form.enableLogin" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="roleForm.title" :visible.sync="roleForm.open" width="500px">
      <el-form ref="roleForm" :model="roleForm" :rules="rules" label-width="120px">
        <el-form-item label="管理员" prop="enable">
          <el-switch v-model="roleForm.role" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRoleForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listEmployee,updateEmployee,exportUser,importTemplate  } from "@/api/talent/employee";
import { getUserRole,updateUserRole } from "@/api/system/role";
import { getToken } from "@/utils/auth";

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
      form: {
        enable: true,
      },
      roleForm: {
        title: "设置管理员",
        open: false,
        roleId: '',
        role: false,
      },
      // 弹出层标题
      title: "修改用户登录信息",
      // 是否显示弹出层
      open: false,
      // 表单校验
      rules: {

      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/talent/employee/importData"
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有用户数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportUser(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function() {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then(response => {
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
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
      this.form.enableLogin = this.form.enableLogin == '1';
    },
    handleAddRole(row) {
      getUserRole({eeId:row.eeId}).then(response => {
        this.roleForm.role = response.data.roleId == 1
        this.roleForm.open = true;
        this.roleForm.eeId = row.eeId;
      });
    },
    /** 提交表单 */
    submitRoleForm() {
      const data = {};
      data.roleId = this.roleForm.role ? 1 : 2;
      data.eeId = this.roleForm.eeId
      updateUserRole(data).then(response => {
          this.msgSuccess("修改成功");
          this.roleForm.open = false;
      });
    },
    /** 提交表单 */
    submitForm() {
      this.form.enableLogin = this.form.enableLogin ? '1' : '0'
      updateEmployee(this.form).then(response => {
          this.msgSuccess("修改成功");
          this.open = false;
          this.getList();
      });
    },
    cancel() {
      this.open = false;
      this.roleForm.open = false;
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
