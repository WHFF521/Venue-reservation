package com.ygstar.backend.util;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class Response<T> {
    private Integer code;
    private String desc;
    private Date timestamp = new Date();
    private T data;
}
