<template>
  <div class="app-container">
    <h4 class="form-header h4">基本信息</h4>

    <el-row>
      <el-col :span="2"><div style="width:1px;height:1px"></div></el-col>
      <el-col :span="20">
        <el-table v-loading="positionLoading" size="mini" highlight-current-row :data="position">
          <el-table-column label="岗位编号" width="100" align="center" prop="positionId" />
          <el-table-column label="岗位名称(中)"  align="center" prop="cLabel" :show-overflow-tooltip="true" />
          <el-table-column label="岗位名称(英)"  align="center" prop="eLabel" :show-overflow-tooltip="true" />
          <el-table-column label="上级岗位"  align="center" prop="parentName" :show-overflow-tooltip="true" />
          <el-table-column label="备注"  align="center" prop="remark" :show-overflow-tooltip="true" />
        </el-table>
      </el-col>
    </el-row>


    <el-row>
      <el-col :span="11">
        <h4 class="form-header h4" style="margin-top:18px">搜索人员信息</h4>
        <el-table v-loading="employeeLoading" size="mini" :data="employeeList" >
          <el-table-column label="员工号" align="center" prop="eeId" width="100"/>
          <el-table-column label="英文名" align="center" prop="engName" />
          <el-table-column label="中文名" align="center" prop="chiName"  :show-overflow-tooltip="true">
            <template #header>
              <el-input
                v-model="queryParams.eeId"
                size="mini"
                placeholder="输入工号搜索">
                <template #append>
                  <el-button icon="el-icon-search" @click="getEmployeeList" size="mini"></el-button>
                </template>
              </el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="2">
        <el-button @click="transferEmployee" type="primary" size="mini" style="margin: auto;display: block;margin-top: 100px;">
          >
        </el-button>
      </el-col>
      <el-col :span="11">
        <h4 class="form-header h4" style="margin-top:18px">已添加人员信息</h4>
        <el-table ref="ownerTable" v-loading="ownerLoading" size="mini" :data="ownerList" :header-row-style="{'height':'41px'}">
          <el-table-column label="员工号" align="center" prop="eeId" width="130">
            <template #default="scope">
              <el-tag size="mini" v-if="scope.row.owner == '1'">主管</el-tag>
              <span style="margin-left: 10px">{{ scope.row.eeId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="英文名" align="center" prop="engName" />
          <el-table-column label="中文名" align="center" prop="chiName"  :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" width="170" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                v-if="scope.row.owner == '1'"
                @click="updateSupervisor(scope.row, '0')"
              >取消主管</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                v-else
                @click="updateSupervisor(scope.row, '1')"
              >设为主管</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" layout="total, prev, pager, next"/>
      </el-col>
    </el-row>



    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
        <el-button @click="close()" size="small">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { listPosition,listPositionEmployee,assignOwner,updatePositionEmployee,deletePositionEmployee } from "@/api/talent/position";
import { listEmployee } from "@/api/talent/employee";

export default {
  name: "AuthRole",
  data() {
    return {
       // 遮罩层
      positionLoading: true,
      employeeLoading: false,
      ownerLoading: false,

      // 员工list
      employeeList: [],
      // owner list
      ownerList: [],

      // 查询员工参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      position: [],
      // 分页信息
      total: 1,
      pageNum: 1,
      pageSize: 10,
      // 选中角色编号
      ownerEeIds:[],
      // 角色信息
      roles: [],
      // 用户信息
      form: {}
    };
  },
  created() {
    const positionId = this.$route.params && this.$route.params.positionId;
    if (positionId) {
      this.positionLoading = true;
      listPosition({positionId: positionId}).then(response => {
          this.position = response.rows;
          this.positionLoading = false;
        }
      );
      this.ownerLoading = true;
      listPositionEmployee({positionId: positionId}).then(response => {
          this.ownerList = response.rows;
          this.ownerLoading = false;
        }
      );
    }
  },
  methods: {
    getEmployeeList() {
      this.employeeLoading = true;
      listEmployee(this.queryParams).then(response => {
          this.employeeList = response.rows;
          this.employeeLoading = false;
        }
      );
    },
    getPositionEmployee() {
      this.ownerLoading = true;
      listPositionEmployee({positionId: this.position[0].positionId}).then(response => {
          this.ownerList = response.rows;
          this.ownerLoading = false;
        }
      );
      this.employeeList = [];
    },
    /** 将搜索表格的employee转移到已添加表格中 */
    transferEmployee() {
      const data = {};
      data.positionId = this.position[0].positionId;
      data.eeId = this.employeeList[0].eeId;
      data.owner = '0';
      assignOwner(data).then((response) => {
        this.msgSuccess("添加成功");
        this.getPositionEmployee();
      });
    },
    /** 更新主管 */
    updateSupervisor(row, owner) {
      const data = {};
      data.positionId = this.position[0].positionId;
      data.eeId = row.eeId;
      data.owner = owner;
      updatePositionEmployee(data).then(response => {
        this.msgSuccess("修改成功");
        this.getPositionEmployee();
      })
    },
    /** 删除按钮 */
    handleDelete(row) {
      const data = {};
      data.positionId = this.position[0].positionId;
      data.eeId = row.eeId;
      this.$confirm('是否确认删除该用户?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return deletePositionEmployee(data);
        }).then(() => {
          return this.getPositionEmployee();
        }).catch(() => {});
    },
    /** 关闭按钮 */
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/system/position" });
    },
  },
};
</script>
