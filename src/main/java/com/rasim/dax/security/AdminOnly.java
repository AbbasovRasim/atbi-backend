package com.rasim.dax.security;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
public @interface AdminOnly {
}