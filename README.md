# Alumni-network-backend2

### REST endpoints

#### Group
- GET /api/v1/group
  - Returns a list of group that are non private or private group that the logged in user is a member of.
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
    

#### Post
- GET /api/v1/post
  - Returns a collection of posts sent from the logged in user.
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
  - Statuscodes
    - 200 OK

- GET /api/v1/post/user/{userId}
  - Returns a collection of posts sent to the logged in user from the specified user.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified user id doesn't connect to a user.
    - 200 OK

- GET /api/v1/post/group/{groupId}
  - Returns a collection of posts sent to the specified group from the logged in user.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified group id doesn't connect to a group.
    - 200 OK

- GET /api/v1/post/topic/{topicId}
  - Returns a collection of posts sent to the specified topic from the logged in user.
  - Statuscodes
    - 400 BAD REQUEST -> If the specified topic id doesn't connect to a topic.
    - 200 OK

- GET /api/v1/post/event/{eventId}
  - Returns a collection of posts sent to the specified event from the logged in user.
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
