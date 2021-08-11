<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="技能名称" prop="competenceName">
        <el-input
          v-model="queryParams.cLabel"
          placeholder="请输入技能名称(中)"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="技能名称" prop="competenceName">
        <el-input
          v-model="queryParams.eLabel"
          placeholder="请输入技能名称(英)"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级技能"
                    prop="parentId"
                    :rules="[
                      { type: 'number', message: '技能编号必须为数字值'}
                    ]">
        <el-input
          v-model.number="queryParams.parentId"
          placeholder="输入父技能编号查找其子技能"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:dict:add']"
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
          v-hasPermi="['system:dict:edit']"
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
          v-hasPermi="['system:dict:remove']"
        >删除</el-button>
        <el-button
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="competenceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="技能编号" width="100" align="center" prop="competenceId" />
      <el-table-column label="技能名称(中)" align="center" prop="cLabel" :show-overflow-tooltip="true" />
      <el-table-column label="技能名称(英)" align="center" prop="eLabel" :show-overflow-tooltip="true" />
      <el-table-column label="上级技能" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
            <span class="link-type" @click="searchParent(scope.row)">{{ scope.row.parentName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="路径" align="center" prop="path" :show-overflow-tooltip="true" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['talent:competence:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['talent:competence:remove']"
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" @close="closeDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="技能名称(中)" prop="cLabel">
          <el-input v-model="form.cLabel" placeholder="请输入技能名称" />
        </el-form-item>
        <el-form-item label="技能名称(英)" prop="eLabel">
          <el-input v-model="form.eLabel" placeholder="请输入技能名称" />
        </el-form-item>
        <el-form-item v-if="treeShow" label="上级技能" prop="parentId">
          <el-cascader ref="competence" :props="props"  placeholder="请选择上级技能" clearable></el-cascader>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="禁用" prop="disable">
          <el-switch v-model="form.disable" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
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
import { listCompetence,addCompetence,updateCompetence,delCompetence } from "@/api/talent/competence";
import { importTemplate,exportCompetence } from "@/api/talent/competence";
import { getToken } from "@/utils/auth";

export default {
  name: "competence",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 字典表格数据
      competenceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cLabel: undefined,
        eLabel: undefined,
        parentId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cLabel: [
          { required: true, message: "技能中文名称不能为空", trigger: "blur" }
        ],
        eLabel: [
          { required: true, message: "技能英文名称不能为空", trigger: "blur" }
        ]
      },
      // cascader级联选框
      props: {
        checkStrictly: true,
        lazy: true,
        lazyLoad:this.cascaderLazyLoad
      },
      // 通过v-if组件来创建销毁技能树，强制刷新
      treeShow: true,
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "技能导入",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/talent/competence/importData"
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询上级技能列表 */
    getList() {
      this.loading = true;
      this.competenceList = [];
      listCompetence(this.queryParams).then(response => {
          response.rows.forEach(item => {
            item.disable = item.disable == '1';
            this.competenceList.push(item);
          })
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        dictId: undefined,
        competenceName: undefined,
        parentId: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.$refs['queryForm'].validate((valid) => {
        if (valid) {
          this.queryParams.pageNum = 1;
          this.getList();
        } else {
          this.msgError("表单校验未通过，请重新检查提交内容");
        }
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        cLabel: undefined,
        eLabel: undefined,
        status: undefined
      },
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加技能";
      this.treeShow = true;
    },
    closeDialog() {
      this.treeShow = false;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.competenceId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const competenceId = row.competenceId || this.ids[0];
      const competenceList = JSON.parse(JSON.stringify(this.competenceList))
      this.form = competenceList.find(function (item) {  // find 返回符合筛选挑条件的第一个元素
          return item.competenceId == competenceId;
      })
      this.open = true;
      this.title = "修改技能";
      this.treeShow = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.disable = this.form.disable ? '1' : '0';
          let competenceNode = this.$refs.competence.getCheckedNodes()[0]
          if(competenceNode != undefined)
            this.form.parentId = competenceNode.data.value;
          if (this.form.competenceId != undefined) {
            updateCompetence(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCompetence(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const competenceIds = row.competenceId || this.ids;
      console.log(competenceIds)
      this.$confirm('是否确认删除技能编号为"' + competenceIds + '"的技能及其所有下级技能?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCompetence(competenceIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 点击父技能 **/
    searchParent(row){
      this.queryParams.cLabel = row.parentName;
      this.handleQuery();
    },
    /** 级联选框懒加载方法 **/
    cascaderLazyLoad (node, resolve) {
      if (!node) {
        return false
      }
      const { level } = node;
      listCompetence({parentId: level == 0 ? level : node.data.value}).then(response => {
          const nodes = response.rows
            .map(item => ({
            value: item.competenceId,
            label: item.cLabel
          }));
          // 通过调用resolve将子节点数据返回，通知组件数据加载完成
          resolve(nodes);
        }
      );
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
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有用户数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportCompetence(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function() {});
    }
  }
};
</script>
