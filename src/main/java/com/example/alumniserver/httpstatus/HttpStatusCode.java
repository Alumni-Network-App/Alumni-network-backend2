package com.example.alumniserver.httpstatus;

import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.User;
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
     * Used for checking if a group accepts non members to post, if the group is private,
     * and the user is not a member this method will return a 403 forbidden status.
     *
     * @param isMember used for checking if the user is a member.
     * @param isPrivate checks if the group is private.
     * @return a status code indicating if a user can post to this group.
     */
    public HttpStatus getForbiddenForGroupStatus(boolean isMember, boolean isPrivate) {
        return (isPrivate == true && !isMember) ? HttpStatus.FORBIDDEN : HttpStatus.OK;
    }

    /**
     * Used for checking if a topic / post allows a user to post to it,
     * depending on if the user is a member.
     *
     * @param isMember the value used for checking if the user is a member.
     * @return a status code indicating if a user is allowed to make a post.
     */
    public HttpStatus getForbiddenStatus(boolean isMember) {
        return (!isMember) ? HttpStatus.FORBIDDEN : HttpStatus.OK;
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


    //TODO ingen aning hur jag ska kolla detta, har inte förstått hur modellerna funkar....
    public HttpStatus getForbiddenIfNotGroupMember (Group group, User user){

        return (true) ? HttpStatus.FORBIDDEN : HttpStatus.OK;
    }

    public HttpStatus getForbiddenForUpdateEventStatus(Event event, User user) {
        return (event.getUser() != user) ? HttpStatus.FORBIDDEN : HttpStatus.OK;
    }
}
