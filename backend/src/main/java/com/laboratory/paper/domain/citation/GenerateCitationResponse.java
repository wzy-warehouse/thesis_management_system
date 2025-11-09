package com.laboratory.paper.domain.citation;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class GenerateCitationResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -1635231059079892061L;

    private String citationText;
}
