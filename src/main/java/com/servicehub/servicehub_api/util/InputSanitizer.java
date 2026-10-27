package com.servicehub.servicehub_api.util;
import java.util.Locale;
public final class InputSanitizer {
    private InputSanitizer() { }
    public static String text(String value) { if (value == null) return null; String cleaned = value.trim(); return cleaned.isEmpty() ? null : cleaned.replaceAll("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F]", ""); }
    public static String email(String value) { String cleaned = text(value); return cleaned == null ? null : cleaned.toLowerCase(Locale.ROOT); }
}
