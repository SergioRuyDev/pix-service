package com.sergioruy.model.enumeration;

public enum StatusPix {

    CREATED, // transaction created but not finish.

    IN_PROCESS, // transaction in process but not finish.

    APPROVED, // transaction approved but not finish.

    REJECTED, // transaction reject but not finish.

    DONE, // transaction finish with success.

    UNDONE, // transaction cannot be finish and the value be reversal.

    CANCELLED // transaction is cancelled, the balance is not affected.
}
