package com.github.martinfrank.sport.trainingbikeapp.fishing

import android.graphics.drawable.AnimationDrawable
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.github.martinfrank.sport.trainingbikeapp.R

class FishingGameRenderer (fishingGameRootLayout: LinearLayout) {

    private var progress1Parent: LinearLayout = fishingGameRootLayout.findViewById<LinearLayout>(R.id.fishing_progress_task1)
    private var fishingTask1Progress: ProgressBar = progress1Parent.findViewById(R.id.taskProgressBar)
    private var fishingTask1Success: CheckBox = progress1Parent.findViewById(R.id.taskSuccessCheckBox)

    private var progress2Parent: LinearLayout = fishingGameRootLayout.findViewById<LinearLayout>(R.id.fishing_progress_task2)
    private var fishingTask2Progress: ProgressBar = progress2Parent.findViewById(R.id.taskProgressBar)
    private var fishingTask2Success: CheckBox = progress2Parent.findViewById(R.id.taskSuccessCheckBox)

    private var progress3Parent: LinearLayout = fishingGameRootLayout.findViewById<LinearLayout>(R.id.fishing_progress_task3)
    private var fishingTask3Progress: ProgressBar = progress3Parent.findViewById(R.id.taskProgressBar)
    private var fishingTask3Success: CheckBox = progress3Parent.findViewById(R.id.taskSuccessCheckBox)

    private var currentTaskText: TextView = fishingGameRootLayout.findViewById(R.id.textCurrentTask)

    private var eventParent = fishingGameRootLayout.findViewById<LinearLayout>(R.id.fishing_event)
    private var currentEventText: TextView = eventParent.findViewById(R.id.currentEventText)
    private var currentEventIcon: ImageView  = eventParent.findViewById(R.id.currentEventIcon)

    private var fishingImage: ImageView = fishingGameRootLayout.findViewById<ImageView>(R.id.fishingImage)

    private var currentFishingImageId = R.drawable.background_sea_00
    private var currentEventIconId = R.drawable.icon_event_fine_progress

    init {
        fishingTask1Progress.max = FishingGame.TASK_LENGTH
        fishingTask2Progress.max = FishingGame.TASK_LENGTH
        fishingTask3Progress.max = FishingGame.TASK_LENGTH
    }



    fun render(fishingGame: FishingGame) {
        renderProgress(fishingGame)
        renderTaskText(fishingGame)
        renderImage(fishingGame)
        renderEvents(fishingGame)
        
//
//        progress1Parent.setBackgroundResource(R.drawable.style_simple_border_black)
//        progress2Parent.setBackgroundResource(R.drawable.style_simple_border_black)
//        progress3Parent.setBackgroundResource(R.drawable.style_simple_border_black)
//        var currentTask = fishingGame.getCurrentTask()
//        var currentImageId = 0
//        when (currentTask) {
//            FishingGame.TASKS.DRIVING_OUT -> {
//                currentTaskText.setText(R.string.fishing_task_rowing)
//                progress1Parent.setBackgroundResource(R.drawable.style_simple_border_red)
//                currentImageId = R.drawable.animation_rowing_normal
//            }
//
//            FishingGame.TASKS.TOSSING_LINE -> {
//                currentTaskText.setText(R.string.fishing_task_tossing_line)
//                progress2Parent.setBackgroundResource(R.drawable.style_simple_border_red)
//                currentImageId = R.drawable.animation_fishing_normal
//            }
//            FishingGame.TASKS.PULL_CATCH -> {
//                currentTaskText.setText(R.string.fishing_task_pull_catch)
//                progress3Parent.setBackgroundResource(R.drawable.style_simple_border_red)
//                currentImageId = R.drawable.animation_pulling_00
//            }
//            FishingGame.TASKS.SHOW_CATCH -> {
//                currentTaskText.setText(R.string.fishing_task_pull_catch)
//                progress3Parent.setBackgroundResource(R.drawable.style_simple_border_red)
//                currentImageId = R.drawable.animation_pulling_01
//            }
//        }
//
//        if (currentFishingImageId != currentImageId) {
//            currentFishingImageId = currentImageId
//            fishingImage.setImageResource(currentFishingImageId)
//            var frameAnimation = fishingImage.drawable as AnimationDrawable
//            frameAnimation.start();
//        }
//
//        var currentEvent = fishingGame.getCurrentEvent()
//        var eventIconId = R.drawable.icon_event_fine_progress
//        when (currentEvent) {
//            FishingGame.EVENT.TAILWIND -> {
//                currentEventText.setText(R.string.fishing_event_tailwind)
//                eventIconId = R.drawable.icon_event_tail_wind
//            }
//            FishingGame.EVENT.CONTRARY_WIND -> {
//                currentEventText.setText(R.string.fishing_event_contrary_wind)
//                eventIconId = R.drawable.icon_event_contrary_wind
//            }
//            FishingGame.EVENT.FINE_PROGRESS -> {
//                currentEventText.setText(R.string.fishing_event_fine_progress)
//                eventIconId = R.drawable.icon_event_fine_progress
//            }
//            FishingGame.EVENT.COUNTER_CURRENT -> {
//                currentEventText.setText(R.string.fishing_event_counter_current)
//                eventIconId = R.drawable.icon_event_counter_current
//            }
//            FishingGame.EVENT.UP_CURRENT -> {
//                currentEventText.setText(R.string.fishing_event_up_current)
//                eventIconId = R.drawable.icon_event_up_current
//            }
//            FishingGame.EVENT.OUT_BURST -> {
//                currentEventText.setText(R.string.fishing_event_outburst)
//                eventIconId = R.drawable.icon_event_fish_outburst
//            }
//            FishingGame.EVENT.VICTORY -> {
//                currentEventText.setText(R.string.fishing_event_victory)
//                eventIconId = R.drawable.icon_event_victory
//            }
//        }
//        if(currentEventIconId != eventIconId){
//            currentEventIconId = eventIconId
//            currentEventIcon.setImageResource(currentEventIconId)
//        }

    }

