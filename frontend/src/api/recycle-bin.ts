import type { Response } from '@/types/Response'
import httpInstance from '@/utils/request/http'

/**
 * 按照文件夹删除文件
 * @param folderId
 * @returns
 */
export const deleteByFolderId = (folderId: number): Promise<Response<void>> => {
  return httpInstance.delete(`/recycle-bin/delete-by-folder-id/${folderId}`)
}
