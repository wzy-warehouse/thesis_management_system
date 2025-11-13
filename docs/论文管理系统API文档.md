## <font style="color:rgb(0, 0, 0);">基础说明</font>
+ <font style="color:rgb(0, 0, 0);">基础路径：</font>`<font style="color:rgb(0, 0, 0);">/api</font>`
+ <font style="color:rgb(0, 0, 0);">数据格式：请求 / 响应均为 JSON</font>
+ <font style="color:rgb(0, 0, 0);">认证方式：请求头携带</font>`<font style="color:rgb(0, 0, 0);">cookie: token</font>`<font style="color:rgb(0, 0, 0);">（登录后获取）</font>
+ <font style="color:rgb(0, 0, 0);">响应格式：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,       // 200-成功，其他-错误
  "message": "成功", // 提示信息
  "data": {}         // 业务数据
}
```

## <font style="color:rgb(0, 0, 0);">用户管理 API</font>
### <font style="color:rgb(0, 0, 0);">登录</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/user/login</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "username": "zhangsan",
  "password": "123456",
  "remember": true
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

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

### 检查登录状态
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/user/</font><font style="color:rgb(0, 0, 0);background-color:rgba(0, 0, 0, 0.06);">check-login</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">GET</font>`
+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

### 检查上次登录是否过期
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/user/</font><font style="color:rgb(0, 0, 0);background-color:rgba(0, 0, 0, 0.06);">check-remember</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">GET</font>`
+ <font style="color:rgb(0, 0, 0);">请求：</font>**<font style="color:rgb(0, 0, 0);">json</font>**

```json
{
  "token": ""
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

### 自动登录
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/user/</font><font style="color:rgb(0, 0, 0);background-color:rgba(0, 0, 0, 0.06);">auto-login</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求：</font>**<font style="color:rgb(0, 0, 0);">json</font>**

```json
{
  "token": ""
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "xxx",
    "user": {
      "id": 1,
      "username": "zhangsan"
    }
  }
}
```

### <font style="color:rgb(0, 0, 0);">修改密码</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/user/change-pwd</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "oldPwd": "123456",
  "newPwd": "654321"
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

### <font style="color:rgb(0, 0, 0);">新建用户</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/user/create</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "username": "lisi",
  "password": "123456"
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "用户创建成功",
  "data": {
    "id": 2
  }
}
```

## <font style="color:rgb(0, 0, 0);">论文管理 API</font>
### <font style="color:rgb(0, 0, 0);">论文上传</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/paper/upload</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>`<font style="color:rgb(0, 0, 0);">multipart/form-data</font>`
    - `<font style="color:rgb(0, 0, 0);">files</font>`<font style="color:rgb(0, 0, 0);">：上传的文件（可多个）</font>
    - `<font style="color:rgb(0, 0, 0);">folderId</font>`<font style="color:rgb(0, 0, 0);">：可选，默认文件夹 ID</font>
+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "paperIds": [1, 2]  // 新增论文ID列表
  }
}
```

### <font style="color:rgb(0, 0, 0);">获取论文详情</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/paper/{id}</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">GET</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

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

### <font style="color:rgb(0, 0, 0);">编辑论文信息</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/paper/update</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">PUT</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "id": 1,
  "title": "修改后的标题",
  "innovation": "修改后的创新点"
  // 其他需修改的字段
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

### <font style="color:rgb(0, 0, 0);">删除论文（移至回收站）</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/paper/delete/{id}</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">DELETE</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "已移至回收站"
}
```

## <font style="color:rgb(0, 0, 0);">文件夹管理 API</font>
### <font style="color:rgb(0, 0, 0);">创建文件夹</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/folder/create</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "name": "机器学习",
  "parentId": 0  // 父文件夹ID，0为根目录
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "data": {
    "folderId": 1
  }
}
```

### <font style="color:rgb(0, 0, 0);">论文添加到文件夹</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/folder/add-paper</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "folderId": 1,
  "paperIds": [1, 2]
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "message": "添加成功"
}
```

## <font style="color:rgb(0, 0, 0);">分享 API</font>
### <font style="color:rgb(0, 0, 0);">生成分享链接</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/share/create</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">POST</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">请求体：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "paperId": 1,
  "permission": 1, // 1-仅查看，2-允许下载
  "expireDays": 7  // 有效期（天），null为永久
}
```

+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "data": {
    "shareToken": "xxx",
    "url": "http://ip:port/share?token=xxx"
  }
}
```

## <font style="color:rgb(0, 0, 0);">引用 API</font>
### <font style="color:rgb(0, 0, 0);">生成参考文献</font>
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/citation/generate/{paperId}</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">GET</font>`
+ <font style="color:rgb(0, 0, 0);">请求头：</font>`<font style="color:rgb(0, 0, 0);">Authorization: Bearer {token}</font>`
+ <font style="color:rgb(0, 0, 0);">参数：</font>`<font style="color:rgb(0, 0, 0);">formatId</font>`<font style="color:rgb(0, 0, 0);">（可选，引用格式 ID，默认使用默认格式）</font>
+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "data": {
    "citationText": "张三, 李四. 论文标题[J]. 期刊名, 2023, 10(2): 1-10."
  }
}
```

## 安全API
### 获取sm2加密公钥
+ 路径：`<font style="color:rgb(0, 0, 0);">/crypto/sm2/public-key</font>`
+ <font style="color:rgb(0, 0, 0);">路径：</font>`<font style="color:rgb(0, 0, 0);">/citation/generate/{paperId}</font>`
+ <font style="color:rgb(0, 0, 0);">方法：</font>`<font style="color:rgb(0, 0, 0);">GET</font>`
+ <font style="color:rgb(0, 0, 0);">响应：</font>**<font style="color:rgba(0, 0, 0, 0.85);">json</font>**

```json
{
  "code": 200,
  "data": {
    "publicKey": ""
  }
}
```

