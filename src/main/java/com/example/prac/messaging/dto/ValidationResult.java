package com.example.prac.messaging.dto;

import java.io.Serializable;

public record ValidationResult(boolean isValid, String explanation)  implements Serializable {}
