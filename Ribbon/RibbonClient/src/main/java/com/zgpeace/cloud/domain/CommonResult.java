package com.zgpeace.cloud.domain;

import lombok.*;

@Data
@NoArgsConstructor
public class CommonResult<T> {

  private T data;
  private String message;
  private Integer code;

  public CommonResult(T data, String message, Integer code) {
    this.data = data;
    this.message = message;
    this.code = code;
  }

  public CommonResult(String message, Integer code) {
    this(null, message, code);
  }

  public CommonResult(T data) {
    this(data, "操作成功", 200);
  }

}
