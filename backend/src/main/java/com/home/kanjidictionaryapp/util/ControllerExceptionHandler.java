package com.home.kanjidictionaryapp.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = "com.home.kanjidictionaryapp.controller")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
