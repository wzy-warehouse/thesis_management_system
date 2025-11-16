<template>
  <el-row class="logo-bg">
    <el-col class="logo" :span="8">
      <img :src="logoImg" alt="logo图标" />
    </el-col>
    <el-col class="logo-text" :span="16"> 文献库 </el-col>
  </el-row>
</template>
<script name="LogoPart" setup lang="ts">
import logoImg from '@/assets/images/logo.svg'
</script>
<style scoped>
.logo-bg {
  width: 100%;
  height: 70px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  align-items: center;
}

.logo-bg .logo {
  text-align: right;
  height: 32px;
}

.logo-bg .logo-text {
  text-align: left;
  font-size: 24px;
  color: #fff;
  font-weight: 600;
  padding-left: 15px;
}
</style>
