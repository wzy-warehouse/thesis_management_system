<template>
  <el-dialog
    v-model="dialogVisible"
    title="上传论文"
    width="600px"
    :before-close="handleClose"
    append-to-body
  >
    <uploader :options="options" :file-status-text="statusText" class="uploader-box">
      <!-- 不支持上传的浏览器提示 -->
      <uploader-unsupport>
        当前浏览器不支持拖拽上传，请使用 Chrome、Edge、Firefox等现代浏览器。
      </uploader-unsupport>

      <!-- 拖拽上传区域 - 点击时触发文件选择 -->
      <uploader-drop class="uploader-drop-area">
        <!-- 隐藏上传按钮但保持功能 -->
        <uploader-btn
          :single="false"
          :attrs="{ accept: '.pdf' }"
          class="cover-upload-btn"
        ></uploader-btn>
        <p>拖拽文件到此处上传，或点击此处上传文件</p>
        <p class="upload-tips">支持PDF格式，最大支持100MB</p>
      </uploader-drop>

      <!-- 文件上传列表及进度显示 -->
      <uploader-list>
        <template
          #file="{ file, progress, formatedSize, formatedAverageSpeed, formatedTimeRemaining }"
        >
          <div class="file-item">
            <div class="file-info">
              <span class="file-name">{{ file.name }}</span>
              <span class="file-size">{{ formatedSize }}</span>
            </div>

            <!-- 进度条 -->
            <div class="progress-container">
              <el-progress
                :percentage="progress * 100"
                :status="file.error ? 'exception' : file.isComplete ? 'success' : 'normal'"
                stroke-width="2"
              ></el-progress>

              <!-- 进度信息 -->
              <div class="progress-info">
                <span v-if="file.isUploading">
                  {{ Math.round(progress * 100) }}% · {{ formatedAverageSpeed }} · 剩余{{
                    formatedTimeRemaining
                  }}
                </span>
                <span v-else-if="file.error">上传失败，点击重试</span>
                <span v-else-if="file.isComplete">上传成功</span>
                <span v-else-if="file.paused">已暂停</span>
                <span v-else>等待中</span>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="file-actions">
              <el-button
                icon="Refresh"
                size="mini"
                @click="file.retry()"
                v-if="file.error || file.paused"
              ></el-button>
              <el-button
                icon="Pause"
                size="mini"
                @click="file.pause()"
                v-if="file.isUploading"
              ></el-button>
              <el-button icon="Delete" size="mini" @click="file.remove()"></el-button>
            </div>
          </div>
        </template>
      </uploader-list>
    </uploader>
  </el-dialog>
</template>

<script name="UploadPaper" setup lang="ts">
import { ref } from 'vue'
import config from '@/config/config.json'
import { FileUploadUtiles } from '@/utils/upload/FileUploadUtiles'
import { useUserStore } from '@/stores/useUserStore'
import type { Response } from '@/types/Response'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  currentFolderId: number
}>()

const emits = defineEmits<{
  (e: 'cancel-upload'): void
}>()

const dialogVisible = ref(true)

// 上传配置
const options = ref({
  target: '/api/paper/upload', // 后端上传接口
  chunkSize: config.fileUpload.chunkSize, // 5MB分片大小
  forceChunkSize: true,
  simultaneousUploads: config.fileUpload.simultaneousUploads, // 同时上传3个分片
  query: {
    folderId: props.currentFolderId, // 传递文件夹ID
  },
  withCredentials: true, // 跨域请求携带cookie
  testChunks: false, // 开启分片校验
  maxChunkRetries: config.fileUpload.maxChunkRetries, // 分片失败最大重试次数
  chunkRetryInterval: config.fileUpload.chunkRetryInterval, // 重试间隔1秒
  generateUniqueIdentifier: (file: File) => {
    /*
     * 此处使用自定义的hash方式，因为框架底层使用的同步函数的形式，因此用内容判断会无法使用
     */
    // 组合字段：操作用户 + 文件大小 + 最后修改时间 + 文件名
    const uniqueStr = `${useUserStore().id}-${file.size}-${file.lastModified}-${file.name}`
    // 对组合字符串做简单哈希（同步计算，避免字符串过长）
    return `${FileUploadUtiles.simpleHash(uniqueStr)}_${file.name.split('.')[1]}`
  },
  uploadMethod: 'post',
  // 检查分片是否已经上传
  checkChunkUploadedByResponse: (chunk: unknown, message: string) => {
    // 转换为json对象
    const response: Response = JSON.parse(message)
    if (response.code != 200) {
      ElMessage.error(response.message)
      return false
    }
    return true
  },
})

// 状态文本映射
const statusText = ref({
  success: '上传成功',
  error: '上传失败',
  uploading: '上传中',
  paused: '已暂停',
  waiting: '等待中',
})

// 关闭对话框
function handleClose() {
  emits('cancel-upload')
}
</script>

<style scoped>
.uploader-box {
  width: 100%;
  padding: 15px 0;
}

.uploader-drop-area {
  border: 2px dashed #ccc;
  border-radius: 4px;
  padding: 30px;
  text-align: center;
  margin-bottom: 20px;
  cursor: pointer;
}

.hidden-upload-btn {
  display: none;
}

.upload-tips {
  margin-top: 10px;
  color: #666;
  font-size: 12px;
}

.file-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.file-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.file-name {
  font-weight: 500;
}

.file-size {
  color: #666;
  font-size: 12px;
}

.progress-container {
  margin: 8px 0;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.file-actions {
  display: flex;
  gap: 5px;
  margin-top: 5px;
}

.cover-upload-btn {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0; /* 透明 */
  cursor: pointer;
  z-index: 1; /* 确保在最上层 */
}
</style>
