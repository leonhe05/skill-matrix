<template>
  <div class="app-container">
    <el-form  ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="请选择岗位" prop="eeId">
        <el-cascader :options="options"
                     ref="position"
                     @change = "changePosition"
                     :props="{ checkStrictly: true,value: 'positionId',label: 'cLabel',disabled: 'disable' }"
                     size="small" style="width:300px"/>
      </el-form-item>
      <transition name="el-fade-in-linear">
        <el-form-item v-if="owner == 1">
          <el-button type="primary" @click="edit" v-if="!edited" >评分</el-button>
          <el-button type="warning" @click="cancelEdit" v-if="edited">返回</el-button>
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
          >导出</el-button>
        </el-form-item>
      </transition>

    </el-form>
    <div class="rating-container">
      <img v-if="logo" :src="logo" style="position:absolute" />
      <el-row>
        <el-row class="title">
          Skill Matrix Rating
        </el-row>
        <el-row>
          <el-col :span="20" style="display: flex;align-items: center;">
                <el-progress type="circle" :width="30" :percentage="20" :color="colors"/><span class="example-text">1-未掌握</span>
                <el-progress type="circle" :width="30" :percentage="40" :color="colors"/><span class="example-text">2-培训中</span>
                <el-progress type="circle" :width="30" :percentage="60" :color="colors"/><span class="example-text">3-可在监管下作业</span>
                <el-progress type="circle" :width="30" :percentage="80" :color="colors"/><span class="example-text">4-可独立作业</span>
                <el-progress type="circle" :width="30" :percentage="100" :color="colors"/><span class="example-text">5-可培训他人</span>
          </el-col>
          <el-col :span="4">
            <span style="color: #666666;font-weight: 600;line-height: 40px;">当前岗位: {{ position.cLabel }}</span>
          </el-col>
        </el-row>
      </el-row>
      <transition name="el-fade-in-linear">
        <div v-if="show">
          <el-table
            highlight-current-row
            :default-sort = "{prop: 'sum', order: 'descending'}"
            size="medium"
            :data="rating"
            style="width: 100%">
            <el-table-column prop="name" fixed="left" label="员工 \ 技能" width="120" align="center">
            </el-table-column>
            <el-table-column align="center" v-for="col in competences" min-width="120" :prop="'competence' + col.competenceId" :label="col.cLabel">
              <template #default="scope">
                <el-progress type="circle" :width="35"
                             v-if="!edited"
                             :percentage="scope.row['competence' + col.competenceId] == undefined ? 0 : parseInt(scope.row['competence' + col.competenceId]) * 20"
                             :color="colors"/>

                <el-popover
                  placement="top"
                  :width="250"
                  trigger="hover"
                  v-if="edited">
                  <template #reference>
                    <el-progress type="circle" :width="35"
                                 :percentage="scope.row['competence' + col.competenceId] == undefined ? 0 : parseInt(scope.row['competence' + col.competenceId]) * 20"
                                 :color="colors"/>
                  </template>
                  <div style="height:20px;font-family: PingFang SC;font-weight: 600;" v-if="scope.row['lastRating' + col.competenceId] > 0">
                    更改记录：{{scope.row['lastRating' + col.competenceId]}} ->
                    {{scope.row['competence' + col.competenceId]}}
                  </div>
                  <div style="height:25px;font-size:12px;color:#999;" v-if="scope.row['lastRating' + col.competenceId] > 0">
                    {{scope.row['updateBy' + col.competenceId]}} / {{scope.row['updateTime' + col.competenceId]}}
                  </div>
                  <div style="height:25px;font-size:12px;color:#999;" v-else-if="scope.row['competence' + col.competenceId] > 0">
                    首评 {{scope.row['createBy' + col.competenceId]}} / {{scope.row['createTime' + col.competenceId]}}
                  </div>
                  <el-rate
                    @change="changeRating(scope.row.eeId, col.competenceId, $event)"
                    v-model="scope.row['competence' + col.competenceId]"
                    show-text
                    :texts="texts"
                    :colors="colors2">
                  </el-rate>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column fixed="right" sortable prop="sum" label="总分 / 百分位" width="120" align="center">
              <template #default="scope">
                <span style="margin-left: 10px">{{ scope.row.sum }}</span>
                <span style="margin-left: 10px">
                  <el-tag class="per-tag" v-if="percentNum.length != 0" :color="tagColors(scope.row.percentage)">{{ scope.row.percentage }}%</el-tag>
                </span>
              </template>
            </el-table-column>
          </el-table>


          <el-row style="margin-top:10px;">
            <el-col :span="1" v-for="(item,i) in percentNum">
              <div class="percentage-tips" :style="{ background: percentColors[i] }">{{ (i+1) * 10 }} %</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="1" v-for="(item,i) in percentNum">
              <div class="percentage-tips percentage-index">{{item}}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
          </el-row>

        </div>
      </transition>
      <el-table
        v-if="tempShow"
        size="medium"
        :data="temp"
        style="width: 100%">
        <el-table-column prop="name" label="" align="center">
        </el-table-column>
      </el-table>
      <el-row></el-row>
    </div>

  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'
