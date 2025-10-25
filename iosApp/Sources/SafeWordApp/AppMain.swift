import SwiftUI
import Shared

@main
struct SafeWordApp: App {
    @StateObject private var viewModel = SafeWordRootViewModel()

    var body: some Scene {
        WindowGroup {
            RootDashboardView(viewModel: viewModel)
        }
    }
}
