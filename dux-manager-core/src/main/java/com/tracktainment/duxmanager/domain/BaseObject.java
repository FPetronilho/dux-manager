package com.tracktainment.duxmanager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.duxmanager.annotation.Encrypted;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Base object with common fields")
public class BaseObject {

    @Encrypted
    @Schema(description = "Unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Creation timestamp", example = "2023-03-15T14:30:45")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-03-25T09:12:33")
    private LocalDateTime updatedAt;
}
