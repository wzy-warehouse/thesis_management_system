package com.laboratory.paper.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Share implements Serializable {
    @Serial
    private static final long serialVersionUID = 2991657185466161275L;

    private String shareToken;
    private String url;
}
