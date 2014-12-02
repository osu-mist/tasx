package edu.oregonstate.mist.tasx

class TaskController {

    static defaultAction = "list"

    static scaffold = false

    /**
     * Create, update, or read a task
     *
     * @return  task view model
     */
    Map details() {
        User user = getUserOrLogin()

        Task task = Task.get(params.id) ?:
                    new Task()

        if (params.submitting) {
            Date from = stringToDate(params.from)
            Date to = stringToDate(params.to)

            Interval schedule = task.schedule
            schedule.setInterval(from, to)
            schedule.save()

            task.description = params.description
            task.schedule = schedule
            task.priority = Integer.parseInt(params.priority)
            task.status = stringToStatus(params.status)
            task.user = user

            task.save([flush:true])
            redirect([action: "details", id: task.id])
        } else {
            return [ params:       params,
                     from:         task.schedule?.fromDate?.format(DATEFORMAT),
                     to:           task.schedule?.toDate?.format(DATEFORMAT),
                     task:         task
            ]
        }
    }

    /**
     * Display a list of tasks
     *
     * @return  task list view model
     */
    Map list() {
        User user = getUserOrLogin()

        List taskList = Task.findAllWhere([user: user])

        return [ taskList: taskList ]
    }

    private final String DATEFORMAT = "MM/dd/yyyy"

    private Date stringToDate(String dateString) {
        return Date.parse(DATEFORMAT, dateString)
    }

    private static Task.Status stringToStatus(String statusString) {
        switch(statusString) {
            case "done":      return Task.Status.DONE
            case "cancelled": return Task.Status.CANCELLED
            case "deleted":   return Task.Status.DELETED
            case "todo":
                default:      return Task.Status.TODO
        }
    }

    private User getUserOrLogin() {
        return (User)session["user"] ?:
               redirect([controller: "user", action: "login"])
    }
}
