package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the status of an agreement.
 *
 * @author Natalie Werch
 */
@Getter
public enum AgreementStatus {

    ACTIVE,
    INACTIVE,
    PENDING,
    TERMINATED,
    COMPLETED
}
