import config from '@/config/config.json'
import SparkMD5 from 'spark-md5'

// 分片大小
const CHUNK_SIZE = config.fileUpload.chunkSize

export const FileUploadUtiles = {
  /**
   * 同步简单哈希（对字符串做快速哈希，无需异步）
   * @param str 要哈希的字符串
   * @returns 32位哈希字符串
   */
  simpleHash: (str: string): string => {
    let hash = 0
    for (let i = 0; i < str.length; i++) {
      const charCode = str.charCodeAt(i)
      hash = (hash << 5) - hash + charCode
      hash |= 0 // 转为32位整数
    }
    return Math.abs(hash).toString(16)
  },

  // 对文件进行hash
  hash: (file: File) => {
    return new Promise((resolve) => {
      // 获取分片
      const chunks = FileUploadUtiles.createChunks(file)

      // spark
      const spark = new SparkMD5()

      function _read(i: number) {
        if (i >= chunks.length) {
          resolve(spark.end())
          return
        }
        const blob = chunks[i]
        const reader = new FileReader()
        reader.onload = (e) => {
          const bytes = e.target?.result
          spark.append(bytes as ArrayBuffer)
          _read(i + 1)
        }

        reader.readAsArrayBuffer(blob as Blob)
      }

      _read(0)
    })
  },

  // 对文件进行分片
  createChunks: (file: File, chunkSize: number = CHUNK_SIZE) => {
    const chunks: Blob[] = []
    for (let i = 0; i < file.size; i += chunkSize) {
      chunks.push(file.slice(i, i + chunkSize))
    }
    return chunks
  },
}
