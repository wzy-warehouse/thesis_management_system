export interface PaperInfoResponse {
  data: PaperInfoResponseData[]
  totalPage: number
}

export interface PaperInfoResponseData {
  id: number
  title: string
  author: string
  publishTime: Date
  journal: string
  journalType: string
  researchDirection: string
  innovation: string
  deficiency: string
  keywords: string
  createTime: Date
}
