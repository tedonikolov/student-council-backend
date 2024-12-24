package bg.tuvarna.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateNewsDTO(
    @NotNull(message = "Въведете заглавие!") String title,
    @NotNull(message = "Въведете подзаглавие!") String subtitle,
    @NotEmpty(message = "Въведете съдържание!") List<String> content
) {
}
