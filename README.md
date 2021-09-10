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
