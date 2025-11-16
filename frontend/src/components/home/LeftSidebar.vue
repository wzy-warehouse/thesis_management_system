<template>
  <div class="left-sidebar">
    <LogoPart />
    <BarPart
      @content-change="(...args: unknown[]) => $emit('content-change', ...args)"
      @folder-id-change="(...args: unknown[]) => $emit('folder-id-change', ...args)"
    />
    <BottomFunctionGroup
      @content-change="(...args: unknown[]) => $emit('content-change', ...args)"
    />
  </div>
</template>
<script name="LeftSidebar" setup lang="ts">
import BarPart from './left/BarPart.vue'
import BottomFunctionGroup from './left/BottomFunctionGroup.vue'
import LogoPart from './left/LogoPart.vue'
</script>
<style scoped>
.left-sidebar {
  width: 240px;
  height: 100%;
  background-color: rgba(44, 62, 80, 1);
  position: fixed;
  top: 0;
  left: 0;
}
</style>
