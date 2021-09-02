package com.example.alumniserver.httpstatus;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusCodeTest {

    private HttpStatusCode statusCode = new HttpStatusCode();

    @Test
    void getSeeOtherCode_noInput_seeOtherCode() {
        assertEquals(HttpStatus.SEE_OTHER, statusCode.getSeeOtherCode());
    }

    @Test
    void getForbiddenForGroupStatus_inGroupFalseIsPrivateFalse_okCode() {
        assertEquals(HttpStatus.OK, statusCode.getForbiddenForGroupStatus(false, false));
    }

    @Test
    void getForbiddenForGroupStatus_inGroupTrueIsPrivateFalse_okCode() {
        assertEquals(HttpStatus.OK, statusCode.getForbiddenForGroupStatus(true, false));
    }

    @Test
    void getForbiddenForGroupStatus_inGroupTrueIsPrivateTrue_okCode() {
        assertEquals(HttpStatus.OK, statusCode.getForbiddenForGroupStatus(true, true));
    }

    @Test
    void getForbiddenForGroupStatus_inGroupFalseIsPrivateTrue_forbiddenCode() {
        assertEquals(HttpStatus.FORBIDDEN, statusCode.getForbiddenForGroupStatus(false, true));
    }

    @Test
    void getForbiddenStatus_isMemberFalse_forbiddenCode() {
        assertEquals(HttpStatus.FORBIDDEN, statusCode.getForbiddenStatus(false));
    }

    @Test
    void getForbiddenStatus_isMemberTrue_okCode() {
        assertEquals(HttpStatus.OK, statusCode.getForbiddenStatus(true));
    }

    @Test
    void getContentStatus_onPostSuccess_noContentCode() {
        assertEquals(HttpStatus.NO_CONTENT, statusCode.getContentStatus());
    }

    @Test
    void getFoundStatus_emptyObject_notFoundStatus() {
        assertEquals(HttpStatus.NOT_FOUND, statusCode.getFoundStatus(null));
    }

    @Test
    void getFoundStatus_nonEmptyObject_okStatus() {
        assertEquals(HttpStatus.OK, statusCode.getFoundStatus("Test"));
    }
}