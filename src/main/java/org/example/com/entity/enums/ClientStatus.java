package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the status of a client.
 *
 * @author Natalie Werch
 */
@Getter
public enum ClientStatus {

    ACTIVE,
    INACTIVE,
    CLOSED,
    VIP,
    BLOCKED,
    ARCHIVED
}
