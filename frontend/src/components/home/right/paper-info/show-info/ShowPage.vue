<template>
  <el-config-provider :locale="customLocale">
    <div class="show-page-bg">
      <el-pagination
        :hide-on-single-page="true"
        :current-page="queryPaper?.pageNum"
        :page-size="queryPaper?.pageSize"
        :page-sizes="[10, 20, 30, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="queryPaper?.totalPage"
        @update:current-page="(newPage: number) => updatePageNum?.(newPage)"
        @update:page-size="(newSize: number) => updatePageSize?.(newSize)"
      />
    </div>
  </el-config-provider>
</template>

<script name="ShowPage" setup lang="ts">
import type { QueryPaper } from '@/types/paper/QueryPaper.ts'
import { inject, reactive, type Ref } from 'vue'
import config from '@/config/config.json'

//  定义自定义国际化配置（只覆盖分页组件的文本）
const customLocale = reactive({
  el: {
    pagination: config.pagination,
  },
})

// 祖父组件提供的数据和方法
const queryPaper = inject<Ref<QueryPaper>>('query-paper')
const updatePageNum = inject<(newPageNum: number) => void>('update-page-num')
const updatePageSize = inject<(newPageSize: number) => void>('update-page-size')
</script>

<style scoped>
.show-page-bg {
  width: 100%;
  height: 100%;
  padding: 10px 30px;
  display: flex;
  justify-content: flex-end;
  align-items: end;
  box-sizing: border-box;
}
</style>
