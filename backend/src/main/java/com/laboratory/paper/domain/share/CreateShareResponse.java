package com.laboratory.paper.domain.share;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CreateShareResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 2991657185466161275L;

    private String shareToken;
    private String url;
}
