package com.github.martinfrank.sport.trainingbikeapp

class FishingGame {

    enum class TASKS{
        DRIVING_OUT, //TASK1
        TOSSING_LINE, //TASK2
        PULL_CATCH, //TASK3
        SHOW_CATCH //finish move :-D
    }
    enum class VELOCITY{
        SLOW,
        MEDIUM,
        FAST
    }
    enum class EVENT{
        FINE_PROGRESS,
        CONTRARY_WIND,
        TAILWIND,
        COUNTER_CURRENT,
        UP_CURRENT,
        OUT_BURST,
        VICTORY
    }

    private var task1Progress = 0 //fishing
    private var task1Success = false

    private var task2Progress = 0 //tossing line
    private var task2Success = false

    private var task3Progress = 0 //tossing line
    private var task3Success = false

    private var currentTask = TASKS.DRIVING_OUT
    private var currentVelocity = VELOCITY.MEDIUM
    private var currentEvent = EVENT.FINE_PROGRESS
    private var currentEventDuration = 0
    private var currentEventElapsedTime = 0

    companion object {
        var TASK_LENGTH: Int = 100000
    }

    fun tick(tick: Long, distance: Int, cadence: Int, power: Int, speed: Int, calories: Int, strange: Int) {
        makeProgress(cadence)
        makeEvents()
    }

    private fun makeEvents() {
        currentEventElapsedTime ++

        if (currentEventElapsedTime >= currentEventDuration){
            if(currentTask == TASKS.DRIVING_OUT){
                makeEventsDrivingOut()
            }

            if(currentTask == TASKS.TOSSING_LINE){
                makeEventsTossingLine()
            }

            if(currentTask == TASKS.PULL_CATCH){
                makeEventsPullCatch()
            }

            if(currentTask == TASKS.SHOW_CATCH){
                currentEvent = EVENT.VICTORY
            }
        }

    }

    private fun makeEventsPullCatch() {
        var newEvent = EVENT.FINE_PROGRESS
        val isGood = Math.random() < 0.3
        currentEventDuration = 160 + (Math.random() * 10).toInt()
        currentEventElapsedTime = 0
        if(currentEvent == EVENT.OUT_BURST){
            newEvent = EVENT.FINE_PROGRESS
        }
        if(currentEvent == EVENT.FINE_PROGRESS && !isGood){
            newEvent = EVENT.OUT_BURST
            currentEventDuration += 30
        }
        if(currentEvent == EVENT.UP_CURRENT && isGood){
            newEvent = EVENT.UP_CURRENT
            currentEventDuration -= 20
        }
        currentEvent = newEvent
    }

    private fun makeEventsTossingLine() {
        var newEvent = EVENT.FINE_PROGRESS
        val isGood = Math.random() < 0.3
        currentEventDuration = 160 + (Math.random() * 10).toInt()
        currentEventElapsedTime = 0
        if(currentEvent == EVENT.COUNTER_CURRENT){
            newEvent = EVENT.FINE_PROGRESS
        }
        if(currentEvent == EVENT.FINE_PROGRESS && isGood){
            newEvent = EVENT.UP_CURRENT
        }
        if(currentEvent == EVENT.FINE_PROGRESS && !isGood){
            newEvent = EVENT.COUNTER_CURRENT
            currentEventDuration += 30
        }
        if(currentEvent == EVENT.UP_CURRENT && isGood){
            newEvent = EVENT.UP_CURRENT
            currentEventDuration -= 20
        }
        if(currentEvent == EVENT.UP_CURRENT && !isGood){
            newEvent = EVENT.FINE_PROGRESS
        }
        currentEvent = newEvent
    }

    private fun makeEventsDrivingOut() {
        var newEvent = EVENT.FINE_PROGRESS
        val isGood = Math.random() < 0.3
        currentEventDuration = 160 + (Math.random() * 10).toInt()
        currentEventElapsedTime = 0
        if(currentEvent == EVENT.CONTRARY_WIND){
            newEvent = EVENT.FINE_PROGRESS
        }
        if(currentEvent == EVENT.FINE_PROGRESS && isGood){
            newEvent = EVENT.TAILWIND
        }
        if(currentEvent == EVENT.FINE_PROGRESS && !isGood){
            newEvent = EVENT.CONTRARY_WIND
            currentEventDuration += 30
        }
        if(currentEvent == EVENT.TAILWIND && isGood){
            newEvent = EVENT.TAILWIND
            currentEventDuration -= 20
        }
        if(currentEvent == EVENT.TAILWIND && !isGood){
            newEvent = EVENT.FINE_PROGRESS
        }
        currentEvent = newEvent
    }

    private fun makeProgress(cadence: Int) {
        if(currentTask == TASKS.DRIVING_OUT) {
            makeProgressDrivingOut(cadence)
        }

        if(task1Progress >= TASK_LENGTH && currentTask == TASKS.DRIVING_OUT ) {
            resetEvent();
            task1Success = true
            currentTask = TASKS.TOSSING_LINE
        }

        if(currentTask == TASKS.TOSSING_LINE) {
            makeProgressTossingLine(cadence)
        }

        if(task2Progress >= TASK_LENGTH && currentTask == TASKS.TOSSING_LINE ) {
            resetEvent();
            task2Success = true
            currentTask = TASKS.PULL_CATCH
        }

        if(currentTask == TASKS.PULL_CATCH) {
            makeProgressPullingOut(cadence)
        }

        if(task3Progress >= TASK_LENGTH && currentTask == TASKS.PULL_CATCH ) {
            resetEvent()
            task3Success = true
            currentTask = TASKS.SHOW_CATCH
        }
    }

    private fun makeProgressPullingOut(cadence: Int) {
        task3Progress += cadence
        if(currentEvent == EVENT.OUT_BURST){
            task3Progress += -140
            if(task3Progress < 0){
                task3Progress = 0
            }
        }
    }

    private fun makeProgressTossingLine(cadence: Int) {
        task2Progress += cadence
        if(currentEvent == EVENT.COUNTER_CURRENT){
            task2Progress += -110
            if(task2Progress < 0){
                task2Progress = 0
            }
        }
        if(currentEvent == EVENT.UP_CURRENT){
            task2Progress += +45
        }
    }

    private fun makeProgressDrivingOut(cadence: Int) {
        task1Progress += cadence
        if(currentEvent == EVENT.CONTRARY_WIND){
            task1Progress += -90
            if(task1Progress < 0){
                task1Progress = 0
            }
        }
        if(currentEvent == EVENT.TAILWIND){
            task1Progress += +45
        }
    }

    private fun resetEvent() {
        currentEvent = EVENT.FINE_PROGRESS
        currentEventElapsedTime = 0
        currentEventDuration = 0
    }

    fun reset(){
        task1Progress = 0 //fishing
        task1Success = false
        task2Progress = 0 //fishing
        task2Success = false
        task3Progress = 0 //fishing
        task3Success = false
        currentTask = TASKS.DRIVING_OUT
    }

    fun getTask1Progress(): Int {
        return task1Progress
    }
    fun getTask1Success(): Boolean {
        return task1Success
    }
    fun getTask2Progress(): Int {
        return task2Progress
    }
    fun getTask2Success(): Boolean {
        return task2Success
    }
    fun getTask3Progress(): Int {
        return task3Progress
    }
    fun getTask3Success(): Boolean {
        return task3Success
    }
    fun getCurrentTask(): TASKS {
        return currentTask
    }
    fun getCurrentVelocity(): VELOCITY {
        return currentVelocity
    }
    fun getCurrentEvent(): EVENT {
        return currentEvent
    }

}