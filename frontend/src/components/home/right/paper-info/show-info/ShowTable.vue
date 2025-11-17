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
        <template #default>
          <el-row style="justify-content: center">
            <el-button size="small">查看</el-button>
            <el-button type="primary" size="small">分享</el-button>
          </el-row>
          <el-row style="margin-top: 10px; justify-content: center">
            <el-button type="success" size="small">下载</el-button>
            <el-button type="danger" size="small">删除</el-button>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script name="ShowTable" setup lang="ts">
import type { PaperInfoResponseData } from '@/types/paper/PaperInfoResponse.ts'

defineProps<{
  tableDatas: PaperInfoResponseData[]
}>()

const emit = defineEmits(['update:hasSelection'])

// 监听复选框选中变化
const handleSelectionChange = (selection: PaperInfoResponseData[]) => {
  const hasSelection = selection.length > 0
  emit('update:hasSelection', hasSelection)
}
</script>
<style scoped></style>
