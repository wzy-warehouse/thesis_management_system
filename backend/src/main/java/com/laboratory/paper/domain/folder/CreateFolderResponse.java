package com.laboratory.paper.domain.folder;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CreateFolderResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -6643803509710485292L;

    private Long folderId;
}