import store from "@/store/index";
import { listMyPositionTree,listRating,checkOwner,editRating } from "@/api/talent/rating";
import { listCompetenceByPosition } from "@/api/talent/competence";
import { exportRating } from "@/api/talent/rating";

export default {
  data() {
    return {
      texts: ['未掌握', '培训中', '可以监管下作业', '可独立作业', '可培训他人'],
      user: {},
      owner: 0,
      edited: false,
      logo: logoImg,
      options: [],
      competences: [],
      position: {},
      temp: [],
      tempShow: true,
      rating: [],//[{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'},{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'},{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'},{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'},{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'},{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'},{name: '张三', 'competence8' : '5', 'competence10' : '1', 'competence27' : '3', 'competence40' : '4'}],
      show: false,
      colors: [
          {color: '#ff5722', percentage: 30},
          {color: '#ff9800', percentage: 50},
          {color: '#ffeb3b', percentage: 70},
          {color: '#bed65e', percentage: 90},
          {color: '#4caf50', percentage: 100}
      ],
      colors2: ['#ff5722', '#ffeb3b','#4caf50'],
      percentNum: [],
      percentColors: ['#ff5722','#ff711e','#ff901e','#ecb843','#ffc522','#eadb73','#c2ea73',' #85de32','#43e600','']
    };
  },
  created() {
    this.user = this.$store.state.user.info;
    this.getTree();
  },
  methods: {
      increase() {
        this.percentage += 20;
        if (this.percentage > 100) {
          this.percentage = 20;
        }
      },
    /** 获取岗位树 */
    getTree() {
      listMyPositionTree().then(response => {
          var temp = JSON.stringify(response.data);
          this.options = JSON.parse(temp.replaceAll('"true"','true').replaceAll('"false"','false'));
      });
    },
    changePosition() {
      this.show = false;
      this.tempShow = false;
      /** 从级联框中获取选中岗位 */
      const node = this.$refs.position.getCheckedNodes()[0];
      this.position = node.data;
      const position = {'positionId' : node.value};

      this.getCompetence(position);
      this.getRating(position);
      const query = {'positionId' : node.value,'eeId' : this.user.eeId};
      this.checkOwner(query);
    },
    /** 判断员工是否为该岗位的主管 */
    checkOwner(query){
      checkOwner(query).then(response => {
        this.owner = response.data;
      })
    },
    /** 获取该岗位的技能 */
    getCompetence(query) {
      listCompetenceByPosition(query).then(response => {
        this.competences = response.data;
        this.show = true;
      });
    },
    /** 获取该岗位所有员工的评分 */
    getRating(query) {
      listRating(query).then(response => {
        const employeeList = [];
        response.data.forEach(item => {
            const result = employeeList.find(function (employee) {  // find 返回符合筛选挑条件的第一个元素
                              return employee.eeId == item.eeId;
                           })
            if(result == undefined){
                const len = employeeList.push({
                                'eeId':item.eeId,
                                'chiName': item.chiName,
                                'engName': item.engName,
                                'name': item.chiName,
                                'rating': item.rating
                            });
                employeeList[len - 1]['competence' + item.competenceId] = item.rating;
                employeeList[len - 1]['lastRating' + item.competenceId] = item.lastRating;
                employeeList[len - 1]['updateBy' + item.competenceId] = item.updateBy;
                employeeList[len - 1]['updateTime' + item.competenceId] = item.updateTime;
                employeeList[len - 1]['createBy' + item.competenceId] = item.createBy;
                employeeList[len - 1]['createTime' + item.competenceId] = item.createTime;
                employeeList[len - 1]['sum'] = item.rating;
            } else{
                result['competence' + item.competenceId] = item.rating;
                result['lastRating' + item.competenceId] = item.lastRating;
                result['updateBy' + item.competenceId] = item.updateBy;
                result['updateTime' + item.competenceId] = item.updateTime;
                result['createBy' + item.competenceId] = item.createBy;
                result['createTime' + item.competenceId] = item.createTime;
                result['sum'] += item.rating;
            }
        })

        const temp = JSON.parse(JSON.stringify(employeeList));
        // 排序
        temp.sort(function (a,b){
          return a.sum - b.sum;
        });
        // 生成百分位数组
        this.percentage(temp);
        // 赋值百分位
        employeeList.forEach(item => {
          const index = this.percentNum.findIndex(item2 => item2 > item.sum);
          if(index == 0)
            item.percentage = 1;
          else if(index == -1)
            item.percentage = 90;
          else
            item.percentage = index * 10;
        })
        this.rating = employeeList;
      });
    },
    /** 生成百分位数组对应的分数 */
    percentage(employeeList){
      const n = employeeList.length;
      this.percentNum = [];
      if(n < 9) return null;
      for(let p = 1; p < 10 ; p++){     //p 表示计算第百分之p*10百分位
        const exponent = (n + 1) * p * 10 * 0.01;
        const j = parseInt(exponent)　  //指数的整数部分
        const g = exponent - j;         //指数的小数部分
        const percentNum = 0;;               //第百分之p*10百分位对应的分数
        if(g == 0){
          percentNum = employeeList[j - 1].sum;
        } else{
          percentNum = (employeeList[j].sum * g + (1 - g) * employeeList[j - 1].sum).toFixed(1);
        }
        this.percentNum.push(percentNum);
      }
    },
    changeRating(eeId,competenceId,rating) {
      const data = {eeId:eeId,competenceId:competenceId,rating:rating,positionId:this.position.positionId}
      editRating(data).then(response => {
        /** 从级联框中获取选中岗位 */
        const node = this.$refs.position.getCheckedNodes()[0];
        this.position = node.data;
        const position = {'positionId' : node.value};
        this.getCompetence(position);
        this.getRating(position);
      })
    },
    edit() {
      this.edited = true;

    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = {'positionId': this.position.positionId, 'cLabel': this.position.cLabel};
      this.$confirm('是否确认导出所有用户数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportRating(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function() {});
    },
    cancelEdit() {
      this.edited = false;
    },
    tagColors(percent) {
      if(percent == 1)
        return '#fd4f18';
      return this.percentColors[percent / 10 - 1];
    }
  }
}
</script>


<style>
  .transition-box {
    margin-bottom: 10px;
    width: 200px;
    height: 100px;
    border-radius: 4px;
    background-color: #409EFF;
    text-align: center;
    color: #fff;
    padding: 40px 20px;
    box-sizing: border-box;
    margin-right: 20px;
  }
  .example-text{
    font-family:'PingFang SC';
    font-size: 14px;
    color: #666666;
    font-weight: 600;
    margin: 10px;
  }
  .rating-container{
    box-shadow: 0px 0px 5px 4px #e4e4e4;
    border-radius: 5px;
    max-width: 1600px;
    min-height: 1000px;
    margin: auto;
    margin-bottom: 50px;
    padding: 30px;
  }

  .title{
    font-weight: 800;
    font-family: emoji;
    text-align: center;
    padding: 15px;
    font-size: 30px;
    color: rgb(3 121 121);
  }
  .el-progress__text{
    display: none;
  }
  .percentage-tips{
    text-align: center;
    padding:5px 10px;
    color: #444;
    font-family: PingFang SC;
    font-weight: bold;
  }
  .percentage-index{
    background: #fafafa;
  }
  .per-tag{
    color: #444;
    font-weight: bolder;
    font-size: 14px;
    font-family: PingFang SC;
  }
</style>

