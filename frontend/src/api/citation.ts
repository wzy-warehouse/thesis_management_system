import type { GenerateCitationResponse } from '@/types/citation/GenerateCitationResponse'
import type { Response } from '@/types/Response'
import httpInstance from '@/utils/request/http'

/**
 * 生成参考文献
 * @param paperId
 * @param formatId
 * @returns
 */
export const generate = (
  paperId: number,
  formatId?: number,
): Promise<Response<GenerateCitationResponse>> => {
  return httpInstance.get(`/citation/generate/${paperId}`, {
    params: {
      formatId,
    },
  })
}
