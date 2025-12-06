<template>
  <div>
    <el-table :data="tableDatas" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="50" />
      <el-table-column align="center" type="index" width="50" />
      <el-table-column align="center" property="title" label="论文标题" />
      <el-table-column align="center" property="author" label="作者" />
      <el-table-column align="center" label="期刊名称/期刊类型">
        <template #default="scope">
          <el-row>
            <el-col>{{ scope.row.journal }} / </el-col>
            <el-col>{{ scope.row.journalType }}</el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column align="center" property="publishTime" label="发表时间" />
      <el-table-column align="center" property="researchDirection" label="研究方向" />
      <el-table-column align="center" fixed="right" label="操作">
        <template #default="scope">
          <el-row style="justify-content: center">
            <el-button size="small">查看</el-button>
            <el-button type="primary" size="small">分享</el-button>
          </el-row>
          <el-row style="margin-top: 10px; justify-content: center">
            <el-button type="success" size="small">下载</el-button>
            <el-button type="danger" size="small" @click="deletePaper(scope.row.id)"
              >删除</el-button
            >
          </el-row>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 删除 -->
  <DeletePaper
    :paper-id="paperId"
    :folder-id="folderId"
    @close-delete-paper="closeDeletePaper"
    v-if="viewDeletePaper"
  />
</template>
<script name="ShowTable" setup lang="ts">
import type { PaperInfoResponseData } from '@/types/paper/PaperInfoResponse.ts'
import DeletePaper from './table-operation/DeletePaper.vue'
import { ref } from 'vue'

defineProps<{
  tableDatas: PaperInfoResponseData[]
  folderId: number
}>()

// 论文id
const paperId = ref<number>(0)

// 删除组件
const viewDeletePaper = ref<boolean>(false)

const emit = defineEmits<{
  (e: 'update:hasSelection', hasSelection: boolean): void
  (e: 'update:selectedPaperIds', selectedPapers: PaperInfoResponseData[]): void
}>()

// 监听复选框选中变化
const handleSelectionChange = (selection: PaperInfoResponseData[]) => {
  const hasSelection = selection.length > 0

  // 给父组件传递选中的论文
  emit('update:selectedPaperIds', selection)
  emit('update:hasSelection', hasSelection)
}

// 删除
const deletePaper = (id: number) => {
  viewDeletePaper.value = true
  paperId.value = id
}

// 删除组件关闭
const closeDeletePaper = () => {
  viewDeletePaper.value = false
}
</script>
<style scoped></style>
