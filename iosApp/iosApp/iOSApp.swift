import SwiftUI
import Firebase

@main
struct iOSApp: App {
    init(){
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
