package edu.oregonstate.mist.tasx

class UserRestController {

    static responseFormats = ["json"]

    /**
     * Respond with all users serialized as JSON
     *
     * $ curl -X GET -H "Accept:application/json" /tasx/rest/user
     *
     * @return       JSON response
     */
    def index() {
        respond(User.findAll().collect(serializeUser))
    }

    /**
     * Respond with user serialized as JSON
     *
     * $ curl -X GET -H "Accept:application/json" /tasx/rest/user/ID
     *
     * @param user   from URL
     * @return       JSON response
     */
    def show(User user) {
        respond(serializeUser(user))
    }

    private Closure serializeUser = {
        user ->
            [ id:    user.id,
              name:  user.name,
              email: user.email,
              tasks: user.tasks ]
    }
}