    private fun renderEvents(fishingGame: FishingGame) {
        var currentEvent = fishingGame.getCurrentEvent()
        var eventIconId = R.drawable.icon_event_fine_progress
        when (currentEvent) {
            FishingGame.EVENT.TAILWIND -> {
                currentEventText.setText(R.string.fishing_event_tailwind)
                eventIconId = R.drawable.icon_event_tail_wind
            }
            FishingGame.EVENT.CONTRARY_WIND -> {
                currentEventText.setText(R.string.fishing_event_contrary_wind)
                eventIconId = R.drawable.icon_event_contrary_wind
            }
            FishingGame.EVENT.FINE_PROGRESS -> {
                currentEventText.setText(R.string.fishing_event_fine_progress)
                eventIconId = R.drawable.icon_event_fine_progress
            }
            FishingGame.EVENT.COUNTER_CURRENT -> {
                currentEventText.setText(R.string.fishing_event_counter_current)
                eventIconId = R.drawable.icon_event_counter_current
            }
            FishingGame.EVENT.UP_CURRENT -> {
                currentEventText.setText(R.string.fishing_event_up_current)
                eventIconId = R.drawable.icon_event_up_current
            }
            FishingGame.EVENT.OUT_BURST -> {
                currentEventText.setText(R.string.fishing_event_outburst)
                eventIconId = R.drawable.icon_event_fish_outburst
            }
            FishingGame.EVENT.VICTORY -> {
                currentEventText.setText(R.string.fishing_event_victory)
                eventIconId = R.drawable.icon_event_victory
            }
        }
        if(currentEventIconId != eventIconId){
            currentEventIconId = eventIconId
            currentEventIcon.setImageResource(currentEventIconId)
        }
    }

    private fun renderImage(fishingGame: FishingGame) {
        var currentTask = fishingGame.getCurrentTask()
        var currentImageId = 0
        when (currentTask) {
            FishingGame.TASKS.DRIVING_OUT -> {
                currentImageId = R.drawable.animation_rowing_normal
            }

            FishingGame.TASKS.TOSSING_LINE -> {
                currentImageId = R.drawable.animation_fishing_normal
            }
            FishingGame.TASKS.PULL_CATCH -> {
                currentImageId = R.drawable.animation_pulling_00
            }
            FishingGame.TASKS.SHOW_CATCH -> {
                currentImageId = R.drawable.animation_pulling_01
            }
        }

        if (currentFishingImageId != currentImageId) {
            currentFishingImageId = currentImageId
            fishingImage.setImageResource(currentFishingImageId)
            var frameAnimation = fishingImage.drawable as AnimationDrawable
            frameAnimation.start();
        }

    }

    private fun renderTaskText(fishingGame: FishingGame) {
        var currentTask = fishingGame.getCurrentTask()
        when (currentTask) {
            FishingGame.TASKS.DRIVING_OUT -> {
                currentTaskText.setText(R.string.fishing_task_rowing)
            }

            FishingGame.TASKS.TOSSING_LINE -> {
                currentTaskText.setText(R.string.fishing_task_tossing_line)
            }
            FishingGame.TASKS.PULL_CATCH -> {
                currentTaskText.setText(R.string.fishing_task_pull_catch)
            }
            FishingGame.TASKS.SHOW_CATCH -> {
                currentTaskText.setText(R.string.fishing_event_victory)
            }
        }
    }

    private fun renderProgress(fishingGame: FishingGame) {
        fishingTask1Progress.progress = fishingGame.getTask1Progress()
        fishingTask1Success.isChecked = fishingGame.getTask1Success()

        fishingTask2Progress.progress = fishingGame.getTask2Progress()
        fishingTask2Success.isChecked = fishingGame.getTask2Success()

        fishingTask3Progress.progress = fishingGame.getTask3Progress()
        fishingTask3Success.isChecked = fishingGame.getTask3Success()
        progress1Parent.setBackgroundResource(R.drawable.style_simple_border_black)
        progress2Parent.setBackgroundResource(R.drawable.style_simple_border_black)
        progress3Parent.setBackgroundResource(R.drawable.style_simple_border_black)
        var currentTask = fishingGame.getCurrentTask()
        when (currentTask) {
            FishingGame.TASKS.DRIVING_OUT -> {
                progress1Parent.setBackgroundResource(R.drawable.style_simple_border_red)
            }

            FishingGame.TASKS.TOSSING_LINE -> {
                progress2Parent.setBackgroundResource(R.drawable.style_simple_border_red)
            }
            FishingGame.TASKS.PULL_CATCH -> {
                progress3Parent.setBackgroundResource(R.drawable.style_simple_border_red)
            }
            FishingGame.TASKS.SHOW_CATCH -> {
                progress3Parent.setBackgroundResource(R.drawable.style_simple_border_red)
            }
        }
    }
}