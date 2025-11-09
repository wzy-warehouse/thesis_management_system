export interface PaperUpdateRequest {
  id: number
  title?: string
  author?: string
  publishTime?: Date
  journal?: string
  journalType?: string
  researchDirection?: string
  innovation?: string
  deficiency?: string
  keywords?: string
}
