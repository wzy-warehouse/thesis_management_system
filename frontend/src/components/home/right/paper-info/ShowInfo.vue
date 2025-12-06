<template>
  <div class="show-info-bg">
    <div class="navs">
      <el-button type="danger" :disabled="disabledDeleteBtn" @click="deleteSelected"
        >删除选中</el-button
      >
    </div>
    <div class="table-box">
      <ShowTable
        :table-datas="tableDatas"
        :folder-id="folderId"
        @update:hasSelection="handleHasSelection"
        @update:selectedPaperIds="updateSelectedPaperIds"
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
import { inject, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { $api } from '@/api/api'

// 选中的数据
const selectedPaperIds = ref<number[]>([])

const props = defineProps<{
  tableDatas: PaperInfoResponseData[]
  folderId: number
}>()

// 禁止选点击
const disabledDeleteBtn = ref(true)

const handleHasSelection = (hasSelection: boolean) => {
  disabledDeleteBtn.value = !hasSelection
}

const deletePaperSuccess = inject<(paperId: number) => void>('delete-paper-success')

// 删除选中
function deleteSelected() {
  ElMessageBox.alert('确定要删除选中的论文吗？', '提示', {
    confirmButtonText: '确定',
    confirmButtonClass: 'el-button-info',
  }).then(() => {
    // 批量删除
    $api.paper.batchDeleteById(selectedPaperIds.value, props.folderId).then((res) => {
      if (res.code == 200) {
        ElMessage.success('删除成功')
        selectedPaperIds.value.forEach((paperId) => {
          deletePaperSuccess?.(paperId)
        })
      } else {
        ElMessage.error('删除失败：' + res.message)
      }
    })
  })
}

// 更新选中数据
function updateSelectedPaperIds(paper: PaperInfoResponseData[]) {
  selectedPaperIds.value = []
  paper.forEach((item) => {
    selectedPaperIds.value.push(item.id)
  })
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
