package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeesDTO {
    @NotBlank(message = "name can't be blank")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Customer name must contain only letters and spaces")
    private String name;

    @NotBlank(message = "department can't be blank")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Customer department must contain only letters and spaces")
    private String department;

    @NotBlank(message = "designation can't be blank")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Customer designation must contain only letters and spaces")
    private String designation;

    @NotNull(message = "salary can't be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "salary should be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "salary must be a valid decimal number")
    private BigDecimal salary;

    @NotNull(message = "Enter Joining Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date joiningDate;

    @NotBlank(message = "status can't be blank")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Customer status must contain only letters and spaces")
    private String status;
}
