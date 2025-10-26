import Foundation
import Intents

enum SiriShortcuts {
    static func donateStartListening() {
        donate(
            activityType: "com.safeword.intent.startListening",
            title: "Start SafeWord listening",
            phrase: "Start SafeWord"
        )
    }

    static func donateTriggerTest() {
        donate(
            activityType: "com.safeword.intent.runTest",
            title: "Trigger SafeWord test",
            phrase: "Run SafeWord test"
        )
    }

    private static func donate(activityType: String, title: String, phrase: String) {
        let activity = NSUserActivity(activityType: activityType)
        activity.title = title
        activity.isEligibleForSearch = true
        activity.isEligibleForPrediction = true
        activity.persistentIdentifier = NSUserActivityPersistentIdentifier(activityType)
        activity.suggestedInvocationPhrase = phrase

        let interaction = INInteraction(userActivity: activity, response: nil)
        interaction.donate(completion: nil)
    }
}
