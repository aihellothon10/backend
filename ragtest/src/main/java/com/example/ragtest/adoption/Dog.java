package com.example.ragtest.adoption;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

record Dog(@Id Integer id, String name, String description, LocalDate dob, String owner) {
}
