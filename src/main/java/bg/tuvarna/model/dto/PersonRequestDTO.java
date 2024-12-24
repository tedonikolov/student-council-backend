package bg.tuvarna.model.dto;

import bg.tuvarna.enums.CouncilRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PersonRequestDTO(
    @NotEmpty(message = "Въведете име!") String name,
    @NotNull(message = "Изберете снимка!") String imageUrl,
    @NotEmpty(message = "Изберете длъжност!") List<CouncilRole> roles,
    String description,
    String grade
) {
}
