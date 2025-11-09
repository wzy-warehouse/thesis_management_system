import type { PaperInfoResponse } from '@/types/paper/PaperInfoResponse'
import type { PaperUpdateRequest } from '@/types/paper/PaperUpdateRequest'
import type { PaperUploadRequest } from '@/types/paper/PaperUploadRequest'
import type { Response } from '@/types/Response'
import httpInstance from '@/utils/request/http'

/**
 * 上传文件
 * @param uploadDatas
 * @returns 上传的文件id列表
 */
export const upload = (uploadDatas: PaperUploadRequest): Promise<Response<number[]>> => {
  const formData = new FormData()
  uploadDatas.files.forEach((file) => {
    formData.append('files', file)
  })

  if (uploadDatas.folderId) {
    formData.append('folderId', uploadDatas.folderId.toString())
  }
  return httpInstance.post('/paper/upload', formData)
}

/**
 * 根据id查询论文
 * @param id
 * @returns
 */
export const queryById = (id: number): Promise<Response<PaperInfoResponse>> => {
  return httpInstance.get(`/paper/${id}`)
}

/**
 * 修改论文信息
 * @param updateData 修改 数据
 * @returns
 */
export const update = (updateData: PaperUpdateRequest): Promise<Response<void>> => {
  return httpInstance.put('/paper/update', updateData)
}

/**
 * 按照id删除论文
 * @param id
 * @returns
 */
export const deleteById = (id: number): Promise<Response<void>> => {
  return httpInstance.delete(`/paper/delete/${id}`)
}
