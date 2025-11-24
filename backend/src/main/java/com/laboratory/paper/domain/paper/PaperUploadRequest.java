package com.laboratory.paper.domain.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PaperUploadRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8182917504119072693L;

    private Integer chunkNumber;
    private Integer chunkSize;
    private Integer currentChunkSize;
    private Integer totalSize;
    private String identifier;
    private String filename;
    private String relativePath;
    private Integer totalChunks;
    private Long folderId;
    private Long paperId;
    private String filePath;
}
