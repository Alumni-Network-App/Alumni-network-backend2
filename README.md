# Experis Academy Case - Alumni network (API)

## Deploys (Using Heroku)
Frontend: https://alumni-network-deluxe.herokuapp.com/

Backend: https://alumni-network-backend.herokuapp.com/

## Technologies

- Postgresql - Database
- Hibernate
- Gradle
- JUnit - Unit testing framework
- Postman - endpoint testing
- Spring boot

## Api


### REST endpoints

#### Group
- GET /api/v1/group
  - Returns a list of group that are non private or private group that the logged in user is a member of.
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - name: default is an empty string for full search. Search for a group name, doesn't need to be exact, but needs to contain the string.
  - Statuscode
    - 200 OK
  
- GET /api/v1/group/{groupId}
  - Returns the group with the specified id.
  - Statuscodes
    - 400 BAD REQUEST -> When the entered group id doesn't point to any group.
    - 403 FORBIDDEN -> If the logged in user can't access that group.
    - 200 OK
    
- POST /api/v1/group
  - Returns a link to the newly created group.
  - Body (Required)
    - name -> Name of the group
    - description -> Describes the group
    - isPrivate -> If the group is private
  - Statuscode
    - 400 BAD REQUEST -> When attempting to add a group without a name.
    - 201 CREATED

- POST /api/v1/group/{groupId}/join
  - Returns a link to the group that the user has joined.
  - Statuscode
    - 403 FORBIDDEN -> If the logged in user can't join that group.
    - 400 BAD REQUEST -> When the entered group id doesn't point to any group.
    - 303 SEE OTHER -> When the user is already a member, links to the group.
    - 201 CREATED
    
- POST /api/v1/group/{groupId}/join/{userId}
  - Returns a link to the group that the logged in user is trying to add the user to.
  - Statuscode
    - 403 FORBIDDEN -> If the logged in user can't add other users to that group.
    - 400 BAD REQUEST -> When the specified group id / user id doesnt point to a group / user.
    - 303 SEE OTHER -> When the user is already a member, links to the group.
    - 201 CREATED


#### Topic
- GET /api/v1/topic
  - Returns a list of topics.
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - name: default is an empty string for full search. Search for a topic name, doesn't need to be exact, but needs to contain the string.
  - Statuscode
    - 200 OK

- GET /api/v1/topic/{topicId}
  - Returns a specific topic bound to the specified topic id.
  - Statuscode
    - 400 BAD REQUEST -> When the requested topic bound to the id doesn't exist.
    - 200 OK

- POST /api/v1/topic
  - Returns a link to the newly created topic.
  - Body (Required)
    - name -> Name of the topic.
    - description -> Describes the topic.
  - Statuscode
    - 400 BAD REQUEST -> When the topic is missing a name and description.
    - 201 CREATED

- POST /api/v1/topic/{topicId}/join
  - Returns a link to the topic which the user subscribes to.
  - Statuscode
    - 400 BAD REQUEST -> When the requested topic doesn't exist.
    - 303 SEE OTHER -> If the user is already subscribed, returns a link to that topic.
    - 201 CREATED -> When a subscription has been created for the logged in user.

#### Post
- GET /api/v1/post
  - Returns a collection of posts sent from the logged in user.
  - Optionally supports pagination / search / filter
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search posts that contains the given string(title, content), doesn't need to be exact, but needs to contain the string.
    - type: default is an empty string to ignore what receiver type a post has. Filters posts based on type (group, event, user), needs to be exact.
  - Statuscodes
    - 200 OK

- GET /api/v1/post/{postId}
  - Returns a post with the specified id.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user is trying to access a post which does not belong to them.
    - 400 BAD REQUEST -> If the specified post id doesn't link to a post.
    - 200 OK

- GET /api/v1/post/user
  - Returns a collection of posts sent to the logged in user.
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search posts that contains the given string(title, content), doesn't need to be exact, but needs to contain the string.
  - Statuscodes
    - 200 OK

- GET /api/v1/post/user/{userId}
  - Returns a collection of posts sent to the logged in user from the specified user.
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search posts that contains the given string(title, content), doesn't need to be exact, but needs to contain the string.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified user id doesn't connect to a user.
    - 200 OK

