<template>
  <div class="user-profile">
    <el-card class="user-card" shadow="hover">
      <div class="user-header">
        <span class="header-title">个人信息</span>
        <el-button type="text" class="edit-button" icon="el-icon-edit" @click="editInfo">编辑</el-button>
      </div>
      <div class="info-section">
        <div class="info-item">
          <span class="label">姓名：</span>
          <span>{{ userInfo.name }}</span>
        </div>
        <div class="info-item">
          <span class="label">年龄：</span>
          <span>{{ userInfo.age }}</span>
        </div>
        <div class="info-item">
          <span class="label">性别：</span>
          <span>{{ userInfo.gender }}</span>
        </div>
        <!-- <div class="info-item">
          <span class="label">出生日期：</span>
          <span>{{ userInfo.birthDate }}</span>
        </div> -->
        <div class="info-item">
          <span class="label">地址：</span>
          <span>{{ userInfo.address }}</span>
        </div>
        <div class="info-item">
          <span class="label">电话：</span>
          <span>{{ userInfo.phone }}</span>
        </div>
        <div class="info-item">
          <span class="label">个人描述：</span>
          <span>{{ userInfo.description }}</span>
        </div>
      </div>
    </el-card>

    <el-dialog
      title="编辑个人信息"
      :visible.sync="editDialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="editForm.age"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="editForm.birthDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%;"
          ></el-date-picker>
        </el-form-item> -->
        <el-form-item label="地址" prop="address">
          <el-input v-model="editForm.address"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="editForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="个人描述" prop="description">
          <el-input v-model="editForm.description"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getInfo, save } from "@/api/profile";

export default {
  data() {
    return {
      userInfo: {
        name: "",
        age: null,
        gender: "",
        birthDate: "",
        address: "",
        phone: "",
        description: ""
      },
      editDialogVisible: false,
      editForm: {
        name: "",
        age: null,
        gender: "",
        birthDate: "",
        address: "",
        phone: "",
        description: ""
      },
      editRules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        age: [
          { required: true, message: "请输入年龄", trigger: "blur" },
          // { type: "number", message: "年龄必须为数字值" }
        ],
        gender: [{ required: true, message: "请选择性别", trigger: "change" }],
        birthDate: [{ required: true, message: "请选择出生日期", trigger: "change" }],
        address: [{ required: true, message: "请输入地址", trigger: "blur" }],
        phone: [{ required: true, message: "请输入电话", trigger: "blur" }],
        description: [{ required: true, message: "请输入个人描述", trigger: "blur" }]
      }
    };
  },
  mounted() {
    this.fetchUserInfo();
  },
  methods: {
    fetchUserInfo() {
      getInfo()
        .then(response => {
          const data = response.data;
          this.userInfo = {
            name: data.name,
            age: this.calculateAge(data.birthDate),
            gender: data.gender,
            birthDate: data.birthDate,
            address: data.address,
            phone: data.phone,
            description: data.description
          };
        })
        .catch(error => {
          console.error("信息获取失败：", error);
        });
    },
    calculateAge(birthDate) {
      const birth = new Date(birthDate);
      const today = new Date();
      let age = today.getFullYear() - birth.getFullYear();
      const monthDiff = today.getMonth() - birth.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
      }
      return age;
    },
    editInfo() {
      this.editForm = { ...this.userInfo };
      this.editDialogVisible = true;
    },
    saveEdit() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          save(this.editForm)
            .then(() => {
              this.$message({
                type: "success",
                message: "个人信息已更新"
              });
              this.userInfo = { ...this.editForm };
              this.editDialogVisible = false;
            })
            .catch(error => {
              console.error("个人信息更新失败：", error);
              this.$message.error("个人信息更新失败");
            });
        }
      });
    }
  }
};
</script>

<style scoped>
.user-profile {
  padding: 40px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.user-card {
  margin-bottom: 40px;
  width: 600px; /* 调整展示框的宽度 */
  padding: 40px; /* 调整展示框的内边距 */
}

.user-header {
  font-size: 28px; /* 调整标题的字体大小 */
  margin-bottom: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.header-title {
  font-weight: bold;
}

.edit-button {
  color: #409EFF;
  font-weight: bold;
}

.info-section {
  font-size: 20px; /* 调整信息内容的字体大小 */
  text-align: left;
}

.info-item {
  margin-bottom: 20px; /* 调整每个信息项的底部间距 */
}

.label {
  font-weight: bold;
}

.dialog-footer {
  text-align: right;
}
</style>
