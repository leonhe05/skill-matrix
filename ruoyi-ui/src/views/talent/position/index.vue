<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="岗位名称" prop="positionName">
        <el-input
          v-model="queryParams.cLabel"
          placeholder="请输入岗位名称(中)"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="岗位名称" prop="positionName">
        <el-input
          v-model="queryParams.eLabel"
          placeholder="请输入岗位名称(英)"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级岗位"
                    prop="parentId"
                    :rules="[
                      { type: 'number', message: '岗位编号必须为数字值'}
                    ]">
        <el-input
          v-model.number="queryParams.parentId"
          placeholder="输入父岗位编号查找其子岗位"
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
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" highlight-current-row :data="positionList" row-key="key" :tree-props="{children: 'competences',hasChildren: 'hasChildren'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :selectable="selectable"/>
      <el-table-column label="岗位编号" width="100" align="center" prop="positionId" />
      <el-table-column label="岗位名称(中)" align="center" prop="cLabel" :show-overflow-tooltip="true" />
      <el-table-column label="岗位名称(英)" align="center" prop="eLabel" :show-overflow-tooltip="true" />
      <el-table-column label="上级岗位" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span class="link-type" @click="searchParent(scope.row)" v-if="scope.row.competences != undefined">
            {{ scope.row.parentName }}
          </span>
          <span class="link-type"  v-else>
            {{ scope.row.parentName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="路径" align="center" prop="path" :show-overflow-tooltip="true" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.competences != undefined"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['talent:position:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.competences != undefined"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['talent:position:remove']"
          >删除</el-button>
          <el-dropdown size="mini" v-if="scope.row.competences != undefined" @command="(command) => handleCommand(command, scope.row)">
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleAssignCompetence" icon="el-icon-key"
                                >分配技能</el-dropdown-item>
              <el-dropdown-item command="handleAssignOwner" icon="el-icon-circle-check"
                                >分配人员</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
        <el-form-item label="岗位名称(中)" prop="cLabel">
          <el-input v-model="form.cLabel" placeholder="请输入岗位名称" />
        </el-form-item>
        <el-form-item label="岗位名称(英)" prop="eLabel">
          <el-input v-model="form.eLabel" placeholder="请输入岗位名称" />
        </el-form-item>
        <el-form-item label="上级岗位" prop="parentId">
          <el-cascader v-model="form.casPosition" ref="position" :props="positionProps" :options="positionTree" placeholder="请选择上级岗位" clearable></el-cascader>
        </el-form-item>
        <el-form-item label="技能">
          <el-cascader v-model="form.casCompetence" ref="competence" :props="competenceProps" :options="competenceTree" clearable></el-cascader>
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
  </div>
</template>

<script>
import { listPosition,listPositionTree,addPosition,updatePosition,delPosition } from "@/api/talent/position";
import { listCompetence,listCompetenceTree } from "@/api/talent/competence";

export default {
  name: "position",
  data() {
    return {
      test: [],
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
      positionList: [],
      // 岗位树
      positionTree: [],
      // 技能树
      competenceTree: [],
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
          { required: true, message: "岗位中文名称不能为空", trigger: "blur" }
        ],
        eLabel: [
          { required: true, message: "岗位英文名称不能为空", trigger: "blur" }
        ]
      },
      // cascader级联选框
      positionProps: {
        checkStrictly: true,
        label: 'cLabel',
        value: 'positionId'
      },
      competenceProps: {
        multiple: true,
        label: 'cLabel',
        value: 'competenceId'
      },
    };
  },
  created() {
    this.getList();
    this.getTree();
  },
  methods: {
    /** 查询上级岗位列表 */
    getList() {
      this.loading = true;
      this.positionList = [];
      listPosition(this.queryParams).then(response => {
          response.rows.forEach(item => {
            item.disable = item.disable == '1';
            this.positionList.push(item);

            item.key = item.positionId;     //为树重新生成key,避免控制台报重复key的错
            item.competences.forEach(competence => {
              competence.key = item.key + '-' + competence.competenceId;
            })
          })
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 获取岗位树 */
    getTree() {
      listPositionTree().then(response => {
          this.positionTree = response.data;
      });
      listCompetenceTree().then(response => {
          this.competenceTree = response.data;
      });
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
        positionName: undefined,
        parentId: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleAssignCompetence":
          this.handleAssignCompetence(row);
          break;
        case "handleAssignOwner":
          this.handleAssignOwner(row);
          break;
        default:
          break;
      }
    },
    /** */
    handleAssignCompetence(row){
      const positionId = row.positionId;
      this.$router.push("/assign/competence/" + positionId);
    },
    /** 分配主管操作 */
    handleAssignOwner: function(row) {
      const positionId = row.positionId;
      this.$router.push("/assign/owner/" + positionId);
    },
    /** 表格row的单选框是否可选*/
    selectable(row,index){
        return row.competences != undefined;
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
      this.title = "添加岗位";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.positionId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      console.log(row);
      const positionId = row.positionId || this.ids[0];
      const positionList = JSON.parse(JSON.stringify(this.positionList))
      this.form = positionList.find(function (item) {  // find 返回符合筛选挑条件的第一个元素
          return item.positionId == positionId;
      })
      /** 根据该岗位的技能填充模态里的competence级联框 */
      this.form.casCompetence = [];
      this.form.competences.forEach(item => {
        this.form.casCompetence.push(item.path.split('-'));
      })
      /** 根据该岗位的路径减掉自己的id，得出父亲的路径，再填充模态里的position级联框 */
      this.form.casPosition = this.form.path.split('-').map(item => parseInt(item));
      this.form.casPosition.pop();

      this.title = "修改岗位";
      this.open = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          /** switch的true or false 转换成数据库存储的1 or 0 */
          this.form.disable = this.form.disable ? '1' : '0';

          /** 从级联框中获取上级岗位 */
          let positionNode = this.$refs.position.getCheckedNodes()[0]
          if(positionNode != undefined)
            this.form.parentId = positionNode.data.positionId;

          /** 从级联框中获取其所有技能 */
          this.form.competences = this.$refs.competence.getCheckedNodes(true).map(item => {
            return { 'competenceId' : item.value};
          })
          if (this.form.positionId != undefined) {
            updatePosition(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPosition(this.form).then(response => {
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
      const positionIds = row.positionId || this.ids;
      console.log(positionIds)
      this.$confirm('是否确认删除岗位编号为"' + positionIds + '"的岗位及其所有下级岗位?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPosition(positionIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 点击父岗位 **/
    searchParent(row){
      this.queryParams.cLabel = row.parentName;
      this.handleQuery();
    },
  }
};
</script>

<style>
  .el-table__row--level-1{
    background: #eff6f9 !important;
  }
</style>