- GET /api/v1/post/group/{groupId}
  - Returns a collection of posts sent to the specified group from the logged in user.
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search posts that contains the given string(title, content), doesn't need to be exact, but needs to contain the string.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified group id doesn't connect to a group.
    - 200 OK

- GET /api/v1/post/topic/{topicId}
  - Returns a collection of posts sent to the specified topic from the logged in user.
  - Optionally supports pagination / search / filter
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search posts that contains the given string(title, content), doesn't need to be exact, but needs to contain the string.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified topic id doesn't connect to a topic.
    - 200 OK

- GET /api/v1/post/event/{eventId}
  - Returns a collection of posts sent to the specified event from the logged in user.
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search posts that contains the given string(title, content), doesn't need to be exact, but needs to contain the string.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified event id doesn't connect to an event.
    - 200 OK

- POST /api/v1/post
  - Returns a link to the newly created post.
  - Body (Required)
    - topic -> the topic of the post, specified as ' "topic": { id: x } ' (where x can be any number).
    - content -> The message content of the post.
    - title -> The message title of the post.
    - receiverType -> The receiver type of the post (can be "user", "group" or "event").
    - receiverId -> A valid id for a receiver.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user is trying to make a post to a receiver which they are not allowed to.
    - 400 BAD REQUEST -> If any of the body has a field which is malformed / if the receiver doesn't exist.
    - 201 CREATED -> When a post is succesfully created, returns a link the new post.

- PUT /api/v1/post/{postId}
  - Returns a link to the newly updated post.
  - Body (Required)
    - content -> The message content of the post.
    - title -> The message title of the post.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user is trying to update fields in a post that are not allowed to be changed (topic, receiverType, receiverId)
    - 400 BAD REQUEST -> If any of the body has a field which is malformed or if the entered postId doesn't link to a post.
    - 200 OK -> When a post is succesfully created, returns a link the new post.

#### Reply
- GET /api/v1/reply/{replyId}
  - Returns a reply with the specified reply id.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user attempts to access a reply which they haven't made.
    - 400 BAD REQUEST -> If the specified reply id doesn't link to a reply.
    - 200 OK

- GET /api/v1/reply/user
  - Returns a list of replies that the logged in user has made
  - Optionally supports pagination / search
    - size: default is 20
    - page: default is page 0
    - search: default is an empty string for full search. Search replies that contains the given string(content), doesn't need to be exact, but needs to contain the string.
  - Statuscodes
    - 200 OK

- GET /api/v1/reply/post/{postId}
  - Returns a collection of replies that are linked to the post with the post id.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user attempts to access a post which they haven't made.
    - 400 BAD REQUEST -> If the specified post id doesn't link to a post.
    - 200 OK

- POST /api/v1/reply/post/{postId}
  - Returns a link to the newly created reply.
  - Body (Required)
    - content -> The message content of the reply.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user attempts to create a reply to a post they don't have access to.
    - 400 BAD REQUEST -> If the specified post id doesn't link to a post.
    - 201 CREATED

- PUT /api/v1/reply/{replyId}
  - Returns a link to the newly created reply.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user attempts to edit a reply they haven't made.
    - 400 BAD REQUEST -> If the specified reply id doesn't link to a reply.
    - 200 OK

#### User
- GET /api/v1/user
  - Returns a link to the logged in user information.
  - Statuscodes
    - 303 SEE OTHER

- GET /api/v1/user/{userId}
  - Returns the user with the specified user id.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified user id doesn't link to a user.
    - 200 OK

- POST /api/v1/user/
  - Returns a link to the newly created user.
  - Body (Required)
    - name -> name / alias of the user.
    - picture -> an img url
    - status -> A description about the users current situation.
    - bio -> Information about the users life.
    - funFact -> Something funny about the user.
  - Statuscodes
    - 201 CREATED

- PATCH /api/v1/reply/post/{postId}
  - Returns a link to the newly created reply.
  - Body (Required)
    - picture -> an img url
    - status -> A description about the users current situation.
    - bio -> Information about the users life.
    - funFact -> Something funny about the user.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user attempts to change their name.
    - 400 BAD REQUEST -> If the specified user id doesn't link to a user.
    - 200 OK

