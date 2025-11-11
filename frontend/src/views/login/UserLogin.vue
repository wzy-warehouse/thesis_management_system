<template>
  <div class="login-bg">
    <div class="form-bg">
      <div class="title">登录</div>
      <div class="form-box">
        <el-form :model="loginForm" :rules="useLoginValidator" ref="loginFormRef">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="用户名" />
          </el-form-item>

          <el-form-item prop="password">
            <el-input v-model="loginForm.password" placeholder="密码" type="password" />
          </el-form-item>

          <el-form-item prop="remember">
            <el-checkbox v-model="loginForm.remember">记住密码(7天)</el-checkbox>
          </el-form-item>

          <el-form-item>
            <el-button class="login-btn" type="success" @click="handleLogin">登录系统</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script name="UserLogin" setup lang="ts">
import type { LoginRequest } from '@/types/user/LoginRequest'
import { reactive, ref } from 'vue'
import { useLoginValidator } from '@/hooks/user/useLoginValidator'
import type { ElForm } from 'element-plus'
import { $api } from '@/api/api'
// 表单数据
const loginForm: LoginRequest = reactive({
  username: 'admin',
  password: 'admin@123',
  remember: false,
})

// 表单引用（用于手动触发验证）
const loginFormRef = ref<InstanceType<typeof ElForm>>()

// 登录处理
const handleLogin = async () => {
  // 手动触发表单验证
  const isValid = await loginFormRef.value?.validate()
  if (!isValid) return

  // 验证通过，调用后端接口
  $api.user.login(loginForm).then((res) => {
    console.log(res)
  })
}
</script>
<style scoped>
.login-bg {
  left: 0px;
  top: 0px;
  width: 100%;
  height: 100vh;
  line-height: 20px;
  background: linear-gradient(
    150.14deg,
    rgba(228, 234, 241, 0) 0.98%,
    rgba(228, 234, 241, 0.31) 0.98%,
    rgba(228, 234, 241, 1) 112.22%
  );
}

.form-bg {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 30%;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 1);
  box-shadow: 0px 8px 24px 0px rgba(0, 0, 0, 0.08);
  transform: translate(-50%, -50%);
}

.form-bg .title {
  line-height: 50px;
  color: rgba(44, 62, 80, 1);
  font-size: 48px;
  text-align: center;
  font-family: '微软雅黑';
  font-weight: bold;
  margin: 20px auto;
}

.form-bg .form-box {
  width: 90%;
  margin: 0 auto 30px auto;
}

.form-bg .form-box .login-btn {
  display: block;
  width: 100%;
}
</style>
