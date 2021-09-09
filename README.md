# Alumni-network-backend2

### REST endpoints

#### Group
- GET /api/v1/group
  - Returns a list of groups connected to the logged in user.
  - Statuscode
    - 200 OK
  
- GET /api/v1/group/{groupId}
  - Returns the group with the specified id.
  - Statuscodes
    - 404 NOT FOUND -> When that group doesn't exist.
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
    - 404 NOT FOUND -> When that group doesn't exist.
    - 303 SEE OTHER -> When the user is already a member, links to the group.
    - 403 FORBIDDEN -> If the logged in user can't join that group.
    - 201 CREATED
    
- POST /api/v1/group/{groupId}/join/{userId}
  - Returns a link to the group that the logged in user is trying to add the user to.
  - Statuscode
    - 404 NOT FOUND -> When that group doesn't exist.
    - 303 SEE OTHER -> When the user is already a member, links to the group.
    - 403 FORBIDDEN -> If the logged in user can't add other users to that group.
    - 201 CREATED
    
    
