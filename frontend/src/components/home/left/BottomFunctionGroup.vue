<template>
  <div class="bottom-bg">
    <el-row>
      <el-col :span="24">
        <el-button type="danger" plain @click="emit('content-change', 'recycle-bin')"
          >回收站</el-button
        >
      </el-col>
    </el-row>
  </div>
</template>
<script name="BottomFunctionGroup" setup lang="ts">
// 接收父组件传递的emit
const emit = defineEmits(['content-change'])
</script>
<style scoped>
::v-deep .el-button {
  background-color: rgba(255, 255, 255, 0.1) !important;
  display: block;
  width: 80%;
  margin: 30px auto;
  border: 0;
}

.create-btn {
  color: #fff;
}

.create-btn:hover {
  color: #409eff;
}
</style>
