export const useRenameFolderValidator = {
  // 目录名称验证
  name: [
    {
      required: true,
      message: '目录名称不能为空。',
      trigger: 'blur',
    },
    {
      max: 100,
      message: '目录名称不能超过100个字符',
      trigger: 'blur',
    },
  ],
}
