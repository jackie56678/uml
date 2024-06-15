<template>
    <div class="health-record-container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>健康档案</span>
          <el-button style="float: right;" type="primary" @click="editMode = !editMode">
            {{ editMode ? '取消编辑' : '编辑' }}
          </el-button>
        </div>
        <el-form :model="healthRecord" label-width="120px" class="health-record-form">
          <el-form-item label="姓名">
            <el-input v-model="healthRecord.name" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="healthRecord.gender" :disabled="!editMode">
              <el-option label="男" value="男"></el-option>
              <el-option label="女" value="女"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input-number v-model="healthRecord.age" :disabled="!editMode"></el-input-number>
          </el-form-item>
          <el-form-item label="身高 (米)">
            <el-input-number v-model="healthRecord.height" :disabled="!editMode" :step="0.01" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="体重 (千克)">
            <el-input-number v-model="healthRecord.weight" :disabled="!editMode" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="血型">
            <el-select v-model="healthRecord.bloodType" :disabled="!editMode">
              <el-option label="A" value="A"></el-option>
              <el-option label="B" value="B"></el-option>
              <el-option label="AB" value="AB"></el-option>
              <el-option label="O" value="O"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="过敏史">
            <el-input v-model="healthRecord.allergies" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="病史">
            <el-input v-model="healthRecord.medicalHistory" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="当前用药">
            <el-input v-model="healthRecord.medications" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="吸烟状态">
            <el-input v-model="healthRecord.smokingStatus" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="饮酒情况">
            <el-input v-model="healthRecord.alcoholUse" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="体力活动">
            <el-input v-model="healthRecord.physicalActivity" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="饮食情况">
            <el-input v-model="healthRecord.diet" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="血压情况">
            <el-input v-model="healthRecord.bloodPressure" :disabled="!editMode"></el-input>
          </el-form-item>
          <el-form-item label="胆固醇水平">
            <el-input-number v-model="healthRecord.cholesterolLevel" :disabled="!editMode" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="血糖水平">
            <el-input-number v-model="healthRecord.glucoseLevel" :disabled="!editMode" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item v-if="editMode">
            <el-button type="primary" @click="saveHealthRecord">保存</el-button>
            <el-button @click="editMode = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </template>
  
  <script>
  import { getHealthRecord, updateHealthRecord } from '@/api/healthRecord'
  
  export default {
    data() {
      return {
        editMode: false,
        healthRecord: {
          uid: null,
          name: '',
          gender: '',
          age: null,
          height: null,
          weight: null,
          bloodType: '',
          allergies: '',
          medicalHistory: '',
          medications: '',
          smokingStatus: '',
          alcoholUse: '',
          physicalActivity: '',
          diet: '',
          bloodPressure: '',
          cholesterolLevel: null,
          glucoseLevel: null
        }
      }
    },
    created() {
      this.fetchHealthRecord()
    },
    methods: {
      fetchHealthRecord() {
        // Assume the user ID is available in the route params or through some global state
        const uid = this.$route.params.uid || 1 // Default to 1 for example
        getHealthRecord(uid).then(response => {
          this.healthRecord = response.data
        }).catch(error => {
          this.$message.error('无法获取健康档案数据')
        })
      },
      saveHealthRecord() {
        updateHealthRecord(this.healthRecord).then(response => {
          this.$message.success('健康档案更新成功')
          this.editMode = false
        }).catch(error => {
          this.$message.error('更新健康档案失败')
        })
      }
    }
  }
  </script>
  
  <style scoped>
  .health-record-container {
    padding: 20px;
  }
  </style>
  