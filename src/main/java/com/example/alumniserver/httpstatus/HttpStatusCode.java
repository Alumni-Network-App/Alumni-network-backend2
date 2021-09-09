package com.example.alumniserver.httpstatus;

import com.example.alumniserver.model.Group;
import org.springframework.http.HttpStatus;

public class HttpStatusCode<T> {

    /**
     * Used for redirecting a currently authenticated user to their info page.
     * @return the status for an authenticated user.
     */
    public HttpStatus getSeeOtherCode() {
        return HttpStatus.SEE_OTHER;
    }

    /**
     * Used for checking if a topic / post allows a user to post to it,
     * depending on if the user is a member.
     *
     * @param isAdded the value used for checking if the user is a member.
     * @return a status code indicating if a user is allowed to make a post.
     */
    public HttpStatus getForbiddenStatus(boolean isAdded) {
        return (!isAdded) ? HttpStatus.FORBIDDEN : HttpStatus.OK;
    }

    /**
     * Used when the REST method POST has successfully added a new element.
     *
     * @return the Http status NO_CONTENT (204).
     */
    public HttpStatus getContentStatus() {
        return HttpStatus.NO_CONTENT;
    }

    /**
     * Checks if the passed value has any content, if not return the status code
     * 404 not found.
     * <p>
     * This method will always return a Http status.
     *
     * @param t an object to be checked if it is empty or not.
     * @return a status code indicating if the object was found.
     */
    public HttpStatus getFoundStatus(T t) {
        return (t == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    }

    public HttpStatus getForbiddenPostingStatus(T t) {
        return (t == null) ? HttpStatus.FORBIDDEN : HttpStatus.CREATED;
    }

}
