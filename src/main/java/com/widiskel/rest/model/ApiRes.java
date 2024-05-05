package com.widiskel.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiRes<T> {
    private String rc;
    private String msg;
    private T data;
    private String errors;
}
