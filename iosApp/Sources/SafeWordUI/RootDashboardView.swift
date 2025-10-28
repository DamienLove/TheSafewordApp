import SwiftUI
import Combine
import UserNotifications
import Shared

final class SafeWordRootViewModel: ObservableObject {
    private let container: SharedIosAppContainer
    private let toggleListening: SharedToggleListeningUseCase
    private let updateSafeWordsUseCase: SharedUpdateSafeWordsUseCase
    private let updateSensitivityUseCase: SharedUpdateSensitivityUseCase
    private let toggleIncludeLocationUseCase: SharedToggleIncludeLocationUseCase
    private let stateAdapter: SharedStateFlowAdapter
    private var dashboardJob: SharedJob?
    private var applyingState = false

    @Published var safeWordOne: String = ""
    @Published var safeWordTwo: String = ""
    @Published var sensitivity: Double = 50
    @Published var includeLocation: Bool = true
    @Published var listeningEnabled: Bool = false
    @Published var peerStatus: String = "Idle"
    @Published var contactsCount: Int = 0

    init() {
        container = SharedIosAppContainer()
        toggleListening = SharedToggleListeningUseCase(settingsGateway: container.settingsGateway)
        updateSafeWordsUseCase = SharedUpdateSafeWordsUseCase(settingsGateway: container.settingsGateway)
        updateSensitivityUseCase = SharedUpdateSensitivityUseCase(settingsGateway: container.settingsGateway)
        toggleIncludeLocationUseCase = SharedToggleIncludeLocationUseCase(settingsGateway: container.settingsGateway)
        stateAdapter = container.engine.dashboardAdapter(scope: container.coroutineScope)

        if let current = stateAdapter.current() as? SharedDashboardState {
            apply(state: current)
        }

        dashboardJob = stateAdapter.collect { [weak self] value in
            guard let state = value as? SharedDashboardState else { return }
            DispatchQueue.main.async {
                self?.apply(state: state)
            }
        }
    }

    deinit {
        dashboardJob?.cancel(cause: nil)
    }

    func setListening(enabled: Bool) {
        guard !applyingState else { return }
        toggleListening.invoke(enabled: enabled) { _, _ in }
    }

    func saveSafeWords() {
        updateSafeWordsUseCase.invoke(wordOne: safeWordOne, wordTwo: safeWordTwo) { _, _ in }
    }

    func updateSensitivity() {
        guard !applyingState else { return }
        let level = KotlinInt(int: Int32(sensitivity))
        updateSensitivityUseCase.invoke(level: level) { _, _ in }
    }

    func updateIncludeLocation(enabled: Bool) {
        guard !applyingState else { return }
        toggleIncludeLocationUseCase.invoke(include: enabled) { _, _ in }
    }

    func runTest() {
        container.engine.runTest(completionHandler: { _ in })
        SiriShortcuts.donateTriggerTest()
    }

    private func apply(state: SharedDashboardState) {
        applyingState = true
        defer { applyingState = false }

        if let settings = state.settings {
            safeWordOne = settings.safeWordOne
            safeWordTwo = settings.safeWordTwo
            sensitivity = Double(settings.sensitivity.intValue)
            includeLocation = settings.includeLocation.boolValue
            listeningEnabled = settings.listeningEnabled.boolValue
        }
        if let contacts = state.contacts {
            contactsCount = Int(contacts.size.intValue)
        }
        switch state.bridgeState {
        case is SharedPeerBridgeStateConnected:
            peerStatus = "Connected"
        case let error as SharedPeerBridgeStateError:
            peerStatus = error.message ?? "Error"
        default:
            peerStatus = "Idle"
        }
    }
}

struct RootDashboardView: View {
    @ObservedObject var viewModel: SafeWordRootViewModel

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Safe Words")) {
                    TextField("First", text: .safeWordOne)
                    TextField("Second", text: .safeWordTwo)
                    Button("Save") { viewModel.saveSafeWords() }
                }

                Section(header: Text("Sensitivity")) {
                    Slider(value: .sensitivity, in: 1...100, step: 1, onEditingChanged: { editing in
                        if !editing { viewModel.updateSensitivity() }
                    })
                    Text("Current: \(Int(viewModel.sensitivity))")
                }

                Section(header: Text("Options")) {
                    Toggle("Include location", isOn: .includeLocation)
                        .onChange(of: viewModel.includeLocation) { value in
                            viewModel.updateIncludeLocation(enabled: value)
                        }
                    Toggle("Listening", isOn: .listeningEnabled)
                        .onChange(of: viewModel.listeningEnabled) { value in
                            viewModel.setListening(enabled: value)
                        }
                }

                Section(header: Text("Status")) {
                    Text("Peer bridge: \(viewModel.peerStatus)")
                    Text("Contacts configured: \(viewModel.contactsCount)")
                    Button("Run Test Alert") { viewModel.runTest() }
                }
            }
            .navigationTitle("SafeWord")
        }
    }
}
