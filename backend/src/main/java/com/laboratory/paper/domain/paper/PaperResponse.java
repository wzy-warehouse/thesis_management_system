package com.laboratory.paper.domain.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaperResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8736487586137758624L;

    private List<PaperResponseData> data;

    private Integer totalPage;

    public PaperResponse() {
        this.data = new ArrayList<>();
    }
}