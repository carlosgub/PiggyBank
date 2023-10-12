import SwiftUI
import Firebase
import shared

@main
struct iOSApp: App {
    init(){
        AppModuleKt.doInitKoin()
        FirebaseApp.configure()
    }

	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color.white.ignoresSafeArea(.all) // status bar color
			    ContentView()
			}.preferredColorScheme(.light)
		}
	}
}
