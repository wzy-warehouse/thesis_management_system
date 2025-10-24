## 1. 基础说明
+ 基础路径：`/api`
+ 数据格式：请求 / 响应均为 JSON
+ 认证方式：请求头携带`Authorization: Bearer {token}`（登录后获取）
+ 响应格式：**json**

```json
{
  "code": 200,       // 200-成功，其他-错误
  "message": "成功", // 提示信息
  "data": {}         // 业务数据
}
```

## 2. 用户管理 API
### 2.1 登录
+ 路径：`/user/login`
+ 方法：`POST`
+ 请求体：**json**

```json
{
  "username": "zhangsan",
  "password": "123456",
  "remember": true
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "xxx",
    "user": {
      "id": 1,
      "username": "zhangsan"
    }
  }
}
```

### 2.2 修改密码
+ 路径：`/user/change-pwd`
+ 方法：`POST`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：**json**

```json
{
  "oldPwd": "123456",
  "newPwd": "654321"
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "message": "密码修改成功"
}
```

### 2.3 新建用户
+ 路径：`/user/create`
+ 方法：`POST`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：**json**

```json
{
  "username": "lisi",
  "password": "123456"
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "message": "用户创建成功",
  "data": {
    "id": 2
  }
}
```

## 3. 论文管理 API
### 3.1 论文上传
+ 路径：`/paper/upload`
+ 方法：`POST`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：`multipart/form-data`
    - `files`：上传的文件（可多个）
    - `folderId`：可选，默认文件夹 ID
+ 响应：**json**

```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "paperIds": [1, 2]  // 新增论文ID列表
  }
}
```

### 3.2 获取论文详情
+ 路径：`/paper/{id}`
+ 方法：`GET`
+ 请求头：`Authorization: Bearer {token}`
+ 响应：**json**

```json
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "xxx",
    "author": "xxx",
    "researchDirection": "xxx",
    // ...其他字段
  }
}
```

### 3.3 编辑论文信息
+ 路径：`/paper/update`
+ 方法：`PUT`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：**json**

```json
{
  "id": 1,
  "title": "修改后的标题",
  "innovation": "修改后的创新点"
  // 其他需修改的字段
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

### 3.4 删除论文（移至回收站）
+ 路径：`/paper/delete/{id}`
+ 方法：`DELETE`
+ 请求头：`Authorization: Bearer {token}`
+ 响应：**json**

```json
{
  "code": 200,
  "message": "已移至回收站"
}
```

## 4. 文件夹管理 API
### 4.1 创建文件夹
+ 路径：`/folder/create`
+ 方法：`POST`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：**json**

```json
{
  "name": "机器学习",
  "parentId": 0  // 父文件夹ID，0为根目录
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "data": {
    "folderId": 1
  }
}
```

### 4.2 论文添加到文件夹
+ 路径：`/folder/add-paper`
+ 方法：`POST`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：**json**

```json
{
  "folderId": 1,
  "paperIds": [1, 2]
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "message": "添加成功"
}
```

## 5. 分享 API
### 5.1 生成分享链接
+ 路径：`/share/create`
+ 方法：`POST`
+ 请求头：`Authorization: Bearer {token}`
+ 请求体：**json**

```json
{
  "paperId": 1,
  "shareType": 1,  // 1-原文，2-归纳内容
  "permission": 1, // 1-仅查看，2-允许下载
  "expireDays": 7  // 有效期（天），null为永久
}
```

+ 响应：**json**

```json
{
  "code": 200,
  "data": {
    "shareToken": "xxx",
    "url": "http://ip:port/share?token=xxx"
  }
}
```

## 6. 引用 API
### 6.1 生成参考文献
+ 路径：`/citation/generate/{paperId}`
+ 方法：`GET`
+ 请求头：`Authorization: Bearer {token}`
+ 参数：`formatId`（可选，引用格式 ID，默认使用默认格式）
+ 响应：**json**

```json
{
  "code": 200,
  "data": {
    "citationText": "张三, 李四. 论文标题[J]. 期刊名, 2023, 10(2): 1-10."
  }
}
```

