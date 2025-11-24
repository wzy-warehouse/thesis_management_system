package com.laboratory.paper.utils;

import java.nio.file.Path;

public class FileUtils {

    /**
     * 将根路径与多个子路径拼接，返回最终的Path对象
     * @param root 根路径（必须为有效Path对象，不可为null）
     * @param paths 可变参数，多个子路径（支持相对路径、空字符串，null元素会被忽略）
     * @return 拼接后的完整Path对象
     * @throws IllegalArgumentException 如果root为null
     */
    public static Path buildFilePath(Path root, String... paths) {
        // 校验根路径非空
        if (root == null) {
            throw new IllegalArgumentException("根路径root不能为null");
        }

        Path result = root;
        // 遍历所有子路径，逐个拼接
        for (String path : paths) {
            // 忽略null或空字符串（避免拼接出无效路径）
            if (path == null || path.isBlank()) {
                continue;
            }
            // 拼接
            result = result.resolve(path);
        }

        // 规范化路径
        return result.normalize();
    }
}