#### Event
- GET /api/v1/event
  - Returns a list of events which the user in some way is connected to (either creator or one of the invited groups / topics / users)
  - Statuscodes
    - 200 OK

- GET /api/v1/event/{eventId}
  - Returns a specific event with the specified id which the user is creator of.
  - Statuscodes
    - 403 FORBIDDEN -> If the logged in user attempts to access information about a specific event which they aren't creator of.
    - 200 OK

- POST /api/v1/event
  - Body (Required)
    - name -> Name of the event.
    - description -> Describes the event.
    - bannerImg -> An image url for the banner of an event.
    - startTime -> When the event start, please provide both a date and time.
    - endTime -> When the event ends, please provide a data, and time.
    - One of the following -> groups, topics and / or invitedUsers.
  - Status codes
    - 403 FORBIDDEN -> If the user attempts to invite a group or topic for which the user is not a member of.
    - 400 BAD REQUEST -> If topics, groups or invitedUsers isn't specified.
    - 201 CREATED

- PUT /api/v1/event/{eventId}
  - Body (Atleast one of the below are required)
    - name -> Name of the event.
    - description -> Describes the event.
    - bannerImg -> An image url for the banner of an event.
    - startTime -> When the event start, please provide both a date and time.
    - endTime -> When the event ends, please provide a data, and time.
  - Status codes
    - 403 FORBIDDEN -> If the user attempts to update an event for which they aren't the creator.
    - 400 BAD REQUEST -> if The event doesn't exist.
    - 200 OK

- POST /api/v1/event/{eventId}/invite/group/{groupId}
  - Creates an event invite for a specific group and then returns the event.
  - Status codes
    - 403 FORBIDDEN -> If the user attempts to invite a group for which the user is not a member or if the user is not the creator.
    - 400 BAD REQUEST -> If the specified event or group doesn't exist, can also happen when the group is already invited.
    - 201 CREATED

- DELETE /api/v1/event/{eventId}/invite/group/{groupId}
  - Deletes an event invite for a specific group and then returns the event.
  - Status codes
    - 403 FORBIDDEN -> If the user attempts to delete a group for which the user is not a member or if the user is not the creator.
    - 400 BAD REQUEST -> If the specified event or group doesn't exist, can also happen when the group is not invited.
    - 200 CREATED

- POST /api/v1/event/{eventId}/invite/user/{userId}
  - Creates an event invite for a specific user and then returns the event.
  - Status codes
    - 403 FORBIDDEN -> If the user is not the creator.
    - 400 BAD REQUEST -> If the specified event or user doesn't exist, can also happen when the user is already invited.
    - 201 CREATED

- DELETE /api/v1/event/{eventId}/invite/user/{userId}
  - Deletes an event invite for a specific user and then returns the event.
  - Status codes
    - 403 FORBIDDEN -> If the user is not the creator.
    - 400 BAD REQUEST -> If the specified event or user doesn't exist, can also happen when the user is not invited.
    - 200 CREATED

- POST /api/v1/event/{eventId}/invite/topic/{topicId}
  - Creates an event invite for a specific topic and then returns the event.
  - Status codes
    - 403 FORBIDDEN -> If the user is not the creator or if the user is not subscribed to the topic.
    - 400 BAD REQUEST -> If the specified event or topic doesn't exist, can also happen when the topic is already invited.
    - 201 CREATED

- DELETE /api/v1/event/{eventId}/invite/topic/{topicId}
  - Deletes an event invite for a specific topic and then returns the event.
  - Status codes
    - 403 FORBIDDEN -> If the user is not the creator or if the user is not subscribed to the topic.
    - 400 BAD REQUEST -> If the specified event or topic doesn't exist, can also happen when the topic is not invited.
    - 200 CREATED

- POST /api/v1/event/{eventId}/rsvp
  - Creates a rsvp for a specific event.
  - Body (required)
    - guestCount -> The number of guests the logged in user plans to bring to the event.
  - Status codes
    - 403 FORBIDDEN -> If the user is not part of the invited guests of the specified event.
    - 400 BAD REQUEST -> If the specified event id doesn't connect to an event.
    - 201 CREATED
