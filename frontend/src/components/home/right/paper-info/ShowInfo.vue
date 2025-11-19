<template>
  <div class="show-info-bg">
    <div class="navs">
      <el-button type="danger" :disabled="disabledDeleteBtn">删除选中</el-button>
    </div>
    <div class="table-box">
      <ShowTable
        :table-datas="tableDatas"
        :folder-id="folderId"
        @update:hasSelection="handleHasSelection"
      />
    </div>
    <div class="pages-box">
      <ShowPage />
    </div>
  </div>
</template>
<script name="ShowInfo" setup lang="ts">
import ShowTable from '@/components/home/right/paper-info/show-info/ShowTable.vue'
import ShowPage from '@/components/home/right/paper-info/show-info/ShowPage.vue'
import type { PaperInfoResponseData } from '@/types/paper/PaperInfoResponse.ts'
import { ref } from 'vue'

defineProps<{
  tableDatas: PaperInfoResponseData[]
  folderId: number
}>()

// 禁止选点击
const disabledDeleteBtn = ref(true)

const handleHasSelection = (hasSelection: boolean) => {
  disabledDeleteBtn.value = !hasSelection
}
</script>
<style scoped>
.show-info-bg {
  width: 100%;
  height: calc(100% - 60px);
}

.show-info-bg .navs {
  width: 100%;
  height: 50px;
  padding: 0px 30px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.show-info-bg .table-box {
  width: 100%;
  height: calc(100% - 120px);
  overflow-y: auto;
}

.show-info-bg .pages-box {
  width: 100%;
  height: 70px;
}
</style>
