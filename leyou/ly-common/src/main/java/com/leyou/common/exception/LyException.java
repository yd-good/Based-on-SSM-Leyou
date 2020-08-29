/*
信息:
*/
package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyException extends RuntimeException{
    private ExceptionEnums exceptionEnums;
}
